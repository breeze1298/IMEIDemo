package com.example.imeidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    private EditText username,password,verify_password;
    private Button register;
    String imei;

    private static final String USERNAME = "Username";
    private static final String PASSWORD="Password";
    private static final String IMEI="IMEI";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        username=findViewById(R.id.signup_username);
        password=findViewById(R.id.signup_password);
        verify_password=findViewById(R.id.verify_password);
        register=findViewById(R.id.register);

        // Getting IMEI Number

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei=telephonyManager.getDeviceId();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt1=username.getText().toString();
                String txt2=password.getText().toString();
                String txt3=verify_password.getText().toString();

                if(TextUtils.isEmpty(txt1) || TextUtils.isEmpty(txt2) || TextUtils.isEmpty(txt3))
                {
                    Toast.makeText(signup.this, "Empty Credentials !!! ", Toast.LENGTH_SHORT).show();
                }
                else if (txt2.length()<6)
                {
                    Toast.makeText(signup.this, "Password too Short !", Toast.LENGTH_SHORT).show();
                }
                else if(txt2.contentEquals(txt3))
                {
                    savenote();
                }
                else
                {
                    Toast.makeText(signup.this, "Password Doesn't Match !! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void savenote() {

        String txt1=username.getText().toString();
        String txt2=verify_password.getText().toString();
        String i=imei;

        Map<String, Object> note = new HashMap<>();
        note.put(USERNAME,txt1);
        note.put(PASSWORD,txt2);
        note.put(IMEI,i);

        db.collection("User").document(i).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(signup.this, "Registered Successfully ", Toast.LENGTH_SHORT).show();
                username.setText("");
                password.setText("");
                verify_password.setText("");
                startActivity(new Intent(signup.this,product.class));// Change the MainActivity to Shopping List Activity
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup.this, "Error :  Details Cannot be saved  !! ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}