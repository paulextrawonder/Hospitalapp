package com.example.eeganalysistoolkit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eeganalysistoolkit.R;
import com.example.eeganalysistoolkit.adapter.ChatAdapter;
import com.example.eeganalysistoolkit.model.Chat;
import com.example.eeganalysistoolkit.model.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatActivity extends AppCompatActivity {

    private CollectionReference datasRefChat;
    private CollectionReference datasRefUser;
    private CollectionReference dataToken;
    private ChatAdapter chatAdapter;
    private RecyclerView mChatRecycler;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getReferenceDataBase();
        mChatRecycler = (RecyclerView) findViewById(R.id.message_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        mChatRecycler.setNestedScrollingEnabled(true);
        mChatRecycler.setLayoutManager(manager);
        chatAdapter = new ChatAdapter(datasRefChat.orderBy("date"));
        mChatRecycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    ChatActivity.this.mChatRecycler.postDelayed(new Runnable() {
                        public void run() {
                            ChatActivity.this.mChatRecycler.smoothScrollToPosition(chatAdapter.getItemCount());
                        }
                    }, 100);
                }
            }
        });
        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                ChatActivity.this.mChatRecycler.smoothScrollToPosition(chatAdapter.getItemCount());
            }
        });
        mChatRecycler.setAdapter(chatAdapter);
    }

    private void getReferenceDataBase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        datasRefChat = FirebaseFirestore.getInstance().collection("Chat");
        datasRefUser = FirebaseFirestore.getInstance().collection("Users");
    }

    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        chatAdapter.stopListening();
    }

    public void sendMessageClick(View view) {
        EditText editText = findViewById(R.id.writeMessage);
        String message = editText.getText().toString();
        editText.setText("");
        FirebaseHelper helper = new FirebaseHelper();

        Chat chat = new Chat(message, user.getUid(), "receiverId");
        if (helper.sendMessageChat(chat, datasRefChat)) {
            //sendNotif(message);
        }

    }

    /*public void sendNotif(final String message) {
        final  APIService apiService  = Config.getFCMService();
        dataToken.addSnapshotListener(new EventListener<QuerySnapshot>() {
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                if (e == null) {
                    for (DocumentSnapshot dSnap : queryDocumentSnapshots.getDocuments()) {
                        if (!user.getEmail().equals(dSnap.getId())) {
                            Token token = (Token) dSnap.toObject(Token.class);
                            if (token != null) {
                                apiService.sendNotification(new Sender(token.getToken(), new Notification(message, user.getDisplayName()))).enqueue(new Callback<MyRespence>() {
                                    public void onResponse(Call<MyRespence> call, Response<MyRespence> response) {

                                    }

                                    public void onFailure(Call<MyRespence> call, Throwable t) {
                                        Toast.makeText(ChatActivity.this, "Failure !!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }*/


}
