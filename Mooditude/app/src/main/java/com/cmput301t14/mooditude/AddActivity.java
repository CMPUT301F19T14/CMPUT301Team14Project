package com.cmput301t14.mooditude;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import static com.cmput301t14.mooditude.SelfActivity.EXTRA_MESSAGE_Email;

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

    private String messageEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        messageEmail = intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Email);
        MenuBar menuBar = new MenuBar(AddActivity.this, messageEmail,2);

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


                    // push the MoodEvent to database
                    User user=  new User();
                    Log.i("TAG","Add User");
                    user.pushMoodEvent(moodEvent);

                    // go to selfActivity
                    Intent intent = new Intent(AddActivity.this, SelfActivity.class);
                    intent.putExtra(EXTRA_MESSAGE_Email, messageEmail);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
