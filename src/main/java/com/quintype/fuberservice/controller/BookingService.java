package com.quintype.fuberservice.controller;

import com.quintype.fuberservice.api.errors.BadInputPayload;
import com.quintype.fuberservice.booking.RequestProcessor;
import com.quintype.fuberservice.booking.RideRequest;
import com.quintype.fuberservice.repository.RideRequestRepository;
import com.quintype.fuberservice.repository.RiderRepository;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.quintype.fuberservice.common.Constants.RIDE_REQUEST_PATH;
import static com.quintype.fuberservice.common.Constants.RIDE_REQUEST_USER_UNKNOWN_MSG;
import static com.quintype.fuberservice.common.Constants.RIDE_SOURCE_AND_TARGET_SAME_MSG;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path(RIDE_REQUEST_PATH)
public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class.getSimpleName());

    @Inject
    private RiderRepository riderRepository;
    @Inject
    private RideRequestRepository rideRequestRepository;
    @Inject
    private RequestProcessor requestProcessor;
    @Context
    UriInfo uriInfo;

    @POST
    @Consumes(APPLICATION_JSON)
    public Response createNewRideRequest(RideRequest rideRequest) {
        return getResponseMsgForBadInputPayload(rideRequest)
                .map(errMsg -> Response.status(Response.Status.BAD_REQUEST).entity(errMsg).build())
                .orElseGet(() -> {
                    UUID generatedReqId = UUID.randomUUID();
                    rideRequest.setTrackingId(generatedReqId);
                    logger.info("Submitted ride request: {} for processing", rideRequest);
                    requestProcessor.process(rideRequest);
                    URI locationUri = Link.fromUri(uriInfo.getAbsolutePath() + "/" + generatedReqId.toString())
                            .build().getUri();
                    return Response.accepted().location(locationUri).build();
                });
    }

    @Path("{rideId}")
    @GET
    @Produces(APPLICATION_JSON)
    public Response getRideRequestById(@PathParam("rideId")UUID id) {
        return rideRequestRepository.getById(id)
                .map(rideRequest -> {
                    // If a ride request is converted to a trip, we send the trip URI in the
                    // location header.
                    rideRequest.getTripAssigned().map(trip -> {
                        URI tripUri = Link.fromUri(uriInfo.getBaseUri() + "trips" + "/"
                                + trip.getTrackingId().get().toString())
                                .build().getUri();
                        logger.info("Assigned trip is available in URI: {} in location header",tripUri);
                        return Response.ok(rideRequest).header("Location",tripUri).build();
                    });
                    return Response.ok(rideRequest).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAllRides() {
        List<RideRequest> allRideRequests = rideRequestRepository.getAllRideRequests();
        logger.info("{} ride requests were fetched",allRideRequests.size());
        return Response.ok(allRideRequests).build();
    }

    @Path("{rideId}")
    @PUT
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response updateRideRequest(@PathParam("rideId")UUID id, RideRequest modifiedRequest) {
        logger.info("Received request to update ride request with id: {}",id);
        rideRequestRepository.updateRideRequest(modifiedRequest);
        logger.info("Ride request: {} was updated. State after update - {}",
                modifiedRequest.getTrackingId(), modifiedRequest.getRequestState());
        URI locationUri = Link.fromUri(uriInfo.getAbsolutePath() + "/" + modifiedRequest.getTrackingId()
                .toString()).build().getUri();
        return Response.ok().location(locationUri).build();
    }

    private Optional<BadInputPayload> getResponseMsgForBadInputPayload(RideRequest rideRequest) {
        if (isRequestFromUnRegisteredUser(rideRequest)) {
            logger.warn("Rejecting ride request for un-registered user: {}",rideRequest.getRiderId());
            String errMsg = String.format(RIDE_REQUEST_USER_UNKNOWN_MSG, rideRequest.getRiderId().toString());
            return Optional.of(new BadInputPayload(errMsg));
        }

        if (sourceAndDestinationAreSame(rideRequest)) {
            logger.warn("Rejecting ride request since source:{} and destination:{} are same",
                    rideRequest.getSource().getCoordinate(),rideRequest.getSource().getCoordinate());
            return Optional.of(new BadInputPayload(RIDE_SOURCE_AND_TARGET_SAME_MSG));
        }

        return Optional.empty();
    }

    // In reality, this will typically be an external service invocation
    private boolean isRequestFromUnRegisteredUser(RideRequest rideRequest) {
        return !riderRepository.getById(rideRequest.getRiderId()).isPresent();
    }

    // This can be validated at the app side as well
    private boolean sourceAndDestinationAreSame(RideRequest rideRequest) {
        return rideRequest.getSource().getCoordinate()
                .equals(rideRequest.getDestination().getCoordinate());
    }
}
