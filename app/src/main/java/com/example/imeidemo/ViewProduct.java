package com.example.imeidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.sql.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ViewProduct extends AppCompatActivity {

    String productnm;
    TextView p1, p2,p3;
    ImageView pimage;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_PRICE = "price";
    private static final String KEY_DESC="description";

    private Button btnadd;

    private String cnameProduct,sig_cname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        p1 = findViewById(R.id.t1);
        p2 = findViewById(R.id.t2);
        p3=findViewById(R.id.t3);
        pimage = findViewById(R.id.imageView);
        btnadd = findViewById(R.id.btnaddtocart);

        // Here i got the Product Name which is been Clicked by the user in product.java
        Intent getData = getIntent();
        productnm = getData.getStringExtra("PRODUCT");
        //Got the Customer Name from MainActivity to Product to ViewProduct
        cnameProduct = getData.getStringExtra("cname");
        sig_cname=getData.getStringExtra("sig_cname");

       getDetails();

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();

            }
        });

    }


    private void getDetails()
    {

        DocumentReference noteRef = db.document("Product/" + productnm);
        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString(KEY_NAME);
                    String price = documentSnapshot.getString(KEY_PRICE);
                    String image = documentSnapshot.getString(KEY_IMAGE);
                    String desc=documentSnapshot.getString(KEY_DESC);

                    p1.setText(name);
                    p2.setText(price);
                    p3.setText(desc);
                    //Getting the image url and loading the image in the image view
                    //Context context = pimage.getContext();
                    Picasso.get().load(image).into(pimage);


                } else {
                    Toast.makeText(ViewProduct.this, "Document does not exist !!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ViewProduct.this, "Error !! ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveData()
    {
        String doc = cnameProduct;//Customer Name
        if (doc.isEmpty())
        {
            Toast.makeText(ViewProduct.this, "Error", Toast.LENGTH_SHORT).show();
        }
        else
        {
           String s1=p1.getText().toString();
           String s2=p2.getText().toString();

            Map<String, Object> note = new HashMap<>();
            note.put(KEY_NAME, s1);
            note.put(KEY_PRICE, s2);
            String s=s1;// Product Name
            db.collection(doc).document(s).set(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(ViewProduct.this, s1 + " Added to Cart ", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ViewProduct.this, "Error in Database", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }
}

