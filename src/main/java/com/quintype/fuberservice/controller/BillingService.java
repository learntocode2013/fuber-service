package com.quintype.fuberservice.controller;

import com.quintype.fuberservice.billing.BillingUtils;
import com.quintype.fuberservice.repository.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/bill")
public class BillingService {
    @Inject
    private TripRepository tripRepository;


    @Path("{tripId}")
    @GET
    @Produces(APPLICATION_JSON)
    public Response getBillForTrip(@PathParam("tripId")UUID id) {
        return tripRepository.getById(id)
                .map(trip -> BillingUtils.computeBillForTrip(trip))
                .map(invoice -> Response.ok(invoice).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
