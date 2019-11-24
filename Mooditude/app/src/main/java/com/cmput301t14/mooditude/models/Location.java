package com.cmput301t14.mooditude.models;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.firebase.firestore.GeoPoint;
/**
 * Location class used to store GPS coordinates as GeoPoint
 */
public class Location {
    private Double latitude=0.0;
    private Double longtitude=0.0;
    private String address ="";

    GeoPoint geopoint;

    public Location(){
        this.geopoint=null;
    }


    /**
     * Location Constructor
     * @param latitude, longtitude
     */

    public Location(Double latitude, Double longtitude) {
//        this.latitude = latitude;
//        this.longtitude = longtitude;
        geopoint= new GeoPoint(latitude,longtitude);
    }
    /**
     * Location Constructor
     * @param latitude, longtitude,addres
     */
    public Location(Double latitude, Double longtitude, String addres) {
//        this.latitude = latitude;
//        this.longtitude = longtitude;
        this.address = addres;
        geopoint= new GeoPoint(latitude,longtitude);
    }

    /**
     * Location Constructor
     * @param geopoint
     */
    public Location(GeoPoint geopoint ) {
        this.geopoint= geopoint;
    }

    /**
     * get the getGeopoint for the mood
     * @return
     */
    public GeoPoint getGeopoint() {
        return geopoint;
    }


    /**
     * get the getAddress for the mood
     * @return
     */

    public String getAddress() {
        return address;
    }


    /**
     * get the setAddress for the mood
     * @param address
     * @return
     */
    public void setAddress(String address) {
        this.address = address;
    }


}
