package com.quintype.fuberservice.controller;

import com.quintype.fuberservice.booking.RideRequest;
import com.quintype.fuberservice.repository.RideRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBookingService {
    private RideRequestRepository rideRequestRepository;
    private BookingService bookingService;

    @BeforeEach
    void beforeEach() {
        rideRequestRepository = new RideRequestRepository();
        bookingService = new BookingService();
    }

    @Test
    void rideRequestFailsOnBadPayload() {
        RideRequest rideRequest = rideRequestRepository.getAllRideRequests().get(0);
        rideRequest.setRiderId(UUID.randomUUID());
        Response response = bookingService.createNewRideRequest(rideRequest);
        assertEquals(response.getStatusInfo().getFamily(), Response.Status.Family.CLIENT_ERROR);
    }
}
