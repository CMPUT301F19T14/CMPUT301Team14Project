package com.cmput301t14.mooditude.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import android.widget.TextView;

import android.widget.Toast;


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

    ImageButton googleMapButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get the Intent that started this activity and extract the string
        Intent thisIntent = getIntent();
        final String messageEmail = thisIntent.getStringExtra(EXTRA_MESSAGE_Email);
        MenuBar menuBar = new MenuBar(HomeActivity.this, messageEmail, 0);

        user = new User();
        googleMapButton = findViewById(R.id.home_map_button);

        setUpMoodEventList();
        googleMapHandler();




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
//        User user = new User();
        user.listenFollowingMoodEvents(selfMoodEventDataList, selfMoodEventAdapter);

        // click to view moodEvent
        selfMoodEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // go to ViewEditMoodEventFragment
                MoodEvent selectedMoodEvent = (MoodEvent) parent.getItemAtPosition(position);
                ViewEditMoodEventFragment.newInstance(selectedMoodEvent, false).show(getSupportFragmentManager(), "MoodEvent");
            }
        });

        selfMoodEventList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final String email = ((TextView) view.findViewById(R.id.userNameTextView)).getText().toString();
                final String email = selfMoodEventDataList.get(i).getEmail();

                Log.i("selfMood", "Here:" + email);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
//                                onConfirmPressed(selectedMoodEvent);
//                                message.delete();
                                new User().unfollow(email);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                alert.setMessage("Are you sure that you want to unfollow?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
                return true;

//                return true;
            }
        });


    }

    /**
     * Opens google map when user clicks map icon
     */
    public void googleMapHandler(){
        googleMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
                intent.putExtra("displayOption", "following");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
