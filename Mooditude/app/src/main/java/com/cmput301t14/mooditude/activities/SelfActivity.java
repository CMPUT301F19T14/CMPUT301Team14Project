package com.cmput301t14.mooditude.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;


import com.cmput301t14.mooditude.models.Mood;
import com.cmput301t14.mooditude.services.MenuBar;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.services.MoodFilterListener;
import com.cmput301t14.mooditude.services.User;


import com.cmput301t14.mooditude.adapters.SelfMoodEventAdapter;
import com.cmput301t14.mooditude.models.MoodEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a class for user's profile purpose and one part of Main Interface after login.
 * It contains the following functions:
 * 1. show the number of mood event and with clicking item to show the summary list of mood events
 * 2. show the number of user's follower and following, and with clicking item to show the summary list of follower/following
 * 3. show the user's own Mood History as a Custom ListView
 */
public class SelfActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE_Email = "com.cmput301t14.mooditude.email";
    public static final String EXTRA_MESSAGE_Mode = "com.cmput301t14.mooditude.mode";

    ListView selfMoodEventList;
    ArrayAdapter<MoodEvent> selfMoodEventAdapter;
    ArrayList<MoodEvent> selfMoodEventDataList;
    ArrayList<MoodEvent> filteredMoodEventDataList;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String messageEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Intent intent = getIntent();
        messageEmail = intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Email);

        if (messageEmail == null) {
            messageEmail = String.valueOf(user.getEmail());
        }
        if (messageEmail.compareTo(String.valueOf(user.getEmail())) != 0) {
            messageEmail = String.valueOf(user.getEmail());
        }

        MenuBar menuBar = new MenuBar(SelfActivity.this, messageEmail, 4);

        setUpMoodEventList();
        setUpDeleteMoodEvent();

        final String TAG = "Sample";
        final TextView followerTextView;
        final TextView followingTextView;
        final TextView numberFollowerTextView;
        final TextView numberFollowingTextView;
        final TextView userNameTextView;
        final TextView numberMoodEvents;

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Users");


        followerTextView = findViewById(R.id.follower);
        numberFollowerTextView = findViewById(R.id.number_of_follower);
        numberFollowingTextView = findViewById(R.id.number_of_following);
        followingTextView = findViewById(R.id.following);
        userNameTextView = findViewById(R.id.userNametextView);
        numberMoodEvents = findViewById(R.id.number_of_mood_events);

        User user = new User();
        // Set up userName listener
        user.listenUserName(userNameTextView);
        user.listenFollowerNumber(numberFollowerTextView);
        user.listenFollowingNumber(numberFollowingTextView);
        user.listenMoodHistoryNumber(numberMoodEvents);



        TextView happyTextView = findViewById(R.id.happyTextView);
        happyTextView.setBackgroundColor(-3090735);

        TextView sadTextView = findViewById(R.id.sadTextView);
        sadTextView.setBackgroundColor(-3090735);

        TextView angryTextView = findViewById(R.id.angryTextView);
        angryTextView.setBackgroundColor(-3090735);

        TextView excitedTextView = findViewById(R.id.excitedTextView);
        excitedTextView.setBackgroundColor(-3090735);

        Map<String,TextView> emotionTextViewList= new HashMap<>();
        emotionTextViewList.put("HAPPY",happyTextView);
        emotionTextViewList.put("SAD",sadTextView);
        emotionTextViewList.put("ANGRY",angryTextView);
        emotionTextViewList.put("EXCITED",excitedTextView);

        for(String emotion: User.getFilerList().keySet()){
            TextView v =emotionTextViewList.get(emotion);
            if (User.getFilerList().get(emotion)){
                v.setBackgroundColor(new Mood(emotion).getColor());
                v.getBackground().setAlpha(50);
            }
            else{
                v.setBackgroundColor(Color.rgb(208, 214, 209));
            }
            v.setOnClickListener(new MoodFilterListener(emotion));
        }



        //if click on item, go to DisplayFollow activity to show the summary list
        followerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelfActivity.this, DisplayFollow.class);
                intent.putExtra(EXTRA_MESSAGE_Mode, DisplayFollow.ListMode.Followers.toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });
        followingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelfActivity.this, DisplayFollow.class);
                intent.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                intent.putExtra(EXTRA_MESSAGE_Mode, DisplayFollow.ListMode.Followings.toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    /**
     * setup the Mood History, MoodEvent ListView and sync the data
     * and the click to edit functionality
     */
    private void setUpMoodEventList() {
        selfMoodEventList = findViewById(R.id.self_mood_event_list);
        selfMoodEventDataList = new ArrayList<>();

        selfMoodEventAdapter = new SelfMoodEventAdapter(this, selfMoodEventDataList);

        selfMoodEventList.setAdapter(selfMoodEventAdapter);

        // listen to selfMoodEventDataList sync with database
        User user = new User();
        user.listenSelfMoodEvents(selfMoodEventDataList, selfMoodEventAdapter);

        // click to view moodEvent
        selfMoodEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // go to ViewEditMoodEventFragment
                MoodEvent selectedMoodEvent = (MoodEvent) parent.getItemAtPosition(position);
                ViewEditMoodEventFragment.newInstance(selectedMoodEvent, true).show(getSupportFragmentManager(), "MoodEvent");
            }
        });
    }

    /**
     * setup the MoodEvent ListView item long click to delete functionality
     */
    private void setUpDeleteMoodEvent() {
        // long click to delete
        selfMoodEventList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final MoodEvent selectedMoodEvent = (MoodEvent) parent.getItemAtPosition(position);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                onConfirmPressed(selectedMoodEvent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(SelfActivity.this);
                alert.setMessage("Are you sure that you want to delete?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
                return true;
            }
        });
    }

    /**
     * Called by SelfMoodEventAdapter,
     * When delete is confirmed, remove the moodEvent from the list
     */
    public void onConfirmPressed(MoodEvent selectedMoodEvent) {
        User user = new User();
        user.deleteMoodEvent(selectedMoodEvent);
    }
}
