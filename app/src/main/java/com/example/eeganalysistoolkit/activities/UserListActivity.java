package com.example.eeganalysistoolkit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.eeganalysistoolkit.Patient;
import com.example.eeganalysistoolkit.R;
import com.google.firebase.firestore.CollectionReference;
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
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Users").document("Patient").collection("profile");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    return;
                }
                List<Patient> patient = queryDocumentSnapshots.toObjects(Patient.class);
                List<String> otems  = new ArrayList<>();
                for(Patient pp : patient){
                    otems.add(pp.getFirstaname());
                }
                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(UserListActivity.this, android.R.layout.simple_list_item_1, otems);

                listView.setAdapter(itemsAdapter);
            }
        });


    }
}
