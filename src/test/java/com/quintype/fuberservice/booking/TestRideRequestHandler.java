package com.quintype.fuberservice.booking;

import com.quintype.fuberservice.repository.RideRequestRepository;
import com.quintype.fuberservice.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRideRequestHandler {
    private RideRequestRepository rideRequestRepository;
    private TripRepository tripRepository;

    @BeforeEach
    void beforeAll() {
        rideRequestRepository = new RideRequestRepository();
        tripRepository = new TripRepository();
    }

    @Test
    @DisplayName("Verify that a ride request results in a trip assignment if nearby cabs are available")
    void tripIsAssignedForARideRequestIfCabAvailable() {
        RideRequest rideRequest = rideRequestRepository.getAllRideRequests().get(0);
        RideRequestHandler rideRequestHandler = new RideRequestHandler(rideRequest,tripRepository);
        rideRequestHandler.run();
        assertTrue(rideRequest.getTripAssigned().isPresent());
    }

    @Test
    @DisplayName("Verify that a ride request does not result in a trip assignment if nearby cabs are not available")
    void tripIsNotAssignedForARideRequestIfCabNotAvailable() {
        RideRequest rideRequest = rideRequestRepository.getAllRideRequests().get(0);
        tripRepository.deleteAllTrips();
        RideRequestHandler rideRequestHandler = new RideRequestHandler(rideRequest,tripRepository);
        rideRequestHandler.run();
        assertFalse(rideRequest.getTripAssigned().isPresent());
    }
}
