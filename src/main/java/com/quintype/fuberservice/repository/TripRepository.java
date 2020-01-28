package com.quintype.fuberservice.repository;

import com.jasongoodwin.monads.Try;
import com.quintype.fuberservice.booking.Trip;
import com.quintype.fuberservice.booking.TripUpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Singleton
public class TripRepository {
    private static final Logger logger = LoggerFactory.getLogger(TripRepository.class.getSimpleName());
    private static final RiderRepository riderRepository = new RiderRepository();
    private static final VehicleRepository vehicleRepository = new VehicleRepository();

    @Inject
    private TripUpdateHandler tripUpdateHandler;

    private final Map<UUID, Trip> tripsById = new HashMap<>();

    public TripRepository() {
        // Cache all trips in memory
        fetchAllTrips()
                .stream()
                .filter(trip -> trip.getTrackingId().isPresent())
                .forEach(trip -> tripsById.put(trip.getTrackingId().get(),trip));
    }

    public Optional<Trip> getById(UUID id) {
        return Optional.ofNullable(tripsById.get(id));
    }

    public List<Trip> getTripsBasedOnState(Trip.State state) {
        return tripsById.values()
                .stream()
                .filter(trip -> trip.getPresentState().equals(state))
                .collect(Collectors.toList());
    }

    public List<Trip> getAllTripsByRiderId(UUID riderId) {
        return tripsById.values()
                .stream()
                .filter(trip -> trip.getRiderId().equals(riderId))
                .collect(Collectors.toList());
    }

    // In reality, this will be a service call with a failure probability
    public Try<UUID> attemptNewTripRegistration(Trip newTrip) {
        return Try.ofFailable(() -> {
            // In reality, unique id generation would not be so simplistic :)
            UUID tripId = newTrip.getTrackingId().orElseGet(() -> UUID.randomUUID());
            newTrip.setTrackingId(tripId);
            tripsById.put(tripId, newTrip);
            vehicleRepository.markBooked(newTrip.getAssignedVehicle());
            logger.info("A new trip with id: {} has been created in the system", newTrip.getTrackingId().get());
            return tripId;
        });
    }

    // In reality, this will be a service call with a failure probability
    public Try<UUID> attemptUpdate(Trip updatePayload) {
        return Try.ofFailable(() -> {
            UUID tripId = updatePayload.getTrackingId().get();
            Trip existing = tripsById.get(tripId);
            tripsById.put(tripId,updatePayload);
            logger.info("Trip with id: {} was updated", updatePayload.getTrackingId().get());
            Optional.ofNullable(tripUpdateHandler)
                    .map(handler -> {
                        handler.handleUpdate(existing,updatePayload);
                        return updatePayload.getTrackingId();
                    }).orElseThrow(() -> {
                        logger.error("Trip update handler not triggered for trip: {}",updatePayload.getTrackingId());
                        return new RuntimeException("Trip update handler not triggered");
                    });
            return tripId;
        });
    }

    public void deleteAllTrips() {
        tripsById.clear();
    }

    Trip getRandomTrip() {
        List<Trip> trips = fetchAllTrips();
        Random r = new Random();
        int index = r.nextInt(trips.size() - 1);
        return trips.get(index);
    }

    private List<Trip> fetchAllTrips() {
        List<Trip> trips = IntStream.range(1, 5)
                .mapToObj(i -> {
                    Trip trip = new Trip(
                            riderRepository.getRandomRiderLocation(),
                            riderRepository.getRandomRiderLocation(),
                            vehicleRepository.getRandomVehicle(),
                            riderRepository.getRandomRider().getUniqueId().get(),
                            120.32f + i);
                    trip.setTrackingId(UUID.randomUUID());
                    return trip;
                }).collect(Collectors.toList());
        return trips;
    }
}
