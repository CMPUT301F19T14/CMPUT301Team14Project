package com.cmput301t14.mooditude;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SelfMoodEventAdapter extends ArrayAdapter<MoodEvent> {
    private ArrayList<MoodEvent> moodEventList;
    private Context context;

    public SelfMoodEventAdapter(Context context, ArrayList<MoodEvent> moodEventList){
        super(context,0, moodEventList);
        this.moodEventList = moodEventList;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.self_mood_event_list_content, parent,false);
        }

        final MoodEvent moodEvent = moodEventList.get(position);

        TextView timeTextView = view.findViewById(R.id.time_textview);
        TextView emoticonTextView = view.findViewById(R.id.emoticon_textview);
        ImageButton deleteButton = view.findViewById(R.id.delete_button);
        ImageButton editButton = view.findViewById(R.id.edit_button);

        timeTextView.setText(moodEvent.getDatetime().toString());
        emoticonTextView.setText(moodEvent.getMood().getEmoticon());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                ((SelfActivity)context).onConfirmPressed(moodEvent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Are you sure that you want to delete?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: go to edit MoodEvent
            }
        });

        return view;

    }
}
