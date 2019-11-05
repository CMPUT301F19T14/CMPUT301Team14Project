package com.cmput301t14.mooditude;

import android.graphics.Color;

import java.util.HashMap;

public class Mood {
    enum MoodEnum {HAPPY, SAD, ANGRY, EXCITED};

    private HashMap<MoodEnum, Integer> colorMap;
    private HashMap<MoodEnum, String> emoticonMap;

    private MoodEnum moodEnum;

    /**
     * Constructor
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

    public String getMood(){
        return this.moodEnum.toString();
    }

    public void setMood(String moodString){
        this.moodEnum = MoodEnum.valueOf(moodString);
    }

    public int getColor(){
        return this.colorMap.get(moodEnum);
    }

    public String getEmoticon(){
        return this.emoticonMap.get(moodEnum);
    }

}