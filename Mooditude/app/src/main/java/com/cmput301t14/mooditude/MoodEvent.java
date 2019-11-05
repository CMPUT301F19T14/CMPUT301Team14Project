package com.cmput301t14.mooditude;

import java.time.LocalDateTime;

public class MoodEvent {

    private  Integer author;
    private Mood mood;
    private LocalDateTime datetime;
    private Location location;
    private SocialSituation socialSituation;
    private String textComment;
//    photoComment: Bitmap

    private static final int LENGTH_COMMENT=20;


    public MoodEvent(Integer author) {
        this.author = author;
        this.datetime= LocalDateTime.now();
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Double latitude, Double longtitude) {
        this.location = new Location(latitude,longtitude);
    }

    public String getTextComment() {
        return textComment;
    }

    public void setTextComment(String textComment) {

        if(textComment<=LENGTH_COMMENT){
            this.textComment = textComment;
            test
        }

        this.textComment=textComment.substring(0,LENGTH_COMMENT);



    }
}
