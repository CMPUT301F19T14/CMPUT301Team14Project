package com.cmput301t14.mooditude.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;


import com.cmput301t14.mooditude.models.Mood;
import com.cmput301t14.mooditude.services.MenuBar;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.services.MoodFilterListener;
import com.cmput301t14.mooditude.services.User;


import com.cmput301t14.mooditude.adapters.SelfMoodEventAdapter;
import com.cmput301t14.mooditude.models.MoodEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    ArrayList<MoodEvent> filteredSelfMoodEventDataList;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String messageEmail;



    private static final int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_TAKE_PHOTO = 100;

    private User userService;

    ImageButton googleMapButton;
    ImageButton signOutButton;


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

        userService = new User();
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
        googleMapButton = findViewById(R.id.googleMapsImageButton);

        googleMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Clicked Map Button",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SelfActivity.this, MapsActivity.class);
                intent.putExtra("displayOption", "self");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
//                finish();
            }
        });

        signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SelfActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set up userName listener
        userService.listenUserName(userNameTextView);
        userService.listenFollowerNumber(numberFollowerTextView);
        userService.listenFollowingNumber(numberFollowingTextView);
        userService.listenMoodHistoryNumber(numberMoodEvents);

/**
 *  Moved to User Class with realtime listener
 *  Original functinality:
 *  Follower Number
 *  Following Number
 *  Number of Moodevents
 */
//        final DocumentReference documentReference = collectionReference.document(messageEmail);
        //get the total number of followers/following
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task< DocumentSnapshot > task) {
//
//                if (task.isSuccessful()) {
//                    DocumentSnapshot doc = task.getResult();
//
//                    if(!doc.contains("MoodHistory")){
//                        CollectionReference moodHistory = documentReference.collection("MoodHistory");
//                        moodHistory.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    Log.d("TAG", task.getResult().size() + "");
//                                    numberMoodEvents.setText(String.valueOf(task.getResult().size()));
//                                } else {
//                                    Log.d(TAG, "Error getting documents: ", task.getException());
//                                }
//                            }
//                        });
//
//                    }
//                    else{
//                        numberMoodEvents.setText("0");
//
//                    }
//
//                    userNameTextView.setText(String.valueOf(doc.get("user_name")));
//
//                    ArrayList<String> followerList = (ArrayList<String>) doc.get("followers");
//                    if(followerList==null){
//                        numberFollowerTextView.setText("0");
//                    }
//                    else{
//                        numberFollowerTextView.setText(String.valueOf(followerList.size()));
//                    }
//
//                    ArrayList<String> followingList = (ArrayList<String>) doc.get("following");
//                    if(followerList==null){
//                        numberFollowingTextView.setText("0");
//                    }
//                    else{
//                        numberFollowingTextView.setText(String.valueOf(followingList.size()));
//                    }
//
//                }
//            }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
//            }
//        });

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
     * setup the filter views and listeners
     */
    private void setUpFilters(){
        TextView happyTextView = findViewById(R.id.happyTextView);
        TextView sadTextView = findViewById(R.id.sadTextView);
        TextView angryTextView = findViewById(R.id.angryTextView);
        TextView excitedTextView = findViewById(R.id.excitedTextView);

        Map<String,TextView> emotionTextViewList= new HashMap<>();
        emotionTextViewList.put("HAPPY",happyTextView);
        emotionTextViewList.put("SAD",sadTextView);
        emotionTextViewList.put("ANGRY",angryTextView);
        emotionTextViewList.put("EXCITED",excitedTextView);

        for(String mood: userService.getFilterList().keySet()){
            TextView v =emotionTextViewList.get(mood);
            v.setText(new Mood(mood).getEmoticon());
            if (userService.getFilterList().get(mood)){
                v.setBackgroundColor(new Mood(mood).getColor());
                v.getBackground().setAlpha(50);
            }
            else{
                v.setBackgroundColor(Color.GRAY);
                v.getBackground().setAlpha(50);
            }
            v.setOnClickListener(new MoodFilterListener(userService, mood));
        }
    }

    /**
     * setup the Mood History, MoodEvent ListView and sync the data
     * and the click to edit functionality
     */
    private void setUpMoodEventList() {
        selfMoodEventList = findViewById(R.id.self_mood_event_list);
        selfMoodEventDataList = new ArrayList<>();
        filteredSelfMoodEventDataList = new ArrayList<>();

        selfMoodEventAdapter = new SelfMoodEventAdapter(this, filteredSelfMoodEventDataList);

        selfMoodEventList.setAdapter(selfMoodEventAdapter);

        // listen to selfMoodEventDataList sync with database
        userService.listenSelfMoodEvents(filteredSelfMoodEventDataList, selfMoodEventDataList, selfMoodEventAdapter);

        // click to view moodEvent
        selfMoodEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // go to ViewEditMoodEventFragment
                MoodEvent selectedMoodEvent = (MoodEvent) parent.getItemAtPosition(position);
                ViewEditMoodEventFragment.newInstance(selectedMoodEvent, true).show(getSupportFragmentManager(), "MoodEvent");
            }
        });

        setUpFilters();
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
        removeDatabaseURI(selectedMoodEvent.getPhotoUrl());
        userService.deleteMoodEvent(selectedMoodEvent);
    }

    public void removeDatabaseURI(String photoUrl){
        if(photoUrl != null) {
            String photoPath = photoUrl.substring(photoUrl.indexOf("%") + 3, photoUrl.indexOf("?"));

            final StorageReference photoRef = FirebaseStorage.getInstance().getReference("photo").child(photoPath);

            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Log.d("Delete Photo", "onSuccess: deleted file");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Log.d("Delete Photo", "onFailure: did not delete file");
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            Fragment yourFragment = getSupportFragmentManager().findFragmentById(R.id.fragment); // same tag while adding fragment for the first time.// same tag while adding fragment for the first time.
            if (yourFragment != null) {
                yourFragment.onActivityResult(requestCode, resultCode, data); //calling method that should be defined in your fragment.
            }
        }
        else if(requestCode ==REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Fragment yourFragment = getSupportFragmentManager().findFragmentById(R.id.fragment); // same tag while adding fragment for the first time.// same tag while adding fragment for the first time.
            if (yourFragment != null) {
                yourFragment.onActivityResult(requestCode, resultCode, data); //calling method that should be defined in your fragment.
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
