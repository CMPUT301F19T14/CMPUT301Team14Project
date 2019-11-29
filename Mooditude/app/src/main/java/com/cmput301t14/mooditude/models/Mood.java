package com.cmput301t14.mooditude.models;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class Mood to store the pre-defined mood and their color, emoticon
 */
public class Mood implements Parcelable {
    private enum MoodEnum {HAPPY, SAD, ANGRY, EXCITED};

    private HashMap<MoodEnum, Integer> colorMap;
    private HashMap<MoodEnum, String> emoticonMap;

    private MoodEnum moodEnum;

    /**
     * Mood Constructor
     * @param moodString mood representation
     */
    public Mood(String moodString){
        this.moodEnum = MoodEnum.valueOf(moodString);

        this.colorMap = new HashMap<>();
        colorMap.put(MoodEnum.HAPPY, Color.GREEN);
        colorMap.put(MoodEnum.SAD, Color.BLUE);
        colorMap.put(MoodEnum.ANGRY, Color.RED);
        colorMap.put(MoodEnum.EXCITED, Color.YELLOW);

        this.emoticonMap = new HashMap<>();
        emoticonMap.put(MoodEnum.HAPPY, "\uD83D\uDE03");
        emoticonMap.put(MoodEnum.SAD, "\uD83D\uDE41");
        emoticonMap.put(MoodEnum.ANGRY, "\uD83D\uDE20");
        emoticonMap.put(MoodEnum.EXCITED, "\uD83D\uDE0E");
    }

    /**
     * get Mood as a string
     * @return mood string representation
     */
    public String getMood(){
        return this.moodEnum.toString();
    }

    /**
     * Set mood with a string
     * @param moodString - a string represents one of the defined mood
     */
    public void setMood(String moodString){
        this.moodEnum = MoodEnum.valueOf(moodString);
    }

    /**
     * get the Color for the mood
     * @return color of mood event
     */
    public int getColor(){
        return this.colorMap.get(moodEnum);
    }

    /**
     * get the Emoticon for the mood
     * @return emoticon of mood event
     */
    public String getEmoticon(){
        return this.emoticonMap.get(moodEnum);
    }

    protected Mood(Parcel in) {
        colorMap = (HashMap) in.readValue(HashMap.class.getClassLoader());
        emoticonMap = (HashMap) in.readValue(HashMap.class.getClassLoader());
        moodEnum = (MoodEnum) in.readValue(MoodEnum.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(colorMap);
        dest.writeValue(emoticonMap);
        dest.writeValue(moodEnum);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Mood> CREATOR = new Parcelable.Creator<Mood>() {
        @Override
        public Mood createFromParcel(Parcel in) {
            return new Mood(in);
        }

        @Override
        public Mood[] newArray(int size) {
            return new Mood[size];
        }
    };


    /**
     * Get Google Map Marker Color based on Mood
     * @param mood mood
     * @return color hash map of marker on map
     */
    public static Float getMoodMapMarkerColor(Mood mood){

        Map<String,Float> markerColorHashMap = new HashMap<>();
        markerColorHashMap.put("HAPPY", BitmapDescriptorFactory.HUE_GREEN);
        markerColorHashMap.put("EXCITED", BitmapDescriptorFactory.HUE_YELLOW);
        markerColorHashMap.put("SAD", BitmapDescriptorFactory.HUE_VIOLET);
        markerColorHashMap.put("ANGRY", BitmapDescriptorFactory.HUE_RED);


        return markerColorHashMap.get(mood.getMood());
    }

}