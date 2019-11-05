package com.cmput301t14.mooditude;

import java.util.StringTokenizer;

/**
 * One Method class used to validate the input of moodevent.
 */

public class MoodEventValidator {

    static private String errorMessage="";

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

    public static boolean checkComment(String Comment){

        if(Comment == null || Comment.isEmpty())
            return true;

        StringTokenizer tokens = new StringTokenizer(Comment);
        if(tokens.countTokens()<=20)
            return true;
        else{
            errorMessage=errorMessage.concat("Message is longer than 20\n");
            return false;
        }
    }

    public static String getErrorMessage() {
        String result=errorMessage ;
        errorMessage="";
//        errorMessage="Test";
        return result;
    }
}
