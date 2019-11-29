package com.cmput301t14.mooditude.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.location.LocationListener;
import android.location.LocationManager;
import android.text.InputType;
import android.view.LayoutInflater;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.cmput301t14.mooditude.models.Location;
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
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * Fragment Class for Editing and Viewing MoodEvent detail, selected from the Mood History
 */
public class ViewEditMoodEventFragment extends DialogFragment implements Serializable {

    private Spinner moodSpinner;
    private Spinner socialSituationSpinner;
    private EditText commentEditText;
    private Spinner locationSpinner;
    private ImageButton photoButton;
    private ImageButton cameraButton;

    private String moodString;
    private String socialSituationString;
    private String locationString;

    private MoodEvent selectedMoodEvent;
    private Boolean editable;

    private Double lat;
    private Double lon;
    private Location newMoodEventLocation;


    private ImageView imageView;
    private Uri mImageUri;

    private String downloadUrlStr;

    private StorageReference mStorageRef;


    private static final int PICK_IMAGE_REQUEST = 1;

    //variable for camera
    private static final int REQUEST_TAKE_PHOTO = 100;
    private Uri camPhotoURI = null;
    private String camImageStoragePath;

    private Boolean deletePhotoFlag = false;



    /**
     * onAttach for the Fragment, using the super's method
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * create the dialog of handle View/Edit of MoodEvent
     * pull the values from DB and show them
     * submit the change to DB on click ok
     * cancel goes back
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_edit_mood_event_fragment_layout, null);
        moodSpinner = view.findViewById(R.id.frag_mood_spinner);
        socialSituationSpinner = view.findViewById(R.id.frag_social_situation_spinner);
        commentEditText = view.findViewById(R.id.frag_comment_edittext);
        locationSpinner = view.findViewById(R.id.location_spinner);
        photoButton = view.findViewById(R.id.frag_photo_button);
        imageView=view.findViewById(R.id.testimage);

        cameraButton = view.findViewById(R.id.frag_camera_button);
        //check for camera permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraButton.setEnabled(false);
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        mStorageRef = FirebaseStorage.getInstance().getReference("photo");




        Bundle args = getArguments();
        if (args != null){

            selectedMoodEvent = args.getParcelable("moodEvent");
            editable = (Boolean) args.getSerializable("editable");

        }

        if (selectedMoodEvent != null) {

            moodString = selectedMoodEvent.getMood().getMood();
            socialSituationString = selectedMoodEvent.getSocialSituation().getSocialSituation();
            commentEditText.setText(selectedMoodEvent.getTextComment());

            showImage(selectedMoodEvent.getPhotoUrl());

            // set dropdown moodSpinner Adapter
            final ArrayAdapter<CharSequence> moodArrayAdapter = ArrayAdapter.createFromResource(getContext(),
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

            // select the existing mood value in the spinner
            int positionOfItem = moodArrayAdapter.getPosition(
                    selectedMoodEvent.getMood().getEmoticon()+selectedMoodEvent.getMood().getMood());
            moodSpinner.setSelection(positionOfItem, true);
            View itemView = moodSpinner.getChildAt(positionOfItem);
            long itemId = moodSpinner.getAdapter().getItemId(positionOfItem);
            moodSpinner.performItemClick(itemView, positionOfItem, itemId);

            // set dropdown socialSituationSpinner Adapter
            ArrayAdapter<CharSequence> socialSituationArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.social_situation_string_array, android.R.layout.simple_spinner_item);
            socialSituationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            socialSituationSpinner.setAdapter(socialSituationArrayAdapter);
            socialSituationSpinner.setSelection(socialSituationArrayAdapter.getPosition(selectedMoodEvent.getSocialSituation().getSocialSituation()),true);
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


            photoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFireChooser();
                }
            });



            cameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    takePictureIntent();
                }
            });


            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(selectedMoodEvent.getPhotoUrl() != null){
                        deletePhotoFlag = true;
                        clearPhotoView();
                        return true;
                    }
                    return false;
                }
            });



            setUpLocationSpinner();


            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final AlertDialog d;
            if (editable){
                // editable, can submit by "OK" and can "Cancel"
                d = builder.setView(view)
                        .setTitle("MoodEvent")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("OK", null).create();
            }
            else{
                // not editable, can only close
                d = builder.setView(view)
                        .setTitle("MoodEvent")
                        .setNegativeButton("Close", null).create();

                // lock the fields
                this.disableEdit();
            }

            /* Use View.OnclickListener to get manual control of Dialog dismiss, only dismiss
            after all validation passed and value updated , use validator 's
            onValidationFailed and onValidationSucceeded to handle input requirement validation*/
            d.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // validate the input fields
                            if(deletePhotoFlag){
                                selectedMoodEvent.setPhotoUrl(null);
                            }

