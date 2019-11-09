package com.cmput301t14.mooditude;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Adapter Class for the list of MoodEvents in SelfActivity
 */
public class SelfMoodEventAdapter extends ArrayAdapter<MoodEvent> {
    private ArrayList<MoodEvent> moodEventList;
    private Context context;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * Constructor for the adapter
     * @param context
     * @param moodEventList - the data list of moodEvents
     */
    public SelfMoodEventAdapter(Context context, ArrayList<MoodEvent> moodEventList){
        super(context,0, moodEventList);
        this.moodEventList = moodEventList;
        this.context = context;
    }


    /**
     * get the view for each item in the list
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.self_mood_event_list_content, parent,false);
        }

        final MoodEvent moodEvent = moodEventList.get(position);

        TextView timeTextView = view.findViewById(R.id.time_textview);
        TextView emoticonTextView = view.findViewById(R.id.emoticon_textview);

//      times by 1000 to change from seconds to miliseconds
        timeTextView.setText(sdf.format(new Date(moodEvent.getDatetime().getSeconds()*1000)));

        emoticonTextView.setText(moodEvent.getMood().getEmoticon());
        LinearLayout moodEventEntry= view.findViewById(R.id.mood_event_entry);
        moodEventEntry.setBackgroundColor(moodEvent.getMood().getColor());
        moodEventEntry.getBackground().setAlpha(50);
        return view;
    }
}
