package com.cmput301t14.mooditude.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;

/**
 * The Message class that only contains text to display
 */
public class TextMessage extends Message {

    private String text;

    /**
     * Text Message Constructor
     * @param text the string to display
     * @param sender the sender user of the message
     * @param receiver the receiver user of the message
     * @param datetime the timestamp of the message
     * @param newMessage flag for read/unread
     */
    public TextMessage(String text, String sender, String receiver, Timestamp datetime, boolean newMessage) {
        super(sender, receiver, datetime, newMessage);
        this.text = text;
        this.type="text";
    }

    /**
     * get the string representation of the message
     * @return message string
     */
    @Override
    public String toStringContent() {
        return this.text;
    }

    /**
     * upload the message into the given messageBox db collection reference
     * @param messageBox db collection reference
     */
    public void invoke(CollectionReference messageBox){
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
