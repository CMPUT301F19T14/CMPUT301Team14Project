package com.cmput301t14.mooditude;

import android.graphics.Color;

import com.cmput301t14.mooditude.models.Location;
import com.cmput301t14.mooditude.models.Mood;
import com.cmput301t14.mooditude.models.MoodEvent;
import com.cmput301t14.mooditude.models.SocialSituation;
import com.google.firebase.Timestamp;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MoodEventUnitTest {

    @Test
    public void creation_getter_setter(){

        String author = "tester";
        Mood mood = new Mood("HAPPY");
        Location location = new Location(1.1,2.2,"Test Address");
        SocialSituation socialSituation = new SocialSituation("ALONE");
        String textComment = "some comment";
        Timestamp timestamp = Timestamp.now();
        String email = "tester@test.com";
        String photoUrl = "test.url";

        MoodEvent me= new MoodEvent(author,mood,location, socialSituation, textComment, timestamp, email,photoUrl);

        assertEquals(me.getAuthor(), author);
        assertEquals(me.getMood(), mood);
        assertEquals(me.getLocation(), location);
        assertEquals(me.getSocialSituation(), socialSituation);
        assertEquals(me.getTextComment(), textComment);
        assertEquals(me.getDatetime(), timestamp);
        assertEquals(me.getEmail(), email);
        assertEquals(me.getPhotoUrl(), photoUrl);

        assertEquals(me.getMood().getMood(),"HAPPY");
        assertEquals(me.getMood().getColor(), Color.GREEN);

        Mood nmood = new Mood("SAD");
        Location nlocation = new Location(4.4,3.3,"Test Address2");
        SocialSituation nsocialSituation = new SocialSituation("CROWD");
        String ntextComment = "some comment2";
        Timestamp ntimestamp = Timestamp.now();
        String nphotoUrl = "test2.url";

        me.setDatetime(ntimestamp);
        me.setLocation(nlocation);
        me.setMood(nmood);
        me.setPhotoUrl(nphotoUrl);
        me.setSocialSituation(nsocialSituation);
        me.setTextComment(ntextComment);
    }
}
