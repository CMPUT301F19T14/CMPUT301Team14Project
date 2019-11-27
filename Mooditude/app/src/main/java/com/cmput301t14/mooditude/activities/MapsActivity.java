package com.cmput301t14.mooditude.activities;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.MoodEvent;
import com.cmput301t14.mooditude.services.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String displayOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        Intent intent = getIntent();
        displayOption = intent.getStringExtra("displayOption");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        User user = new User();
        if (displayOption.equals("self")){
            user.listenSelfMoodEventsOnMap(mMap);
        }
        else if (displayOption.equals("following")){
            user.listenFollowingMoodEventsOnMap(mMap);
        }

        setUpMarkerClickHandler();

    }

    private void setUpMarkerClickHandler(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                MoodEvent selectedMoodEvent = (MoodEvent) marker.getTag();
                Boolean editable = false;
                if (displayOption.equals("self")){
                    editable = true;
                } else if(displayOption.equals("following")){
                    editable = false;
                }
                ViewEditMoodEventFragment.newInstance(selectedMoodEvent, editable).show(getSupportFragmentManager(), "MoodEvent");
                return false;
            }
        });
    }
}
