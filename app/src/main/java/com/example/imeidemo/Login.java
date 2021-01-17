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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {


    EditText txt_username, txt_password;
    Button b_login, b_signup;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_username = findViewById(R.id.username);
        txt_password = findViewById(R.id.password);
        b_login = findViewById(R.id.login);
        b_signup = findViewById(R.id.signup);


        // LOGIN Logic
        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               loadnote();

            }
        });

        // Redirecting to the Sign up page

        b_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, signup.class));
                Toast.makeText(Login.this, "Going to Sign Up Page", Toast.LENGTH_SHORT).show();
            }
        });


    }



       public void loadnote()
       {

        String txt_u = txt_username.getText().toString();
        String txt_p = txt_password.getText().toString();

        if (txt_u.isEmpty() || txt_p.isEmpty())
        {
            Toast.makeText(Login.this, "Empty Credentials !!!", Toast.LENGTH_SHORT).show();
        }
        else
            {

            Toast.makeText(this, txt_u +"", Toast.LENGTH_SHORT).show();

            DocumentReference noteRef = db.collection("User").document(txt_u);

            noteRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(Login.this, txt_u+"", Toast.LENGTH_SHORT).show();
                        DocumentSnapshot document = task.getResult();
                        String s1 = document.getString(USERNAME);
                        String s2 = document.getString(PASSWORD);
                        if (document != null)
                        {
                            if ((s1.equalsIgnoreCase(txt_u)) && (s2.equalsIgnoreCase(txt_p)))
                            {
                                Toast.makeText(Login.this, "Login Successful ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, MainActivity.class));// Change Main Activity to Shopping Cart Activity
                            } else
                            {
                                Toast.makeText(Login.this, "Error : ", Toast.LENGTH_SHORT).show();
                            }

                        }
                     }
            }
            });

            }
       }
}
