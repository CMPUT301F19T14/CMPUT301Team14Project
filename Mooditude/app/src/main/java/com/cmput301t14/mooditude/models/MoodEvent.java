package com.cmput301t14.mooditude.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

/**
 * MoodEvent class contains:
 * Author
 * mood
 * location
 * datetime
 * socialsituation
 * textComment
 */
public class MoodEvent implements Parcelable {

    private String author;
    private Mood mood;
    private Timestamp datetime;
    private Location location;
    private SocialSituation socialSituation;
    private String textComment;
    private String photoUrl;
//    photoComment: Bitmap

    /**
     * MoodEvent constructor
     * @param mood
     * @param location
     * @param socialSituation
     * @param textComment
     */
    public MoodEvent(String author,Mood mood, Location location, SocialSituation socialSituation, String textComment) {
        this.author = author;
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
//        this.datetime=LocalDateTime.now();
        this.datetime= Timestamp.now();
    }

    public MoodEvent(Mood mood, Location location, SocialSituation socialSituation, String textComment) {
        this.author = "";
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
//        this.datetime=LocalDateTime.now();
        this.datetime= Timestamp.now();
    }

    public MoodEvent(Mood mood, Location location, SocialSituation socialSituation, String textComment, String photoUrl) {
        this.author = "";
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.photoUrl = photoUrl;
//        this.datetime=LocalDateTime.now();
        this.datetime= Timestamp.now();
    }



    /**
     * MoodEvent constructor
     * @param mood
     * @param location
     * @param socialSituation
     * @param textComment
     * @param timestamp
     */
    public MoodEvent(String author, Mood mood, Location location, SocialSituation socialSituation, String textComment, Timestamp timestamp, String photoUrl) {
        this.author = author;
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.datetime=timestamp;
        this.photoUrl = photoUrl;

    }
    public MoodEvent(Mood mood, Location location, SocialSituation socialSituation, String textComment, Timestamp timestamp) {
        this.author = "";
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.datetime=timestamp;
    }

    public MoodEvent(Mood mood, Location location, SocialSituation socialSituation, String textComment, Timestamp timestamp, String photoUrl) {
        this.author = "";
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.datetime=timestamp;
        this.photoUrl = photoUrl;
    }

    public MoodEvent(String author, Mood mood, Location location, SocialSituation socialSituation, String textComment, Timestamp timestamp) {
        this.author = author;
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.datetime=timestamp;
    }

    public String getAuthor() {
        return author;
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
    public Timestamp getDatetime() {
        return datetime;
    }

    /**
     * datetime setter
     * @param datetime
     */
    public void setDatetime(Timestamp datetime) {
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



    public String getPhotoUrl() {
        return photoUrl;
    }


    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    protected MoodEvent(Parcel in) {
        author = in.readString();
        mood = (Mood) in.readValue(Mood.class.getClassLoader());
        datetime = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        location = (Location) in.readValue(Location.class.getClassLoader());
        socialSituation = (SocialSituation) in.readValue(SocialSituation.class.getClassLoader());
        textComment = in.readString();
        photoUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeValue(mood);
        dest.writeValue(datetime);
        dest.writeValue(location);
        dest.writeValue(socialSituation);
        dest.writeString(textComment);
        dest.writeString(photoUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MoodEvent> CREATOR = new Parcelable.Creator<MoodEvent>() {
        @Override
        public MoodEvent createFromParcel(Parcel in) {
            return new MoodEvent(in);
        }

        @Override
        public MoodEvent[] newArray(int size) {
            return new MoodEvent[size];
        }
    };
}