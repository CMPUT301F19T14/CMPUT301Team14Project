package com.cmput301t14.mooditude;

//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DisplayFollow extends AppCompatActivity {

    ListView followList;
    ArrayAdapter<String> followAdapter;
    ArrayList<String> followDataList;
    String myID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_follow);

        final String TAG = "Sample";
        String userEmail = "";


        FirebaseFirestore db;


        followList = findViewById(R.id.followList);
        followDataList = new ArrayList<>();
        followAdapter = new CustomList(this, followDataList);
        followList.setAdapter(followAdapter);


        db = FirebaseFirestore.getInstance();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        String messageMode = intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Mode);

        final String messageEmail = intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Email);
        myID = messageEmail;

        final CollectionReference collectionReference = db.collection("Users");

        final DocumentReference documentReference = collectionReference.document(myID);

        if (messageMode.compareTo("Follower") == 0){

            final TextView followMode = findViewById(R.id.followMode);
            followMode.setText("Follwer List");

            //https://dzone.com/articles/cloud-firestore-read-write-update-and-delete

            documentReference.get().addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
                @Override
                public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                    followDataList.clear();
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        ArrayList<String> followerList = (ArrayList<String>) doc.get("followers");

                        for (String f : followerList) {
                            //userEmail = doc.getId();
//                        Log.d(TAG, String.valueOf(doc.getData().get("Follower")));
//
//
//                        String followerUser = (String) doc.getData().get("Follower");
                            //the follower list for the userEmail:
                            followDataList.add(f);

                        }
                        followAdapter.notifyDataSetChanged();
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                }
            });




        }

        else if(messageMode.compareTo("Following") == 0){

            final TextView followMode = findViewById(R.id.followMode);
            followMode.setText("Following List");

            documentReference.get().addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
                @Override
                public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                    followDataList.clear();
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        ArrayList<String> followingList = (ArrayList<String>) doc.get("following");

                        for (String f : followingList) {

                            followDataList.add(f);

                        }
                        followAdapter.notifyDataSetChanged();
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
