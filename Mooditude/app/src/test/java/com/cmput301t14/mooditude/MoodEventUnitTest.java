package com.cmput301t14.mooditude;

import android.graphics.Color;

import com.cmput301t14.mooditude.models.Location;
import com.cmput301t14.mooditude.models.Mood;
import com.cmput301t14.mooditude.models.MoodEvent;
import com.cmput301t14.mooditude.models.SocialSituation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MoodEventUnitTest {
    @Test
    public void creation(){

        Mood m=new Mood("HAPPY");
        Location l = new Location(1.1,2.2,"Test Address");

        SocialSituation st= new SocialSituation("ALONE");


        MoodEvent me= new MoodEvent( 1, m,l,st,"");

        assertEquals(me.getAuthor(),(Integer)1);

        assertEquals(me.getMood().getMood(),"HAPPY");
        assertEquals(me.getMood().getColor(), Color.GREEN);
//        assertEquals(me.getLocation().getLongtitude(),(Double)2.2);
//
//        public MoodEvent(Integer author, Mood mood, LocalDateTime datetime,
//                Location location, SocialSituation socialSituation,
//                String textComment) {
    }
}
