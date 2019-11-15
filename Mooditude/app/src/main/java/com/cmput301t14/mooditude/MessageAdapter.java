package com.cmput301t14.mooditude;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    Context context;
    ArrayList<Message> messageArrayList;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView messageDatetimeTextView, messageContentTextView;

        public MessageViewHolder(View view) {
            super(view);
            messageDatetimeTextView=view.findViewById(R.id.message_datetime);
            messageContentTextView=view.findViewById(R.id.message_content);
        }

    }

    public MessageAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.message_content,parent,false);
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.message_content, parent, false);

//        MessageViewHolder vh = new MessageViewHolder(v);
        return new MessageAdapter.MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.textView.setText("test");
        holder.messageContentTextView.setText(messageArrayList.get(position).toStringContent());
        holder.messageDatetimeTextView.setText(messageArrayList.get(position).toStringDatetime());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 1;
//        return mDataset.length;
    }
}

