package com.cmput301t14.mooditude.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;
/**
 * Location class used to store GPS coordinates as GeoPoint
 */
public class Location implements Parcelable {
    //    private Double latitude=0.0;
//    private Double longtitude=0.0;
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



    protected Location(Parcel in) {
        address = in.readString();
        geopoint = (GeoPoint) in.readValue(GeoPoint.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeValue(geopoint);
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