package com.cmput301t14.mooditude.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301t14.mooditude.adapters.FollowerFollowingListAdapter;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.Person;
import com.cmput301t14.mooditude.services.FollowFollowingListOnClickListener;
import com.cmput301t14.mooditude.services.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a class used to display a list of emails of follower or following for the current user
 */
public class DisplayFollow extends AppCompatActivity {

    private ArrayAdapter<Person> followAdapter;
    private ArrayList<Person> followDataList;

    private CollectionReference collectionReference;

    public enum ListMode {Followers, Followings}



    /**
     * On create activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_following_list);

        Intent intent = getIntent();
        final ListMode listMode = ListMode.valueOf(intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Mode));


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ListView followList = findViewById(R.id.followList);
        followDataList = new ArrayList<>();
        followAdapter = new FollowerFollowingListAdapter(this, followDataList,listMode);
        followList.setAdapter(followAdapter);
        String followers = "Followers";
        String followings = "Followings";

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
//            followList.setOnItemClickListener(new FollowFollowingListOnClickListener.Followers(followDataList));
        }

        TextView title=findViewById(R.id.followMode);

        if(listMode==ListMode.Followers){
            title.setText(followers);
        }
        if(listMode==ListMode.Followings){
            title.setText(followings);
        }

    }

    /**
     * Refresh follow and following list.
     */
    private void refreshList(){
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    followDataList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        followDataList.add(new Person(doc.getId(), doc.getString("user_name")));
                        Log.i("refreshList",doc.getString("user_name"));
                        Log.i("doc.getId()",doc.getId());
                    }
                    followAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
