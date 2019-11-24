package com.cmput301t14.mooditude.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.graphics.Color;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;

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
import android.widget.Toast;

import com.cmput301t14.mooditude.models.Location;
import com.cmput301t14.mooditude.services.MenuBar;
import com.cmput301t14.mooditude.models.Mood;
import com.cmput301t14.mooditude.models.MoodEvent;
import com.cmput301t14.mooditude.services.MoodEventValidator;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.SocialSituation;
import com.cmput301t14.mooditude.services.User;

import static com.cmput301t14.mooditude.activities.SelfActivity.EXTRA_MESSAGE_Email;


/**
 * Activity for the user to add a MoodEvent
 */
public class AddActivity extends AppCompatActivity {

    ImageButton submitButton;
    Spinner moodSpinner;
    Spinner socialSituationSpinner;
    EditText commentEditText;
//    TextView locationTextView;
    Spinner locationSpinner;
    TextView photoTextView;

    private String commentString;
    private String moodString;
    private String socialSituationString;
    private String locationString;

    private String messageEmail;

    LocationManager locationManager;
    LocationListener locationListener;

    private Double lat;
    private Double lon;
    private Location newMoodEventLocation;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

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
        locationSpinner = findViewById(R.id.location_spinner);
//        locationTextView = findViewById(R.id.location_textview);
        photoTextView = findViewById(R.id.photo_textview);

        setUpMoodSpinner();
        setUpSocialSituationSpinner();
        setUpLocationSpinner();
        setUpSubmitButton();


//        getCurrentDeviceLocation();
    }

    /**2
     * setup the mood spinner dropdown menu
     */
    private void setUpMoodSpinner(){
        // set dropdown moodSpinner Adapter
        ArrayAdapter<CharSequence> moodArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.mood_string_array, android.R.layout.simple_spinner_item);
        moodArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(moodArrayAdapter);
        // set moodSpinner on item select
        moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Mood happy = new Mood("HAPPY");
                Mood sad = new Mood("SAD");
                Mood excited = new Mood("EXCITED");
                Mood angry = new Mood("ANGRY");
                String spinnerStr = parent.getItemAtPosition(position).toString();
                if (spinnerStr.equals(happy.getEmoticon() + happy.getMood())){
                    moodString = happy.getMood();
                    view.setBackgroundColor(happy.getColor());
                }
                else if (spinnerStr.equals(sad.getEmoticon() + sad.getMood())){
                    moodString = sad.getMood();
                    view.setBackgroundColor(sad.getColor());
                }
                else if (spinnerStr.equals(excited.getEmoticon() + excited.getMood())){
                    moodString = excited.getMood();
                    view.setBackgroundColor(excited.getColor());
                }
                else if (spinnerStr.equals(angry.getEmoticon() + angry.getMood())){
                    moodString = angry.getMood();
                    view.setBackgroundColor(angry.getColor());
                }
                view.getBackground().setAlpha(50);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing selected
            }
        });
    }

    /**
     * setup the social situation spinner dropdown menu
     */
    private void setUpSocialSituationSpinner(){
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
    }

    private void setUpLocationSpinner(){
        ArrayAdapter<CharSequence> locationArrayAdapter = ArrayAdapter.createFromResource(this,R.array.new_mood_event_location_string_array, android.R.layout.simple_spinner_item);
        locationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationArrayAdapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationString = parent.getItemAtPosition(position).toString();
                if (locationString.equals("INCLUDE LOCATION")){
                    getCurrentDeviceLocation();
//                    while (lat == null || lon == null){
//
//                    }
//                    newMoodEventLocation = new Location(lat,lon);
                }
                else if (locationString.equals("NO LOCATION")){
                    newMoodEventLocation = new Location();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing selected
            }
        });
    }

    /**
     * setup the submit button for submitting the mood event,
     * validate and then push the MoodEvent to the database
     */
    private void setUpSubmitButton(){
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
                    MoodEvent moodEvent = new MoodEvent(mood,
                            newMoodEventLocation,
//                            null,
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

    public void getCurrentDeviceLocation(){

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                newMoodEventLocation = new Location(lat,lon);
                Toast.makeText(getApplicationContext(),"lat:"+lat.toString()+"lon:"+lon.toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
        }

    }
}
