package com.example.eeganalysistoolkit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.example.eeganalysistoolkit.R;
import com.example.eeganalysistoolkit.model.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ScrollView layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mPatient = (Button) findViewById(R.id.patient);
        progressBar = findViewById(R.id.progress);
        layout = findViewById(R.id.layout_main);
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

                }
            });
        }else {
            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }

        mPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDashboard(LoginActivity.class);
            }
        });


    }


    private void goToDashboard(Class activity){
        Intent intent = new Intent(MainActivity.this, activity);
        startActivity(intent);
        finish();
        progressBar.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        return;
    }









}
