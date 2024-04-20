package com.example.shopkart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    private List<Message> messages;
    private Context context;
    private String currUser;

    private static final int MESSAGE_SENT = 1;
    private static final int MESSAGE_RECEIVED = 2;

    public MessageListAdapter(Context context,List<Message> messageList, String currUser) {
        this.context = context;
        this.messages = messageList;
        this.currUser = currUser;
    }

    @Override
    public int getItemViewType(int position){
        Message msg = messages.get(position);
        if(msg.getSender().equals(currUser)){
            return MESSAGE_SENT;
        }else{
            return MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_message,parent,false);
            return new ChatSentViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recieved_message,parent,false);
            return new ChatRecViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message msg = messages.get(position);
        switch (holder.getItemViewType()){
            case MESSAGE_SENT:
                ((ChatSentViewHolder) holder).bind(msg);
                break;
            case MESSAGE_RECEIVED:
                ((ChatRecViewHolder) holder).bind(msg);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}

class ChatRecViewHolder extends RecyclerView.ViewHolder {
    TextView msg;
    public ChatRecViewHolder(@NonNull View msgView) {
        super(msgView);
        msg = msgView.findViewById(R.id.messageRecieved);
    }

    void bind(Message message){
        msg.setText(message.getMessages());
    }
}

class ChatSentViewHolder extends RecyclerView.ViewHolder {
    TextView msg;
    public ChatSentViewHolder(@NonNull View msgView) {
        super(msgView);
        msg = msgView.findViewById(R.id.messageSent);
    }

    void bind(Message message){
        msg.setText(message.getMessages());
    }
}
