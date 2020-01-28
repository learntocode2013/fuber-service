package com.quintype.fuberservice.booking;

import com.quintype.fuberservice.billing.Invoice;
import com.quintype.fuberservice.repository.InvoiceRepository;
import com.quintype.fuberservice.repository.TripRepository;
import com.quintype.fuberservice.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestTripUpdateHandler {
    private TripRepository tripRepository;
    private VehicleRepository vehicleRepository;
    private TripUpdateHandler tripUpdateHandler;
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    void beforeEach() {
        tripRepository = new TripRepository();
        vehicleRepository = new VehicleRepository();
        invoiceRepository = new InvoiceRepository();
        tripUpdateHandler = new TripUpdateHandler(invoiceRepository,vehicleRepository);
    }

    @Test
    void vehicleInventoryIsUpdatedOnTripCompletion() {
        int availableCabsBeforeCompletion = vehicleRepository.getAllAvailableVehicles().get().size();
        Trip trip = tripRepository.getTripsBasedOnState(Trip.State.NotStarted).get(0);
        Optional<Invoice> maybeInvoice = invoiceRepository.getByTripId(trip.getTrackingId().get());
        assertFalse(maybeInvoice.isPresent());
        trip.complete();
        tripUpdateHandler.handleUpdate(trip, trip);
        int availableCabsAfterCompletion = vehicleRepository.getAllAvailableVehicles().get().size();
        maybeInvoice = invoiceRepository.getByTripId(trip.getTrackingId().get());
        assertTrue(availableCabsAfterCompletion > availableCabsBeforeCompletion);
        assertTrue(maybeInvoice.isPresent());
    }

    @Test
    void vehicleInventoryIsUpdatedOnTripCancellation() {
        int availableCabsBeforeCancellation = vehicleRepository.getAllAvailableVehicles().get().size();
        Trip trip = tripRepository.getTripsBasedOnState(Trip.State.NotStarted).get(0);
        Optional<Invoice> maybeInvoice = invoiceRepository.getByTripId(trip.getTrackingId().get());
        assertFalse(maybeInvoice.isPresent());
        trip.cancel(Trip.State.RiderCancelled);
        tripUpdateHandler.handleUpdate(trip, trip);
        int availableCabsAfterCancellation = vehicleRepository.getAllAvailableVehicles().get().size();
        maybeInvoice = invoiceRepository.getByTripId(trip.getTrackingId().get());
        assertTrue(availableCabsAfterCancellation > availableCabsBeforeCancellation);
        assertTrue(maybeInvoice.isPresent());
    }
}
