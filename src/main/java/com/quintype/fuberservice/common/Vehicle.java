package com.quintype.fuberservice.common;

import java.util.Objects;
import java.util.StringJoiner;

public class Vehicle {
    public enum Type {
        HATCHBACK,
        SEDAN,
        SUV
    }

    public enum Status {
        AVAILABLE,
        BOOKED_FOR_RIDE
    }

    private Type type;
    private String vehicleNumber;
    private Coordinate presentLocation;
    private Color color;
    private User driver;
    private Status status = Status.AVAILABLE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return getVehicleNumber().equals(vehicle.getVehicleNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVehicleNumber());
    }

    public Vehicle() {
    }

    public Vehicle(Type type, String vehicleNumber, Coordinate presentLocation, Color color, User driver) {
        this.type = type;
        this.vehicleNumber = vehicleNumber;
        this.presentLocation = presentLocation;
        this.color = color;
        this.driver = driver;
    }

    public Type getType() {
        return type;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public Coordinate getPresentLocation() {
        return presentLocation;
    }

    public Color getColor() {
        return color;
    }

    public User getDriver() {
        return driver;
    }

    public Status getStatus() {
        return status;
    }

    public Vehicle setStatus(Status status) {
        this.status = status;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setPresentLocation(Coordinate presentLocation) {
        this.presentLocation = presentLocation;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Vehicle.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("vehicleNumber='" + vehicleNumber + "'")
                .add("presentLocation=" + presentLocation)
                .add("color=" + color)
                .add("driver=" + driver)
                .add("status=" + status)
                .toString();
    }
}
