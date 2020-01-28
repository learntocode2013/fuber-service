package com.quintype.fuberservice.repository;

import com.quintype.fuberservice.billing.Invoice;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class InvoiceRepository {
    private final Map<UUID, Invoice> invoiceByTripId = new HashMap<>();

    public InvoiceRepository() {
    }

    public void saveInvoiceForTrip(UUID tripId, Invoice invoice) {
        invoiceByTripId.put(tripId, invoice);
    }

    public Optional<Invoice> getByTripId(UUID tripId) {
        return Optional.ofNullable(invoiceByTripId.get(tripId));
    }
}
