package com.example.eeganalysistoolkit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.eeganalysistoolkit.model.Patient;
import com.example.eeganalysistoolkit.R;
import com.example.eeganalysistoolkit.model.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        final ListView listView = findViewById(R.id.list_user);
        final boolean isAdmin = getIntent().getBooleanExtra("admin",false);
        final CollectionReference reference = FirebaseFirestore.getInstance().collection("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = reference.document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        final Profile mProfile = document.toObject(Profile.class);
                        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if(e != null){
                                    return;
                                }
                                final List<Profile> profiles = queryDocumentSnapshots.toObjects(Profile.class);
                                final List<String> listName  = new ArrayList<>();
                                for(Profile profile : profiles){
                                    if(isAdmin && !profile.isApproved()){
                                        listName.add(profile.getFirstName() + " " + profile.getLastName());
                                    } else if(!profile.getUsertype().equals(mProfile.getUsertype())){
                                        listName.add(profile.getFirstName() + " " + profile.getLastName());
                                    }
                                }
                                ArrayAdapter<String> itemsAdapter =
                                        new ArrayAdapter<String>(UserListActivity.this, android.R.layout.simple_list_item_1, listName);

                                listView.setAdapter(itemsAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if(isAdmin){
                                            Intent intentDetail = new Intent(UserListActivity.this,ProfileDetailActivity.class);
                                            intentDetail.putExtra("userId",profiles.get(position).getId());
                                            startActivity(intentDetail);
                                        }else {
                                            Intent intent = new Intent(UserListActivity.this,ChatActivity.class);
                                            intent.putExtra("receiverId",profiles.get(position).getId());
                                            intent.putExtra("typeUser",profiles.get(position).getUsertype());
                                            startActivity(intent);
                                        }

                                    }
                                });
                            }
                        });

                    }
                } else {
                    Log.d(UserListActivity.class.getName(), "get failed with ", task.getException());
                }
            }
        });



    }
}
