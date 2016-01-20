package com.aashishgodambe.android.showstores.Model;

/**
 * Created by Aashish on 1/19/2016.
 */
public class Item {

    private String details;
    private double latitude;
    private double longitude;
    private String style;

    public Item() {
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Item{" +
                "details='" + details + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", style='" + style + '\'' +
                '}';
    }
}
