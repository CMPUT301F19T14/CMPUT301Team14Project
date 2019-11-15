package com.cmput301t14.mooditude;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;

import java.text.SimpleDateFormat;

import static java.lang.Boolean.TRUE;

public abstract class Message {
    protected String sender;
    protected String receiver;
    protected Timestamp datetime;
    protected boolean newMessage;
    protected String type;
    protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public Message(String receiver) {
        this.receiver = receiver;
        this.sender = new User().getEmail();
        this.datetime= Timestamp.now();
        this.newMessage=TRUE;
    }

    public Message(String sender, String receiver, Timestamp datetime, boolean newMessage) {
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

    public Message(String sender, String receiver, boolean newMessage) {
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

    abstract void invoke();
    abstract String toStringContent();
    abstract String toStringDatetime();
}
