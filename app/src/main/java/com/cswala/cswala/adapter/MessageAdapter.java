package com.cswala.cswala.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.cswala.cswala.R;
import com.cswala.cswala.pojos.Message;
import com.cswala.cswala.utils.MessageListener;
import com.cswala.cswala.viewholders.MessageHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;

public class MessageAdapter extends FirebaseRecyclerAdapter<Message, MessageHolder> {

    private final MessageListener messageListener;
    private final String uId;
    private final Context context;

    public MessageAdapter(@NonNull FirebaseRecyclerOptions<Message> options, MessageListener messageListener, String uId, Context context) {
        super(options);
        this.messageListener = messageListener;
        this.uId = uId;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
        holder.bind(model, uId);
    }

    @Override
    public void onError(@NonNull DatabaseError error) {
        super.onError(error);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        messageListener.onMessageReceived();
    }



    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item_view, parent, false);
        return new MessageHolder(view, context);
    }
}
