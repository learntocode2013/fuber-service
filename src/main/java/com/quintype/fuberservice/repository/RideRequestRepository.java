package com.quintype.fuberservice.repository;

import com.quintype.fuberservice.booking.RideRequest;
import com.quintype.fuberservice.common.Color;
import com.quintype.fuberservice.common.UserPreference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class RideRequestRepository {
    private static final Logger logger = LoggerFactory.getLogger(RideRequestRepository.class.getSimpleName());
    private static final RiderRepository riderRepository = new RiderRepository();
    private static final TripRepository tripRepository = new TripRepository();

    private final Map<UUID, RideRequest> rideRequestsById = new HashMap<>();

    public RideRequestRepository() {
        // Cache all existing ride requests in memory
        fetchAllRideRequests()
                .stream()
                .forEach(rideRequest -> rideRequestsById.put(rideRequest.getTrackingId().get(),rideRequest));
    }

    public List<RideRequest> getAllRideRequests() {
        return rideRequestsById.values().stream().collect(Collectors.toList());
    }

    public Optional<RideRequest> getById(UUID id) {
        return Optional.ofNullable(rideRequestsById.get(id));
    }

    public RideRequestRepository registerNewRideRequest(RideRequest newRideRequest) {
        rideRequestsById.put(newRideRequest.getTrackingId().get(),newRideRequest);
        return this;
    }

    public UUID updateRideRequest(RideRequest rideRequest) {
        rideRequestsById.put(rideRequest.getTrackingId().get(),rideRequest);
        return rideRequest.getTrackingId().get();
    }

    private List<RideRequest> fetchAllRideRequests() {
        List<RideRequest> result = new ArrayList<>();
        result.add(
                new RideRequest(
                        riderRepository.getRandomRiderLocation(),
                        riderRepository.getRandomRiderLocation(),
                        riderRepository.getRandomRider().getUniqueId().get()
                        )
                        .setTrackingId(UUID.randomUUID())
                        .setUserPreference(new UserPreference(Color.Pink))
        );
        result.add(
                new RideRequest(
                        riderRepository.getRandomRiderLocation(),
                        riderRepository.getRandomRiderLocation(),
                        riderRepository.getRandomRider().getUniqueId().get()
                )
                .setTrackingId(UUID.randomUUID())
                .assignTrip(tripRepository.getRandomTrip())
        );
        result.add(
                new RideRequest(
                        riderRepository.getRandomRiderLocation(),
                        riderRepository.getRandomRiderLocation(),
                        riderRepository.getRandomRider().getUniqueId().get()
                )
                .setTrackingId(UUID.randomUUID())
                .close()
        );
        result.add(
                new RideRequest(
                        riderRepository.getRandomRiderLocation(),
                        riderRepository.getRandomRiderLocation(),
                        riderRepository.getRandomRider().getUniqueId().get()
                )
                .setTrackingId(UUID.randomUUID())
                .setUserPreference(new UserPreference(Color.Silver))
                .cancel()
        );
        return result;
    }
}
