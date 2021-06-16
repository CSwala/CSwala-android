package com.cswala.cswala.viewholders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cswala.cswala.R;
import com.cswala.cswala.Models.Message;
import com.goodayapps.widget.AvatarView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class MessageHolder extends RecyclerView.ViewHolder {
    private final TextView nameText;
    private final TextView msgText;
    private final AvatarView profileContainer;
    private final ConstraintLayout container;
    private final ImageView imageText;
    private final Context context;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");

    private static final int[] colors = {
            Color.parseColor("#D84437"), // RED
            Color.parseColor("#0F9D58"), // GREEN
            Color.parseColor("#4285F4"), // BLUE
            Color.parseColor("#9400D3"), // VIOLET
    };

    private static final HashMap<String, Integer> colorMap = new HashMap<>();

    private final Random random = new Random();

    public MessageHolder(@NonNull View itemView, Context context) {
        super(itemView);
        nameText = itemView.findViewById(R.id.name_text);
        msgText = itemView.findViewById(R.id.msg_text);
        profileContainer = itemView.findViewById(R.id.profile_container);
        imageText = itemView.findViewById(R.id.image_text);
        container = itemView.findViewById(R.id.container);
        this.context = context;
    }

    public void bind(@NonNull final Message msg, String myUId) {
        msgText.setText(msg.getContent());

        String keyString = msg.getSenderName();
        if (keyString == null || keyString.trim().equals("")) {
            keyString = "Unknown";
        }

        String tag = keyString.substring(0, 1).toUpperCase();
        if (msg.getSenderId().equals(myUId)) {
            keyString += " (Me)";
            tag = "Me";
        }

        nameText.setText(keyString);
        profileContainer.setPlaceholderText(tag);

        Integer color = colorMap.get(msg.getSenderId());
        if (color == null) {
            color = colors[random.nextInt(colors.length)];
            colorMap.put(msg.getSenderId(), color);
        }
        profileContainer.setBackgroundPlaceholderColor(color);

        if (msg.getSenderId().equals(myUId)) {
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete message")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reference.child(msg.getMsgId()).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            if (error != null) {
                                                Log.d("MessageHolder", error.getMessage());
                                                Toast.makeText(context, "Cannot delete the message", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                    return true;
                }
            });
        }

        Glide.with(context).load(msg.getImageUrl()).into(imageText);

    }
}
