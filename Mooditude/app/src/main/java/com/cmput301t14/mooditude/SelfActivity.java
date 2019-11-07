package com.cmput301t14.mooditude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This is a class for user's profile purpose and one part of Main Interface after login.
 * It contains the following functions:
 * 1. show the number of mood event and with clicking item to show the summary list of mood events
 * 2. show the number of user's follower and following, and with clicking item to show the summary list of follower/following
 */
public class SelfActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE_Email = "com.cmput301t14.mooditude.email";
    public static final String EXTRA_MESSAGE_Mode = "com.cmput301t14.mooditude.mode";


    ListView selfMoodEventList;
    ArrayAdapter<MoodEvent> selfMoodEventAdapter;
    ArrayList<MoodEvent> selfMoodEventDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);



        Intent intent = getIntent();
        final String messageEmail = intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Email);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        //listener user want to jump to another parts of Main Interface and jump to the required activity
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent intent0 = new Intent(SelfActivity.this, HomeActivity.class);
                        intent0.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        intent0.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent0);
                        break;
                    case R.id.navigation_search:
                        Intent intent1 = new Intent(SelfActivity.this, SearchActivity.class);
                        intent1.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_add:
                        Intent intent2 = new Intent(SelfActivity.this, AddActivity.class);
                        intent2.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_notification:
                        Intent intent3 = new Intent(SelfActivity.this, NotificationActivity.class);
                        intent3.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_self:

                        break;
                }
                return false;

            }
        });


        final String TAG = "Sample";
        final TextView FollowerTV;
        final TextView FollowingTV;
        final TextView numFollowerTV;
        final TextView numFollowingTV;
        final TextView userNameTV;


        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Users");

        final DocumentReference documentReference = collectionReference.document(messageEmail);


        FollowerTV = findViewById(R.id.follower);
        numFollowerTV = findViewById(R.id.number_of_follower);
        numFollowingTV = findViewById(R.id.number_of_following);
        FollowingTV = findViewById(R.id.following);
        userNameTV = findViewById(R.id.userNametextView);

        //get the total number of followers/following
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task< DocumentSnapshot > task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    userNameTV.setText(String.valueOf(doc.get("user_name")));

                    ArrayList<String> followerList = (ArrayList<String>) doc.get("followers");
                    if(followerList==null){
                        numFollowerTV.setText("0");
                    }
                    else{
                        numFollowerTV.setText(String.valueOf(followerList.size()));
                    }

                    ArrayList<String> followingList = (ArrayList<String>) doc.get("following");
                    if(followerList==null){
                        numFollowingTV.setText("0");
                    }
                    else{
                        numFollowingTV.setText(String.valueOf(followingList.size()));
                    }

                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        //if click on item, go to DisplayFollow activity to show the summary list
        FollowerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String require = "Follower";
                Intent intent = new Intent(SelfActivity.this, DisplayFollow.class);
                intent.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                intent.putExtra(EXTRA_MESSAGE_Mode, require);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });
        FollowingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String require = "Following";
                Intent intent = new Intent(SelfActivity.this, DisplayFollow.class);
                intent.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                intent.putExtra(EXTRA_MESSAGE_Mode, require);
               // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });

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
                ViewEditMoodEventFragment.newInstance(selectedMoodEvent).show(getSupportFragmentManager(), "MoodEvent");
            }
        });

        // long click to delete
        selfMoodEventList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final MoodEvent selectedMoodEvent = (MoodEvent) parent.getItemAtPosition(position);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
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

    /** Called by SelfMoodEventAdapter,
     *  When delete is confirmed, remove the moodEvent from the list
     */
    public void onConfirmPressed(MoodEvent selectedMoodEvent) {
        User user = new User();
        user.deleteMoodEvent(selectedMoodEvent);
    }
}
