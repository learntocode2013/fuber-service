package com.quintype.fuberservice.booking;

import com.quintype.fuberservice.repository.RideRequestRepository;
import com.quintype.fuberservice.repository.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public class RequestProcessor {
    private static final Logger logger = LoggerFactory.getLogger(RequestProcessor.class.getSimpleName());
    private final ExecutorService workerPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Inject
    private RideRequestRepository rideRequestRepository;
    @Inject
    private TripRepository tripRepository;

    public RequestProcessor() {
    }

    public void process(RideRequest rideRequest) {
        logger.info("Processing a new ride request with : {}",rideRequest);
        // Save the ride request for later reference
        rideRequestRepository.registerNewRideRequest(rideRequest);
        RideRequestHandler requestHandler = new RideRequestHandler(rideRequest,tripRepository);
        CompletableFuture.runAsync(requestHandler,workerPool)
                .whenComplete((v,err) -> {
                    if (err != null) {
                        logger.error("Failed to service request with id: {} due to",rideRequest.getTrackingId().get(),err);
                    } else {
                        //
                    }
                });
    }
}
