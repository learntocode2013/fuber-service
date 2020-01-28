package com.quintype.fuberservice.booking;

import com.quintype.fuberservice.billing.BillingUtils;
import com.quintype.fuberservice.repository.InvoiceRepository;
import com.quintype.fuberservice.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static com.quintype.fuberservice.common.Vehicle.Status.AVAILABLE;

public class TripUpdateHandler {
    private static final Logger logger = LoggerFactory.getLogger(TripUpdateHandler.class.getSimpleName());

    @Inject
    private InvoiceRepository invoiceRepository;
    @Inject
    private VehicleRepository vehicleRepository;

    public TripUpdateHandler() {
    }

    public TripUpdateHandler(InvoiceRepository invoiceRepository,VehicleRepository vehicleRepository) {
        this.invoiceRepository = invoiceRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public void handleUpdate(Trip beforeUpdate, Trip afterUpdate) {
        // There can be multiple handlers. In the interest of time, we are
        // just handling trip completion.
        switch (afterUpdate.getPresentState()) {
            case Completed:
                handleTripCompletion(beforeUpdate, afterUpdate);
                break;
            case Started:
                handleTripStarted(beforeUpdate, afterUpdate);
                break;
            case RiderCancelled:
                handleTripCancellationByRider(beforeUpdate, afterUpdate);
                break;
            case DriverCancelled:
                handleTripCancellationByDriver(beforeUpdate, afterUpdate);
                break;
            case InProgress:
                handleTripProgress(beforeUpdate, afterUpdate);
                break;
            default:
                logger.error("Cannot handle trip in state: {}", afterUpdate.getPresentState());
        }


    }

    private void handleTripProgress(Trip beforeUpdate, Trip afterUpdate) {
        // do nothing for now until we figure out what to do
    }

    private void handleTripCancellationByDriver(Trip beforeUpdate, Trip afterUpdate) {
        afterUpdate.cancel(Trip.State.DriverCancelled);
        vehicleRepository.markAvailable(afterUpdate.getAssignedVehicle());
    }

    private void handleTripCancellationByRider(Trip beforeUpdate, Trip afterUpdate) {
        afterUpdate.cancel(Trip.State.RiderCancelled);
        vehicleRepository.markAvailable(afterUpdate.getAssignedVehicle());
    }

    private void handleTripStarted(Trip beforeUpdate, Trip afterUpdate) {
        vehicleRepository.markBooked(afterUpdate.getAssignedVehicle());
        afterUpdate.start();
    }

    private void handleTripCompletion(Trip beforeUpdate, Trip afterUpdate) {
        afterUpdate.complete();
        afterUpdate.getAssignedVehicle().setStatus(AVAILABLE);
        vehicleRepository.markAvailable(afterUpdate.getAssignedVehicle());
        BillingUtils.computeBillForTrip(afterUpdate)
                .ifPresent(invoice -> invoiceRepository.saveInvoiceForTrip(afterUpdate.getTrackingId().get(),invoice));
    }
}
