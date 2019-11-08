package com.cmput301t14.mooditude;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import static com.cmput301t14.mooditude.SelfActivity.EXTRA_MESSAGE_Email;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get the Intent that started this activity and extract the string
        Intent thisIntent = getIntent();
        final String messageEmail = thisIntent.getStringExtra(EXTRA_MESSAGE_Email);
        MenuBar menuBar = new MenuBar(HomeActivity.this, messageEmail, 0);

    }
}
