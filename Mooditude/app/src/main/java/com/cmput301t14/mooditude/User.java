package com.cmput301t14.mooditude;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User{
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private static String userName;

    public static final ArrayList<MoodEvent> moodEvents= new ArrayList<>();


    public User(){
//        this.db=db;
        db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
//        moodEvents=new ArrayList<MoodEvent>();
        getUserName();
    }

    public void pushMoodEvent(MoodEvent moodEvent){
        Location location  =moodEvent.getLocation();
        LocalDateTime localDateTime=moodEvent.getDatetime();
        Integer author= moodEvent.getAuthor();
        Log.i("TAG","1");
        Mood mood = moodEvent.getMood();
        SocialSituation socialSituation= moodEvent.getSocialSituation();
        String textComment= moodEvent.getTextComment();
        Log.i("TAG","2");
        if(db==null){
            Log.i("TAG","db null");
        }

        final CollectionReference collectionReference = db.collection("Users");
        DocumentReference userFile = db.collection("Users").document(user.getEmail());
        final CollectionReference moodHistory = db.collection("Users")
                .document(user.getEmail()).collection("MoodHistory");
//        Log.i("TAG","3");
//
        DocumentReference moodEntry=moodHistory.document(moodEvent.getDatetime().toString());

//
        HashMap<String,Object> moodHash = new HashMap<>();
        moodHash.put("Location",location.getGeopoint());
        moodHash.put("Mood",mood.getMood());
        moodHash.put("Comment",textComment);
        moodHash.put("DateTime",localDateTime.toString());
        moodHash.put("SocialSituation",socialSituation.getSocialSituation());

        moodEntry.set(moodHash);
//        moodEntry.set(moodEvent);
    }

    private MoodEvent MoodEventHashCast(HashMap hashMap){
        return null;
    }

    public ArrayList<MoodEvent> getSelfMoodEvents(ArrayList<MoodEvent> temp){

        final CollectionReference collectionReference = db.collection("Users")
                .document(user.getEmail()).collection("MoodHistory");
        Log.i("DT:",user.getEmail());

//        moodEvents=temp;


        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                moodEvents.clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
//                    MoodEvent moodEvent=(MoodEvent)doc.getData();
//                public MoodEvent(Integer author, Mood mood, Location location,
//                            SocialSituation socialSituation, String textComment) {
//                    Map<String,Object> map=doc.getData();
//                    Integer author=1;
//                    Mood mood= new Mood(map.get("Mood").toString());
//                    Location location= new Location(map.get("Location").toString())
//                    doc.get("Comment")

                    Integer author=1;
//                    String textComment=doc.get("Comment").toString();
                    String textComment=doc.getString("Comment");
                    Mood mood= new Mood(doc.getString("Mood"));
                    SocialSituation socialSituation= new SocialSituation(doc.getString("SocialSituation"));
                    Location location= new Location(doc.getGeoPoint("Location"));

//                    Log.i("DTAG:",doc.getData().toString());
//                    Log.i("DTAG:",doc.get("Location").getClass().getName());

                    MoodEvent moodEvent=new MoodEvent(author, mood, location,socialSituation,textComment);
//                public MoodEvent(Integer author, Mood mood, Location location,
//                            SocialSituation socialSituation, String textComment)
                    moodEvents.add(moodEvent);
//                    Log.i("LOGGER:",moodEvents.toString());
                }
            }
        });
        return moodEvents;
    }

    public String getUserName(){

//        Log.i("TAG",user.getEmail());
//        return "test";
//        userName="";

        DocumentReference docRef = db.collection("Users").document(user.getEmail());
//        Log.i("TAG","T1");
//        Source source = Source.SERVER;

//        result=docRef.get(source).getResult().getString("user_name");
//
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                    if (document != null) {
//                        userName =document.getString("user_name");
////                        Log.i("LOGGER",userName);
//
//                    }
//                }
//            }
//        });

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                        userName =documentSnapshot.getString("user_name");
//                        Log.i("LOGGER",userName);

                    }
            }
        });

//        while(userName==null){
//
//            Log.i("LOGGER","empString==null");
//        }
        return userName;
    }


}
