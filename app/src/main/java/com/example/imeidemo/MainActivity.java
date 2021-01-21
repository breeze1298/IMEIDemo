package com.example.imeidemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 100;
    private String imei_number;
    private static final String USERNAME = "Customer_Name";
    private static final String IMEI = "IMEI";
    private Button alog;
    int cnt=0;
    //create firebase instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checkPermission(Manifest.permission.READ_PHONE_STATE, PERMISSION_CODE);

        alog=findViewById(R.id.button_admin);

        //Getting IMEI Number
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei_number = telephonyManager.getDeviceId();


        alog.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(MainActivity.this,admin_login.class));
                return false;
            }
        });

        verify();

    }

   /* private void checkPermission(String readPhoneState, int permissionCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{permission},
                    requestCode);
        } else {
            Toast.makeText(MainActivity.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }*/

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
        {
            super.onRequestPermissionsResult(requestCode, permissions,grantResults);

            if (requestCode == PERMISSION_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error : Permission ", Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void verify()
        {
        DocumentReference noteRef = db.collection("User").document(imei_number);
        noteRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
        DocumentSnapshot document = task.getResult();
        String s1 = document.getString(USERNAME);
        String s2 = document.getString(IMEI);
        String i = imei_number;
            if (i.equalsIgnoreCase(s2))
            {
                        Toast.makeText(MainActivity.this, "Welcome Back " + s1, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, product.class));

            } else {

                        Intent intent = new Intent(MainActivity.this, sign_up.class);
                        intent.putExtra("number", i);
                        startActivity(intent);
            }
            } else {
            Toast.makeText(MainActivity.this, "Error : Network Error !!", Toast.LENGTH_SHORT).show();
                 }

            }
        });


        }

}