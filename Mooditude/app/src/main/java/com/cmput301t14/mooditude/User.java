package com.cmput301t14.mooditude;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User class is one class to implement all user related fucntions.
 * Includes:
 * push Mood Event to database
 * delete Mood Event
 * setup database connections
 */

public class User{
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String userName;

    /**
     * User Constructor
     * Initialize db
     * mAuth
     * user
     */

    public User(){
        db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        fetchUserName();
    }

    /**
     * push Mood Event to database.
     * @param moodEvent
     */

    public void pushMoodEvent(final MoodEvent moodEvent){
        CollectionReference moodHistory = db.collection("Users")
                .document(user.getEmail()).collection("MoodHistory");

        final DocumentReference moodEntry=moodHistory.document(moodEvent.getDatetime().toString());

        final HashMap<String,Object> moodHash = new HashMap<>();

        moodEntry.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        Log.d("TAG", "Document exists!");
                        Location location  =moodEvent.getLocation();
                        LocalDateTime localDateTime=moodEvent.getDatetime();
//                        Integer author= moodEvent.getAuthor();
                        Mood mood = moodEvent.getMood();
                        SocialSituation socialSituation= moodEvent.getSocialSituation();
                        String textComment= moodEvent.getTextComment();
                        if (location.getGeopoint() != document.getGeoPoint("Location")){
                            moodHash.put("Location",location.getGeopoint());
                        }
                        if (mood.getMood() != document.getString("Mood")){
                            moodHash.put("Mood",mood.getMood());
                        }
                        if (textComment != document.getString("Comment")){
                            moodHash.put("Comment",textComment);
                        }
                        if (localDateTime.toString() != document.getString("DateTime")){
                            moodHash.put("DateTime",localDateTime.toString());
                        }
                        if (socialSituation.getSocialSituation() != document.getString("SocialSituation")){
                            moodHash.put("SocialSituation",socialSituation.getSocialSituation());
                        }
                        moodEntry.update(moodHash);
                    } else {
                        Log.d("TAG", "Document does not exist!");
                        Location location  =moodEvent.getLocation();
                        LocalDateTime localDateTime=moodEvent.getDatetime();
//                        Integer author= moodEvent.getAuthor();
                        Mood mood = moodEvent.getMood();
                        SocialSituation socialSituation= moodEvent.getSocialSituation();
                        String textComment= moodEvent.getTextComment();
                        moodHash.put("Location",location.getGeopoint());
                        moodHash.put("Mood",mood.getMood());
                        moodHash.put("Comment",textComment);
                        moodHash.put("DateTime",localDateTime.toString());
                        moodHash.put("SocialSituation",socialSituation.getSocialSituation());
                        moodEntry.set(moodHash);
                    }
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }
            }
        });
    }

    /**
     * delete Mood Event from server.
     * @param selectedMoodEvent
     */
    public void deleteMoodEvent(MoodEvent selectedMoodEvent){
        CollectionReference moodHistory = db.collection("Users")
                .document(user.getEmail()).collection("MoodHistory");

        // delete the moodEvent from Firebase if it is selected,
        // Note the SnapshotListener will handle the local update as well
        moodHistory.document(selectedMoodEvent.getDatetime().toString())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "Data deletion successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Data deletion failed" + e.toString());
                    }
                });
    }

    /**
     * Connect Array Adapter to database to retrieve online information from database.
     * @param moodEventDataList
     * @param moodEventAdapter
     */

    public void listenSelfMoodEvents(final ArrayList<MoodEvent> moodEventDataList, final ArrayAdapter<MoodEvent> moodEventAdapter){
        CollectionReference collectionReference = db.collection("Users")
                .document(user.getEmail()).collection("MoodHistory");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                moodEventDataList.clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    Integer author=1;
                    String textComment=doc.getString("Comment");
                    Mood mood= new Mood(doc.getString("Mood"));
                    SocialSituation socialSituation= new SocialSituation(doc.getString("SocialSituation"));
                    Location location= new Location(doc.getGeoPoint("Location"));
                    LocalDateTime datetime = LocalDateTime.parse(doc.getString("DateTime"));
                    MoodEvent moodEvent=new MoodEvent(author, mood, location,socialSituation,textComment,datetime);
                    moodEventDataList.add(moodEvent);
                }
                moodEventAdapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * fetch user name from database.
     */
    private void fetchUserName(){
        DocumentReference docRef = db.collection("Users").document(user.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userName = document.getData().get("user_name").toString();
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * return user name to user
     * @return
     */
    public String getUserName(){
        return this.userName;
    }


}
