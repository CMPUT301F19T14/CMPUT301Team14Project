package com.cmput301t14.mooditude.activities;

//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301t14.mooditude.adapters.CustomList;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.Person;
import com.cmput301t14.mooditude.services.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is a class used to display a list of emails of follower or following for the current user
 */
public class DisplayFollow extends AppCompatActivity {

    ListView followList;
    ArrayAdapter<Person> followAdapter;
    ArrayList<Person> followDataList;
    String myID;

    FirebaseFirestore db;
    public enum ListMode {Followers, Followings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_follow);

        // Get the Intent that started this activity and extract the string for mode(Follower/Following) and userEmail
        Intent intent = getIntent();
        ListMode listMode = ListMode.valueOf(intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Mode));
//        final String messageEmail = intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Email);
//        myID = messageEmail;

        db = FirebaseFirestore.getInstance();

        followList = findViewById(R.id.followList);
        followDataList = new ArrayList<>();
        followAdapter = new CustomList(this, followDataList);
        followList.setAdapter(followAdapter);

        final CollectionReference collectionReference=db.collection("Users").document(new User().getEmail())
                .collection(listMode.toString());

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    followDataList.clear();
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        followDataList.add(new Person(doc.getId(),doc.getString("user_name")));
                    }
                    followAdapter.notifyDataSetChanged();
                }
            }
        });









//        final CollectionReference collectionReference = db.collection("Users");
//        final DocumentReference documentReference = collectionReference.document(myID).collection("Followers");

        //according to the require mode, get the required filed from database and show.
//        if (messageMode.compareTo("Follower") == 0){
//
//            final TextView followMode = findViewById(R.id.followMode);
//            followMode.setText("Follwer List");
//
//            //Reference: https://dzone.com/articles/cloud-firestore-read-write-update-and-delete
//            documentReference.get().addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
//                @Override
//                public void onComplete(@NonNull Task< DocumentSnapshot > task) {
//                    followDataList.clear();
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot doc = task.getResult();
//                        ArrayList<String> followerList = (ArrayList<String>) doc.get("followers");
//
//                        for (String f : followerList) {
//
//                            followDataList.add(f);
//
//                        }
//                        followAdapter.notifyDataSetChanged();
//                    }
//                }
//            })
//            .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//        else if(messageMode.compareTo("Following") == 0){
//
//            final TextView followMode = findViewById(R.id.followMode);
//            followMode.setText("Following List");
//
//            documentReference.get().addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
//                @Override
//                public void onComplete(@NonNull Task< DocumentSnapshot > task) {
//                    followDataList.clear();
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot doc = task.getResult();
//                        ArrayList<String> followingList = (ArrayList<String>) doc.get("following");
//
//                        for (String f : followingList) {
//
//                            followDataList.add(f);
//
//                        }
//                        followAdapter.notifyDataSetChanged();
//                    }
//                }
//            })
//            .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
    }
}
