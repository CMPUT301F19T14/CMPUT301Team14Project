package com.cmput301t14.mooditude;

import java.util.ArrayList;

public class SocialSituation {
    private enum socialSituation_enum{
        ALONE,ONE_PERSON,SEVERAL,CROWD
    }

    private socialSituation_enum socialSituation;

    public SocialSituation(String argv) {
        this.socialSituation = socialSituation_enum.valueOf(argv);
    }

    public String getSocialSituation( ) {
        return this.socialSituation.toString();
    }

    public void setSocialSituation(String argv) {
        this.socialSituation = socialSituation_enum.valueOf(argv);
    }
}
