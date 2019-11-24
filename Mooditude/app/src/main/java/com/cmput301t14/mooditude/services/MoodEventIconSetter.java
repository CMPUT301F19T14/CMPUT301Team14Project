package com.cmput301t14.mooditude.services;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.MoodEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

// This is one class dedicated to set different icons for different mood events
public class MoodEventIconSetter {
    private MoodEvent moodEvent;


    public MoodEventIconSetter(MoodEvent moodEvent) {
        this.moodEvent = moodEvent;
    }

    public  void setLocationIcon(ImageView imageView){
        if (moodEvent.getLocation().getGeopoint() == null) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public  void setCommentIcon(ImageView imageView){
        if (moodEvent.getTextComment().isEmpty() || moodEvent.getTextComment() == null) {
//            commentImage.setColorFilter(colorGreyIcon, PorterDuff.Mode.SRC_ATOP);
            imageView.setVisibility(View.GONE);
        } else {
//            commentImage.clearColorFilter();
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public  void setSocialSituationIcon(ImageView imageView){
        if (moodEvent.getSocialSituation()== null) {
//            commentImage.setColorFilter(colorGreyIcon, PorterDuff.Mode.SRC_ATOP);
            imageView.setVisibility(View.GONE);
        } else if(moodEvent.getSocialSituation().getSocialSituation().equals("ALONE")) {
//            commentImage.clearColorFilter();
            Log.i("Self",moodEvent.getSocialSituation().getSocialSituation());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_alone);
        } else if(moodEvent.getSocialSituation().getSocialSituation().equals("ONE_PERSON")) {
//            commentImage.clearColorFilter();
            Log.i("Self",moodEvent.getSocialSituation().getSocialSituation());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_with_one_person);
        } else if(moodEvent.getSocialSituation().getSocialSituation().equals("SEVERAL")) {
//            commentImage.clearColorFilter();
            Log.i("Self",moodEvent.getSocialSituation().getSocialSituation());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_several);
        }else if(moodEvent.getSocialSituation().getSocialSituation().equals("CROWD")) {
//            commentImage.clearColorFilter();
            Log.i("Self",moodEvent.getSocialSituation().getSocialSituation());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_group);
        }
    }

    public  void setTimeView(TextView textView){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        textView.setText(sdf.format(new Date(moodEvent.getDatetime().getSeconds() * 1000)));
    }
}
