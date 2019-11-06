package com.cmput301t14.mooditude;

import java.time.LocalDateTime;

public class MoodEvent {

    private Integer author;
    private Mood mood;
    private LocalDateTime datetime;
    private Location location;
    private SocialSituation socialSituation;
    private String textComment;
//    photoComment: Bitmap


    public MoodEvent(Integer author, Mood mood, Location location, SocialSituation socialSituation, String textComment) {
        this.author = author;
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;

        this.datetime=LocalDateTime.now();
    }

    public MoodEvent(Integer author, Mood mood, Location location, SocialSituation socialSituation, String textComment, LocalDateTime datetime) {
        this.author = author;
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;

        this.datetime=datetime;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Mood getMood() {
        return this.mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public SocialSituation getSocialSituation() {
        return socialSituation;
    }

    public void setSocialSituation(SocialSituation socialSituation) {
        this.socialSituation = socialSituation;
    }

    public String getTextComment() {
        return textComment;
    }

    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }
}
