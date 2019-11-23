package com.cmput301t14.mooditude.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.app.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private String temp;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private ProgressBar mProgressBar;

    private StorageTask mUploadTask;


    static final int REQUEST_TAKE_PHOTO = 100;
    Uri camPhotoURI;
    String camImageStoragePath;





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

        mProgressBar = findViewById(R.id.progress_bar);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            photoButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }


        setUpMoodSpinner();
        setUpSocialSituationSpinner();
        setUpPhotoViews();
        setUpSubmitButton();
    }

    /**
     * Request permission from user
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                photoButton.setEnabled(true);
            }
        }
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
                //openFireChooser();

                takePictureIntent();
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

                uploadFile();


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
        //xianda
        if (requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(photoImageView);

        }


        //shixiong
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Log.i("cam", "back photo file sucess");
                Log.i("cam",String.valueOf(camPhotoURI));

                //Get the photo
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(camImageStoragePath, options);
                photoImageView.setImageBitmap(bitmap);

                //add to gallery
                galleryAddPic(getApplicationContext(), camImageStoragePath);


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if (mImageUri != null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                                photoTextView.setText(temp);


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // getting image uri and converting into string
                                    Uri downloadUrl = uri;
                                    temp = downloadUrl.toString();
                                    Toast.makeText(AddActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                                    uploadDatabase(temp);


                                }
                            });


//                            Toast.makeText(AddActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
////                            photo.setmImageUrl(taskSnapshot.getStorage().getDownloadUrl().toString());
////                            if(photo.getmImageUrl()!=null){
////                                temp = photo.getmImageUrl();
//                            temp=taskSnapshot.getStorage().getDownloadUrl().toString();
//                                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
//                                uploadDatabase(temp);

//                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            mProgressBar.setProgress((int) progress);
//                        }
//                    });

        }
        if (mUploadTask.isComplete()){
            Toast.makeText(getApplicationContext(), "123", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadDatabase(String temp){
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


    /**
     * Add camera photo to gallery
     * @param context
     * @param filePath
     */
    private void galleryAddPic(Context context, String filePath) {
        Log.i("cam", "photo gallery sucess");

        MediaScannerConnection.scanFile(context,
                new String[]{filePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });

    }



//  file:///storage/emulated/0/Pictures/CameraDemo/IMG_20191121_172846.jpg
    private static File createImageFile() throws IOException {
        Log.i("cam", "create file ");
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.e("File", "Oops! Failed create ");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    

    private void takePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.i("cam", "photo clikck");
        File photoFile = null;

        try {
            photoFile = createImageFile();

        }catch (IOException ex) {
            Log.i("cam", "photo file failed");
        }

        if (photoFile != null) {
            camImageStoragePath = photoFile.getAbsolutePath();
            camPhotoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, camPhotoURI);
            Log.i("cam", "photo file sucess");
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

        }



    }





}
