package com.cmput301t14.mooditude.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;

import static java.lang.Boolean.TRUE;

public class TextMessage extends Message {

    private String text;

    public TextMessage(String text, String receiver) {
        super(receiver);
        this.text = text;
        this.type = "text";
    }

    public TextMessage(String text, String sender, String receiver, Timestamp datetime, boolean newMessage) {
        super(sender, receiver, datetime, newMessage);
        this.text = text;
        this.type="text";
    }

    public TextMessage(String text, String sender, String receiver, Timestamp datetime) {
        super(sender, receiver, datetime);
        this.text = text;
        this.type="text";
    }

    public TextMessage(String text, String sender, String receiver, boolean newMessage) {
        super(sender, receiver, newMessage);
        this.text = text;
        this.type="text";
    }

    public TextMessage(String text, String sender, String receiver) {
        super(sender, receiver);
        this.text = text;
        this.type="text";
    }

    @Override
    public String toStringContent() {
        return this.text;
    }

    public void  invoke(CollectionReference messageBox){
//        CollectionReference senderMsgBox= usersCollection.document(this.sender).collection("MessageBox");
        Timestamp timestamp= this.datetime;
        String epochTimeString= String.valueOf(timestamp.getSeconds());
        DocumentReference messageEntry=messageBox.document(epochTimeString);
        HashMap<String,Object> messageHash = new HashMap<>();
        messageHash.put("text", this.text);
        messageHash.put("sender",this.receiver);
        messageHash.put("receiver",this.sender);
        messageHash.put("newMessage", this.newMessage);
        messageHash.put("datetime", this.datetime);
        messageHash.put("type", "text");
        messageEntry.set(messageHash);
    }

}
