package com.cmput301t14.mooditude;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Intent intent = getIntent();
        final String messageEmail = intent.getStringExtra(SelfActivity.EXTRA_MESSAGE_Email);
        MenuBar menuBar = new MenuBar(NotificationActivity.this, messageEmail,3);

        TextView title = (TextView) findViewById(R.id.activityTitle3);
        title.setText("Notification Activity");

    }
}
