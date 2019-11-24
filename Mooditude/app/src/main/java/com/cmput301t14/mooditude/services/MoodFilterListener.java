package com.cmput301t14.mooditude.services;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.cmput301t14.mooditude.models.Mood;

public class MoodFilterListener implements View.OnClickListener {
    String mood;
    User user;

    public MoodFilterListener(User user, String mood) {
        this.mood = mood;
        this.user = user;
    }

    /**
     * when onClick, set the flag in the user.FilterList, and display the correct color
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getBackground() instanceof ColorDrawable) {
            if (user.getFilterList().get(mood)) {
                // if true, display the corresponding color
                v.setBackgroundColor(Color.GRAY);
                v.getBackground().setAlpha(50);
                user.getFilterList().put(mood,Boolean.FALSE); // set the FilterList flag
            } else {
                // if false display grey color
                v.setBackgroundColor(new Mood(mood).getColor());
                v.getBackground().setAlpha(50);
                user.getFilterList().put(mood,Boolean.TRUE); // set the FilterList flag
            }
        }
        user.filterMoodEventList(); // let user class filter the data
    }
}