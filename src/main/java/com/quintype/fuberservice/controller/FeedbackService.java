package com.quintype.fuberservice.controller;

import com.quintype.fuberservice.common.Feedback;
import com.quintype.fuberservice.repository.FeedbackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("reviews")
public class FeedbackService {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class.getSimpleName());
    @Inject
    private FeedbackRepository reviewRepository;

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAllReviews() {
        return Response.ok(reviewRepository.getAllSubmittedFeedback()).build();
    }

    @Path("{reviewId}")
    @GET
    @Produces(APPLICATION_JSON)
    public Response getById(@PathParam("reviewId") UUID id) {
        return reviewRepository.getById(id)
                .map(review -> Response.ok(review).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createReview(Feedback review) {
        UUID reviewId = reviewRepository.saveFeedback(review);
        logger.info("User review with id: {} was saved", reviewId);
        return Response.accepted().location(URI.create(reviewId.toString())).build();
    }
}
