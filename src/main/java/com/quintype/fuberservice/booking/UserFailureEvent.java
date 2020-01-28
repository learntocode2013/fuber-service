package com.quintype.fuberservice.booking;

import com.quintype.fuberservice.common.User;

public class UserFailureEvent {
    public enum Type {
        RIDER_CREATION_FAILURE,
        RIDER_UPDATE_FAILURE,
        RIDER_DELETION_FAILURE,
        DRIVER_CREATION_FAILURE,
        DRIVER_UPDATE_FAILURE,
        DRIVER_DELETION_FAILURE
    }
    private final User failureContext;
    private final Type type;

    public UserFailureEvent(User failureContext, Type type) {
        this.failureContext = failureContext;
        this.type = type;
    }

    public User getFailureContext() {
        return failureContext;
    }

    public Type getType() {
        return type;
    }
}
