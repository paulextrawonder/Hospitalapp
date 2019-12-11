package com.example.eeganalysistoolkit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.eeganalysistoolkit.model.Patient;
import com.example.eeganalysistoolkit.R;
import com.example.eeganalysistoolkit.model.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

public class UserListActivity extends AppCompatActivity {

    private String typeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        final ListView listView = findViewById(R.id.list_user);
        typeUser = getIntent().getStringExtra("userType");
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Users").document(typeUser).collection("profile");


        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    return;
                }
                final List<Patient> patient = queryDocumentSnapshots.toObjects(Patient.class);
                final List<String> listName  = new ArrayList<>();
                for(Patient pp : patient){
                    listName.add(pp.getFirstaname() + " " + pp.getLname());
                }
                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(UserListActivity.this, android.R.layout.simple_list_item_1, listName);

                listView.setAdapter(itemsAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(UserListActivity.this,ChatActivity.class);
                        intent.putExtra("receiverId",patient.get(position).getId());
                        intent.putExtra("typeUser",patient.get(position).getUsertype());
                        startActivity(intent);
                    }
                });
            }
        });


    }
}
