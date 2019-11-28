package com.cmput301t14.mooditude.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301t14.mooditude.models.MoodEvent;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.services.MoodEventIconSetter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * adapter class for following's MoodEvent list in home activity
 */
public class FollowingMoodEventAdapter extends ArrayAdapter<MoodEvent> {
    private ArrayList<MoodEvent> moodEventList;
    private Context context;

    /**
     * Constructor for the adapter
     *
     * @param context - caller's context
     * @param moodEventList - the data list of moodEvents
     */
    public FollowingMoodEventAdapter(Context context, ArrayList<MoodEvent> moodEventList) {
        super(context, 0, moodEventList);
        this.moodEventList = moodEventList;
        this.context = context;
    }

    /**
     * getView for each item in the list
     * @param position
     * @param convertView
     * @param parent
     * @return view created
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.following_mood_event_list_content, parent, false);
        }

        final MoodEvent moodEvent = moodEventList.get(position);
//        final int colorGreyIcon = ContextCompat.getColor(context, R.color.colorGreyIcon);

        TextView timeTextView = view.findViewById(R.id.time_textview);
        TextView emoticonTextView = view.findViewById(R.id.emoticon_textview);
        ImageView locationImage = view.findViewById(R.id.locationImage);
        ImageView commentImage = view.findViewById(R.id.commentImage);
        ImageView socialSituationImage = view.findViewById(R.id.socialSituationImage);
        TextView userNameTextView= view.findViewById(R.id.userNameTextView);
        ImageView photoImage = view.findViewById(R.id.imageView3);

        MoodEventIconSetter moodEventIconSetter = new MoodEventIconSetter(moodEvent);
        moodEventIconSetter.setTimeView(timeTextView);
        moodEventIconSetter.setLocationIcon(locationImage);
        moodEventIconSetter.setCommentIcon(commentImage);
        moodEventIconSetter.setSocialSituationIcon(socialSituationImage);
        moodEventIconSetter.setPhotoIcon(photoImage);


        userNameTextView.setText(moodEvent.getAuthor());
        emoticonTextView.setText(moodEvent.getMood().getEmoticon());
        LinearLayout moodEventEntry = view.findViewById(R.id.mood_event_entry);
        moodEventEntry.setBackgroundColor(moodEvent.getMood().getColor());
        moodEventEntry.getBackground().setAlpha(50);

        return view;
    }
}
