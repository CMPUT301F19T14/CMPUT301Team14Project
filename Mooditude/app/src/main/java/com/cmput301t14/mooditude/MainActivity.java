package com.cmput301t14.mooditude;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button signInBtn = findViewById(R.id.starter_sign_in_button);
        final Button joinNowBtn = findViewById(R.id.stater_join_now_button);

//        Testing Code
//        Intent intentSignIn = new Intent(MainActivity.this, SignInActivity.class);
//        startActivity(intentSignIn);
//        Testing End
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignIn = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intentSignIn);
            }
        });

        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJoinNow = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intentJoinNow);
            }
        });

    }

}
