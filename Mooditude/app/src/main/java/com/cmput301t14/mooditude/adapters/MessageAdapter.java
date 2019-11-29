package com.cmput301t14.mooditude.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cmput301t14.mooditude.models.FollowRequestMessage;
import com.cmput301t14.mooditude.models.Message;
import com.cmput301t14.mooditude.R;

import java.util.ArrayList;

/**
 * Adapter for message in notification activity
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private Context context;
    private ArrayList<Message> messageArrayList;

    /**
     * Inner data class to hold message view info
     */
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

    /**
     * Constructor
     * @param context context
     * @param messageArrayList ArrayList stores messages
     */
    public MessageAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
        Log.i("LOGB", "MessageAdapter: " + messageArrayList.size());
    }

    /**
     * Create View
     * @param parent parent of the view
     * @param viewType type of the view
     * @return MessageAdapter.MessageViewHolder(view);
     */
    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.message_content, parent, false);
        return new MessageAdapter.MessageViewHolder(view);
    }

    /**
     * Bind View with data
     * @param holder view holder
     * @param position position to query
     */
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.i("LOGB", "MessageAdapter position: " + position);
        final MessageViewHolder messageViewHolder = holder;
        final Message message = messageArrayList.get(position);
        final int positionFinal = position;

        messageViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                message.delete();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Are you sure that you want to delete?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
                return true;
            }
        });

        if (message.getType().equals("followRequest")) {
            final FollowRequestMessage followRequestMessage = (FollowRequestMessage) message;
            holder.view.findViewById(R.id.messageViewButton).setVisibility(View.VISIBLE);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageArrayList.get(positionFinal).setNewMessage();
                    message.setNewMessage();
                    messageViewHolder.messageContentTextView.setTypeface(null, Typeface.NORMAL);
                    Log.i("LOGA", "HERE2");

                    PopupMenu popup = new PopupMenu(view.getContext(), view.findViewById(R.id.messageViewButton), Gravity.END);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.popmenu_follow_request);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.popmenu_accept_follow:
                                    //handle menu1 click
                                    followRequestMessage.accept();
                                    return true;
                                case R.id.popmenu_reject_follow:
                                    followRequestMessage.reject();
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
        } else if (message.getType().equals("text")) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    message.setNewMessage();
                    messageViewHolder.messageContentTextView.setTypeface(null, Typeface.NORMAL);
                }
            });
        }

        holder.messageContentTextView.setText(messageArrayList.get(position).toStringContent());
        holder.messageDatetimeTextView.setText(messageArrayList.get(position).toStringDatetime());
        if (message.isNewMessage()) {
            holder.messageContentTextView.setTypeface(null, Typeface.BOLD);
            Log.i("LOGA", "HERE1");
        } else {
            holder.messageContentTextView.setTypeface(null, Typeface.NORMAL);
        }


    }

    // Return the size of data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
//        return 1;
        return messageArrayList.size();
    }
}

