package com.cmput301t14.mooditude;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String> {

    private ArrayList<String> follows;
    private Context context;

    public CustomList(Context context, ArrayList<String> people){
        super(context,0, people);
        this.follows = people;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        String follow = follows.get(position);

        TextView followEmail = view.findViewById(R.id.follow_text);


        followEmail.setText(follow);


        return view;

    }
}
