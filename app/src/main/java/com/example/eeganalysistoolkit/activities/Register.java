package com.example.eeganalysistoolkit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.eeganalysistoolkit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    FirebaseAuth auth;
    CollectionReference reference;
    RadioButton selectedRadioButton, selectedRadioButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        final EditText firstName = (EditText) findViewById(R.id.editText5);
        final EditText lastName = (EditText) findViewById(R.id.editText6);
        final EditText age = (EditText) findViewById(R.id.editText10);
        final EditText password = (EditText) findViewById(R.id.editText4);
        final EditText passwordconfirm = (EditText) findViewById(R.id.editText7);
        final EditText city = (EditText) findViewById(R.id.editText9);
        final EditText mobilephoneno = (EditText) findViewById(R.id.editText3);
        final EditText socialNumberId = (EditText) findViewById(R.id.editText8);
        final EditText email = (EditText) findViewById(R.id.editText2);
        final EditText username = findViewById(R.id.editText11);
        final RadioGroup genderu = (RadioGroup) findViewById(R.id.genderu);
        final RadioGroup usertype = (RadioGroup) findViewById(R.id.usertype);
        Button register = (Button) findViewById(R.id.button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // final String email1 = email.getText().toString();
                String namee = firstName.getText().toString();
                String lastname = lastName.getText().toString();
                String ageold = age.getText().toString();
                String town = city.getText().toString();
                String passw = password.getText().toString();
                String copassw = passwordconfirm.getText().toString();
                String mobile = mobilephoneno.getText().toString();
                String social = socialNumberId.getText().toString();
                String emailm = email.getText().toString();
                String user = username.getText().toString();

                if (genderu.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
                } else {
                    // get selected radio button from radioGroup
                    int selectedId1 = usertype.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    selectedRadioButton1 = (RadioButton) findViewById(selectedId1);
                    Toast.makeText(getApplicationContext(), selectedRadioButton1.getText().toString() + " is selected", Toast.LENGTH_SHORT).show();
                }
                if (usertype.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select usertype", Toast.LENGTH_SHORT).show();

                } else {
                    // get selected radio button from radioGroup
                    int selectedId = genderu.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    selectedRadioButton = (RadioButton) findViewById(selectedId);
                    Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString() + " is selected", Toast.LENGTH_SHORT).show();


                }

                if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(lastname) || TextUtils.isEmpty(ageold) || TextUtils.isEmpty(town) || TextUtils.isEmpty(passw) || TextUtils.isEmpty(copassw)
                        || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(social) || TextUtils.isEmpty(emailm) || TextUtils.isEmpty(user)) {
                    Toast.makeText(Register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ageold.length() > 3) {
                    Toast.makeText(Register.this, "Age's symbols amount too big", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (social.length() > 11) {
                    Toast.makeText(Register.this, "SocialId number too long", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!emailm.contains("@")) {
                    Toast.makeText(Register.this, "Not email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mobile.length() > 10) {
                    Toast.makeText(Register.this, "Mobile phone number too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passw.length() < 8) {
                    Toast.makeText(Register.this, "Password must be at least 8 symbols length", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (copassw.length() < 8) {
                    Toast.makeText(Register.this, "Confirm password must be at least 8 symbols lenght", Toast.LENGTH_SHORT).show();
                    return;


                }
                if (!copassw.equals(passw)) {
                    Toast.makeText(Register.this, "Confirm password must be same as password", Toast.LENGTH_SHORT).show();
                    return;


                }

                register(namee, lastname, ageold, passw, town, mobile, social, emailm, selectedRadioButton.getText().toString(), selectedRadioButton1.getText().toString());
            }
        });


    }

    private void register(final String firstname, final String lname, final String age, final String password, final String city, final String mobilephoneno,
                          final String socialNumberId, final String email, final String genderu, final String usertype) {
        final ProgressBar progressBar = findViewById(R.id.progress_register);
        final ScrollView container = findViewById(R.id.layout_register);
        progressBar.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();
                            reference = FirebaseFirestore.getInstance().collection("Users");
                            Map<String, Object> hashMap = new HashMap<String, Object>();
                            hashMap.put("id", userid);
                            hashMap.put("firstName", firstname);
                            hashMap.put("lastName", lname);
                            hashMap.put("age", age);
                            hashMap.put("city", city);
                            hashMap.put("mobilePhone", mobilephoneno);
                            hashMap.put("socialNumberId", socialNumberId);
                            hashMap.put("gender", genderu);
                            hashMap.put("userType", usertype);
                            hashMap.put("approvedUser",false);


                            reference.document(userid).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(Register.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(Register.this, "You can't  register with email or password", Toast.LENGTH_SHORT).show();
                            // return;
                            container.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }


                    }
                });
    }
}