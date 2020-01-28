package com.quintype.fuberservice.booking;

import com.jasongoodwin.monads.Try;
import com.quintype.fuberservice.billing.BillingUtils;
import com.quintype.fuberservice.common.Color;
import com.quintype.fuberservice.common.Location;
import com.quintype.fuberservice.common.Vehicle;
import com.quintype.fuberservice.repository.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static com.quintype.fuberservice.common.Utils.compute_KM_DistanceBetween;

public class RideRequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RideRequestHandler.class.getSimpleName());
    private static final VehicleLocator vehicleLocator = new VehicleLocator();

    private final RideRequest request;
    private final TripRepository tripRepository;

    public RideRequestHandler(RideRequest request, TripRepository tripRepository) {
        this.request = request;
        this.tripRepository = tripRepository;
    }

    @Override
    public void run() {
        logger.info("Handling a new ride request: {}", request);

        BiPredicate<Vehicle, Color> filterCondition = getFilterBasedOnUserPrefs(request);

        // Get nearest cab based on rider's current location
        Try<Trip> tripOrErr = fetchNearestCab(request.getSource(), filterCondition)
                .map(vehicle -> convertRideRequestToTrip(request, vehicle))
                .get()
                .recoverWith(t -> {
                    logger.warn("Failed to convert ride request: {} to a trip due to {}", request.getTrackingId().get(),t);
                    request.close();
                    return Try.failure(new RuntimeException("No vehicle is available at this time"));
                });
        tripOrErr.onSuccess(trip -> request.assignTrip(trip));
    }

    private BiPredicate<Vehicle, Color> getFilterBasedOnUserPrefs(RideRequest aRequest) {
        BiPredicate<Vehicle, Color> colorCondition = (v,c) -> c.equals(v.getColor());
        BiPredicate<Vehicle, Color> acceptAllCondition = (v,c) -> true;

        return aRequest.getUserPreference()
                .map(pref -> colorCondition)
                .orElse(acceptAllCondition);
    }

    private Optional<Vehicle> fetchNearestCab(Location pickupPoint, BiPredicate<Vehicle, Color> filterCondition) {
        return vehicleLocator.locateNearestCabs(pickupPoint)
                .stream()
                .filter(vehicle -> {
                    Color chosenColor = request.getUserPreference()
                            .map(pref -> pref.getPreferredVehicleColor())
                            .orElse(Color.Any);
                    boolean hasDesiredColor = filterCondition.test(vehicle, chosenColor);
                    logger.info("Does vehicle: {} have {} color ? {}", vehicle.getVehicleNumber(),
                            chosenColor.name(),hasDesiredColor);
                    return hasDesiredColor;
                })
                .findFirst();
    }

    private Try<Trip> convertRideRequestToTrip(RideRequest aRequest, Vehicle availableVehicle) {
        // We will attempt to save out trip on a best effort basis. Due to sheer nature
        // of eventual consistency, the vehicle might not be available when we try to change
        // the state of it and this condition can be a common occurrence
        return Try.ofFailable(() -> {
            availableVehicle.setStatus(Vehicle.Status.BOOKED_FOR_RIDE);
            Trip newTrip = new Trip(
                    aRequest.getSource(),
                    aRequest.getDestination(),
                    availableVehicle,
                    aRequest.getRiderId(),
                    getEstimatedFare(aRequest.getSource(),aRequest.getDestination()));
            // We pass over the request id as trip id
            newTrip.setTrackingId(aRequest.getTrackingId().get());
            tripRepository.attemptNewTripRegistration(newTrip).getUnchecked();
            return newTrip;
        });
    }

    private double getEstimatedFare(Location source, Location destination) {
        double KM_distance = compute_KM_DistanceBetween(
                source.getCoordinate(),
                destination.getCoordinate());
        return BillingUtils.computeFareForDistance(KM_distance);
    }
}
