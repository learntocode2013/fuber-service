package com.quintype.fuberservice.common;

public class UserPreference {
    private Color preferredVehicleColor;

    public UserPreference() {
    }

    public UserPreference(Color preferredVehicleColor) {
        this.preferredVehicleColor = preferredVehicleColor;
    }

    public Color getPreferredVehicleColor() {
        return preferredVehicleColor;
    }

    public void setPreferredVehicleColor(Color preferredVehicleColor) {
        this.preferredVehicleColor = preferredVehicleColor;
    }
}
