package com.cmput301t14.mooditude.services;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.MoodEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is one class dedicated to set different icons for different mood events
 */
public class MoodEventIconSetter {
    private MoodEvent moodEvent;

    /**
     * constructor
     * @param moodEvent the moodevents
     */
    public MoodEventIconSetter(MoodEvent moodEvent) {
        this.moodEvent = moodEvent;
    }

    /**
     * set icon for image
     * @param imageView location icon image view
     */
    public  void setLocationIcon(ImageView imageView){
        if (moodEvent.getLocation() == null) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * set comment icon
     * @param imageView comment icon image
     */
    public  void setCommentIcon(ImageView imageView){
        if (moodEvent.getTextComment().isEmpty() || moodEvent.getTextComment() == null) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * set social situation icon
     * @param imageView social situation icon image view
     */
    public  void setSocialSituationIcon(ImageView imageView){
        if (moodEvent.getSocialSituation()== null) {
            imageView.setVisibility(View.GONE);
        } else if(moodEvent.getSocialSituation().getSocialSituation().equals("ALONE")) {
            Log.i("Self",moodEvent.getSocialSituation().getSocialSituation());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_alone);
        } else if(moodEvent.getSocialSituation().getSocialSituation().equals("WITH_ANOTHER_PERSON")) {
            Log.i("Self",moodEvent.getSocialSituation().getSocialSituation());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_with_another_person);
        } else if(moodEvent.getSocialSituation().getSocialSituation().equals("SEVERAL")) {
            Log.i("Self",moodEvent.getSocialSituation().getSocialSituation());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_several);
        }else if(moodEvent.getSocialSituation().getSocialSituation().equals("CROWD")) {
            Log.i("Self",moodEvent.getSocialSituation().getSocialSituation());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_group);
        }
    }

    /**
     * set time
     * @param textView mood's time
     */
    public  void setTimeView(TextView textView){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        textView.setText(sdf.format(new Date(moodEvent.getDatetime().getSeconds() * 1000)));
    }

    /**
     * set time icon
     * @param imageView photo icon image view
     */
    public  void setPhotoIcon(ImageView imageView){
        if (moodEvent.getPhotoUrl() == null) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
    }
}
