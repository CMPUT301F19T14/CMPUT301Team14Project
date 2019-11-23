package com.cmput301t14.mooditude.activities;

//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301t14.mooditude.adapters.CustomList;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.FollowRequestMessage;
import com.cmput301t14.mooditude.models.Person;
import com.cmput301t14.mooditude.services.FollowFollowingListOnClickListener;
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
import java.util.HashMap;
import java.util.Map;

/**
 * This is a class used to display a list of emails of follower or following for the current user
 */
public class DisplayFollow extends AppCompatActivity {

    ListView followList;
    ArrayAdapter<Person> followAdapter;
    ArrayList<Person> followDataList;
    String myID;
    private CollectionReference collectionReference;

    FirebaseFirestore db;

    public enum ListMode {Followers, Followings}

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_follow);

        Intent intent = getIntent();
        final ListMode listMode = ListMode.valueOf(intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Mode));


        db = FirebaseFirestore.getInstance();

        followList = findViewById(R.id.followList);
        followDataList = new ArrayList<>();
        followAdapter = new CustomList(this, followDataList);
        followList.setAdapter(followAdapter);

       collectionReference = db.collection("Users").document(new User().getEmail())
                .collection(listMode.toString());

        refreshList();
        final Map<ListMode,String> promptMap= new HashMap<>();
        promptMap.put(ListMode.Followers,"Are you sure that you want to remove this follower?");
        promptMap.put(ListMode.Followings,"Are you sure that you want to unfollow?");

        followList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String email = followDataList.get(i).getEmail();

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Log.i("BUTTON_POSITIVE", "Here:" + email);
                                if (listMode == ListMode.Followers)
                                    new User().remove(email);
                                if(listMode==ListMode.Followings)
                                    new User().unfollow(email);
                                refreshList();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };


                AlertDialog.Builder alert = new AlertDialog.Builder(DisplayFollow.this);
                alert.setMessage(promptMap.get(listMode))
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
                return true;
            }
        });

        if (listMode == ListMode.Followers){
            followList.setOnItemClickListener(new FollowFollowingListOnClickListener.Followers(followDataList));
        }







    }


    private void refreshList(){
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    followDataList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        followDataList.add(new Person(doc.getId(), doc.getString("user_name")));
                    }
                    followAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
