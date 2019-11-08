package com.cmput301t14.mooditude;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * MoodEvent class contains:
 * Author
 * mood
 * location
 * datetime
 * socialsituation
 * textComment
 */
public class MoodEvent implements Serializable {

    private Integer author;
    private Mood mood;
    private LocalDateTime datetime;
    private Location location;
    private SocialSituation socialSituation;
    private String textComment;
//    photoComment: Bitmap

    /**
     * MoodEvent constructor
     * @param author
     * @param mood
     * @param location
     * @param socialSituation
     * @param textComment
     */
    public MoodEvent(Integer author, Mood mood, Location location, SocialSituation socialSituation, String textComment) {
        this.author = author;
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;

        this.datetime=LocalDateTime.now();
    }

    /**
     * MoodEvent constructor
     * @param author
     * @param mood
     * @param location
     * @param socialSituation
     * @param textComment
     * @param datetime
     */
    public MoodEvent(Integer author, Mood mood, Location location, SocialSituation socialSituation, String textComment, LocalDateTime datetime) {
        this.author = author;
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;

        this.datetime=datetime;
    }

    /**
     * Author getter
     * @return
     */
    public Integer getAuthor() {
        return author;
    }

    /**
     * Author setter
     * @param author
     */
    public void setAuthor(Integer author) {
        this.author = author;
    }

    /**
     * Mood getter
     * @return
     */
    public Mood getMood() {
        return this.mood;
    }

    /**
     * mood setter
     * @param mood
     */
    public void setMood(Mood mood) {
        this.mood = mood;
    }

    /**
     * datetime getter
     * @return
     */
    public LocalDateTime getDatetime() {
        return datetime;
    }

    /**
     * datetime setter
     * @param datetime
     */
    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    /**
     * location getter
     * @return
     */
    public Location getLocation() {
        return location;
    }

    /**
     * location setter
     * @param location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * SocialSituation getter
     * @return
     */
    public SocialSituation getSocialSituation() {
        return socialSituation;
    }

    /**
     *  SocialSituation setter
     * @param socialSituation
     */
    public void setSocialSituation(SocialSituation socialSituation) {
        this.socialSituation = socialSituation;
    }

    /**
     * textComment getter
     * @return
     */
    public String getTextComment() {
        return textComment;
    }

    /**
     * textComment setter
     * @param textComment
     */
    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }
}
