package com.cmput301t14.mooditude.services;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301t14.mooditude.adapters.SearchAdapter;
import com.cmput301t14.mooditude.models.Location;
import com.cmput301t14.mooditude.models.Mood;
import com.cmput301t14.mooditude.models.MoodEvent;
import com.cmput301t14.mooditude.models.SocialSituation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

    static private DocumentReference userDocRef;
    static private CollectionReference followingCollRef;
    static private CollectionReference followerCollRef;
    static private CollectionReference moodHistoryCollRef;

    static public ArrayList<String> followerList = new ArrayList<>();

    static public ArrayList<String> followingList = new ArrayList<>();

    private Map<String, Boolean> filterList;
    private ArrayList<MoodEvent> filteredSelfMoodEventDataList;
    private ArrayList<MoodEvent> moodEventDataList;
    private ArrayAdapter<MoodEvent> moodEventAdapter;

    static private String userName = "";

    /**
     * User class in charge of all the information transfer and communicates with database.
     */
    public User() {
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userDocRef = db.collection("Users").document(getEmail());
        followingCollRef = db.collection("Users").document(user.getEmail()).collection("Followings");
        followerCollRef = db.collection("Users").document(user.getEmail()).collection("Followers");
        moodHistoryCollRef = db.collection("Users").document(user.getEmail()).collection("MoodHistory");

        User.refreshUserName();

        filterList = new HashMap<>();
        filterList.put("HAPPY", Boolean.TRUE);
        filterList.put("SAD", Boolean.TRUE);
        filterList.put("ANGRY", Boolean.TRUE);
        filterList.put("EXCITED", Boolean.TRUE);
    }

    /**
     * Add followerID to followerList
     * @param followerID get the user id of follower
     */
    public void addFollower(String followerID) {
        // add sender to receiver's Followers collection
        Log.i("addFollower", followerID);
        CollectionReference receiverFollowers = userDocRef.collection("Followers");
        final DocumentReference receiverFollowersEntry = receiverFollowers.document(followerID);

        final HashMap<String, Object> followerHash = new HashMap<>();
        DocumentReference followerDocument= db.collection("Users").document(followerID);
        followerDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (task.isSuccessful()) {
                    if (document.exists()) {
                        String followerName= document.getString("user_name");
                        followerHash.put("user_name", followerName);
                        receiverFollowersEntry.set(followerHash);
                    }
                }
            }
        });


    }

    /**
     * Return userName     *
     * @return the user's name
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * refresh user name
     */
    private static void refreshUserName() {
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

    /**
     * get the user email
     * @return user email
     */
    public String getEmail() {
        return user.getEmail();
    }

    /**
     * push Mood Event to database.
     * @param moodEvent the moodevent
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

                        SocialSituation socialSituation= moodEvent.getSocialSituation();
                        String textComment= moodEvent.getTextComment();
                        String photoUrl = moodEvent.getPhotoUrl();
                        if (location == null){
                            moodHash.put("Location",null);
                        } else if (location.getGeopoint() != document.getGeoPoint("Location")){
                            moodHash.put("Location",location.getGeopoint());
                        }
                        if (!mood.getMood().equals(document.getString("Mood"))) {
                            moodHash.put("Mood", mood.getMood());
                        }
                        if (!textComment.equals(document.getString("Comment"))) {
                            moodHash.put("Comment", textComment);
                        }
                        if (!socialSituation.getSocialSituation().equals(document.getString("SocialSituation"))) {
                            moodHash.put("SocialSituation", socialSituation.getSocialSituation());
                        }

                        if(photoUrl == null){
                            moodHash.put("Photograph",null);
                        }else if(!photoUrl.equals(document.getString("Photograph"))){
                            moodHash.put("Photograph", photoUrl);
                        }
                        moodHash.put("DateTime",localDateTime);

                        moodEntry.update(moodHash);
                    } else {
                        Log.d("TAG", "Document does not exist!");
                        Location location = moodEvent.getLocation();
                        Timestamp localDateTime = moodEvent.getDatetime();
                        Mood mood = moodEvent.getMood();

                        SocialSituation socialSituation= moodEvent.getSocialSituation();
                        String textComment= moodEvent.getTextComment();
                        String photoUrl = moodEvent.getPhotoUrl();

                        if (location == null){
                            moodHash.put("Location", null);
                        }
                        else{
                            moodHash.put("Location", location.getGeopoint());
                        }
                        moodHash.put("Mood",mood.getMood());
                        moodHash.put("Comment",textComment);
                        moodHash.put("DateTime",localDateTime);
                        moodHash.put("SocialSituation",socialSituation.getSocialSituation());
                        if (photoUrl == null){
                            moodHash.put("Photograph", null);
                        }
                        else{
                            moodHash.put("Photograph", photoUrl);
                        }
                        moodEntry.set(moodHash);
                    }
                    updateMostRecentMoodEvent();
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }

            }
        });
    }

    /**
     * delete Mood Event from server.
     * @param selectedMoodEvent selected mood event from click
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
        updateMostRecentMoodEvent();
    }

    /**
     * call it to update all follower's following collection after delete/edit/add MoodEvent
     */
    private void updateMostRecentMoodEvent(){
        final HashMap<String,Object> followingHash = new HashMap<>();
        // put receiver's user_name in no matter the most recent MoodEvent exist of not
        followingHash.put("user_name", User.getUserName());
        // set the hash into each follower's following collection
        followerCollRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    String followerEmail = doc.getId();
                    DocumentReference documentReference = db.collection("Users")
                            .document(followerEmail).collection("Followings").document(user.getEmail());
                    documentReference.set(followingHash);
                }
            }
        });
        // then, get the most recent MoodEvent
        moodHistoryCollRef.orderBy("DateTime", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc: queryDocumentSnapshots){
                    // put the most recent MoodEvent in if it exists
                    followingHash.put("user_name", User.getUserName());
                    followingHash.put("DateTime", doc.getTimestamp("DateTime"));
                    followingHash.put("Comment", doc.getString("Comment"));
                    followingHash.put("SocialSituation", doc.get("SocialSituation"));
                    followingHash.put("Mood",doc.get("Mood"));
                    followingHash.put("Location",doc.getGeoPoint("Location"));
                    followingHash.put("Photograph",doc.getString("Photograph"));
                    // set the hash into each follower's following collection
                    followerCollRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                String followerEmail = doc.getId().toString();
                                DocumentReference documentReference = db.collection("Users")
                                        .document(followerEmail).collection("Followings").document(user.getEmail());
                                documentReference.set(followingHash);
                            }
                        }
                    });
                }

            }
        });
    }

    /**
     * Connect Array Adapter to database to retrieve online information from database.
     * @param moodEventDataList list stored the data
     * @param moodEventAdapter mood event adapter
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
                    Location location = null;
                    if (doc.getGeoPoint("Location") != null){
                        location = new Location(doc.getGeoPoint("Location"));
                    }
                    Timestamp datetime = doc.getTimestamp("DateTime");
                    String photo = doc.getString("Photograph");
                    MoodEvent moodEvent=new MoodEvent(mood, location,socialSituation,textComment,datetime, photo);
                    moodEventDataList.add(moodEvent);
                }
                filterMoodEventList();
            }
        });
    }


    /**
     * Filter the input List of MoodEvent by the filterMap (Map<String, Boolean>)
     */
    void filterMoodEventList() {
        this.filteredSelfMoodEventDataList.clear();
        for (Map.Entry<String, Boolean> entry : filterList.entrySet()) {
            String mood = entry.getKey();
            Boolean filterOn = entry.getValue();
            if (filterOn) {
                for (MoodEvent moodEvent : moodEventDataList) {
                    if (moodEvent.getMood().getMood().equals(mood)) {
                        this.filteredSelfMoodEventDataList.add(moodEvent);
                    }
                }
            }
            Collections.sort(filteredSelfMoodEventDataList);
            Collections.reverse(filteredSelfMoodEventDataList);
            this.moodEventAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Listen moodEvents and reflects results on map.
     * @param googleMap googlemap object
     */
    public void listenSelfMoodEventsOnMap(final GoogleMap googleMap){
        CollectionReference collectionReference = db.collection("Users")
                .document(user.getEmail()).collection("MoodHistory");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                googleMap.clear();
                LatLng cameraLocation = null;
                MoodEvent recentMoodEvent = null;
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                        Location location = null;
                        if (doc.getGeoPoint("Location") != null){
                            location = new Location(doc.getGeoPoint("Location"));
                            Mood mood= new Mood(doc.getString("Mood"));
                            String textComment=doc.getString("Comment");
                            SocialSituation socialSituation= new SocialSituation(doc.getString("SocialSituation"));
                            Timestamp datetime = doc.getTimestamp("DateTime");
                            String photo = doc.getString("Photograph");
                            String email=doc.getId();
                            String author = doc.getString("user_name");

                            MoodEvent moodEvent=new MoodEvent(author,mood, location,socialSituation,textComment,datetime,photo);
                            LatLng moodEventLocation = new LatLng(location.getGeopoint().getLatitude(), location.getGeopoint().getLongitude());

                            if (recentMoodEvent != null){
                                int laterEevent = recentMoodEvent.compareTo(moodEvent);
                                if (laterEevent == -1){
                                    recentMoodEvent = moodEvent;
                                    cameraLocation = moodEventLocation;
                                }
                            }
                            else {
                                recentMoodEvent = moodEvent;
                                cameraLocation = moodEventLocation;
                            }


                            Marker marker = googleMap.addMarker(new MarkerOptions()
                                    .position(moodEventLocation)
                                    .title(mood.getEmoticon())
                                    .alpha((float)0.8)
                                    .icon(BitmapDescriptorFactory.defaultMarker(Mood.getMoodMapMarkerColor(mood))));
                            marker.setTag(moodEvent);
                        }
                }
                if (cameraLocation != null){
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraLocation, 17.0f));
                }
            }
        });
    }

    /**
     * Listen mood events than update on Home Acticity
     * @param moodEventDataList list to store data of mood event
     * @param moodEventAdapter mood event adapter
     */
    public void listenFollowingMoodEvents(final ArrayList<MoodEvent> moodEventDataList, final ArrayAdapter<MoodEvent> moodEventAdapter){

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
                    Location location = null;
                    if (doc.getGeoPoint("Location") != null){
                        location = new Location(doc.getGeoPoint("Location"));
                    }
                    Timestamp datetime = doc.getTimestamp("DateTime");
                    String author = doc.getString("user_name");
                    String email= doc.getId();
                    String photo = null;
                    if (doc.getString("Photograph") != null){
                        photo =doc.getString("Photograph");
                    }
                    MoodEvent moodEvent = new MoodEvent(author, mood, location, socialSituation, textComment, datetime,email, photo);
                    moodEventDataList.add(moodEvent);
                }
                Collections.sort(moodEventDataList);
                Collections.reverse(moodEventDataList); // sort in reverse order
                moodEventAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Listen following moodEvents and reflects results on map.
     * @param googleMap googlemap object
     */
    public void listenFollowingMoodEventsOnMap(final GoogleMap googleMap){
        CollectionReference collectionReference = db.collection("Users")
                .document(user.getEmail()).collection("Followings");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                googleMap.clear();
                LatLng cameraLocation = null;
                MoodEvent recentMoodEvent = null;
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){

                    Location location = null;
                    if (doc.getGeoPoint("Location") != null){
                        location = new Location(doc.getGeoPoint("Location"));
                        Mood mood= new Mood(doc.getString("Mood"));
                        String textComment=doc.getString("Comment");
                        SocialSituation socialSituation= new SocialSituation(doc.getString("SocialSituation"));
                        Timestamp datetime = doc.getTimestamp("DateTime");
                        String photo = doc.getString("Photograph");
                        String email=doc.getId();
                        String author = doc.getString("user_name");

                        MoodEvent moodEvent=new MoodEvent(author,mood, location,socialSituation,textComment,datetime,photo);
                        LatLng moodEventLocation = new LatLng(location.getGeopoint().getLatitude(), location.getGeopoint().getLongitude());

                        if (recentMoodEvent != null){
                            int laterEevent = recentMoodEvent.compareTo(moodEvent);
                            if (laterEevent == -1){
                                recentMoodEvent = moodEvent;
                                cameraLocation = moodEventLocation;
                            }
                        }
                        else {
                            recentMoodEvent = moodEvent;
                            cameraLocation = moodEventLocation;
                        }

                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(moodEventLocation)
                                .title(mood.getEmoticon())
                                .snippet("Author: "+author)
                                .alpha((float)0.8)
                                .icon(BitmapDescriptorFactory.defaultMarker(Mood.getMoodMapMarkerColor(mood))));
                        marker.setTag(moodEvent);
                    }


                }
                if (cameraLocation != null){
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraLocation, 17.0f));
                }
            }
        });
    }

    /**
     * Listen user name and reflect result on textView
     * @param textView textview of username
     */
    public void listenUserName(final TextView textView){
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

    /**
     * Listen number of followers and reflects results on textView.
     * @param textView testview of follower number
     */
    public void listenFollowerNumber(final TextView textView) {
        followerCollRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                queryDocumentSnapshots.size();
                textView.setText(String.valueOf(queryDocumentSnapshots.size()));
            }
        });
    }

    /**
     * Listen followers return result on arrayList.
     * @param arrayList store follower
     */
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


    /**
     * Not decent function to notify the searchAdapter that following and follower list has been
     * changed.
     * @param searchAdapter search adapter
     */
    public void notifyFollowFollowingDateChange(final SearchAdapter searchAdapter) {
        followerCollRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (searchAdapter != null){
                    Log.i("searchAdapter","searchAdapter");
                    searchAdapter.notifyDataSetChanged ();
                }
            }
        });

        followingCollRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (searchAdapter != null){
                    Log.i("searchAdapter","searchAdapter");
                    searchAdapter.notifyDataSetChanged ();
                }
            }
        });

    }

    /**
     * Listen to number of following people, reflect result on TextView
     * @param textView testview to display the following number
     */
    public void listenFollowingNumber(final TextView textView) {
        followingCollRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                queryDocumentSnapshots.size();
                textView.setText(String.valueOf(queryDocumentSnapshots.size()));
            }
        });
    }

    /**
     * Listen to following people, return result to one arrayList
     * @param arrayList store following people
     */
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

    /**
     * Listen to number of following people, reflect result on TextView
     * @param textView textview of number of following people
     */
    public void listenMoodHistoryNumber(final TextView textView) {
        moodHistoryCollRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                queryDocumentSnapshots.size();
                textView.setText(String.valueOf(queryDocumentSnapshots.size()));
            }
        });
    }

    /**
     * unfollow target people
     * @param targetUserEmail the email of selected following people
     */
    public void unfollow(String targetUserEmail) {
        followingCollRef.document(targetUserEmail).delete();
        Log.i("unfollow",targetUserEmail);
        db.collection("Users").document(targetUserEmail)
                .collection("Followers").document(user.getEmail())
                .delete();
    }

    /**
     * remove follower
     * @param targetUserEmail the email of selected follower
     */
    public void remove(String targetUserEmail) {
        followerCollRef.document(targetUserEmail).delete();
        db.collection("Users").document(targetUserEmail)
                .collection("Followings").document(user.getEmail())
                .delete();
    }

    /**
     * filter getter
     * @return filter list
     */
    public Map<String, Boolean> getFilterList() {
        return filterList;
    }

}
