package com.example.eeganalysistoolkit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.example.eeganalysistoolkit.R;
import com.example.eeganalysistoolkit.model.Admin;
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

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mDoctor = (Button) findViewById(R.id.doctor);
        Button mPatient = (Button) findViewById(R.id.patient);
        Button mAdmin = (Button) findViewById(R.id.admin);
        final ProgressBar progressBar = findViewById(R.id.progress);
        final ScrollView layout = findViewById(R.id.layout_main);
        layout.setVisibility(View.GONE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final CollectionReference reference = FirebaseFirestore.getInstance().collection("Users");
        if(user != null){
            DocumentReference docRef = reference.document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            final Profile mProfile = document.toObject(Profile.class);
                            if("Doctor".equals(mProfile.getUsertype())){
                                goToDashboard(DoctorDashboard.class);
                            }else if("Admin".equals(mProfile.getUsertype())){
                                goToDashboard(AdministratorDashboard.class);
                            }else if("Patient".equals(mProfile.getUsertype())){
                                goToDashboard(PatientDashboard.class);
                            }
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                }
            });
        }


        mDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDashboard(DriverLoginActivity.class);

            }
        });

        mPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDashboard(CustomerLoginActivity.class);
            }
        });
        mAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDashboard(AdministratorLoginActivity.class);
            }
        });
    }


    private void goToDashboard(Class activity){
        Intent intent = new Intent(MainActivity.this, activity);
        startActivity(intent);
        finish();
        return;
    }









}
