package com.cmput301t14.mooditude;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationUnitTest {


    @Test
    public void creation(){
        Location location = new Location(1.1,2.2,"ETCL");

        assertEquals(location.getAddress(),"ETCL");
    }
}
