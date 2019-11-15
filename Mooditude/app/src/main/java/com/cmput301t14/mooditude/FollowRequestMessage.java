package com.cmput301t14.mooditude;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

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
    String toStringContent() {
        return this.sender+" wants to follow you";
    }

    @Override
    String toStringDatetime() {
        return super.sdf.format(new Date(this.datetime.getSeconds() * 1000));
    }

    @Override
    void invoke() {
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
}
