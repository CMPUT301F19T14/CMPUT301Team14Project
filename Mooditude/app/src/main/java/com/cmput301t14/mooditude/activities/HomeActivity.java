package com.cmput301t14.mooditude.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmput301t14.mooditude.adapters.FollowingMoodEventAdapter;
import com.cmput301t14.mooditude.services.MenuBar;
import com.cmput301t14.mooditude.models.MoodEvent;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.services.User;

import java.util.ArrayList;

import static com.cmput301t14.mooditude.activities.SelfActivity.EXTRA_MESSAGE_Email;

public class HomeActivity extends AppCompatActivity {

    ListView selfMoodEventList;
    ArrayAdapter<MoodEvent> selfMoodEventAdapter;
    ArrayList<MoodEvent> selfMoodEventDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get the Intent that started this activity and extract the string
        Intent thisIntent = getIntent();
        final String messageEmail = thisIntent.getStringExtra(EXTRA_MESSAGE_Email);
        MenuBar menuBar = new MenuBar(HomeActivity.this, messageEmail, 0);

        setUpMoodEventList();

    }

    /**
     * setup the Mood History, MoodEvent ListView and sync the data
     * and the click to edit functionality
     */
    private void setUpMoodEventList() {
        selfMoodEventList = findViewById(R.id.following_mood_event_list);
        selfMoodEventDataList = new ArrayList<>();

        selfMoodEventAdapter = new FollowingMoodEventAdapter(this, selfMoodEventDataList);

        selfMoodEventList.setAdapter(selfMoodEventAdapter);

        // listen to selfMoodEventDataList sync with database
        User user = new User();
        user.listenFollowingMoodEvents(selfMoodEventDataList, selfMoodEventAdapter);

        // click to view moodEvent
        selfMoodEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // go to ViewEditMoodEventFragment
                MoodEvent selectedMoodEvent = (MoodEvent) parent.getItemAtPosition(position);
                ViewEditMoodEventFragment.newInstance(selectedMoodEvent).show(getSupportFragmentManager(), "MoodEvent");
            }
        });
    }


}
