package com.cmput301t14.mooditude;

public class Location {
    private Double latitude=0.0;
    private Double longtitude=0.0;

    private String address ="";

    public Location(Double latitude, Double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public Location(Double latitude, Double longtitude, String addres) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.address = addres;
    }

    public Location(String addres) {
        this.address = addres;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
