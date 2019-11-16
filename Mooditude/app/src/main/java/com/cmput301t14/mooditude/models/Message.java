package com.cmput301t14.mooditude.models;

import com.cmput301t14.mooditude.services.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Boolean.TRUE;

public abstract class Message {
    protected String sender;
    protected String receiver;
    protected Timestamp datetime;
    protected Boolean newMessage;
    protected String type;
    protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public Message(String receiver) {
        this.receiver = receiver;
        this.sender = new User().getEmail();
        this.datetime= Timestamp.now();
        this.newMessage=TRUE;
    }

    public Message(String sender, String receiver, Timestamp datetime, Boolean newMessage) {
        this.sender = sender;
        this.receiver = receiver;
        this.datetime = datetime;
        this.newMessage = newMessage;
    }

    public Message(String sender, String receiver, Timestamp datetime) {
        this.sender = sender;
        this.receiver = receiver;
        this.datetime = datetime;
        this.newMessage=TRUE;
    }

    public Message(String sender, String receiver, Boolean newMessage) {
        this.sender = sender;
        this.receiver = receiver;
        this.newMessage = newMessage;
        this.datetime= Timestamp.now();
    }

    public Message(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.datetime= Timestamp.now();
        this.newMessage=TRUE;
    }


    public String getType() {
        return type;
    }

    public Boolean isNewMessage() {
        return newMessage;
    }

    public void setNewMessage( ) {
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");
        CollectionReference receiverMsgBox= usersCollection.document(receiver).collection("MessageBox");
        String epochTimeString= String.valueOf(this.datetime.getSeconds());
        final DocumentReference messageEntry=receiverMsgBox.document(epochTimeString);
        messageEntry.update("newMessage",false);
    }


    public abstract String toStringContent();

    public String toStringDatetime() {
        return sdf.format(new Date(this.datetime.getSeconds() * 1000));
    }

    public void delete(){
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");
        CollectionReference receiverMsgBox= usersCollection.document(receiver).collection("MessageBox");
        String epochTimeString= String.valueOf(this.datetime.getSeconds());
        final DocumentReference messageEntry=receiverMsgBox.document(epochTimeString);
        messageEntry.delete();
    }
}
