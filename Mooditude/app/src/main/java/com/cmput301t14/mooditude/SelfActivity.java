package com.cmput301t14.mooditude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SelfActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.cmput301t14.mooditude.MESSAGE";

    ListView selfMoodEventList;
    ArrayAdapter<MoodEvent> selfMoodEventAdapter;
    ArrayList<MoodEvent> selfMoodEventDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent intent0 = new Intent(SelfActivity.this, HomeActivity.class);
                        intent0.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent0);
                        break;
                    case R.id.navigation_search:
                        Intent intent1 = new Intent(SelfActivity.this, SearchActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_add:
                        Intent intent2 = new Intent(SelfActivity.this, AddActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_notification:
                        Intent intent3 = new Intent(SelfActivity.this, NotificationActivity.class);
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

        //final Button showFollowerButton;
        final TextView FollowerTV;
        final TextView FollowingTV;
        final TextView numFollowerTV;
        final TextView numFollowingTV;
        //final Button showFollowingButton;


        FirebaseFirestore db;
        //showFollowerButton = findViewById(R.id.show_follower);
        FollowerTV = findViewById(R.id.follower);
        numFollowerTV = findViewById(R.id.number_of_follower);
        numFollowingTV = findViewById(R.id.number_of_following);
        FollowingTV = findViewById(R.id.following);
        //showFollowingButton = findViewById(R.id.show_following);




        FollowerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String require = "Follower";
                Intent intent = new Intent(SelfActivity.this, DisplayFollow.class);
                intent.putExtra(EXTRA_MESSAGE, require);
                startActivity(intent);

            }
        });
        FollowingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String require = "Following";
                Intent intent = new Intent(SelfActivity.this, DisplayFollow.class);
                intent.putExtra(EXTRA_MESSAGE, require);
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
            // TODO: go to view moodEvent Activity
            }
        });
    }

    /** Called by SelfMoodEventAdapter,
     *  When delete is confirmed, remove the moodEvent from the list
     */
    public void onConfirmPressed(MoodEvent selectedMoodEvent) {
        //        selfMoodEventAdapter.remove(selectedMoodEvent);
        //        selfMoodEventAdapter.notifyDataSetChanged();
        User user = new User();
        user.deleteMoodEvent(selectedMoodEvent);
    }
}
