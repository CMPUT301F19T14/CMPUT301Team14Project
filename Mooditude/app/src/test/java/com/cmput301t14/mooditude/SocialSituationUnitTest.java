package com.cmput301t14.mooditude;

import com.cmput301t14.mooditude.models.SocialSituation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SocialSituationUnitTest {

    @Test
    public void creation(){
        SocialSituation test1= new SocialSituation("ALONE");
        assertEquals(test1.getSocialSituation(),"ALONE");
    }
    @Test
    public void set(){
        SocialSituation test1= new SocialSituation("ALONE");
        test1.setSocialSituation("SEVERAL");
        assertEquals(test1.getSocialSituation(),"SEVERAL");
    }



}
