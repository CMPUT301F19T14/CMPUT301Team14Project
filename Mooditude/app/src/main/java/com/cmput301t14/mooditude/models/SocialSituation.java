package com.cmput301t14.mooditude.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * One class to define social situation
 */
public class SocialSituation implements Parcelable {
    private enum socialSituation_enum{
        ALONE,WITH_ANOTHER_PERSON,SEVERAL,CROWD
    }

    private socialSituation_enum socialSituation;

    /**
     * SocialSituation constructor
     * @param argv social situation
     */
    public SocialSituation(String argv) {
        this.socialSituation = socialSituation_enum.valueOf(argv);
    }

    /**
     * SocialSituation getter
     * @return social situation to get
     */
    public String getSocialSituation( ) {
        return this.socialSituation.toString();
    }

    /**
     * SocialSituation setter
     * @param argv social situation to set
     */
    public void setSocialSituation(String argv) {
        this.socialSituation = socialSituation_enum.valueOf(argv);
    }

    private SocialSituation(Parcel in) {
        socialSituation = (socialSituation_enum) in.readValue(socialSituation_enum.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(socialSituation);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SocialSituation> CREATOR = new Parcelable.Creator<SocialSituation>() {
        @Override
        public SocialSituation createFromParcel(Parcel in) {
            return new SocialSituation(in);
        }

        @Override
        public SocialSituation[] newArray(int size) {
            return new SocialSituation[size];
        }
    };
}