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
    private Context context;
    private ArrayList<String> userNameList;
    private ArrayList<String> userEmailList;
    private ArrayList<String> followerList;
    private ArrayList<String> followingList;
    private User user;

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
     * @param context -
     * @param userNameList - the search result list of user names
     * @param userEmailList - the search result list of user emails
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
     * @param parent -
     * @param viewType -
     * @return SearchAdapter.SearchViewHolder
     */
    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(context).inflate(R.layout.search_list_content, parent, false);


        return new SearchAdapter.SearchViewHolder(view);
    }


    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder - search view holder
     * @param position - the position of the user in the adapter
     */
    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        String you = userNameList.get(position) + " (You)";
        String friend = userNameList.get(position) + " (Friend)";
        String follower = userNameList.get(position) + " (Follower)";
        String following = userNameList.get(position) + " (Following)";

        holder.user_email.setText(userEmailList.get(position));
        if(userEmailList.get(position).equals(user.getEmail())){
            holder.user_name.setText(you);
            holder.itemView.setOnClickListener( new SearchOnClickListener.Self(context));
            holder.itemView.findViewById(R.id.messageViewButton).setVisibility(View.INVISIBLE);
        }
        else if(followerList.contains(userEmailList.get(position))&&followingList.contains(userEmailList.get(position))){
            holder.user_name.setText(friend);
            holder.itemView.setOnClickListener( new SearchOnClickListener.Friend(context));
        }
        else if(followerList.contains(userEmailList.get(position))){
            holder.user_name.setText(follower);
            holder.itemView.setOnClickListener( new SearchOnClickListener.Follower(context));
        }
        else if(followingList.contains(userEmailList.get(position))){
            holder.user_name.setText(following);
            holder.itemView.setOnClickListener( new SearchOnClickListener.Following(context));
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
