package com.cmput301t14.mooditude;

import com.google.firebase.Timestamp;

public class AcceptMessage extends Message {
    public AcceptMessage(String receiver) {
        super(receiver);
        this.type="accept";
    }

    public AcceptMessage(String sender, String receiver, Timestamp datetime, boolean newMessage) {
        super(sender, receiver, datetime, newMessage);
        this.type="accept";
    }

    public AcceptMessage(String sender, String receiver, Timestamp datetime) {
        super(sender, receiver, datetime);
        this.type="accept";
    }

    public AcceptMessage(String sender, String receiver, boolean newMessage) {
        super(sender, receiver, newMessage);
        this.type="accept";
    }

    public AcceptMessage(String sender, String receiver) {
        super(sender, receiver);
        this.type="accept";
    }

    @Override
    public String toStringContent() {
        return this.sender+" accepted you to follow";
    }
}
