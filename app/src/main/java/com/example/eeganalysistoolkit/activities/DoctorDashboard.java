package com.example.eeganalysistoolkit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eeganalysistoolkit.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class DoctorDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
        Button mLogout = (Button) findViewById(R.id.logout);

        final Button chatButton = findViewById(R.id.chat_button);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(DoctorDashboard.this, UserListActivity.class);
                chatIntent.putExtra("userType", "Patient");
                startActivity(chatIntent);
            }
        });


        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DoctorDashboard.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });


    }


}
