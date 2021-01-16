package com.example.imeidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {


    private EditText username, password;
    private Button login, signup;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);


        // LOGIN Logic
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadnote();

            }
        });

        // Redirecting to the Sign up page

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, signup.class));
                Toast.makeText(Login.this, "Going to Sign Up Page", Toast.LENGTH_SHORT).show();
            }
        });


    }



    public void loadnote() {

        String txt_u = username.getText().toString();
        String txt_p = password.getText().toString();

        if (TextUtils.isEmpty(txt_u) || TextUtils.isEmpty(txt_p)) {
            Toast.makeText(Login.this, "Empty Credentials !!!", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, username + "" + password, Toast.LENGTH_SHORT).show();

            DocumentReference noteRef = db.document("User" + txt_u);
            noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String s1 = documentSnapshot.getString(USERNAME);
                        String s2 = documentSnapshot.getString(PASSWORD);
                        if ((s1 == txt_u) && (s2 == txt_p)) {
                            Toast.makeText(Login.this, "Login Successful ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));// Change Main Activity to Shopping Cart Activity
                        } else {
                            Toast.makeText(Login.this, "Error : ", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Login.this, "Error ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
