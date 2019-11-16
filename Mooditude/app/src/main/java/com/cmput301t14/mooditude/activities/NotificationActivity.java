package com.cmput301t14.mooditude.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cmput301t14.mooditude.models.FollowRequestMessage;
import com.cmput301t14.mooditude.services.MenuBar;
import com.cmput301t14.mooditude.models.Message;
import com.cmput301t14.mooditude.adapters.MessageAdapter;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.TextMessage;
import com.cmput301t14.mooditude.services.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    CollectionReference messageBoxRef;
    User user;
    ArrayList<Message> messageArrayList;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        user = new User();
        messageArrayList = new ArrayList<>();

        Intent intent = getIntent();
        final String messageEmail = intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Email);
        MenuBar menuBar = new MenuBar(NotificationActivity.this, messageEmail, 3);

//        TextView title = (TextView) findViewById(R.id.activityTitle3);
//        title.setText("Notification Activity");
        recyclerView = findViewById(R.id.message_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        messageBoxRef = FirebaseFirestore.getInstance().collection("Users").document(user.getEmail()).collection("MessageBox");
        messageBoxRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                messageArrayList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String typeStr = doc.getString("type");
                    String sender = doc.getString("sender");
                    String receiver = doc.getString("receiver");
                    Timestamp datetime = doc.getTimestamp("datetime");
                    Boolean newMessage = doc.getBoolean("newMessage");

//                    Log.i("LOGA", typeStr);
                    if (typeStr.equals("followRequest")) {
                        FollowRequestMessage followRequestMessage = new FollowRequestMessage(sender, receiver, datetime, newMessage);
                        messageArrayList.add(followRequestMessage);
                    }
                    else if(typeStr.equals("text")){
                        String textMessage = doc.getString("text");
                        messageArrayList.add(new TextMessage(textMessage,sender,receiver,datetime,newMessage));
                    }
                }
                Log.i("LOGB",String.valueOf(queryDocumentSnapshots.size()));
                Log.i("LOGB",String.valueOf(messageArrayList.size()));
//                searchAdapter = new SearchAdapter(SearchActivity.this, userNameList, userEmailList);
                messageAdapter = new MessageAdapter(NotificationActivity.this, messageArrayList);
                recyclerView.setAdapter(messageAdapter);
            }
        });
//        messageBoxRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
////                        Log.d("TAG", "Document exists!");
//                        Location location  =moodEvent.getLocation();
//                        Timestamp localDateTime=moodEvent.getDatetime();
////                        Integer author= moodEvent.getAuthor();
//                        Mood mood = moodEvent.getMood();
//                        SocialSituation socialSituation= moodEvent.getSocialSituation();
//                        String textComment= moodEvent.getTextComment();
//                        if (location.getGeopoint() != document.getGeoPoint("Location")){
//                            moodHash.put("Location",location.getGeopoint());
//                        }
//                        if (mood.getMood() != document.getString("Mood")){
//                            moodHash.put("Mood",mood.getMood());
//                        }
//                        if (textComment != document.getString("Comment")){
//                            moodHash.put("Comment",textComment);
//                        }
////                        if (localDateTime != document.getTimestamp("DateTime")){
//////                            Log.i("TAG1B",document.getTimestamp("DateTime").toString());
////                            moodHash.put("DateTime",localDateTime);
//////                            Log.i("TAG1",localDateTime.toString());
//////                            Log.i("TAG1B",document.getTimestamp("DateTime").toString());
////                        }
//                        if (socialSituation.getSocialSituation() != document.getString("SocialSituation")){
//                            moodHash.put("SocialSituation",socialSituation.getSocialSituation());
//                        }
//                        moodHash.put("DateTime",localDateTime);
//                        moodEntry.update(moodHash);
//                    }
//            }
//        })

    }
}
