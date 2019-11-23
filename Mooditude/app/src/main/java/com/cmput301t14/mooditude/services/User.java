package com.cmput301t14.mooditude.services;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301t14.mooditude.models.Location;
import com.cmput301t14.mooditude.models.Mood;
import com.cmput301t14.mooditude.models.MoodEvent;
import com.cmput301t14.mooditude.models.SocialSituation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User class is one class to implement all user related fucntions.
 * Includes:
 * push Mood Event to database
 * delete Mood Event
 * setup database connections
 */

public class User {
    static private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    static private DocumentReference userDocRef;
    static CollectionReference followingCollRef;
    static CollectionReference followerCollRef;
    static CollectionReference moodHistoryCollRef;

    static public ArrayList<String> followerList = new ArrayList<>();
    static public ArrayList<String> followingList = new ArrayList<>();

    private Map<String,Boolean> filterList;
    private ArrayList<MoodEvent> filteredSelfMoodEventDataList;
    private ArrayList<MoodEvent> moodEventDataList;
    private ArrayAdapter<MoodEvent> moodEventAdapter;
  
    static private String userName = "";

    /**
     * and
     * User Constructor
     * Initialize db
     * mAuth
     * user
     */

    public User() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
//        Log.i("email","tag: "+getEmail());
        userDocRef = db.collection("Users").document(getEmail());
        if (userDocRef == null) {
            Log.i("email", "tag: " + getEmail());
        }
        followingCollRef = db.collection("Users").document(user.getEmail()).collection("Followings");
        followerCollRef = db.collection("Users").document(user.getEmail()).collection("Followers");
        moodHistoryCollRef = db.collection("Users").document(user.getEmail()).collection("MoodHistory");

        User.refreshUserName();
      
        filterList = new HashMap<>();
        filterList.put("HAPPY",Boolean.TRUE);
        filterList.put("SAD",Boolean.TRUE);
        filterList.put("ANGRY",Boolean.TRUE);
        filterList.put("EXCITED",Boolean.TRUE);
    }

//    public void setupListenOn

    /**
     * Add follower ID to Follower List along with follower's user_name
     *
     * @param followerID
     */
    public void addFollower(String followerID) {
        // add sender to receiver's Followers collection
        Log.i("addFollower",followerID);
        CollectionReference receiverFollowers = userDocRef.collection("Followers");
        final DocumentReference receiverFollowersEntry = receiverFollowers.document(followerID);

        final HashMap<String, Object> followerHash = new HashMap<>();
        followerHash.put("user_name",User.getUserName());
        receiverFollowersEntry.set(followerHash);

    }

    /**
     * Return userName
     *
     * @return
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * Refresh userName. Refresh User name as required.
     */
    public static void refreshUserName() {
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (task.isSuccessful()) {
                    if (document.exists()) {
                        User.userName = document.getString("user_name");
                    }
                }
            }
        });
        Log.i("refreshUserName", User.userName);
    }

    public String getEmail() {
        return user.getEmail();
    }

    /**
     * push Mood Event to database.
     *
     * @param moodEvent
     */

    public void pushMoodEvent(final MoodEvent moodEvent) {
        CollectionReference moodHistory = db.collection("Users")
                .document(user.getEmail()).collection("MoodHistory");

        String epochTimeString = String.valueOf(moodEvent.getDatetime().getSeconds());
        final DocumentReference moodEntry = moodHistory.document(epochTimeString);

        final HashMap<String, Object> moodHash = new HashMap<>();

        moodEntry.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        Log.d("TAG", "Document exists!");
                        Location location = moodEvent.getLocation();
                        Timestamp localDateTime = moodEvent.getDatetime();
//                        Integer author= moodEvent.getAuthor();
                        Mood mood = moodEvent.getMood();
                        SocialSituation socialSituation = moodEvent.getSocialSituation();
                        String textComment = moodEvent.getTextComment();
                        if (location.getGeopoint() != document.getGeoPoint("Location")) {
                            moodHash.put("Location", location.getGeopoint());
                        }
                        if (mood.getMood() != document.getString("Mood")) {
                            moodHash.put("Mood", mood.getMood());
                        }
                        if (textComment != document.getString("Comment")) {
                            moodHash.put("Comment", textComment);
                        }
//                        if (localDateTime != document.getTimestamp("DateTime")){
////                            Log.i("TAG1B",document.getTimestamp("DateTime").toString());
//                            moodHash.put("DateTime",localDateTime);
////                            Log.i("TAG1",localDateTime.toString());
////                            Log.i("TAG1B",document.getTimestamp("DateTime").toString());
//                        }
                        if (socialSituation.getSocialSituation() != document.getString("SocialSituation")) {
                            moodHash.put("SocialSituation", socialSituation.getSocialSituation());
                        }
                        moodHash.put("DateTime", localDateTime);
                        moodEntry.update(moodHash);
                    } else {
                        Log.d("TAG", "Document does not exist!");
                        Location location = moodEvent.getLocation();
                        Timestamp localDateTime = moodEvent.getDatetime();
//                        Integer author= moodEvent.getAuthor();
                        Mood mood = moodEvent.getMood();
                        SocialSituation socialSituation = moodEvent.getSocialSituation();
                        String textComment = moodEvent.getTextComment();

                        moodHash.put("Location", location.getGeopoint());

                        moodHash.put("Mood", mood.getMood());
                        moodHash.put("Comment", textComment);
                        moodHash.put("DateTime", localDateTime);
                        moodHash.put("SocialSituation", socialSituation.getSocialSituation());
//                        Log.i("Timestamp.now()",String.valueOf(Timestamp.now().getSeconds()));
                        moodEntry.set(moodHash);
                    }
                    //--
                    followerCollRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                String followerEmail = doc.getId().toString();
                                DocumentReference documentReference = db.collection("Users")
                                        .document(followerEmail).collection("Followings").document(user.getEmail());
                                Location location = moodEvent.getLocation();
                                Timestamp localDateTime = moodEvent.getDatetime();
