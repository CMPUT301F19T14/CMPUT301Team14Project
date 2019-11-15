package com.cmput301t14.mooditude;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

        // add sender to receiver's Followers collection
        CollectionReference receiverFollowers = usersCollection.document(this.receiver).collection("Followers");
        final DocumentReference receiverFollowersEntry= receiverFollowers.document(this.sender);
        final HashMap<String,Object> followerHash = new HashMap<>();
        receiverFollowersEntry.set(followerHash);

        // add receiver to sender's Followings collection
        CollectionReference senderFollowings = usersCollection.document(this.sender).collection("Followings");
        final DocumentReference senderFollowingsEntry= senderFollowings.document(this.receiver);
        final HashMap<String,Object> followingHash = new HashMap<>();
        // TODO: get receiver's most recent MoodEvent and put it in the senderFollowingsEntry
//        followerHash.put("type", "reject");
        senderFollowingsEntry.set(followingHash);

        // send accept message to sender's MessageBox collection
        CollectionReference senderMsgBox= usersCollection.document(this.sender).collection("MessageBox");
        String epochTimeString= String.valueOf(Timestamp.now().getSeconds());
        final DocumentReference messageEntry=senderMsgBox.document(epochTimeString);
        final HashMap<String,Object> messageHash = new HashMap<>();
        messageHash.put("sender",this.receiver);
        messageHash.put("receiver",this.sender);
        messageHash.put("newMessage", TRUE);
        messageHash.put("datetime", Timestamp.now());
        messageHash.put("type", "accept");
        messageEntry.set(messageHash);
        // TODO: change the type of this message to completed
    }

    public void reject(){
        // send reject message to sender's MessageBox collection
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");
        CollectionReference senderMsgBox= usersCollection.document(this.sender).collection("MessageBox");
        String epochTimeString= String.valueOf(Timestamp.now().getSeconds());
        final DocumentReference messageEntry=senderMsgBox.document(epochTimeString);
        final HashMap<String,Object> messageHash = new HashMap<>();
        messageHash.put("sender",this.receiver);
        messageHash.put("receiver",this.sender);
        messageHash.put("newMessage", TRUE);
        messageHash.put("datetime", Timestamp.now());
        messageHash.put("type", "reject");
        messageEntry.set(messageHash);
        // TODO: change the type of this message to completed
    }
}
