package com.example.eeganalysistoolkit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eeganalysistoolkit.R;
import com.example.eeganalysistoolkit.adapter.ChatAdapter;
import com.example.eeganalysistoolkit.model.Chat;
import com.example.eeganalysistoolkit.model.Conversation;
import com.example.eeganalysistoolkit.model.FirebaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ChatActivity extends AppCompatActivity {

    private static final int FILE_REQUEST = 55;
    private CollectionReference datasRefChat;
    private CollectionReference datasRefUser;
    private CollectionReference dataToken;
    private ChatAdapter chatAdapter;
    private RecyclerView mChatRecycler;
    FirebaseUser user;
    private String receiverId;
    private String conversationId;
    private  Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        receiverId = getIntent().getStringExtra("receiverId");
        String typeUser = getIntent().getStringExtra("typeUser");
        user = FirebaseAuth.getInstance().getCurrentUser();
        if ("Doctor".equals(typeUser)) {
            conversationId = receiverId + user.getUid();
        } else {
            conversationId = user.getUid() + receiverId;
        }
        getReferenceDataBase(conversationId);
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
        ImageButton sendFile = findViewById(R.id.sendFileBtn);
        sendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showFileChooser();
            }
        });

    }

    private void getReferenceDataBase(String conversationId) {

        datasRefChat = FirebaseFirestore.getInstance().collection("Chat").document("Conversation").collection(conversationId);
        datasRefUser = FirebaseFirestore.getInstance().collection("Users").document(receiverId).collection("Conversation");
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

    private void uploadFile() {
        //if there is a file to upload
        FirebaseStorage storageReference = FirebaseStorage.getInstance();
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            StorageReference reference = storageReference.getReference();
            StorageReference riversRef = reference.child("EDFfile/" + filePath.getLastPathSegment() + ".edf");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(final UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    //displaying percentage in progress dialog
                                    sendMessage("file send");
                                }
                            });

                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
            Toast.makeText(getApplicationContext(), "File not found ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadFile();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select EDF File"), FILE_REQUEST);
    }

    public void sendMessageClick(View view) {
        EditText editText = findViewById(R.id.writeMessage);
        String message = editText.getText().toString();
        editText.setText("");
        sendMessage(message);
    }

    private void sendMessage(String message){
        FirebaseHelper helper = new FirebaseHelper();

        Chat chat = new Chat(message, user.getUid(), receiverId);
        if (helper.sendMessageChat(chat, datasRefChat)) {
            Conversation conversation = new Conversation(conversationId, user.getUid(), receiverId);

            datasRefUser.add(conversation);
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
