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
import androidx.core.content.ContextCompat;

import com.cmput301t14.mooditude.models.MoodEvent;
import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.services.MoodEventIconSetter;

import java.util.ArrayList;


/**
 * Adapter Class for the list of MoodEvents in SelfActivity
 */
public class SelfMoodEventAdapter extends ArrayAdapter<MoodEvent> {
    private ArrayList<MoodEvent> moodEventList;
    private Context context;


    /**
     * Constructor for the adapter
     *
     * @param context the context
     * @param moodEventList - the data list of moodEvents
     */
    public SelfMoodEventAdapter(Context context, ArrayList<MoodEvent> moodEventList) {
        super(context, 0, moodEventList);
        this.moodEventList = moodEventList;
        this.context = context;
    }

    /**
     * get the view for each item in the list
     *
     * @param position the moodevent position in list
     * @param convertView
     * @param parent parent of the view
     * @return the view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.self_mood_event_list_content, parent, false);
        }

        final MoodEvent moodEvent = moodEventList.get(position);

        TextView timeTextView = view.findViewById(R.id.time_textview);
        TextView emoticonTextView = view.findViewById(R.id.emoticon_textview);
        ImageView locationImage = view.findViewById(R.id.locationImage);
        ImageView commentImage = view.findViewById(R.id.commentImage);
        ImageView socialSituationImage = view.findViewById(R.id.socialSituationImage);
        ImageView photoImage = view.findViewById(R.id.imageView3);


        MoodEventIconSetter moodEventIconSetter = new MoodEventIconSetter(moodEvent);
        moodEventIconSetter.setTimeView(timeTextView);
        moodEventIconSetter.setLocationIcon(locationImage);
        moodEventIconSetter.setCommentIcon(commentImage);
        moodEventIconSetter.setSocialSituationIcon(socialSituationImage);
        moodEventIconSetter.setPhotoIcon(photoImage);



        emoticonTextView.setText(moodEvent.getMood().getEmoticon());
        LinearLayout moodEventEntry = view.findViewById(R.id.mood_event_entry);
        moodEventEntry.setBackgroundColor(moodEvent.getMood().getColor());
        moodEventEntry.getBackground().setAlpha(50);

        return view;
    }
}
