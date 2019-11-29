package com.cmput301t14.mooditude.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

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

public class MoodEvent implements Parcelable, Comparable {


    private String author;
    private Mood mood;
    private Timestamp datetime;
    private Location location;
    private SocialSituation socialSituation;
    private String textComment;

    private String photoUrl;

    private String email;


    /**
     * MoodEvent constructor
     * @param mood different mood
     * @param location location of mood event
     * @param socialSituation social situation
     * @param textComment comment of mood event
     */
    public MoodEvent(String author,Mood mood, Location location, SocialSituation socialSituation, String textComment) {
        this.author = author;
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.datetime= Timestamp.now();
    }

    public MoodEvent(Mood mood, Location location, SocialSituation socialSituation, String textComment) {
        this.author = "";
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.datetime= Timestamp.now();
    }

    public MoodEvent(Mood mood, Location location, SocialSituation socialSituation, String textComment, String photoUrl) {
        this.author = "";
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.photoUrl = photoUrl;
        this.datetime= Timestamp.now();
    }



    /**
     * MoodEvent constructor
     * @param mood mood
     * @param location location of the mood event
     * @param socialSituation social situation of the mood event
     * @param textComment comment of the mood event
     * @param timestamp time of the post mood event
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

    /**
     * Constructor
     * @param author user name of the mood post
     * @param mood mood
     * @param location location of the mood event
     * @param socialSituation social situation of the mood event
     * @param textComment comment of the mood event
     * @param email email of the author
     * @param timestamp time of the mood event
     */
    public MoodEvent(String author, Mood mood, Location location, SocialSituation socialSituation, String textComment, String email,Timestamp timestamp ) {
        this.author = author;
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.datetime=timestamp;
        this.email = email;
    }

    /**
     * Constructor
     * @param author user name of the mood post
     * @param mood mood
     * @param location location of the mood event
     * @param socialSituation social situation of mood event
     * @param textComment comment of the mood event
     * @param timestamp time of the mood event
     * @param email email of the mood event
     * @param photoUrl photo url of the mood evnt
     */
    public MoodEvent(String author, Mood mood, Location location, SocialSituation socialSituation, String textComment, Timestamp timestamp , String email, String photoUrl) {
        this.author = author;
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.datetime=timestamp;
        this.email = email;
        this.photoUrl = photoUrl;
    }


    /**
     * Contructor
     * @param mood mood
     * @param location location
     * @param socialSituation social situation
     * @param textComment comment
     * @param timestamp time
     */
    public MoodEvent(Mood mood, Location location, SocialSituation socialSituation, String textComment, Timestamp timestamp) {
        this.author = "";
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.datetime=timestamp;
    }

    /**
     * Constructor
     * @param mood mood
     * @param location location
     * @param socialSituation social situation
     * @param textComment comment
     * @param timestamp time
     * @param photoUrl photo url
     */
    public MoodEvent(Mood mood, Location location, SocialSituation socialSituation, String textComment, Timestamp timestamp, String photoUrl) {
        this.author = "";
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.datetime=timestamp;
        this.photoUrl = photoUrl;
    }

    /**
     * Constructor
     * @param author author
     * @param mood mood
     * @param location location
     * @param socialSituation social situation
     * @param textComment comment
     * @param timestamp time
     */
    public MoodEvent(String author, Mood mood, Location location, SocialSituation socialSituation, String textComment, Timestamp timestamp) {
        this.author = author;
        this.mood = mood;
        this.location = location;
        this.socialSituation = socialSituation;
        this.textComment = textComment;
        this.datetime=timestamp;
    }

    /**
     * get email
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * get author
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Mood getter
     * @return mood
     */
    public Mood getMood() {
        return this.mood;
    }

    /**
     * mood setter
     * @param mood mood event
     */
    public void setMood(Mood mood) {
        this.mood = mood;
    }

    /**
     * datetime getter
     * @return time
     */
    public Timestamp getDatetime() {
        return datetime;
    }

    /**
     * datetime setter
     * @param datetime time
     */
    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    /**
     * location getter
     * @return location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * location setter
     * @param location location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * SocialSituation getter
     * @return social situation
     */
    public SocialSituation getSocialSituation() {
        return socialSituation;
    }

    /**
     *  SocialSituation setter
     * @param socialSituation social situation
     */
    public void setSocialSituation(SocialSituation socialSituation) {
        this.socialSituation = socialSituation;
    }

    /**
     * textComment getter
     * @return comment
     */
    public String getTextComment() {
        return textComment;
    }

    /**
     * textComment setter
     * @param textComment comment
     */
    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }


    /**
     * get Photo
     * @return photo url
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * set photo
     * @param photoUrl photo url
     */
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

    /**
     * Override compareTo method of Comparable interface
     * @param moodEvent the MoodEvent object to compare to
     */
    @Override
    public int compareTo(@NonNull Object moodEvent) {
        int result = 0;
        if (moodEvent != null) {
            if (moodEvent instanceof MoodEvent) {
                MoodEvent m = (MoodEvent) moodEvent;
                result = this.getDatetime().compareTo(m.getDatetime());
            }
            else{
                throw new IllegalArgumentException("Invalid input type, must be MoodEvent");
            }
        }
        else{
            throw new IllegalArgumentException("Input cannot be null, must be MoodEvent");
        }

        return result;
    }
}

