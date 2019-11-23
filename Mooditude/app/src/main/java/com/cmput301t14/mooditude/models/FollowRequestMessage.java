package com.cmput301t14.mooditude.models;

import com.cmput301t14.mooditude.services.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import static java.lang.Boolean.TRUE;

public class FollowRequestMessage extends Message {
    public FollowRequestMessage(String receiver) {
        super(receiver);
        this.type="followRequest";
    }

    public FollowRequestMessage(String sender, String receiver, Timestamp datetime, boolean newMessage) {
        super(sender, receiver, datetime, newMessage);
        this.type="followRequest";
    }

    public FollowRequestMessage(String sender, String receiver, Timestamp datetime) {
        super(sender, receiver, datetime);
        this.type="followRequest";
    }

    public FollowRequestMessage(String sender, String receiver, boolean newMessage) {
        super(sender, receiver, newMessage);
        this.type="followRequest";
    }

    public FollowRequestMessage(String sender, String receiver) {
        super(sender, receiver);
        this.type="followRequest";
    }

    @Override
    public String toStringContent() {
        return this.sender+" wants to follow you";
    }

    public void invoke() {
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");
        CollectionReference receiverMsgBox= usersCollection.document(receiver).collection("MessageBox");
        String epochTimeString= String.valueOf(this.datetime.getSeconds());
        final DocumentReference messageEntry=receiverMsgBox.document(epochTimeString);
        final HashMap<String,Object> messageHash = new HashMap<>();
        messageHash.put("sender",this.sender);
        messageHash.put("receiver",this.receiver);
        messageHash.put("newMessage",this.newMessage);
        messageHash.put("datetime",this.datetime);
        messageHash.put("type",this.type);
        messageEntry.set(messageHash);
    }

    public void accept(){
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");

//        // add sender to receiver's Followers collection
////        CollectionReference receiverFollowers = usersCollection.document(this.receiver).collection("Followers");
////        final DocumentReference receiverFollowersEntry= receiverFollowers.document(this.sender);
////        final HashMap<String,Object> followerHash = new HashMap<>();
////        receiverFollowersEntry.set(followerHash);
        new User().addFollower(this.sender);



        // add receiver to sender's Followings collection
        CollectionReference senderFollowings = usersCollection.document(this.sender).collection("Followings");
        final DocumentReference senderFollowingsEntry= senderFollowings.document(this.receiver);
        final HashMap<String,Object> followingHash = new HashMap<>();
        // TODO: get receiver's most recent MoodEvent and put it in the senderFollowingsEntry
//        followerHash.put("type", "reject");
        usersCollection.document(receiver).collection("MoodHistory").orderBy("DateTime", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                HashMap<String,Object> moodEventHash= new HashMap<>();
                for(DocumentSnapshot doc: queryDocumentSnapshots){
                    moodEventHash.put("DateTime", doc.getTimestamp("DateTime"));
                    moodEventHash.put("Comment", doc.getString("Comment"));
                    moodEventHash.put("SocialSituation", doc.get("SocialSituation"));
                    moodEventHash.put("Mood",doc.get("Mood"));
                    moodEventHash.put("Location",doc.getGeoPoint("Location"));
                    moodEventHash.put("user_name", User.getUserName());
                    senderFollowingsEntry.set(moodEventHash);
                }

            }
        });
        senderFollowingsEntry.set(followingHash);

        // send accept message to sender's MessageBox collection
        CollectionReference senderMsgBox= usersCollection.document(this.sender).collection("MessageBox");
        String epochTimeString= String.valueOf(Timestamp.now().getSeconds());
        DocumentReference messageEntry=senderMsgBox.document(epochTimeString);
        HashMap<String,Object> messageHash = new HashMap<>();

        new TextMessage(this.receiver+" accepted you to follow",this.sender,this.receiver, Timestamp.now(),TRUE).invoke(senderMsgBox);

        // delete this message and add new TextMessage to receiver MessageBox collection
        this.delete();
        CollectionReference receiverMsgBox= usersCollection.document(this.receiver).collection("MessageBox");
        new TextMessage("you accepted "+sender+" successfully",this.sender,this.receiver, Timestamp.now(),TRUE).invoke(receiverMsgBox);
    }

    public void reject(){
        // send reject message to sender's MessageBox collection
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");
        CollectionReference senderMsgBox= usersCollection.document(this.sender).collection("MessageBox");
//        String epochTimeString= String.valueOf(Timestamp.now().getSeconds());
//        DocumentReference messageEntry=senderMsgBox.document(epochTimeString);
//        HashMap<String,Object> messageHash = new HashMap<>();
//        messageHash.put("text", receiver+" rejected you to follow");
//        messageHash.put("sender",this.receiver);
//        messageHash.put("receiver",this.sender);
//        messageHash.put("newMessage", TRUE);
//        messageHash.put("datetime", Timestamp.now());
//        messageHash.put("type", "text");
//        messageEntry.set(messageHash);

        new TextMessage(receiver+" rejected you to follow",this.receiver,this.sender, Timestamp.now(),TRUE).invoke(senderMsgBox);
        // delete this message and add new TextMessage to receiver MessageBox collection
        this.delete();
        CollectionReference receiverMsgBox= usersCollection.document(this.receiver).collection("MessageBox");
//        epochTimeString= String.valueOf(Timestamp.now().getSeconds());
//        messageEntry = receiverMsgBox.document(epochTimeString);
//        messageHash = new HashMap<>();
//        messageHash.put("text", "you rejected "+sender+" successfully");
//        messageHash.put("sender",this.sender);
//        messageHash.put("receiver",this.receiver);
//        messageHash.put("newMessage", TRUE);
//        messageHash.put("datetime", Timestamp.now());
//        messageHash.put("type", "text");
//        messageEntry.set(messageHash);
        new TextMessage("you rejected "+sender+" successfully",this.sender,this.receiver, Timestamp.now(),TRUE).invoke(receiverMsgBox);
    }
}
