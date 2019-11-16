package com.cmput301t14.mooditude.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301t14.mooditude.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Create a custom list view that contains user e-mail.
 */
public class CustomList extends ArrayAdapter<String> {

    private ArrayList<String> follows;
    private Context context;

    /**
     * The constructor for CustomList
     * @param context
     * @param people
     */
    public CustomList(Context context, ArrayList<String> people){
        super(context,0, people);
        this.follows = people;
        this.context = context;
    }


    /**
     * Link the list view with content
     * show the content of the list view
     * @param position
     * @param convertView
     * @param parent
     * @return View view
     */
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
