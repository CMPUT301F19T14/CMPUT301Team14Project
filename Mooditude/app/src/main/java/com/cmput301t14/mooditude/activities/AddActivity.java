package com.cmput301t14.mooditude.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301t14.mooditude.models.Location;
import com.cmput301t14.mooditude.models.Photo;
import com.cmput301t14.mooditude.services.MenuBar;
import com.cmput301t14.mooditude.models.Mood;
import com.cmput301t14.mooditude.models.MoodEvent;
import com.cmput301t14.mooditude.services.MoodEventValidator;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.SocialSituation;
import com.cmput301t14.mooditude.services.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static com.cmput301t14.mooditude.activities.SelfActivity.EXTRA_MESSAGE_Email;


/**
 * Activity for the user to add a MoodEvent
 */
public class AddActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    ImageButton submitButton;
    Spinner moodSpinner;
    Spinner socialSituationSpinner;
    EditText commentEditText;
    TextView locationTextView;
    TextView photoTextView;
    ImageView photoImageView;
    ImageButton photoButton;

    private String commentString;
    private String moodString;
    private String socialSituationString;

    private String messageEmail;

    private Uri mImageUri;

    private Photo photo = new Photo();

    private String temp="temp";

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;





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
        photoImageView = findViewById(R.id.photo_imageview);
        photoButton = findViewById(R.id.photo_button);

        mStorageRef = FirebaseStorage.getInstance().getReference("photo");


        setUpMoodSpinner();
        setUpSocialSituationSpinner();
        setUpPhotoViews();
        setUpSubmitButton();
    }

    /**
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
                moodString = parent.getItemAtPosition(position).toString();
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


    private void setUpPhotoViews(){
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFireChooser();
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
                uploadFile();

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
                            new Location(),
//                            null,
                            socialSituation, commentString, temp);

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

    private void openFireChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(photoImageView);

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if (mImageUri != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            photo.setmImageUrl(taskSnapshot.getStorage().getDownloadUrl().toString());
                            if(photo.getmImageUrl()!=null){
                                temp = photo.getmImageUrl();
                                photoTextView.setText(temp);
                                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


        }
        Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();

    }
}
