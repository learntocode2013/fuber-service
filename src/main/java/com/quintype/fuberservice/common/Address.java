package com.quintype.fuberservice.common;

import java.util.Optional;
import java.util.StringJoiner;

import static com.quintype.fuberservice.common.Address.Order.*;
import static com.quintype.fuberservice.common.Address.Type.*;

public class Address {
    public enum Type {
        Home,
        Office,
        Outside
    }

    public enum Order {
        Primary,
        Secondary
    }
    private String houseNo;
    private String propertyName;
    private String area;
    private int pinCode;
    private String state;
    private String cityOrTown;
    private Type type = Home;
    private Order order = Primary;

    private String landmark;

    // Need for marshalling/un-marshalling
    public Address() {
    }

    public Address(String houseNo, String propertyName, String area, int pinCode, String state, String cityOrTown, Type type) {
        this.houseNo = houseNo;
        this.propertyName = propertyName;
        this.area = area;
        this.pinCode = pinCode;
        this.state = state;
        this.cityOrTown = cityOrTown;
        this.type = type;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getArea() {
        return area;
    }

    public int getPinCode() {
        return pinCode;
    }

    public String getState() {
        return state;
    }

    public String getCityOrTown() {
        return cityOrTown;
    }

    public Type getType() {
        return type;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCityOrTown(String cityOrTown) {
        this.cityOrTown = cityOrTown;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Optional<String> getLandmark() {
        return Optional.ofNullable(landmark);
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Address.class.getSimpleName() + "[", "]")
                .add("houseNo='" + houseNo + "'")
                .add("propertyName='" + propertyName + "'")
                .add("area='" + area + "'")
                .add("pinCode=" + pinCode)
                .add("state='" + state + "'")
                .add("cityOrTown='" + cityOrTown + "'")
                .add("type=" + type)
                .add("order=" + order)
                .add("landmark='" + landmark + "'")
                .toString();
    }
}
