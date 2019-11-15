package com.cmput301t14.mooditude;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    Context context;
    ArrayList<Message> messageArrayList;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView messageDatetimeTextView, messageContentTextView;
        View view;

        public MessageViewHolder(View view) {
            super(view);
            this.view = view;
            messageDatetimeTextView = view.findViewById(R.id.message_datetime);
            messageContentTextView = view.findViewById(R.id.message_content);
        }

    }

    public MessageAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.message_content, parent, false);
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
        final MessageViewHolder messageViewHolder=holder;
        final Message message = messageArrayList.get(position);
        final int positionFinal=position;
        if (message.getType().equals("followRequest")) {
            final FollowRequestMessage followRequestMessage = (FollowRequestMessage) message;
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageArrayList.get(positionFinal).setNewMessage();
//                    if(message.isNewMessage()){
//                        Log.i("LOGA","new");
//                    }
//                    else{
//                        Log.i("LOGA","old");
//                    }
                    messageViewHolder.messageContentTextView.setTypeface(null, Typeface.NORMAL);
                    Log.i("LOGA", "HERE2");

                    PopupMenu popup = new PopupMenu(view.getContext(), view.findViewById(R.id.message_content), Gravity.RIGHT);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.popmenu_follow_request);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.popmenu_accept_follow:
                                    //handle menu1 click
//                                    new FollowRequestMessage(receiverEmail).invoke();
//                                    Toast.makeText(context, "Follow request to \""+receiverEmail+"\" sent", Toast.LENGTH_LONG).show();
//                                    followRequestMessage.accept();
                                    return true;
                                case R.id.popmenu_reject_follow:
//                                    followRequestMessage.reject();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
        }
        holder.messageContentTextView.setText(messageArrayList.get(position).toStringContent());
        holder.messageDatetimeTextView.setText(messageArrayList.get(position).toStringDatetime());
        if (message.isNewMessage()) {
            holder.messageContentTextView.setTypeface(null, Typeface.BOLD);
            Log.i("LOGA", "HERE1");
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 1;
//        return mDataset.length;
    }
}

