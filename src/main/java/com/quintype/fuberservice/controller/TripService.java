package com.quintype.fuberservice.controller;

import com.jasongoodwin.monads.Try;
import com.quintype.fuberservice.booking.Trip;
import com.quintype.fuberservice.booking.TripFailureEvent;
import com.quintype.fuberservice.repository.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.UUID;

import static com.quintype.fuberservice.booking.TripFailureEvent.Type.CREATE_FAILED;
import static com.quintype.fuberservice.booking.TripFailureEvent.Type.UPDATE_FAILED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("trips")
public class TripService {
    private static final Logger logger = LoggerFactory.getLogger(TripService.class.getSimpleName());

    @Inject
    private TripRepository repository;

    // Maybe some batch job will pull the trip in a particular state for data analysis
    @GET
    @Produces(APPLICATION_JSON)
    public Response getTripsBasedOnState(@QueryParam("state")Trip.State state) {
        logger.info("Received request to retrieve all trip in state: {}",state);
        return Response.ok(repository.getTripsBasedOnState(state)).build();
    }


    // A mobile app will invoke this to get trip details
    @Path("{tripId}")
    @GET
    @Produces(APPLICATION_JSON)
    public Response getTripDetails(@PathParam("tripId") UUID tripId) {
        return repository.getById(tripId)
                .map(trip -> Response.ok(trip).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response createNewTrip(Trip tripRequest) {
        Try<UUID> tripIdOrErr = repository.attemptNewTripRegistration(tripRequest);
        return tripIdOrErr.map(trackingId -> {
            logger.info("Created trip with tracking id: {} in response to a new trip request",trackingId);
            return Response.accepted().location(URI.create(trackingId.toString())).build();
        }).recover(t -> {
            logger.warn("Failed to honor a new trip request due to ", t);
            publishTripFailureEvent(new TripFailureEvent(tripRequest, CREATE_FAILED));
            return Response.serverError().build();
        });
    }

    // User has changed his/her mind and makes some change
    @Path("{tripId}")
    @PUT
    public Response updateTrip(@PathParam("tripId") UUID tripId, Trip updatedPayload) {
        Try<UUID> tripIdOrErr = repository.getById(tripId)
                .map(trip -> repository.attemptUpdate(updatedPayload))
                .get();

        return tripIdOrErr.map(trackingId -> {
            logger.info("Update request for trip with id: {} was successful",trackingId);
            return Response.ok().build();
        }).recover(t -> {
            publishTripFailureEvent(new TripFailureEvent(updatedPayload, UPDATE_FAILED));
            logger.warn("Failed to update trip with id: {} due to ",tripId,t);
            return Response.serverError().build();
        });
    }

    // We would want some service/system to react to trip failure via
    // configured business rules.
    private void publishTripFailureEvent(TripFailureEvent failureEvent) {
        logger.info("Published failure event of type: {}", failureEvent.getType());
    }
}