                            uploadFile();


                        }
                    });
                }
            });
            return d;
        }
        return null;
    }





    private void clearPhotoView() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        imageView.setImageBitmap(null);

                        removeDatabaseURI(selectedMoodEvent.getPhotoUrl());

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage("Want to delete this photo?")
                .setPositiveButton("Delete", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();

    }

    private void removeDatabaseURI(String photoUrl){
        String photoPath = photoUrl.substring(photoUrl.indexOf("%")+3,photoUrl.indexOf("?"));

        final StorageReference photoRef = mStorageRef.child(photoPath);

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





    /**
     * Request permission from user
     * @param requestCode - The request code passed in requestPermissions
     * @param permissions - The requested permissions
     * @param grantResults - The grant results for the corresponding permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                cameraButton.setEnabled(true);
            }
        }
    }


    /**
     * lock and disable the editable fields when editable is set to false
     */
    private void disableEdit(){
        moodSpinner.setEnabled(false);
        moodSpinner.setFocusable(false);

        socialSituationSpinner.setEnabled(false);
        socialSituationSpinner.setFocusable(false);

        commentEditText.setEnabled(false);
        commentEditText.setInputType(InputType.TYPE_NULL);
        commentEditText.setFocusable(false);

        locationSpinner.setEnabled(false);
        locationSpinner.setFocusable(false);

        photoButton.setEnabled(false);
        photoButton.setFocusable(false);
        cameraButton.setEnabled(false);
        cameraButton.setFocusable(false);
    }

    /**
     * Create the location spinner that allows user to
     * change, keep or remove location of mood event.
     */
    private void setUpLocationSpinner(){
        ArrayAdapter<CharSequence> locationArrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.edit_mood_event_location_string_array, android.R.layout.simple_spinner_item);
        locationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationArrayAdapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationString = parent.getItemAtPosition(position).toString();
                switch (locationString) {
                    case "PREVIOUS LOCATION":

                        if (selectedMoodEvent.getLocation() != null) {
                            newMoodEventLocation = new Location(selectedMoodEvent.getLocation().getGeopoint().getLatitude(), selectedMoodEvent.getLocation().getGeopoint().getLongitude());
                        } else {
                            newMoodEventLocation = null;
                        }
                        break;
                    case "REMOVE LOCATION":
                        newMoodEventLocation = null;
                        break;
                    case "UPDATE LOCATION":
                        getCurrentDeviceLocation();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing selected
            }
        });
    }

    /**
     * Ask user for the permission of getting the current
     * device location.
     */
    private void getCurrentDeviceLocation(){

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                newMoodEventLocation = new Location(lat, lon);
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

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
        }

    }
    /**
     * Constructor like method, get the parameters passed in as Bundle
     * @param moodEvent - the MoodEvent selected in Mood History
     * @return fragment
     */

    static ViewEditMoodEventFragment newInstance(MoodEvent moodEvent, Boolean editable) {
        Bundle args = new Bundle();
        args.putParcelable("moodEvent", moodEvent);
        args.putSerializable("editable", editable);

        ViewEditMoodEventFragment fragment = new ViewEditMoodEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * display the image in the imageView.
     * @param photoUrl - photo url
     */
    private void showImage(String photoUrl){
        Glide.with(this).load(photoUrl).into(imageView);

    }

    /**
     * open the activity for choosing file from folder, and waiting for return result
     * to be received with request code.
     */
    private void openFireChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * callback method when the subsequent activity is done, and the result returns
     * the intent carries the result data app can identify the result and determine
     * how to handle it by request code.
     * @param requestCode - The request code you passed to startActivityForResult()
     * @param resultCode - A result code specified by the second activity
     * @param data - an intent that carries the result data
     */
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(getContext()).load(mImageUri).into(imageView);

        }

        //take a photo
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Log.i("cam", "back photo file success");
                Log.i("cam",String.valueOf(camPhotoURI));

                Picasso.with(getContext()).load(camPhotoURI).into(imageView);

                //add to gallery
                galleryAddPic(getActivity().getApplicationContext(), camImageStoragePath);


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void takePictureIntent() {


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.i("cam", "photo click");
        File photoFile = null;

        try {
            photoFile = createImageFile();

        }catch (IOException ex) {
            Log.i("cam", "photo file failed");
        }

        if (photoFile != null) {
            camImageStoragePath = photoFile.getAbsolutePath();
            camPhotoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getApplicationContext().getPackageName() + ".provider", photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, camPhotoURI);
            Log.i("cam", "photo file success");
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

        }
    }


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

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    /**
     * Add camera photo to gallery
     * @param context - application context
     * @param filePath - path of file
     */
    private void galleryAddPic(Context context, String filePath) {
        Log.i("cam", "photo gallery success");

        MediaScannerConnection.scanFile(context,
                new String[]{filePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });

    }

    /**
     * get the extension for the image file by photo uri.
     * @param uri - image uri
     * @return the extension of file type
     */
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /**
     * upload the image file to the firebase storage
     * with its unique uri and file name.
     * get and save the download url of specific picture to database
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
                                    downloadUrlStr = uri.toString();
                                    uploadDatabase(downloadUrlStr);


                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    });


        }
        else{
            uploadDatabase(downloadUrlStr);
        }


    }

    /**
     * update the moodevent to database for fields
     * @param imageUrl - image url
     */
    private void uploadDatabase(String imageUrl){
        boolean valid = true;

        Mood mood = MoodEventValidator.checkMood(moodString);
        if (mood == null) {
            valid = false;
            ((TextView) moodSpinner.getSelectedView()).setError(MoodEventValidator.getErrorMessage());
        }
        SocialSituation socialSituation = MoodEventValidator.checkSocialSituation(socialSituationString);
        if (socialSituation == null) {
            valid = false;
            ((TextView) socialSituationSpinner.getSelectedView()).setError(MoodEventValidator.getErrorMessage());
        }

        String commentString = commentEditText.getText().toString();
        if (!MoodEventValidator.checkComment(commentString)) {
            valid = false;
            commentEditText.setError(MoodEventValidator.getErrorMessage());
        }

        //for photo
        if(imageUrl == null){
            imageUrl = selectedMoodEvent.getPhotoUrl();
        }

        if (valid) {
            // put actual location and photo
            MoodEvent moodEvent = new MoodEvent(mood,
                    newMoodEventLocation,
                    socialSituation, commentString, selectedMoodEvent.getDatetime(),imageUrl);

            // push the MoodEvent to database
            User user = new User();
            user.pushMoodEvent(moodEvent);
            getDialog().dismiss();
        }


    }

}

