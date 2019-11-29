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


/**
 * activity for displaying notification (messages), requests, text messages
 */
public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Message> messageArrayList;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        User user = new User();
        messageArrayList = new ArrayList<>();

        Intent intent = getIntent();
        final String messageEmail = intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Email);
        MenuBar menuBar = new MenuBar(NotificationActivity.this, messageEmail, 3);

        recyclerView = findViewById(R.id.message_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        CollectionReference messageBoxRef = FirebaseFirestore.getInstance().collection("Users").document(user.getEmail()).collection("MessageBox");
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
                messageAdapter = new MessageAdapter(NotificationActivity.this, messageArrayList);
                recyclerView.setAdapter(messageAdapter);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
