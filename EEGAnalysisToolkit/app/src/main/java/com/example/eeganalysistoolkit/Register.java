package com.example.eeganalysistoolkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;
import android.text.TextUtils;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    protected EditText username, firstname, lname, age, password, passwordconfirm, city, mobilephoneno, socialNumberId, email;
    protected RadioGroup genderu;
    protected RadioGroup usertype;
    FirebaseAuth auth;
    DatabaseReference reference;
    //  private FirebaseAuth mAuth;
    protected Button register;
    RadioButton selectedRadioButton,selectedRadioButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        firstname = (EditText) findViewById(R.id.editText5);
        lname = (EditText) findViewById(R.id.editText6);
        age = (EditText) findViewById(R.id.editText10);
        password = (EditText) findViewById(R.id.editText4);
        passwordconfirm = (EditText) findViewById(R.id.editText7);
        city = (EditText) findViewById(R.id.editText9);
        mobilephoneno = (EditText) findViewById(R.id.editText3);
        socialNumberId = (EditText) findViewById(R.id.editText8);
        email = (EditText) findViewById(R.id.editText2);
        username = findViewById(R.id.editText11);
        genderu = (RadioGroup) findViewById(R.id.genderu);
        usertype = (RadioGroup) findViewById(R.id.usertype);
        register = (Button) findViewById(R.id.button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // final String email1 = email.getText().toString();
                String namee = firstname.getText().toString();
                String lastname = lname.getText().toString();
                String ageold = age.getText().toString();
                String town = city.getText().toString();
                String passw = password.getText().toString();
                String copassw = passwordconfirm.getText().toString();
                String mobile = mobilephoneno.getText().toString();
                String social = socialNumberId.getText().toString();
                String emailm = email.getText().toString();
                String user = username.getText().toString();

                if(genderu.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // get selected radio button from radioGroup
                    int selectedId1 = usertype.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    selectedRadioButton1 = (RadioButton)findViewById(selectedId1);
                    Toast.makeText(getApplicationContext(), selectedRadioButton1.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();
                }
                if(usertype.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(), "Please select usertype", Toast.LENGTH_SHORT).show();

                }


                else
                {
                    // get selected radio button from radioGroup
                    int selectedId = genderu.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    selectedRadioButton = (RadioButton)findViewById(selectedId);
                    Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();



                }

                if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(lastname) || TextUtils.isEmpty(ageold) || TextUtils.isEmpty(town) || TextUtils.isEmpty(passw) || TextUtils.isEmpty(copassw)
                        || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(social) || TextUtils.isEmpty(emailm) || TextUtils.isEmpty(user)) {
                    Toast.makeText(Register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(ageold.length()>3){
                    Toast.makeText(Register.this, "Age's symbols amount too big", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(social.length()>11){
                    Toast.makeText(Register.this, "SocialId number too long", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!emailm.contains("@")){
                    Toast.makeText(Register.this, "Not email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mobile.length()>9){
                    Toast.makeText(Register.this, "Mobile phone number too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passw.length() < 8) {
                    Toast.makeText(Register.this, "Password must be at least 8 symbols length", Toast.LENGTH_SHORT).show();
                    return;

                }  if (copassw.length() < 8) {
                    Toast.makeText(Register.this, "Confirm password must be at least 8 symbols lenght", Toast.LENGTH_SHORT).show();
                    return;


                } if(!copassw.equals(passw)){
                    Toast.makeText(Register.this, "Confirm password must be same as password", Toast.LENGTH_SHORT).show();
                    return;



                }

                else{
                    register(namee,lastname,ageold,passw,copassw,town,mobile,social,emailm,user, selectedRadioButton.getText().toString(), selectedRadioButton1.getText().toString());

                }

            }
        });


    }

    private void register(final String firstname, final String lname, final String age, final String password, final String passwordconfirm, final String city, final String  mobilephoneno,
                          final String socialNumberId, final String email, final String user, final String genderu, final String usertype) {
        Toast.makeText(this, ""+firstname+"=="+lname+"=="+age+""+password+""+city+""+mobilephoneno+""+socialNumberId+""+email+""+genderu+""+usertype+"", Toast.LENGTH_SHORT).show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser!=null;
                            String userid = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users");
                            Map<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("id", userid);
                            hashMap.put("firstaname", firstname);
                            hashMap.put("lname", lname);
                            hashMap.put("age", age);
                            //hashMap.put("password", password);
                            //hashMap.put("passwordconfirm", passwordconfirm);
                            hashMap.put("city", city);
                            hashMap.put("mobilephone", mobilephoneno);
                            hashMap.put("socialNumberId", socialNumberId);
                            hashMap.put("gender", genderu);
                            hashMap.put("usertype", usertype);

                            /*reference.setValue(hashMap);
                            Intent intent=new Intent(Register.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();*/


                            reference.child(usertype).child(userid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(Register.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                        }else{
                            Toast.makeText(Register.this, "You can't  register with email or password", Toast.LENGTH_SHORT).show();
                            // return;
                        }


                    }
                });
        // register.setOnClickListener(new View.OnClickListener() {@Override
///public void onClick(View v) {
        //      String emailString = email.getText().toString();
        //      String passwordString = password.getText().toString();


        //    if (TextUtils.isEmpty(emailString)) {
        //        return;
        //     }
//
        //  if (TextUtils.isEmpty(passwordString)) {
        //       Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
        //    return;
        //    }

        //   if (password.length() < 6) {
        //     Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
        //       return;
        //   }
        // progressBar.setVisibility(View.VISIBLE);
        //create user
        //   auth.createUserWithEmailAndPassword(emailString, passwordString)
        //     .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
        //           @Override
        //        public void onComplete(@NonNull Task<AuthResult> task) {
        //           Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
        //progressBar.setVisibility(View.GONE);
        // If sign in fails, display a message to the user. If sign in succeeds
        // the auth state listener will be notified and logic to handle the
        // signed in user can be handled in the listener.
        //  if (!task.isSuccessful()) {
        //Toast.makeText(Register.this, "Authentication failed." + task.getException(),
        //      Toast.LENGTH_SHORT).show();
        //     } else {
        // Toast.makeText(Register.this, "Authentication success." + task.getException(),
        //    Toast.LENGTH_SHORT).show();
        // startActivity(new
        // Intent(Register.this, MainActivity.class));
        //
        //
        // finish();
        //      }
        //     }
        //   });

        //   }
        //    });


    }

}













//register.setOnClickListener(new View.OnClickListener() {
//   @Override
//  public void onClick(View v) {

//     String emailString = email.getText().toString();
//     String passwordString = password.getText().toString();

//     if (TextUtils.isEmpty(emailString)) {
//         Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
//          return;
//      }
//
//     if (TextUtils.isEmpty(passwordString)) {
//         Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
//         return;
//     }

//    if (password.length() < 6) {
//        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
//        return;
//    }

//  progressBar.setVisibility(View.VISIBLE);
//create user
//    auth.createUserWithEmailAndPassword(emailString, passwordString)
//           .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
//                @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//               Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
// progressBar.setVisibility(View.GONE);
// If sign in fails, display a message to the user. If sign in succeeds
// the auth state listener will be notified and logic to handle the
// signed in user can be handled in the listener.
// if (!task.isSuccessful()) {
//      Toast.makeText(Register.this, "Authentication failed." + task.getException(),
//          Toast.LENGTH_SHORT).show();
//   } else {
//   startActivity(new
//          Intent(Register.this, MainActivity.class));
//  finish();
//      }
//    }
//  });

//  }
//  });