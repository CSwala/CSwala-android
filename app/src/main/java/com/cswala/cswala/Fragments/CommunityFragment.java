package com.cswala.cswala.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.R;
import com.cswala.cswala.adapter.MessageAdapter;
import com.cswala.cswala.observer.ScrollToBottomObserver;
import com.cswala.cswala.pojos.Message;
import com.cswala.cswala.utils.MessageListener;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Date;


public class CommunityFragment extends Fragment {

    private EditText msgInput;
    private AppCompatImageButton sendBtn;
    private MessageAdapter adapter;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private RecyclerView chatRv;
    private LinearLayout sendMsgContainer;
    private FrameLayout progContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_community, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        chatRv = view.findViewById(R.id.chat_rv);
        msgInput = view.findViewById(R.id.msg_input);
        sendBtn = view.findViewById(R.id.send_btn);
        sendMsgContainer = view.findViewById(R.id.send_msg_container);
        progContainer = view.findViewById(R.id.prog_container);

        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived() {
                progContainer.setVisibility(View.GONE);
            }

            @Override
            public void onError(DatabaseError error) {
                Snackbar.make(view, "Some error occurred", Snackbar.LENGTH_SHORT).show();
            }
        };

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

//        chatRv.setVisibility(View.GONE);
//        sendMsgContainer.setVisibility(View.GONE);
        progContainer.setVisibility(View.VISIBLE);

        Query query = database
                .getReference()
                .child("chats")
                .limitToLast(50);

        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .build();

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(true);
        chatRv.setLayoutManager(llm);

        adapter = new MessageAdapter(options, messageListener, auth.getCurrentUser().getUid(), getContext());
        chatRv.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new ScrollToBottomObserver(chatRv, adapter, llm));

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = msgInput.getText().toString().trim();
                if (content.equals("")) return;

                closeKeyboard();
                DatabaseReference ref = database.getReference().child("chats").push();

                String msgId = ref.getKey();
                String senderId = auth.getCurrentUser().getUid();
                String senderName = auth.getCurrentUser().getDisplayName();

                if (senderName == null || senderName.trim().equals("")) {
                    senderName = "Unknown";
                }

                Message message = new Message(msgId, senderName, senderId, content, new Date().getTime());

                ref.setValue(message);


                msgInput.setText("");

            }
        });
        msgInput.addTextChangedListener(new TextWatcher() {
            boolean prevEmpty = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                final boolean isEmpty = s.toString().trim().equals("");
                if (isEmpty == prevEmpty) return;
                prevEmpty = isEmpty;

                if (isEmpty) {
                    sendBtn.setVisibility(View.GONE);
                } else {
                    sendBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    private void closeKeyboard() {
        try {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch(Exception ignored) {}
    }
}