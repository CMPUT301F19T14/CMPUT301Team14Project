package com.cmput301t14.mooditude.services;

import android.content.Context;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.FollowRequestMessage;
import com.cmput301t14.mooditude.models.Person;

import java.util.ArrayList;

public class FollowFollowingListOnClickListener {

    public static class Followers implements AdapterView.OnItemClickListener {

        ArrayList<Person> followDataList;

        public Followers( ArrayList<Person> followDataList) {

            this.followDataList = followDataList;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //                final String receiverEmail = ((TextView) view.findViewById(R.id.searchList_user_email_textView)).getText().toString();
//                final String receiverEmail = "testString";
            final String receiverEmail = followDataList.get(i).getEmail();
            final String userName = followDataList.get(i).getName();
//                Toast.makeText(view.getContext(), receiverEmail, Toast.LENGTH_LONG).show();
            //creating a popup menu
            final Context context = view.getContext();
            PopupMenu popup = new PopupMenu(view.getContext(), view.findViewById(R.id.messageViewButton), Gravity.RIGHT);
            //inflating menu from xml resource
            popup.inflate(R.menu.popmenu_follower);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.popmenu_follower_follow:
                            //handle menu1 click
//                            new FollowRequestMessage(receiverEmail).invoke();
                            new FollowRequestMessage(receiverEmail).invoke();
                            Toast.makeText(context, "Follow request to \"" + userName + "\" sent", Toast.LENGTH_LONG).show();
                            return true;
                        case R.id.popmenu_follower_remove:
                            new User().remove(receiverEmail);
                            Toast.makeText(context, "Remove \"" + userName + "\" as follower", Toast.LENGTH_LONG).show();
                        default:
                            return false;
                    }
                }
            });
            //displaying the popup
            popup.show();
        }
    }

}
