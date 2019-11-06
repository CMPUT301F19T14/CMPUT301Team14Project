package com.cmput301t14.mooditude;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;


import android.content.Intent;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    ImageButton submitButton;
    Spinner moodSpinner;
    Spinner socialSituationSpinner;
    EditText commentEditText;
    TextView locationTextView;
    TextView photoTextView;

    private String commentString;
    private String moodString;
    private String socialSituationString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // find the views
        submitButton = findViewById(R.id.submit_button);
        moodSpinner = findViewById(R.id.mood_spinner);
        socialSituationSpinner = findViewById(R.id.social_situation_spinner);
        commentEditText = findViewById(R.id.comment_edittext);
        locationTextView = findViewById(R.id.location_textview);
        photoTextView = findViewById(R.id.photo_textview);

        // set dropdown moodSpinner Adapter
        ArrayAdapter<CharSequence> moodArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.mood_string_array, android.R.layout.simple_spinner_item);
        moodArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(moodArrayAdapter);
        // set moodSpinner on item select
        moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                moodString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing selected
            }
        });

        // set dropdown socialSituationSpinner Adapter
        ArrayAdapter<CharSequence> socialSituationArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.social_situation_string_array, android.R.layout.simple_spinner_item);
        socialSituationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        socialSituationSpinner.setAdapter(socialSituationArrayAdapter);
        // set socialSituationSpinner on item select
        socialSituationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                socialSituationString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing selected
            }
        });

        // set submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate the input fields
                boolean valid = true;

                Mood mood = MoodEventValidator.checkMood(moodString);
                if (mood == null){
                    valid = false;
                    ((TextView)moodSpinner.getSelectedView()).setError(MoodEventValidator.getErrorMessage());
                }

                SocialSituation socialSituation = MoodEventValidator.checkSocialSituation(socialSituationString);
                if (socialSituation == null){
                    valid = false;
                    ((TextView)socialSituationSpinner.getSelectedView()).setError(MoodEventValidator.getErrorMessage());
                }

                commentString = commentEditText.getText().toString();
                if (!MoodEventValidator.checkComment(commentString)){
                    valid = false;
                    commentEditText.setError(MoodEventValidator.getErrorMessage());
                }

                if (valid){
                    // TODO: put actual location and photo
                    MoodEvent moodEvent = new MoodEvent(1, mood,
                            new Location(0.0,0.0),
                            socialSituation, commentString);

//                    FirebaseFirestore db;
                    User user=  new User();
                    Log.i("TAG","Add User");
                    user.pushMoodEvent(moodEvent);

                    ArrayList<MoodEvent> temp=new ArrayList<>();
                    temp=user.getSelfMoodEvents(temp);
//                    Log.d("121","Data addition successful");
//                    user.getUserName();
//                    user.getUserName();
                    if(temp!=null){
                        Log.i("LOGGER","MoodEvents: "+temp);
                    }


//                    System.out.println("Testing");
                    // TODO: submit the mood Event

                    // go to selfActivity
                    Intent intent = new Intent(AddActivity.this, SelfActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
            }
        });



        // set navigation menu bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent intent0 = new Intent(AddActivity.this, HomeActivity.class);
                        intent0.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent0);
                        break;
                    case R.id.navigation_search:
                        Intent intent1 = new Intent(AddActivity.this, SearchActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_add:

                        break;
                    case R.id.navigation_notification:
                        Intent intent3 = new Intent(AddActivity.this, NotificationActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_self:
                        Intent intent4 = new Intent(AddActivity.this, SelfActivity.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent4);
                        break;
                }
                return false;

            }
        });
    }
}
