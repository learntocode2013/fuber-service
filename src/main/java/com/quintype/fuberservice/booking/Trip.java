package com.quintype.fuberservice.booking;

import com.quintype.fuberservice.common.Location;
import com.quintype.fuberservice.common.Utils;
import com.quintype.fuberservice.common.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

import static com.quintype.fuberservice.common.Constants.TWO_MINUTES_IN_MILLIS;

public class Trip {
    private static final Logger logger = LoggerFactory.getLogger(Trip.class.getSimpleName());

    public enum State {
        NotStarted,
        Started,
        InProgress,
        Completed,
        RiderCancelled,
        DriverCancelled
    }

    private Location source;
    private Location destination;
    private Vehicle assignedVehicle;
    private UUID riderId;
    private double estimatedFare;
    private UUID trackingId;
    private State presentState = State.NotStarted;
    private double fareOnCompletion;
    private double distanceCoveredInKm;
    private long startTime;
    private long endTime;
    private long timeTakenInMillis;

    public void setSource(Location source) {
        this.source = source;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public void setAssignedVehicle(Vehicle assignedVehicle) {
        this.assignedVehicle = assignedVehicle;
    }

    public void setEstimatedFare(double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    public void setPresentState(State presentState) {
        this.presentState = presentState;
    }

    public void setFareOnCompletion(double fareOnCompletion) {
        this.fareOnCompletion = fareOnCompletion;
    }

    public void setDistanceCoveredInKm(double distanceCoveredInKm) {
        this.distanceCoveredInKm = distanceCoveredInKm;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setTimeTakenInMillis(long timeTakenInMillis) {
        this.timeTakenInMillis = timeTakenInMillis;
    }

    public Trip() {
    }

    public Trip(Location source, Location destination, Vehicle assignedVehicle, UUID riderId, double estimatedFare) {
        this.source = source;
        this.destination = destination;
        this.assignedVehicle = assignedVehicle;
        this.riderId = riderId;
        this.estimatedFare = estimatedFare;
    }

    public Trip cancel(State cancellationType) {
        presentState = cancellationType;
        logger.info("Trip: {} was cancelled", trackingId);
        startTime = endTime = System.currentTimeMillis();
        setDistanceCoveredInKm(Utils.compute_KM_DistanceBetween(source.getCoordinate(),destination.getCoordinate()));
        return this;
    }

    public Trip start() {
        presentState = State.Started;
        logger.info("Trip: {} was started", trackingId);
        startTime = System.currentTimeMillis();
        return this;
    }

    public Trip complete() {
        presentState = State.Completed;
        // simulate travel time.
        endTime = startTime + TWO_MINUTES_IN_MILLIS;
        timeTakenInMillis = endTime - startTime;
        setDistanceCoveredInKm(Utils.compute_KM_DistanceBetween(source.getCoordinate(),destination.getCoordinate()));
        return this;
    }

    public UUID getRiderId() {
        return riderId;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setRiderId(UUID riderId) {
        this.riderId = riderId;
    }

    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }

    public Location getSource() {
        return source;
    }

    public Location getDestination() {
        return destination;
    }

    public Vehicle getAssignedVehicle() {
        return assignedVehicle;
    }

    public double getEstimatedFare() {
        return estimatedFare;
    }

    public Optional<UUID> getTrackingId() {
        return Optional.ofNullable(trackingId);
    }

    public State getPresentState() {
        return presentState;
    }

    public double getFareOnCompletion() {
        return fareOnCompletion;
    }

    public double getDistanceCoveredInKm() {
        return distanceCoveredInKm;
    }

    public long getTimeTakenInMillis() {
        return timeTakenInMillis;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Trip.class.getSimpleName() + "[", "]")
                .add("source=" + source)
                .add("destination=" + destination)
                .add("assignedVehicle=" + assignedVehicle)
                .add("riderId=" + riderId)
                .add("estimatedFare=" + estimatedFare)
                .add("trackingId=" + trackingId)
                .add("presentState=" + presentState)
                .add("fareOnCompletion=" + fareOnCompletion)
                .add("distanceCoveredInKm=" + distanceCoveredInKm)
                .add("startTime=" + startTime)
                .add("endTime=" + endTime)
                .add("timeTakenInMillis=" + timeTakenInMillis)
                .toString();
    }
}
