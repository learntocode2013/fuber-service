package com.quintype.fuberservice.controller;

import com.jasongoodwin.monads.Try;
import com.quintype.fuberservice.booking.Trip;
import com.quintype.fuberservice.booking.UserFailureEvent;
import com.quintype.fuberservice.common.User;
import com.quintype.fuberservice.repository.RiderRepository;
import com.quintype.fuberservice.repository.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.quintype.fuberservice.booking.UserFailureEvent.Type.RIDER_CREATION_FAILURE;
import static com.quintype.fuberservice.booking.UserFailureEvent.Type.RIDER_UPDATE_FAILURE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("riders")
public class RiderService {
    private static final Logger logger = LoggerFactory.getLogger(RiderService.class.getSimpleName());

    @Inject
    private RiderRepository repository;
    @Inject
    private TripRepository tripRepository;
    @Context
    UriInfo uriInfo;

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAllRegisteredRiders() {
        List<User> registeredRiders = repository.getRegisteredRiders();
        logger.info("Fetched data of {} registered riders",registeredRiders.size());
        return Response.ok(registeredRiders).build();
    }

    @Path("{riderId}")
    @GET
    @Produces(APPLICATION_JSON)
    public Response getRiderById(@PathParam("riderId") UUID id) {
        logger.info("Received a request to fetch details for rider id: {}",id);
        return repository.getById(id)
                .map(rider -> Response.ok(rider).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @Path("{riderId}/trips")
    @GET
    @Produces(APPLICATION_JSON)
    public Response getAllTripsForRider(@PathParam("riderId") UUID id) {
        List<Trip> allTripsByRiderId = tripRepository.getAllTripsByRiderId(id);
        logger.info("Fetched {} trips for rider with id: {}", allTripsByRiderId.size(), id);
        return Response.ok().build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response createRider(User newRider) {
        Try<UUID> riderIdOrErr = repository.registerNewRider(newRider);
        return riderIdOrErr.map(riderId -> {
            logger.info("Successfully created a new rider with id: {}",riderId);
            URI locationUri = Link.fromUri(uriInfo.getAbsolutePath() + "/" + riderId.toString())
                    .build().getUri();
            return Response.created(locationUri).build();
        }).recover(t -> {
            logger.error("Failed to create a new rider due to ", t);
            publishFailureEvent(new UserFailureEvent(newRider, RIDER_CREATION_FAILURE));
            return Response.serverError().build();
        });
    }

    @Path("{riderId}")
    @PUT
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response modifyRider(@PathParam("riderId") UUID id, User updatedPayload) {
        Try<User> userOrErr = repository.getById(id)
                .map(existing -> repository.updateRider(existing.getUniqueId().get(), updatedPayload))
                .get();
        return userOrErr.map(updatedUser -> {
            UUID riderId = updatedUser.getUniqueId().get();
            URI locationUri = Link.fromUri(uriInfo.getAbsolutePath() + "/" + riderId.toString())
                    .build().getUri();
            return Response.ok().location(locationUri).build();
        }).recover(t -> {
            publishFailureEvent(new UserFailureEvent(updatedPayload,RIDER_UPDATE_FAILURE));
            return Response.serverError().build();
        });
    }

    @Path("{riderId}")
    @DELETE
    public Response deleteRider(@PathParam("riderId")UUID id) {
        return repository.getById(id)
                .map(rider -> {
                    repository.deleteRider(rider);
                    return Response.ok().build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    // We would want some service/system to react to failure via
    // configured business rules.
    private void publishFailureEvent(UserFailureEvent event) {
        // In reality, we would do something more useful than just logging
        logger.info("Failure event of type: {} was published",event.getType());
    }
}
