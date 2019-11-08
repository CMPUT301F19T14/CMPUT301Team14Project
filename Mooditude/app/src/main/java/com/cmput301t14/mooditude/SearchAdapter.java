package com.cmput301t14.mooditude;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * SearchAdapter is used to create a recycler view.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> userNameList;
    ArrayList<String> userEmailList;

    /**
     *
     */
    class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView user_name, user_email;

        public SearchViewHolder(View itemView){
            super(itemView);
            user_name = itemView.findViewById(R.id.searchList_user_name_textView);
            user_email = itemView.findViewById(R.id.searchList_user_email_textView);
        }
    }


    /**
     * The constructor for SearchAdapter class.
     * @param context
     * @param userNameList
     * @param userEmailList
     */
    public SearchAdapter(Context context, ArrayList<String> userNameList, ArrayList<String> userEmailList) {
        this.context = context;
        this.userNameList = userNameList;
        this.userEmailList = userEmailList;
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the
     * given type to represent an item.
     * @param parent
     * @param viewType
     * @return SearchAdapter.SearchViewHolder
     */
    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_list_content,parent,false);

        return new SearchAdapter.SearchViewHolder(view);
    }


    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.user_name.setText(userNameList.get(position));
        holder.user_email.setText(userEmailList.get(position));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return an integer which should be the size of userEmailList.
     */
    @Override
    public int getItemCount() {
        return userEmailList.size();
    }
}
