package com.cmput301t14.mooditude.services;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.FollowRequestMessage;
import com.cmput301t14.mooditude.models.Mood;

public  class MoodFilterListener implements View.OnClickListener {


//    TextView v;
    String emotion;

    public MoodFilterListener(String emotion) {
        this.emotion = emotion;
    }

    @Override
    public void onClick(View v) {
        if (v.getBackground() instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) v.getBackground();
            int color = cd.getColor();
            Log.i("Color", String.valueOf(color));
//                    Log.i("Color2",String.valueOf(Color.GRAY));
            if (color == -3090735) {
                v.setBackgroundColor(new Mood(emotion).getColor());
                v.getBackground().setAlpha(50);
                User.getFilerList().put(emotion,Boolean.FALSE);
            } else {
                v.setBackgroundColor(Color.rgb(208, 214, 209));
                User.getFilerList().put(emotion,Boolean.TRUE);
            }

        }

        Log.i("LOGAA",User.getFilerList().toString());
    }
}

