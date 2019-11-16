package com.cmput301t14.mooditude.adapters;

import android.content.Context;
import android.util.Log;
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
     *
     * @param context
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
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.self_mood_event_list_content, parent, false);
        }

        final MoodEvent moodEvent = moodEventList.get(position);
        final int colorGreyIcon = ContextCompat.getColor(context, R.color.colorGreyIcon);

        TextView timeTextView = view.findViewById(R.id.time_textview);
        TextView emoticonTextView = view.findViewById(R.id.emoticon_textview);
        ImageView locationImage = view.findViewById(R.id.locationImage);
        ImageView commentImage = view.findViewById(R.id.commentImage);
        ImageView socialSituationImage = view.findViewById(R.id.socialSituationImage);

//      times by 1000 to change from seconds to miliseconds
        timeTextView.setText(sdf.format(new Date(moodEvent.getDatetime().getSeconds() * 1000)));

        if (moodEvent.getLocation().getGeopoint() == null) {
            locationImage.setVisibility(View.GONE);
        } else {
            locationImage.setVisibility(View.VISIBLE);
        }

        if (moodEvent.getTextComment().isEmpty() || moodEvent.getTextComment() == null) {
//            commentImage.setColorFilter(colorGreyIcon, PorterDuff.Mode.SRC_ATOP);
            commentImage.setVisibility(View.GONE);
        } else {
//            commentImage.clearColorFilter();
            commentImage.setVisibility(View.VISIBLE);
        }

        if (moodEvent.getSocialSituation()== null) {
//            commentImage.setColorFilter(colorGreyIcon, PorterDuff.Mode.SRC_ATOP);
            socialSituationImage.setVisibility(View.GONE);
        } else if(moodEvent.getSocialSituation().getSocialSituation().equals("ALONE")) {
//            commentImage.clearColorFilter();
            Log.i("Self",moodEvent.getSocialSituation().getSocialSituation());
            socialSituationImage.setVisibility(View.VISIBLE);
            socialSituationImage.setImageResource(R.drawable.ic_alone);
        } else if(moodEvent.getSocialSituation().getSocialSituation().equals("ONE_PERSON")) {
//            commentImage.clearColorFilter();
            Log.i("Self",moodEvent.getSocialSituation().getSocialSituation());
            socialSituationImage.setVisibility(View.VISIBLE);
            socialSituationImage.setImageResource(R.drawable.ic_with_one_person);
        } else if(moodEvent.getSocialSituation().getSocialSituation().equals("SEVERAL")) {
//            commentImage.clearColorFilter();
            Log.i("Self",moodEvent.getSocialSituation().getSocialSituation());
            socialSituationImage.setVisibility(View.VISIBLE);
            socialSituationImage.setImageResource(R.drawable.ic_several);
        }else if(moodEvent.getSocialSituation().getSocialSituation().equals("CROWD")) {
//            commentImage.clearColorFilter();
            Log.i("Self",moodEvent.getSocialSituation().getSocialSituation());
            socialSituationImage.setVisibility(View.VISIBLE);
            socialSituationImage.setImageResource(R.drawable.ic_group);
        }



//        final ImageView imageView = view.findViewById(R.id.socialSituationImage);
//        imageView.setVisibility(View.INVISIBLE);


        emoticonTextView.setText(moodEvent.getMood().getEmoticon());
        LinearLayout moodEventEntry = view.findViewById(R.id.mood_event_entry);
        moodEventEntry.setBackgroundColor(moodEvent.getMood().getColor());
        moodEventEntry.getBackground().setAlpha(50);

        return view;
    }
}
