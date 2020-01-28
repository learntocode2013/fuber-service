package com.quintype.fuberservice.common;

public class Utils {
    public static double compute_KM_DistanceBetween(Coordinate pickup, Coordinate cabLocation) {
        double latDistance = Math.pow(pickup.getLatitude() - cabLocation.getLatitude(),2);
        double lngDistance = Math.pow(pickup.getLongitude() - cabLocation.getLongitude(),2);
        return Math.sqrt(latDistance + lngDistance);
    }
}
