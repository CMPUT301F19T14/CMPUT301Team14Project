package com.cmput301t14.mooditude.activities;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;


import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.cmput301t14.mooditude.activities.SelfActivity.EXTRA_MESSAGE_Email;


/**
 * Activity for the user to add a MoodEvent
 */
public class AddActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageButton submitButton;
    private Spinner moodSpinner;
    private Spinner socialSituationSpinner;
    private EditText commentEditText;
    private Spinner locationSpinner;
    private ImageButton choosePhotoButton;
    private ImageView photoImageView;
    private ImageButton photoButton;

    private String moodString;
    private String socialSituationString;
    private String locationString;

    private String messageEmail;


    private Uri mImageUri;

    private String downloadUrlStr;

    private StorageReference mStorageRef;

    //variable for camera
    static final int REQUEST_TAKE_PHOTO = 100;
    private Uri camPhotoURI = null;
    private String camImageStoragePath;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Double lat;
    private Double lon;
    private Location newMoodEventLocation;

    /**
     * Ask permission to access GPS
     * @param requestCode The request code passed in requestPermissions
     * @param permissions The requested permissions. Never null
     * @param grantResults The grant results for the corresponding permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if(requestCode == 1){
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
      }
      if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                photoButton.setEnabled(true);
            }
        }
      
    }

    /**
     * Activity on create
     * @param savedInstanceState
     */
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
        choosePhotoButton = findViewById(R.id.photo_button2);
        photoImageView = findViewById(R.id.photo_imageview);
        photoButton = findViewById(R.id.photo_button);

        mStorageRef = FirebaseStorage.getInstance().getReference("photo");

        //check for camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            photoButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }


        setUpMoodSpinner();
        setUpSocialSituationSpinner();

        setUpPhotoViews();

        setUpLocationSpinner();
        setUpSubmitButton();
        setUpDeletePhoto();


//        getCurrentDeviceLocation();
    }

    /**
     * set up listener on long click on photo to delete
     * if long press in imageview; clear all previous URI and clear the image view
     */
    private void setUpDeletePhoto() {
        photoImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(mImageUri != null || camPhotoURI != null){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    photoImageView.setImageDrawable(null);
                                    mImageUri = null;
                                    camPhotoURI = null;
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder alert = new AlertDialog.Builder(AddActivity.this);
                    alert.setMessage("Are you sure that you want to delete this photo?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
                            .show();
                    return true;

                }

                return false;
            }
        });

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


    /**
     * set up photo button, Two button:
     * choosePhotoButton is used for uploading the photo from device
     * photoButton is used for taking a picture from camera
     */
    private void setUpPhotoViews(){
        //choose a photo from storage
        choosePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFireChooser();


            }
        });

        //take a photo from camera
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureIntent();
            }
        });
      }

    /**
     * set up location spinner
     */
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

                }
                else if (locationString.equals("NO LOCATION")){
                    newMoodEventLocation = null;
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

    /**
     * set up image chooser, jump to folder content to choose photo file
     */
    private void openFireChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    /**
     * Coming back from camera intent/folder intent; requestCode could justify different returning activity
     * if back from file chooser, get the data URI and draw it to imageview
     * if back from camera, get photo taken URI and draw it to imageview; also add it to gallery
     * @param requestCode - The request code you passed to startActivityForResult()
     * @param resultCode - A result code specified by the second activity
     * @param data - an intent that carries the result data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //choose a photo
        if (requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(photoImageView);

        }


        //take a photo
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

    /**
     * get the extension from chosen file, passing by its uri
     * @param uri - photo uri
     * @return - mime.getExtensionFromMimeType(cR.getType(uri))
     */
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /**
     * After click submit button, upload the photo file to storage, then upload the moodevent to database by call uploadDatabase
     */
    private void uploadFile(){

        if(mImageUri == null){
            if(camPhotoURI != null){
                mImageUri = camPhotoURI;
            }
        }

        if (mImageUri != null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            // getting image uri and converting into string
            StorageTask mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // getting image uri and converting into string
//                                    Uri downloadUrl = uri;
                                    downloadUrlStr = uri.toString();
                                    uploadDatabase(downloadUrlStr);


                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddActivity.this, "Photo Upload Failed", Toast.LENGTH_LONG).show();
                        }
                    });

        }
        else{
            //no photo condition
            uploadDatabase(downloadUrlStr);
        }
    }

    /**
     * upload moodevent to database
     * check the null conditions for different mood attributes, if null condition, do not update to database
     * @param photoUrl - photo download url
     */
    private void uploadDatabase(String photoUrl){
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

        String commentString = commentEditText.getText().toString();
        if (!MoodEventValidator.checkComment(commentString)){
            valid = false;
            commentEditText.setError(MoodEventValidator.getErrorMessage());
        }


                if (valid){
                    // put actual location and photo
                    MoodEvent moodEvent = new MoodEvent(mood,
                            newMoodEventLocation,
                    socialSituation, commentString, photoUrl);

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
     * @param context - application context
     * @param filePath - the path of file
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


    /**
     * createImageFile before take photo form camera, use a folder call CameraDemo to store all photos of this app
     * the filepath should be file:///storage/emulated/0/Pictures/CameraDemo/IMG_20191121_172846.jpg
     * @return if media storage does not exist, return null. Otherwise, return a new photo file
     */
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

        String timeStamp;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    /**
     * Take a photo from camera
     */
    private void takePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.i("cam", "photo click");
        File photoFile;
        photoFile = null;

        try {
            photoFile = createImageFile();

        }catch (IOException ex) {
            Log.i("cam", "photo file failed");
        }

        if (photoFile != null) {
            camImageStoragePath = photoFile.getAbsolutePath();
            camPhotoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, camPhotoURI);
            Log.i("cam", "photo file success");
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

        }

    }

    /**
     * Ask user for the permission of getting the current
     * device location.
     */
    public void getCurrentDeviceLocation(){

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                newMoodEventLocation = new Location(lat,lon);
//                Toast.makeText(getApplicationContext(),"lat:"+lat.toString()+"lon:"+lon.toString(),Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}
