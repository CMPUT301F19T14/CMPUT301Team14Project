package com.cmput301t14.mooditude;

import com.google.firebase.firestore.GeoPoint;

public class Location {
    private Double latitude=0.0;
    private Double longtitude=0.0;

    private String address ="";

    GeoPoint geopoint;

    public Location(Double latitude, Double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;

        geopoint= new GeoPoint(latitude,longtitude);
    }

    public Location(Double latitude, Double longtitude, String addres) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.address = addres;
        geopoint= new GeoPoint(latitude,longtitude);
    }

    public Location(GeoPoint geopoint ) {

        this.geopoint= geopoint;
    }

    public GeoPoint getGeopoint() {
        return geopoint;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
