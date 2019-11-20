package com.cmput301t14.mooditude.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301t14.mooditude.R;
import com.cmput301t14.mooditude.services.SearchOnClickListener;
import com.cmput301t14.mooditude.services.User;

import java.util.ArrayList;

/**
 * SearchAdapter is used to create a recycler view.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> userNameList;
    ArrayList<String> userEmailList;
    ArrayList<String> followerList;
    ArrayList<String> followingList;
    User user;

    /**
     *
     */
    class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView user_name, user_email;
        View itemView;

        public SearchViewHolder(View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.searchList_user_name_textView);
            user_email = itemView.findViewById(R.id.searchList_user_email_textView);
            this.itemView = itemView;
        }
    }


    /**
     * The constructor for SearchAdapter class.
     *
     * @param context
     * @param userNameList
     * @param userEmailList
     */
    public SearchAdapter(Context context, ArrayList<String> userNameList, ArrayList<String> userEmailList, ArrayList<String> followerList, ArrayList<String> followingList) {
        this.context = context;
        this.userNameList = userNameList;
        this.userEmailList = userEmailList;
        this.followerList=followerList;
        this.followingList=followingList;
        this.user= new User();
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the
     * given type to represent an item.
     *
     * @param parent
     * @param viewType
     * @return SearchAdapter.SearchViewHolder
     */
    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(context).inflate(R.layout.search_list_content, parent, false);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final String receiverEmail = ((TextView) view.findViewById(R.id.searchList_user_email_textView)).getText().toString();
////                Toast.makeText(view.getContext(), receiverEmail, Toast.LENGTH_LONG).show();
//                //creating a popup menu
//                PopupMenu popup = new PopupMenu(view.getContext(), view.findViewById(R.id.messageViewButton), Gravity.RIGHT);
//                //inflating menu from xml resource
//                popup.inflate(R.menu.popmenu_follow);
//                //adding click listener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.popmenu_search_add:
//                                //handle menu1 click
//                                new FollowRequestMessage(receiverEmail).invoke();
//                                Toast.makeText(context, "Follow request to \"" + receiverEmail + "\" sent", Toast.LENGTH_LONG).show();
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//                });
//                //displaying the popup
//                popup.show();
//            }
//        });

        return new SearchAdapter.SearchViewHolder(view);
    }


    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        holder.user_email.setText(userEmailList.get(position));
        if(userEmailList.get(position).equals(user.getEmail())){
            holder.user_name.setText(userNameList.get(position)+" (You)");
            holder.itemView.setOnClickListener( new SearchOnClickListener.Self(context));
            holder.itemView.findViewById(R.id.messageViewButton).setVisibility(View.INVISIBLE);
        }
        else if(followerList.contains(userEmailList.get(position))&&followingList.contains(userEmailList.get(position))){
            holder.user_name.setText(userNameList.get(position)+" (Friend)");
            holder.itemView.setOnClickListener( new SearchOnClickListener.Follower(context));
        }
        else if(followerList.contains(userEmailList.get(position))){
            holder.user_name.setText(userNameList.get(position)+" (Follower)");
            holder.itemView.setOnClickListener( new SearchOnClickListener.Follower(context));
        }
        else if(followingList.contains(userEmailList.get(position))){
            holder.user_name.setText(userNameList.get(position)+" (Following)");
            holder.itemView.setOnClickListener( new SearchOnClickListener.Follower(context));
        }
        else{
            holder.user_name.setText(userNameList.get(position));
            holder.itemView.setOnClickListener( new SearchOnClickListener.OtherUser(context));
        }


    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return an integer which should be the size of userEmailList.
     */
    @Override
    public int getItemCount() {
        return userEmailList.size();
    }
}