//                        Integer author= moodEvent.getAuthor();
                                Mood mood = moodEvent.getMood();
                                SocialSituation socialSituation = moodEvent.getSocialSituation();
                                String textComment = moodEvent.getTextComment();

                                moodHash.put("user_name", User.getUserName());
                                moodHash.put("Location", location.getGeopoint());
                                moodHash.put("Mood", mood.getMood());
                                moodHash.put("Comment", textComment);
                                moodHash.put("DateTime", localDateTime);
                                moodHash.put("SocialSituation", socialSituation.getSocialSituation());
//                        Log.i("Timestamp.now()",String.valueOf(Timestamp.now().getSeconds()));
                                documentReference.set(moodHash);

                            }
                        }
                    });

                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }

            }
        });
    }

    /**
     * delete Mood Event from server.
     *
     * @param selectedMoodEvent
     */
    public void deleteMoodEvent(MoodEvent selectedMoodEvent) {
        CollectionReference moodHistory = db.collection("Users")
                .document(user.getEmail()).collection("MoodHistory");

        // delete the moodEvent from Firebase if it is selected,
        // Note the SnapshotListener will handle the local update as well
        String epochTimeString = String.valueOf(selectedMoodEvent.getDatetime().getSeconds());
        moodHistory.document(epochTimeString)
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
     *
     * @param moodEventDataList
     * @param moodEventAdapter
     */
    public void listenSelfMoodEvents(final ArrayList<MoodEvent> filteredSelfMoodEventDataList,
                                     final ArrayList<MoodEvent> moodEventDataList,
                                     final ArrayAdapter<MoodEvent> moodEventAdapter) {
        this.filteredSelfMoodEventDataList = filteredSelfMoodEventDataList;
        this.moodEventDataList = moodEventDataList;
        this.moodEventAdapter = moodEventAdapter;

        CollectionReference collectionReference = db.collection("Users")
                .document(user.getEmail()).collection("MoodHistory");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                moodEventDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String textComment = doc.getString("Comment");
                    Mood mood = new Mood(doc.getString("Mood"));
                    SocialSituation socialSituation = new SocialSituation(doc.getString("SocialSituation"));
                    Location location = new Location(doc.getGeoPoint("Location"));
//                    LocalDateTime datetime = LocalDateTime.parse(doc.getString("DateTime"));
                    Timestamp datetime = doc.getTimestamp("DateTime");
                    MoodEvent moodEvent = new MoodEvent(mood, location, socialSituation, textComment, datetime);
//                    if(doc.getTimestamp("TIMESTAMP")!=null)
//                        Log.i("TAG",doc.getTimestamp("TIMESTAMP").toString());
                    moodEventDataList.add(moodEvent);
                }
                filterMoodEventList();
            }
        });
    }

    /**
     * Filter the input List of MoodEvent by the filterMap (Map<String, Boolean>)
     */
    public void filterMoodEventList(){
        this.filteredSelfMoodEventDataList.clear();
        for (Map.Entry<String, Boolean> entry : filterList.entrySet()) {
            String mood = entry.getKey();
            Boolean filterOn = entry.getValue();
            if (filterOn) {
                for (MoodEvent moodEvent : moodEventDataList) {
                    if (moodEvent.getMood().getMood() == mood) {
                        this.filteredSelfMoodEventDataList.add(moodEvent);
                    }
                }
            }
            Collections.sort(filteredSelfMoodEventDataList);
            Collections.reverse(filteredSelfMoodEventDataList);
            this.moodEventAdapter.notifyDataSetChanged();
        }
    }

    public void listenFollowingMoodEvents(final ArrayList<MoodEvent> moodEventDataList, final ArrayAdapter<MoodEvent> moodEventAdapter) {
        CollectionReference collectionReference = db.collection("Users")
                .document(user.getEmail()).collection("Followings");



        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                moodEventDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.getString("Mood") == null){
                        // handle following users that has no mood event
                        continue;
                    }
                    Mood mood = new Mood(doc.getString("Mood"));
                    String textComment = doc.getString("Comment");
                    SocialSituation socialSituation = new SocialSituation(doc.getString("SocialSituation"));
                    Location location = new Location(doc.getGeoPoint("Location"));
                    Timestamp datetime = doc.getTimestamp("DateTime");
                    String author = doc.getString("user_name");
                    MoodEvent moodEvent = new MoodEvent(author, mood, location, socialSituation, textComment, datetime);
                    moodEventDataList.add(moodEvent);
                }
                Collections.sort(moodEventDataList);
                Collections.reverse(moodEventDataList); // sort in reverse order
                moodEventAdapter.notifyDataSetChanged();
            }
        });
    }


    public void listenUserName(final TextView textView) {
//        DocumentReference docRef = db.collection("Users").document(user.getEmail());
        userDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Object user_name = documentSnapshot.getData().get("user_name");
                if (user_name != null) {
                    textView.setText(user_name.toString());
                    User.userName = user_name.toString();
                }

            }
        });
    }

    public void listenFollowerNumber(final TextView textView) {
        followerCollRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                queryDocumentSnapshots.size();
                textView.setText(String.valueOf(queryDocumentSnapshots.size()));
            }
        });
    }

    public void listenFollower(final ArrayList<String> arrayList) {
        followerCollRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                arrayList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    arrayList.add(doc.getId());
                }
            }
        });
    }

    public void listenFollowingNumber(final TextView textView) {
        followingCollRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                queryDocumentSnapshots.size();
                textView.setText(String.valueOf(queryDocumentSnapshots.size()));
            }
        });
    }

    public void listenFollowing(final ArrayList<String> arrayList) {
        followingCollRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                arrayList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    arrayList.add(doc.getId());
                }
            }
        });
    }

    public void listenMoodHistoryNumber(final TextView textView) {
        moodHistoryCollRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                queryDocumentSnapshots.size();
                textView.setText(String.valueOf(queryDocumentSnapshots.size()));
            }
        });
    }

    public void unfollow(String targetUserEmail) {
        followingCollRef.document(targetUserEmail).delete();
        db.collection("Users").document(targetUserEmail)
                .collection("Follower").document(user.getEmail())
                .delete();
    }

    public void remove(String targetUserEmail) {
        followerCollRef.document(targetUserEmail).delete();
        db.collection("Users").document(targetUserEmail)
                .collection("Following").document(user.getEmail())
                .delete();
    }

    public Map<String, Boolean> getFilterList() {
        return filterList;
    }

     /**
     * Replaced by listenUserName
     */
//    /**
//     * fetch user name from database.
//     */
//    private void fetchUserName(){
//        DocumentReference docRef = db.collection("Users").document(user.getEmail());
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        userName = document.getData().get("user_name").toString();
//                    } else {
//                        Log.d("TAG", "No such document");
//                    }
//                } else {
//                    Log.d("TAG", "get failed with ", task.getException());
//                }
//            }
//        });
//    }
//
//    /**
//     * return user name to user
//     * @return
//     */
//    public String getUserName(){
//        return this.userName;
//    }

}
