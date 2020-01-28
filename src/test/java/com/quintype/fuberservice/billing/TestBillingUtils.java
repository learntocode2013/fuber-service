package com.quintype.fuberservice.billing;

import com.quintype.fuberservice.booking.Trip;
import com.quintype.fuberservice.repository.TripRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestBillingUtils {

    private static TripRepository tripRepository;

    @BeforeAll
    public static void beforeAll() {
        tripRepository = new TripRepository();
    }

    @Test
    @DisplayName("No invoice is generated for Invalid trip")
    void noInvoiceGeneratedForInvalidTrip() {
        Optional<Invoice> maybeInvoice = BillingUtils.computeBillForTrip(null);
        assertTrue(!maybeInvoice.isPresent());
    }

    @Test
    @DisplayName("Fare is 0 for invalid distance")
    void fareIsZeroForInvalidDistance() {
        double fare = BillingUtils.computeFareForDistance(-20);
        assertEquals(0,fare);
    }

    @Test
    @DisplayName("Invoice is generated for a completed trip")
    void invoiceGeneratedForValidTrip() {
        Optional<Invoice> maybeInvoice = BillingUtils.computeBillForTrip(tripRepository
                .getTripsBasedOnState(Trip.State.NotStarted).get(0));
        assertTrue(maybeInvoice.isPresent());
    }

    @Test
    @DisplayName("Fare computed is correct for valid distance")
    void correctFareIsComputed() {
        double actualFare = BillingUtils.computeFareForDistance(20);
        double baseFare = (20 * BillingUtils.PER_KM_RATE_IN_DOGECOIN);
        double expectedFare = baseFare + baseFare * BillingUtils.TAX_RATE;
        assertEquals(expectedFare,actualFare);
    }
}
