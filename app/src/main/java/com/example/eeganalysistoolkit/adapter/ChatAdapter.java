package com.example.eeganalysistoolkit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eeganalysistoolkit.R;
import com.example.eeganalysistoolkit.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends FirestoreAdapter<ChatAdapter.NewChatHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private FirebaseUser user ;
    private String sender;


    public ChatAdapter(Query query) {
        super(query);
        FirebaseAuth auth  = FirebaseAuth.getInstance();
        this.user  = auth.getCurrentUser();
    }


    @NonNull
    @Override
    public NewChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_sender, parent, false);
            return new NewChatHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_receiver, parent, false);
            return new NewChatHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NewChatHolder holder, int position) {
        Chat chat = getSnapshot(position).toObject(Chat.class);
        holder.bind(chat);
        sender = chat.getSenderId();
    }


    static class NewChatHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView profileImage;

        NewChatHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            //profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }


        void bind(final Chat chat) {
            messageText.setText(chat.getMessage());
            //price.setText(String.valueOf(chat.ge()));
            // Format the stored timestamp into a readable String using method.
            Calendar cal = Calendar.getInstance();
            if (chat.getDate() != null) {
                cal.setTime(chat.getDate());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.FRENCH);
                timeText.setText(String.valueOf(dateFormat.format(cal.getTime())));

            }
        }


    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();

    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        if (user.getUid().equals(sender)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }
}
