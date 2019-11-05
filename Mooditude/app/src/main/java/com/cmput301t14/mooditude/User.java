package com.cmput301t14.mooditude;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Source;

import java.time.LocalDateTime;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class User{
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    public void User(){
        db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void pushMoodEvent(MoodEvent moodEvent){
        Location location  =moodEvent.getLocation();
        LocalDateTime localDateTime=moodEvent.getDatetime();
        Integer author= moodEvent.getAuthor();
        Mood mood = moodEvent.getMood();
        SocialSituation socialSituation= moodEvent.getSocialSituation();
        String textComment= moodEvent.getTextComment();

//        final CollectionReference collectionReference = db.collection("Users");
        DocumentReference userFile = db.collection("Users").document(user.getEmail());
        final CollectionReference moodHistory = userFile.collection("MoodHistory");


        DocumentReference moodEntry=moodHistory.document(localDateTime.toString());
        HashMap<String,GeoPoint> geoHash = new HashMap<>();
        moodEntry.set(geoHash);

        HashMap<String,String> stringHash = new HashMap<>();
        stringHash.put("Mood",mood.getMood());
        stringHash.put("Comment",textComment);
        stringHash.put("DateTime",localDateTime.toString());



        moodEntry.set(stringHash);
    }

    public String getUserName(){

        DocumentReference docRef = db.collection("User").document(user.getEmail());
        Source source = Source.CACHE;

        final String result;
        result =String.valueOf(docRef.get(source).getResult().get("user_name:"));
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    result=String.valueOf(document.getData().get("user_name"));
                    Log.d(TAG, "Cached document data: " + document.getData().get("user_name"));
                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
            }
        });

        return result;
    }


}
