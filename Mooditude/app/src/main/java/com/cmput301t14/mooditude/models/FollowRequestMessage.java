package com.cmput301t14.mooditude.models;

import com.cmput301t14.mooditude.services.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import static java.lang.Boolean.TRUE;

/**
 * Message sent when one user try to follow another one
 */
public class FollowRequestMessage extends Message {
    /**
     * Constructor
     * @param receiver id of receiver
     */
    public FollowRequestMessage(String receiver) {
        super(receiver);
        this.type="followRequest";
    }

    /**
     * Constructor
     * @param sender id of sender
     * @param receiver id of receiver
     * @param datetime datetime of the message
     * @param newMessage message is new or not
     */
    public FollowRequestMessage(String sender, String receiver, Timestamp datetime, boolean newMessage) {
        super(sender, receiver, datetime, newMessage);
        this.type="followRequest";
    }


    @Override
    public String toStringContent() {
        return this.sender+" wants to follow you";
    }

    /**
     * Invoke message send from one end to another
     */
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

    /**
     * reaction when receivers agree to be followed
     */
    public void accept(){
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");
        new User().addFollower(this.sender);

        // add receiver to sender's Followings collection
        CollectionReference senderFollowings = usersCollection.document(this.sender).collection("Followings");
        final DocumentReference senderFollowingsEntry= senderFollowings.document(this.receiver);
        final HashMap<String,Object> followingHash = new HashMap<>();
        // put receiver's user_name in the senderFollowingsEntry first
        followingHash.put("user_name", User.getUserName());
        senderFollowingsEntry.set(followingHash);
        // get receiver's most recent MoodEvent，if it exist then put it in the senderFollowingsEntry
        usersCollection.document(receiver).collection("MoodHistory").orderBy("DateTime", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc: queryDocumentSnapshots){
                    followingHash.put("user_name", User.getUserName());
                    followingHash.put("DateTime", doc.getTimestamp("DateTime"));
                    followingHash.put("Comment", doc.getString("Comment"));
                    followingHash.put("SocialSituation", doc.get("SocialSituation"));
                    followingHash.put("Mood",doc.get("Mood"));
                    followingHash.put("Location",doc.getGeoPoint("Location"));
                    followingHash.put("Photograph", doc.getString("Photograph"));
                    senderFollowingsEntry.set(followingHash);
                }

            }
        });

        // send accept message to sender's MessageBox collection
        CollectionReference senderMsgBox= usersCollection.document(this.sender).collection("MessageBox");
        String epochTimeString= String.valueOf(Timestamp.now().getSeconds());
        DocumentReference messageEntry=senderMsgBox.document(epochTimeString);
        HashMap<String,Object> messageHash = new HashMap<>();

        new TextMessage(this.receiver+" accepted you to follow",this.sender,this.receiver, Timestamp.now(),TRUE).invoke(senderMsgBox);

        // delete this message and add new TextMessage to receiver MessageBox collection
        this.delete();
        CollectionReference receiverMsgBox= usersCollection.document(this.receiver).collection("MessageBox");
        new TextMessage("you accepted "+sender+" successfully",this.receiver,this.receiver, Timestamp.now(),TRUE).invoke(receiverMsgBox);
    }

    /**
     * reaction when user declines follow request
     */
    public void reject(){
        // send reject message to sender's MessageBox collection
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");
        CollectionReference senderMsgBox= usersCollection.document(this.sender).collection("MessageBox");

        new TextMessage(receiver+" rejected you to follow",this.receiver,this.sender, Timestamp.now(),TRUE).invoke(senderMsgBox);
        // delete this message and add new TextMessage to receiver MessageBox collection
        this.delete();
        CollectionReference receiverMsgBox= usersCollection.document(this.receiver).collection("MessageBox");

        new TextMessage("you rejected "+sender+" successfully",this.sender,this.receiver, Timestamp.now(),TRUE).invoke(receiverMsgBox);
    }
}
