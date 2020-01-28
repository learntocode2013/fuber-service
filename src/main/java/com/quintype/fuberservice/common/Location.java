package com.quintype.fuberservice.common;

import java.util.StringJoiner;

public class Location {
    private Coordinate coordinate;
    private Address address;

    public Location() {
    }

    public Location(Coordinate coordinate, Address address) {
        this.coordinate = coordinate;
        this.address = address;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Address getAddress() {
        return address;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Location.class.getSimpleName() + "[", "]")
                .add("coordinate=" + coordinate)
                .add("address=" + address)
                .toString();
    }
}
