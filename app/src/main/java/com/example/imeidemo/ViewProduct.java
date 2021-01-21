package com.example.imeidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ViewProduct extends AppCompatActivity {

    String productnm;
    TextView p1,p2;
    ImageView pimage;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_PRICE = "price";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
/*
        p1=findViewById(R.id.t1);
        p2=findViewById(R.id.t2);
        pimage=findViewById(R.id.imageView);

        // Here i got the Product Name which is been Clicked by the user in product.java
        Intent getData = getIntent();
        productnm = getData.getStringExtra("Name");

        DocumentReference noteRef=db.document("Product/"+productnm);
        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString(KEY_NAME);
                    String price = documentSnapshot.getString(KEY_PRICE);
                    String image = documentSnapshot.getString(KEY_IMAGE);

                    p1.setText(name);
                    p2.setText(price);
                    //Getting the image url and loading the image in the image view
                    Context context = pimage.getContext();
                    Picasso.get().load(image).into(pimage);


                }
                else {
                    Toast.makeText(ViewProduct.this,"Document does not exist !!",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ViewProduct.this,"Error !! ",Toast.LENGTH_LONG).show();
                    }
                });*/


    }
}