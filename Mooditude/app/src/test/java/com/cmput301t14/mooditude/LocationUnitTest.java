package com.cmput301t14.mooditude;

import com.cmput301t14.mooditude.models.Location;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationUnitTest {


    @Test
    public void creation(){
        Location location = new Location(1.1,2.2,"ETLC");

        assertEquals(location.getAddress(),"ETLC");
        assertEquals(location.getGeopoint(), new GeoPoint(1.1,2.2));
    }
}
