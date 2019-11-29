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
import com.cmput301t14.mooditude.activities.DisplayFollow;
import com.cmput301t14.mooditude.models.Person;

import java.util.ArrayList;

/**
 * Create a custom list view that contains user e-mail.
 */
public class FollowerFollowingListAdapter extends ArrayAdapter<Person> {

    private ArrayList<Person> people;
    private Context context;
    private DisplayFollow.ListMode listMode;

    /**
     * The constructor for FollowerFollowingListAdapter
     * @param context -
     * @param people - the array list of person
     */
    public FollowerFollowingListAdapter(Context context, ArrayList<Person> people, DisplayFollow.ListMode listMode){
        super(context,0, people);
        this.people = people;
        this.context = context;
        this.listMode=listMode;
    }




    /**
     * Link the list view with followerListContent
     * show the followerListContent of the list view
     * @param position -
     * @param convertView -
     * @param parent -
     * @return View view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.follower_following_list_content, parent,false);
            if(listMode== DisplayFollow.ListMode.Followings){
                view.findViewById(R.id.messageViewButton).setVisibility(View.INVISIBLE);
            }
        }


        Person person = people.get(position);
        TextView tv = view.findViewById(R.id.follow_following_list_name);
        tv.setText(person.getName());
        tv =view.findViewById(R.id.follow_following_list_email);
        tv.setText(person.getEmail());
        return view;

    }
}
