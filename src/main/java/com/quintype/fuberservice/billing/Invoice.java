package com.quintype.fuberservice.billing;

import java.util.StringJoiner;

public class Invoice {
    private double distanceTravelledInKM;
    private double travelMinutes;
    private double actualFare;

    public Invoice() {
    }

    public Invoice(double distanceTravelledInKM, double travelMinutes, double actualFare) {
        this.distanceTravelledInKM = distanceTravelledInKM;
        this.travelMinutes = travelMinutes;
        this.actualFare = actualFare;
    }

    public double getDistanceTravelledInKM() {
        return distanceTravelledInKM;
    }

    public double getTravelMinutes() {
        return travelMinutes;
    }

    public double getActualFare() {
        return actualFare;
    }

    public void setDistanceTravelledInKM(double distanceTravelledInKM) {
        this.distanceTravelledInKM = distanceTravelledInKM;
    }

    public void setTravelMinutes(double travelMinutes) {
        this.travelMinutes = travelMinutes;
    }

    public void setActualFare(double actualFare) {
        this.actualFare = actualFare;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Invoice.class.getSimpleName() + "[", "]")
                .add("distanceTravelledInKM=" + distanceTravelledInKM)
                .add("travelMinutes=" + travelMinutes)
                .add("actualFare=" + actualFare)
                .toString();
    }
}
