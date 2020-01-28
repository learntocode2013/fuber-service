package com.quintype.fuberservice.booking;

import com.quintype.fuberservice.common.Location;
import com.quintype.fuberservice.common.UserPreference;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

import static com.quintype.fuberservice.booking.RideRequest.State.Cancelled;
import static com.quintype.fuberservice.booking.RideRequest.State.Completed_VehicleAssigned;
import static com.quintype.fuberservice.booking.RideRequest.State.Completed_VehicleNotAvailable;
import static com.quintype.fuberservice.booking.RideRequest.State.Searching;
import static com.quintype.fuberservice.booking.RideRequest.State.TimedOut;

public class RideRequest {
    public enum State {
        Searching,
        // Terminal states
        Completed_VehicleNotAvailable,
        Completed_VehicleAssigned,
        Cancelled,
        TimedOut
    }

    private UUID trackingId; // We need this for tracking and reporting
    private State requestState = Searching;
    private Location source;

    public void setSource(Location source) {
        this.source = source;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public void setTripAssigned(Trip tripAssigned) {
        this.tripAssigned = tripAssigned;
    }

    private Location destination;
    private UUID riderId;
    private UserPreference userPreference; // preference can change between rides, so we keep it here
    private Trip tripAssigned; // Not every request will result in a trip

    public RideRequest() {
    }

    public RideRequest(Location source, Location destination, UUID riderId) {
        this.source = source;
        this.destination = destination;
        this.riderId = riderId;
    }

    public State getRequestState() {
        return requestState;
    }

    public Location getSource() {
        return source;
    }

    public Location getDestination() {
        return destination;
    }

    public Optional<UUID> getTrackingId() {
        return Optional.ofNullable(trackingId);
    }

    public RideRequest setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
        return this;
    }

    public RideRequest setUserPreference(UserPreference userPreference) {
        this.userPreference = userPreference;
        return this;
    }

    public RideRequest assignTrip(Trip tripAssigned) {
        this.tripAssigned = tripAssigned;
        setRequestState(Completed_VehicleAssigned);
        return this;
    }

    public RideRequest cancel() {
        setRequestState(Cancelled);
        return this;
    }

    public RideRequest abort() {
        setRequestState(TimedOut);
        return this;
    }

    public RideRequest close() {
        setRequestState(Completed_VehicleNotAvailable);
        return this;
    }

    public UUID getRiderId() {
        return riderId;
    }

    public void setRiderId(UUID riderId) {
        this.riderId = riderId;
    }

    public Optional<UserPreference> getUserPreference() {
        return Optional.ofNullable(userPreference);
    }

    public Optional<Trip> getTripAssigned() {
        return Optional.ofNullable(tripAssigned);
    }

    private void setRequestState(State requestState) {
        this.requestState = requestState;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RideRequest.class.getSimpleName() + "[", "]")
                .add("trackingId=" + trackingId)
                .add("requestState=" + requestState)
                .add("source=" + source)
                .add("destination=" + destination)
                .add("riderId=" + riderId)
                .add("userPreference=" + userPreference)
                .add("tripAssigned=" + tripAssigned)
                .toString();
    }
}
