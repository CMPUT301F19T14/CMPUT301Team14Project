package com.cmput301t14.mooditude;

import com.cmput301t14.mooditude.models.FollowRequestMessage;
import com.cmput301t14.mooditude.models.TextMessage;
import com.google.firebase.Timestamp;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


public class MessageUnitTest {

    @Test
    public void testTextMessage(){
        String sender = "sender@test.com";
        String receiver = "receiver@test.com";
        Timestamp datetime = Timestamp.now();
        Boolean newMessage = true;
        String text = "message text content";
        TextMessage  msg = new TextMessage(text,sender,receiver,datetime,newMessage);

        assertEquals("text", msg.getType());
        assertTrue(true == msg.isNewMessage());
        assertEquals(text, msg.toStringContent());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        assertEquals(sdf.format(new Date(datetime.getSeconds() * 1000)), msg.toStringDatetime());

    }

    @Test
    public void testFollowRequestMessage(){
        String sender = "sender@test.com";
        String receiver = "receiver@test.com";
        Timestamp datetime = Timestamp.now();
        Boolean newMessage = true;
        FollowRequestMessage  msg = new FollowRequestMessage(sender,receiver,datetime,newMessage);

        assertEquals("followRequest", msg.getType());
        assertTrue(true == msg.isNewMessage());
        assertEquals(sender+" wants to follow you", msg.toStringContent());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        assertEquals(sdf.format(new Date(datetime.getSeconds() * 1000)), msg.toStringDatetime());

    }

}
