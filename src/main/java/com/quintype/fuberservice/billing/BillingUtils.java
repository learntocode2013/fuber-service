package com.quintype.fuberservice.billing;

import com.quintype.fuberservice.booking.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.*;

public class BillingUtils {
    private static final Logger logger = LoggerFactory.getLogger(BillingUtils.class.getSimpleName());

    public static final int PER_KM_RATE_IN_DOGECOIN = 2;
    public static final int PER_MIN_RATE_IN_DOGECOIN = 1;
    public static final float TAX_RATE = 18.2f;

    public static Optional<Invoice> computeBillForTrip(Trip completedTrip) {
        if (isNull(completedTrip)) { return Optional.empty(); }
        double kmFare = completedTrip.getDistanceCoveredInKm() * PER_KM_RATE_IN_DOGECOIN;
        double timeFare = (completedTrip.getTimeTakenInMillis()/60000) * PER_MIN_RATE_IN_DOGECOIN;

        double journeyFare = Math.max(kmFare, timeFare);
        double applicableTaxAmt = journeyFare * TAX_RATE;
        double totalFare = journeyFare + applicableTaxAmt;
        logger.info("Trip id: {} | travel distance: {} | minutes travelled: {} | fare: {}",
                completedTrip.getTrackingId().get(),
                completedTrip.getDistanceCoveredInKm(),
                completedTrip.getTimeTakenInMillis()/60000,
                totalFare);
        return Optional.of(new Invoice(completedTrip.getDistanceCoveredInKm(),
                completedTrip.getTimeTakenInMillis()/60000,totalFare));
    }

    public static double computeFareForDistance(double distanceInKM) {
        if (distanceInKM <= 0) { return 0; }
        double journeyFare = distanceInKM * PER_KM_RATE_IN_DOGECOIN;
        double applicableTaxAmt = journeyFare * TAX_RATE;
        return journeyFare + applicableTaxAmt;
    }
}
