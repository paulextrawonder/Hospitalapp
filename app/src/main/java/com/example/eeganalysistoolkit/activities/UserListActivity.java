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
import com.google.firebase.firestore.CollectionReference;
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
        String typeUser = getIntent().getStringExtra("userType");
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Users").document(typeUser).collection("Profiles");


        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    return;
                }
                final List<Patient> patients = queryDocumentSnapshots.toObjects(Patient.class);
                final List<String> listName  = new ArrayList<>();
                for(Profile patient : patients){
                    listName.add(patient.getFirstName() + " " + patient.getLastName());
                }
                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(UserListActivity.this, android.R.layout.simple_list_item_1, listName);

                listView.setAdapter(itemsAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(UserListActivity.this,ChatActivity.class);
                        intent.putExtra("receiverId",patients.get(position).getId());
                        intent.putExtra("typeUser",patients.get(position).getUsertype());
                        startActivity(intent);
                    }
                });
            }
        });


    }
}
