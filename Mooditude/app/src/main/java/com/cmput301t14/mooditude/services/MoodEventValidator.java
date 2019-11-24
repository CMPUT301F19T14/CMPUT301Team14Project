package com.cmput301t14.mooditude.services;

import com.cmput301t14.mooditude.models.Mood;
import com.cmput301t14.mooditude.models.SocialSituation;

import java.util.StringTokenizer;

/**
 * One Method class used to validate the input of Mood Event .
 * provides static methods to validate one mood event.
 */

public class MoodEventValidator {
    static private String errorMessage="";

    /**
     * validate mood
     * @param moodStr
     * @return
     */
    public static Mood checkMood (String moodStr) { //static method
        //block of code to be executed
        Mood mood;
        try{
            mood= new Mood(moodStr);
        } catch (Exception e){
            errorMessage=errorMessage.concat("Invalid Mood\n");
            return null;
        }

        return mood;
    }

    /**
     * validate SocialSituation
     * @param socialSituationStr
     * @return
     */
    public static SocialSituation checkSocialSituation (String socialSituationStr) { //static method
        //block of code to be executed
        SocialSituation socialSituation;
        try{
            socialSituation= new SocialSituation(socialSituationStr);
        } catch (Exception e){
            errorMessage=errorMessage.concat("Invalid Social Situation\n");
            return null;
        }
        return socialSituation;
    }

    /**
     * validate Comment
     * @param Comment
     * @return
     */
    public static boolean checkComment(String Comment){

        if(Comment == null || Comment.isEmpty())
            return true;

        StringTokenizer tokens = new StringTokenizer(Comment);
        if(tokens.countTokens()>3){
            errorMessage=errorMessage.concat("Comment Longer than 3 words\n");
            return false;
        }
        if(Comment.length()>20){
            errorMessage=errorMessage.concat("Comment is longer than 20 characters \n");
            return false;
        }

        return true;
    }

    /**
     * Return error message
     * @return
     */
    public static String getErrorMessage() {
        String result=errorMessage ;
        errorMessage="";
//        errorMessage="Test";
        return result;
    }
}
