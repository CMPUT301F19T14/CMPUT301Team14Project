package com.cmput301t14.mooditude.models;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.firebase.firestore.GeoPoint;
/**
 * Location class used to store GPS coordinates as GeoPoint
 */

public class Location implements Parcelable {
    private String address;
    private GeoPoint geopoint;

    /**
     * Location Constructor
     * @param latitude, longtitude
     */

    public Location(Double latitude, Double longtitude) {
//        this.latitude = latitude;
//        this.longtitude = longtitude;
        address ="";
        geopoint= new GeoPoint(latitude,longtitude);
    }
    /**
     * Location Constructor
     * @param latitude, longtitude,addres
     */
    public Location(Double latitude, Double longtitude, String addres) {
        this.address = addres;
        geopoint= new GeoPoint(latitude,longtitude);
    }

    /**
     * Location Constructor
     * @param geopoint geopoint of location
     */
    public Location(GeoPoint geopoint ) {
        address ="";
        this.geopoint= geopoint;
    }

    /**
     * get the getGeopoint for the mood
     * @return geopoint of location
     */
    public GeoPoint getGeopoint() {
        return geopoint;
    }


    /**
     * get the getAddress for the mood
     * @return the address of the location
     */
    public String getAddress() {
        return address;
    }


    /**
     * get the setAddress for the mood
     * @param address passed address
     */
    public void setAddress(String address) {
        this.address = address;
    }


    /**
     * Location Constructor From Parcel
     * @param in
     */
    private Location(Parcel in) {
        address = in.readString();
        double lat = in.readDouble();
        double lng = in.readDouble();
        geopoint = new GeoPoint(lat, lng);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeDouble(geopoint.getLatitude());
        dest.writeDouble(geopoint.getLongitude());
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}