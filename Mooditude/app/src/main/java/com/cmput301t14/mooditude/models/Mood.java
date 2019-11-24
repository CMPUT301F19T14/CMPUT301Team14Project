package com.cmput301t14.mooditude.models;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class Mood to store the pre-defined mood and their color, emoticon
 */
public class Mood {
    private enum MoodEnum {HAPPY, SAD, ANGRY, EXCITED};

    private HashMap<MoodEnum, Integer> colorMap;
    private HashMap<MoodEnum, String> emoticonMap;

    private MoodEnum moodEnum;

    /**
     * Mood Constructor
     * @param moodString
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
     * @return
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
     * @return
     */
    public int getColor(){
        return this.colorMap.get(moodEnum);
    }

    /**
     * get the Emoticon for the mood
     * @return
     */
    public String getEmoticon(){
        return this.emoticonMap.get(moodEnum);
    }

    public static Float getMoodMapMarkerColor(Mood mood){

        Map<String,Float> markerColorHashMap = new HashMap<>();
        markerColorHashMap.put("HAPPY", BitmapDescriptorFactory.HUE_GREEN);
        markerColorHashMap.put("EXCITED", BitmapDescriptorFactory.HUE_YELLOW);
        markerColorHashMap.put("SAD", BitmapDescriptorFactory.HUE_VIOLET);
        markerColorHashMap.put("ANGRY", BitmapDescriptorFactory.HUE_RED);


        return markerColorHashMap.get(mood.getMood());
    }

}