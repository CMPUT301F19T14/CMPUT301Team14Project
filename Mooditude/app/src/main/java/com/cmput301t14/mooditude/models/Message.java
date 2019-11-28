package com.cmput301t14.mooditude.models;

import android.util.Log;

import com.cmput301t14.mooditude.services.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Boolean.TRUE;

/**
 * abstract message class, provide super class for other types of message classes
 */
public abstract class Message {
    protected String sender;
    protected String receiver;
    protected Timestamp datetime;
    protected Boolean newMessage;
    protected String type;
    protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * constructor for Message
     * @param receiver the receiver user string for this msg
     */
    public Message(String receiver) {
        this.receiver = receiver;
        this.sender = new User().getEmail();
        this.datetime= Timestamp.now();
        this.newMessage=TRUE;
    }

    /**
     * constructor for Message
     * @param sender sender user string
     * @param receiver receiver user string
     * @param datetime timestamp of the message
     * @param newMessage read/unread boolean flag
     */
    public Message(String sender, String receiver, Timestamp datetime, Boolean newMessage) {
        this.sender = sender;
        this.receiver = receiver;
        this.datetime = datetime;
        this.newMessage = newMessage;
    }

    /**
     * return type of message
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * return if message is read/unread
     * @return true-unread, false-read
     */
    public Boolean isNewMessage() {
        return newMessage;
    }

    /**
     * push the newMessage to the db
     */
    public void setNewMessage( ) {
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");
        CollectionReference receiverMsgBox= usersCollection.document(receiver).collection("MessageBox");
        String epochTimeString= String.valueOf(this.datetime.getSeconds());
        final DocumentReference messageEntry=receiverMsgBox.document(epochTimeString);
        messageEntry.update("newMessage",false);
    }

    /**
     * abstract method, should return the string representation of the message
     * @return string representation of the message
     */
    public abstract String toStringContent();

    /**
     * get formatted timestamp String
     * @return
     */
    public String toStringDatetime() {
        return sdf.format(new Date(this.datetime.getSeconds() * 1000));
    }

    /**
     * delete the message from database
     */
    public void delete(){
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");
        CollectionReference receiverMsgBox= usersCollection.document(receiver).collection("MessageBox");
        String epochTimeString= String.valueOf(this.datetime.getSeconds());
        final DocumentReference messageEntry=receiverMsgBox.document(epochTimeString);
        messageEntry.delete();
        Log.i("delete",epochTimeString);
        Log.i("delete",receiver);
    }
}
