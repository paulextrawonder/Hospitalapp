package com.example.eeganalysistoolkit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eeganalysistoolkit.R;
import com.example.eeganalysistoolkit.model.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final TextView txtFirestName,txtLastName,txtEmail,txtAge,txtGender,txtPhone,txtSocialNumber,txtUserType,txtAprouved;
        txtFirestName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtAge = findViewById(R.id.txtAge);
        txtGender = findViewById(R.id.txtGender);
        txtUserType = findViewById(R.id.txtUserType);
        txtSocialNumber = findViewById(R.id.txtSocielNumber);
        txtAprouved = findViewById(R.id.txtAprouved);
        CollectionReference datasRefUser = FirebaseFirestore.getInstance().collection("Users");
        final DocumentReference docRef = datasRefUser.document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Profile profile = task.getResult().toObject(Profile.class);
                    if(profile != null){
                        txtFirestName.setText(profile.getFirstName());
                        txtLastName.setText(profile.getLastName());
                        txtEmail.setText(profile.getEmail());
                        txtPhone.setText(profile.getMobilePhone());
                        txtAge.setText(profile.getAge());
                        txtGender.setText(profile.getGender());
                        txtSocialNumber.setText(profile.getSocialNumberId());
                        txtUserType.setText(profile.getUserType());
                        txtAprouved.setText(""+profile.isApprovedUser());
                    }
                }
            }
        });

        Button approveButton = findViewById(R.id.approvedButton);
        //TODO update code to send new data
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(SettingActivity.this, "update User", Toast.LENGTH_SHORT).show();

            }
        });



    }
}
