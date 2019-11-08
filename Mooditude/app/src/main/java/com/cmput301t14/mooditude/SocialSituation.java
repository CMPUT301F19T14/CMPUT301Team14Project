package com.cmput301t14.mooditude;

import java.util.ArrayList;

/**
 * One class to define social situation
 */
public class SocialSituation {
    private enum socialSituation_enum{
        ALONE,ONE_PERSON,SEVERAL,CROWD
    }

    private socialSituation_enum socialSituation;

    /**
     * SocialSituation constructor
     * @param argv
     */
    public SocialSituation(String argv) {
        this.socialSituation = socialSituation_enum.valueOf(argv);
    }

    /**
     * SocialSituation getter
     * @return
     */
    public String getSocialSituation( ) {
        return this.socialSituation.toString();
    }

    /**
     * SocialSituation getter
     * @param argv
     */
    public void setSocialSituation(String argv) {
        this.socialSituation = socialSituation_enum.valueOf(argv);
    }
}
