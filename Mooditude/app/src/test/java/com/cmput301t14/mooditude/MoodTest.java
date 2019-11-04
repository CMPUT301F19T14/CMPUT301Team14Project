package com.cmput301t14.mooditude;

import android.graphics.Color;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit Test for Mood Class
 */
public class MoodTest {

    private Mood mood;

    @Before
    public void setUp(){
        mood = new Mood("HAPPY");
    }

    @Test
    public void testMood(){
        Mood testMood = new Mood("HAPPY");
        assertSame(testMood.getMood(),"HAPPY");
    }

    @Test
    public void testGetMood(){
        assertSame("HAPPY", mood.getMood());
    }

    @Test
    public void testSetMood(){
        mood.setMood("SAD");
        assertSame("SAD", mood.getMood());
    }

    @Test
    public void testGetColor(){
        assertEquals(mood.getColor(), Color.GREEN);
    }

    @Test
    public void testGetEmoticon(){
        assertSame("\uD83D\uDE03", mood.getEmoticon());
    }
}