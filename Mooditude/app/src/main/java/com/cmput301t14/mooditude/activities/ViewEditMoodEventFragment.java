package com.cmput301t14.mooditude.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
    private TextView photoTextView;
    private ImageButton cameraButton;

    private String commentString;
    private String moodString;
    private String socialSituationString;
    private String locationString;

    private MoodEvent selectedMoodEvent;
    private Boolean editable;

    LocationManager locationManager;
    LocationListener locationListener;

    private Double lat;
    private Double lon;
    private Location newMoodEventLocation;


    ImageView imageView;
    private Uri mImageUri;

    private String temp;

    private StorageReference mStorageRef;


    private StorageTask mUploadTask;
    private static final int PICK_IMAGE_REQUEST = 1;

    //variable for camera
    static final int REQUEST_TAKE_PHOTO = 100;
    Uri camPhotoURI = null;
    String camImageStoragePath;

    Boolean deletePhotoFlag = false;



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
        photoTextView = view.findViewById(R.id.frag_photo_textview);
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

            selectedMoodEvent = (MoodEvent) args.getParcelable("moodEvent");
            editable = (Boolean) args.getSerializable("editable");

        }

        if (selectedMoodEvent != null) {

            moodString = selectedMoodEvent.getMood().getMood();
            socialSituationString = selectedMoodEvent.getSocialSituation().getSocialSituation();
            commentEditText.setText(selectedMoodEvent.getTextComment());

            showimage(selectedMoodEvent.getPhotoUrl());

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
            View itemView = (View) moodSpinner.getChildAt(positionOfItem);
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


            photoTextView.setOnClickListener(new View.OnClickListener() {
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
                            if(deletePhotoFlag == true){
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





    public void clearPhotoView() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        imageView.setImageDrawable(null);

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
     * @param requestCode
     * @param permissions
     * @param grantResults
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
        // TODO: add real location and photo
        moodSpinner.setEnabled(false);
        moodSpinner.setFocusable(false);

        socialSituationSpinner.setEnabled(false);
        socialSituationSpinner.setFocusable(false);

        commentEditText.setEnabled(false);
        commentEditText.setInputType(InputType.TYPE_NULL);
        commentEditText.setFocusable(false);

//        locationTextView.setEnabled(false);
//        locationTextView.setInputType(InputType.TYPE_NULL);
//        locationTextView.setFocusable(false);

        photoTextView.setEnabled(false);
        photoTextView.setInputType(InputType.TYPE_NULL);
        photoTextView.setFocusable(false);
    }

    private void setUpLocationSpinner(){
        ArrayAdapter<CharSequence> locationArrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.edit_mood_event_location_string_array, android.R.layout.simple_spinner_item);
        locationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationArrayAdapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationString = parent.getItemAtPosition(position).toString();
                if (locationString.equals("PREVIOUS LOCATION")){

                    if (selectedMoodEvent.getLocation() != null) {
                        newMoodEventLocation = new Location(selectedMoodEvent.getLocation().getGeopoint().getLatitude(), selectedMoodEvent.getLocation().getGeopoint().getLongitude());
                    }
                    else {
                        newMoodEventLocation = null;
                    }
                }
                else if (locationString.equals("REMOVE LOCATION")){
                    newMoodEventLocation = null;
                }
                else if (locationString.equals("UPDATE LOCATION")){
//                    newMoodEventLocation = new Location();
                    getCurrentDeviceLocation();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing selected
            }
        });
    }

    public void getCurrentDeviceLocation(){

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                newMoodEventLocation = new Location(lat,lon);
//                Toast.makeText(getContext(),"lat:"+lat.toString()+"lon:"+lon.toString(),Toast.LENGTH_SHORT).show();
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
     * @return
     */

    static ViewEditMoodEventFragment newInstance(MoodEvent moodEvent, Boolean editable) {
        Bundle args = new Bundle();
        args.putParcelable("moodEvent", moodEvent);
        args.putSerializable("editable", editable);

        ViewEditMoodEventFragment fragment = new ViewEditMoodEventFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private void showimage(String photoUrl){
//        String url = "com.google.android.gms.tasks.zzu@3042b78";
        Glide.with(this).load(photoUrl).into(imageView);
        Toast.makeText(getContext(), photoUrl, Toast.LENGTH_LONG).show();


//        Picasso.with(getContext()).load(photoUrl).into(imageView);
    }

    private void openFireChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        Toast.makeText(getContext(),"open folder",Toast.LENGTH_SHORT).show();

    }


    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(getContext()).load(mImageUri).into(imageView);

        }

        //take a photo
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Log.i("cam", "back photo file sucess");
                Log.i("cam",String.valueOf(camPhotoURI));

                //Get the photo
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(camImageStoragePath, options);
                imageView.setImageBitmap(bitmap);

                //add to gallery
                galleryAddPic(getActivity().getApplicationContext(), camImageStoragePath);


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
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
            camPhotoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getApplicationContext().getPackageName() + ".provider", photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, camPhotoURI);
            Log.i("cam", "photo file sucess");
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

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
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


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){

        if(mImageUri == null){
            if(camPhotoURI != null){
                mImageUri = camPhotoURI;
            }
        }

        if(mImageUri == null){
            if(camPhotoURI != null){
                mImageUri = camPhotoURI;
            }
        }

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
                                    Toast.makeText(getContext(), temp, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getContext(), temp, Toast.LENGTH_SHORT).show();
                        }
                    });
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            mProgressBar.setProgress((int) progress);
//                        }
//                    });

            if (mUploadTask.isComplete()){
                Toast.makeText(getContext(), "123", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            uploadDatabase(temp);
        }


    }
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

        commentString = commentEditText.getText().toString();
        if (!MoodEventValidator.checkComment(commentString)) {
            valid = false;
            commentEditText.setError(MoodEventValidator.getErrorMessage());
        }

        //for photo
        if(imageUrl == null){
            imageUrl = selectedMoodEvent.getPhotoUrl();
        }

        if (valid) {
            // TODO: put actual location and photo
            MoodEvent moodEvent = new MoodEvent(mood,
                    newMoodEventLocation,
                    socialSituation, commentString, selectedMoodEvent.getDatetime(),imageUrl);

            // push the MoodEvent to database
            User user = new User();
            user.pushMoodEvent(moodEvent);
//                                moodArrayAdapter.notifyDataSetChanged();
            getDialog().dismiss();
        }


    }

}

