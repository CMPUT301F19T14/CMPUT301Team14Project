package com.cmput301t14.mooditude;

import com.cmput301t14.mooditude.services.MoodEventValidator;

import org.junit.Test;
import static org.junit.Assert.*;
public class MoodEventValidatorUnitTest {

    @Test
    public void creation(){
        String s="1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0";
        assertEquals(MoodEventValidator.checkComment(s), false);

        s="1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1";
        assertEquals(MoodEventValidator.checkComment(s),false);

        s=null;
        assertEquals(MoodEventValidator.checkComment(s),true);


        assertEquals(MoodEventValidator.getErrorMessage(),"Comment Longer than 3 words\n" +
                "Comment Longer than 3 words\n");

        assertEquals(MoodEventValidator.getErrorMessage(),"");


        assertEquals(MoodEventValidator.checkMood("HAPPY").getMood(),"HAPPY");
        assertEquals(MoodEventValidator.checkMood("HAHA"),null);

        assertEquals(MoodEventValidator.getErrorMessage(),"Invalid Mood\n");

        assertEquals(MoodEventValidator.getErrorMessage(),"");

        assertEquals(MoodEventValidator.checkSocialSituation("ALONE").getSocialSituation(),"ALONE");
        assertEquals(MoodEventValidator.checkSocialSituation("HAHA"),null);

        assertEquals(MoodEventValidator.getErrorMessage(),"Invalid Social Situation\n");

        assertEquals(MoodEventValidator.getErrorMessage(),"");

    }
}
