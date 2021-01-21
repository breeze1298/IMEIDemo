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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class sign_up extends AppCompatActivity {

    private EditText cname,cnumber,cpassword,caddress;
    private Button register;
    String imei;

    private static final String CUSTOMER = "Customer_Name";
    private static final String NUMBER="Phone_Number";
    private static final String ADDRESS="Address";
    private static final String PASSWORD="Password";
    private static final String IMEI="IMEI";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        cname=findViewById(R.id.C_name);
        cnumber=findViewById(R.id.C_number);
        cpassword=findViewById(R.id.C_password);
        caddress=findViewById(R.id.C_address);
        register=findViewById(R.id.register);

        Intent i_number=getIntent();
        imei=i_number.getExtras().getString("number");


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt1=cname.getText().toString();
                String txt2=cnumber.getText().toString();
                String txt3=caddress.getText().toString();
                String txt4=cpassword.getText().toString();

                if(TextUtils.isEmpty(txt1) || TextUtils.isEmpty(txt2) || TextUtils.isEmpty(txt3) || txt4.isEmpty())
                {
                    Toast.makeText(sign_up.this, "Empty Credentials !!! ", Toast.LENGTH_SHORT).show();
                }
                else if (txt4.length()<6)
                {
                    Toast.makeText(sign_up.this, "Password too Short !", Toast.LENGTH_SHORT).show();
                }
                else if(txt2.length()==10)
                {
                    savenote();
                }
                else
                {
                    Toast.makeText(sign_up.this, "Invalid Phone Number ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

private void savenote() {

        String txt1=cname.getText().toString();
        String txt2=cnumber.getText().toString();
        String txt3=caddress.getText().toString();
        String txt4=cpassword.getText().toString();
        String i=imei;

        Map<String, Object> note = new HashMap<>();
        note.put(CUSTOMER,txt1);
        note.put(NUMBER,txt2);
        note.put(ADDRESS,txt3);
        note.put(PASSWORD,txt4);
        note.put(IMEI,i);

        db.collection("User").document(i).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
         @Override
            public void onSuccess(Void aVoid) {
        Toast.makeText(sign_up.this, "Registered Successfully ", Toast.LENGTH_SHORT).show();
        cname.setText("");
        cnumber.setText("");
        cpassword.setText("");
        caddress.setText("");
        //Moving to the Products Page
        startActivity(new Intent(sign_up.this,product.class));
        }
        }).addOnFailureListener(new OnFailureListener() {
         @Override
        public void onFailure(@NonNull Exception e)
         {
                 Toast.makeText(sign_up.this, "Error :  Details Cannot be saved  !! ", Toast.LENGTH_SHORT).show();
         }
        });

    }
}