package com.cmput301t14.mooditude;

import com.google.firebase.Timestamp;

public class RejectMessage extends Message {
    public RejectMessage(String receiver) {
        super(receiver);
        this.type="reject";
    }

    public RejectMessage(String sender, String receiver, Timestamp datetime, boolean newMessage) {
        super(sender, receiver, datetime, newMessage);
        this.type="reject";
    }

    public RejectMessage(String sender, String receiver, Timestamp datetime) {
        super(sender, receiver, datetime);
        this.type="reject";
    }

    public RejectMessage(String sender, String receiver, boolean newMessage) {
        super(sender, receiver, newMessage);
        this.type="reject";
    }

    public RejectMessage(String sender, String receiver) {
        super(sender, receiver);
        this.type="reject";
    }

    @Override
    public String toStringContent() {
        return this.sender+" rejected you to follow";
    }
}
