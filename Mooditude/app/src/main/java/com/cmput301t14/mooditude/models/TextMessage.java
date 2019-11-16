package com.cmput301t14.mooditude.models;

import com.google.firebase.Timestamp;

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

}
