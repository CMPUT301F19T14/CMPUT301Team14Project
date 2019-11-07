package com.cmput301t14.mooditude;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> userNameList;
    ArrayList<String> userEmailList;

    class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView user_name, user_email;

        public SearchViewHolder(View itemView){
            super(itemView);
            user_name = itemView.findViewById(R.id.searchList_user_name_textView);
            user_email = itemView.findViewById(R.id.searchList_user_email_textView);
        }
    }

    public SearchAdapter(Context context, ArrayList<String> userNameList, ArrayList<String> userEmailList) {
        this.context = context;
        this.userNameList = userNameList;
        this.userEmailList = userEmailList;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_list_content,parent,false);

        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.user_name.setText(userNameList.get(position));
        holder.user_email.setText(userEmailList.get(position));
    }

    @Override
    public int getItemCount() {
        return userEmailList.size();
    }
}
