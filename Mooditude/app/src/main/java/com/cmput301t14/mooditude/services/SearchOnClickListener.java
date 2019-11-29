package com.cmput301t14.mooditude.services;

import android.content.Context;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.models.FollowRequestMessage;

/**
 * Define several on click listener on search list.
 * Different type of user will return different on click listener
 */
public class SearchOnClickListener {
    /**
     * OnClickListener that for neither follower nor following
     */
    public static class OtherUser implements View.OnClickListener {
        Context context;

        public OtherUser(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            final String receiverEmail = ((TextView) view.findViewById(R.id.searchList_user_email_textView)).getText().toString();
//                Toast.makeText(view.getContext(), receiverEmail, Toast.LENGTH_LONG).show();
            //creating a popup menu
            PopupMenu popup = new PopupMenu(view.getContext(), view.findViewById(R.id.messageViewButton), Gravity.END);
            //inflating menu from xml resource
            popup.inflate(R.menu.popmenu_search_other_user);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.popmenu_search_add) {//handle menu1 click
                        new FollowRequestMessage(receiverEmail).invoke();
                        Toast.makeText(context, "Follow request to \"" + receiverEmail + "\" sent", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    return false;
                }
            });
            //displaying the popup
            popup.show();
        }
    }

    /**
     * OnClickListener that for  follower
     */
    public static class Follower implements View.OnClickListener {
        Context context;

        public Follower(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            final String receiverEmail = ((TextView) view.findViewById(R.id.searchList_user_email_textView)).getText().toString();
//                Toast.makeText(view.getContext(), receiverEmail, Toast.LENGTH_LONG).show();
            //creating a popup menu
            PopupMenu popup = new PopupMenu(view.getContext(), view.findViewById(R.id.messageViewButton), Gravity.END);
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
                            Toast.makeText(context, "Follow request to \"" + receiverEmail + "\" sent", Toast.LENGTH_LONG).show();
                            return true;
                        case R.id.popmenu_follower_remove:
                            new User().remove(receiverEmail);
                            Toast.makeText(context, "Remove \"" + receiverEmail + "\" as follower", Toast.LENGTH_LONG).show();
                        default:
                            return false;
                    }
                }
            });
            //displaying the popup
            popup.show();
        }
    }

    /**
     * OnClickListener that  following
     */
    public static class Following implements View.OnClickListener {
        Context context;

        public Following(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            final String receiverEmail = ((TextView) view.findViewById(R.id.searchList_user_email_textView)).getText().toString();
//                Toast.makeText(view.getContext(), receiverEmail, Toast.LENGTH_LONG).show();
            //creating a popup menu
            PopupMenu popup = new PopupMenu(view.getContext(), view.findViewById(R.id.messageViewButton), Gravity.END);
            //inflating menu from xml resource
            popup.inflate(R.menu.popmenu_search_following);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.popmenu_search_following_unfollow) {//handle menu1 click
                        new User().unfollow(receiverEmail);
                        Toast.makeText(context, "Unfollow \"" + receiverEmail + "\"", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    return false;
                }
            });
            //displaying the popup
            popup.show();
        }
    }

    /**
     * OnClickListener that for friends
     */
    public static class Friend implements View.OnClickListener {
        Context context;

        public Friend(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            final String receiverEmail = ((TextView) view.findViewById(R.id.searchList_user_email_textView)).getText().toString();
//                Toast.makeText(view.getContext(), receiverEmail, Toast.LENGTH_LONG).show();
            //creating a popup menu
            PopupMenu popup = new PopupMenu(view.getContext(), view.findViewById(R.id.messageViewButton), Gravity.END);
            //inflating menu from xml resource
            popup.inflate(R.menu.popmenu_friend);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.popmenu_friend_unfollow:
                            //handle menu1 click
//                            new FollowRequestMessage(receiverEmail).invoke();
                            new User().unfollow(receiverEmail);
                            Toast.makeText(context, "Unfollow \"" + receiverEmail + "\"", Toast.LENGTH_LONG).show();
                            return true;
                        case R.id.popmenu_friend_remove:
                            new User().remove(receiverEmail);
                            Toast.makeText(context, "Remove \"" + receiverEmail + "\" as follower", Toast.LENGTH_LONG).show();
                            return true;
                        default:
                            return false;
                    }
                }
            });
            //displaying the popup
            popup.show();
        }
    }

    public static class Self implements View.OnClickListener {
        Context context;

        public Self(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {

        }
    }

}
