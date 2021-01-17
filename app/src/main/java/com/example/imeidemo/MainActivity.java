package com.example.imeidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String imei_number;
    private static final String USERNAME = "Username";
    private static final String IMEI = "IMEI";
    //create firebase instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Getting IMEI Number
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei_number=telephonyManager.getDeviceId();


        DocumentReference noteRef = db.collection("User").document(imei_number);
        noteRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    String s1 = document.getString(USERNAME);
                    String s2=document.getString(IMEI);
                    String i=imei_number;

                        if(i.equalsIgnoreCase(s2)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Welcome Back " + s1, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(MainActivity.this, product.class));
                                }
                            }, 7000);
                        }
                        else
                        {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(MainActivity.this,signup.class));                            }
                            }, 5000);

                        }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Error : Network Error !!", Toast.LENGTH_SHORT).show();
                }

                }
        });

    }
}