package com.quintype.fuberservice.common;

public class Constants {
    public static final String RIDE_REQUEST_PATH = "rideRequests";
    public static final String VEHICLE_PROXIMITY_MSG = "Vehicle: %s %s nearby pickup %s | Km distance from pickup: %s";
    public static final String RIDE_REQUEST_USER_UNKNOWN_MSG = "Ride request cannot be accepted for un-registered user: %s";
    public static final String RIDE_SOURCE_AND_TARGET_SAME_MSG = "Ride source and destination cannot be same";
    public static final long TWO_MINUTES_IN_MILLIS = 120_000;
}
