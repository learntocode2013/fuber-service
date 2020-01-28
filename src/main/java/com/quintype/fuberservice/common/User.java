package com.quintype.fuberservice.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

public class User {
    public enum Type {
        Rider, Driver, Supervisor, HelpAgent
    }
    private String name;
    private String contactNumber;
    private String emailAddress;
    private Type userType;
    private UUID uniqueId;
    private List<Address> addresses = new ArrayList<>();

    public User() {
    }

    public User(String name, String contactNumber, Address address, String emailAddress, Type userType) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
        this.userType = userType;
        addresses.add(address);
    }

    public void addAddress(Address anAddress) {
        addresses.add(anAddress);
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Type getUserType() {
        return userType;
    }

    public Optional<UUID> getUniqueId() {
        return Optional.ofNullable(uniqueId);
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public User setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
        return this;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setUserType(Type userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("contactNumber='" + contactNumber + "'")
                .add("userType=" + userType)
                .add("uniqueId=" + uniqueId)
                .toString();
    }
}
