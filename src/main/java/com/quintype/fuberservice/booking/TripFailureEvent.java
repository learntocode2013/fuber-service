package com.quintype.fuberservice.booking;

public class TripFailureEvent {
    public enum Type {
        CREATE_FAILED,
        UPDATE_FAILED
    }
    private final Trip failureContext;
    private final Type type;

    public TripFailureEvent(Trip failureContext, Type type) {
        this.failureContext = failureContext;
        this.type = type;
    }

    public Trip getFailureContext() {
        return failureContext;
    }

    public Type getType() {
        return type;
    }
}
