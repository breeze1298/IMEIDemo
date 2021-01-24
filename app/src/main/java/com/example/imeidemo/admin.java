package com.example.imeidemo;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class admin extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_PRICE = "price";
    private static final String KEY_DESC="description";


    private static final int PICK_IMAGE_REQUEST = 234;
    private Button buttonChoose, buttonUpload, buttonSave;
    private ImageView imageView;
    private Uri filePath;
    private StorageReference storageReference;
    private EditText pname, price, img_url, desc;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        pname = (EditText) findViewById(R.id.product_name);
        price = (EditText) findViewById(R.id.product_price);
        buttonChoose = (Button) findViewById(R.id.choose);
        buttonSave = findViewById(R.id.btnsave);
        buttonUpload = findViewById(R.id.upload);

        img_url = findViewById(R.id.image_url);
        imageView = findViewById(R.id.product_image);
        desc=findViewById(R.id.product_desc);

        storageReference = FirebaseStorage.getInstance().getReference();

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveNote();
                startActivity(new Intent(admin.this, product.class));

            }
        });
    }


        private void showFileChooser()
        {
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
                filePath=data.getData();
                try{
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                    imageView.setImageBitmap(bitmap);
                    //wait(1160);
                    //uploadFile();
                }catch (IOException e){e.printStackTrace();}

            }
        }

        public void uploadFile()
        {
            if (filePath != null)
            {
                final ProgressDialog progressDialog =new ProgressDialog(this);
                progressDialog.setTitle("Uploading");

                final String name=pname.getText().toString();

                progressDialog.show();

                final StorageReference  riversRef =storageReference.child("Product/"+ name +".jpg");

                riversRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        progressDialog.dismiss();
                        imageView.setImageURI(filePath);
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                String img=String.valueOf(uri);
                                img_url.setText(img);
                            }
                        });
                        Toast.makeText(getApplicationContext(),"File Uploaded" ,Toast.LENGTH_LONG).show();
                        imageView.setImageBitmap(null);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress=(100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploading "+((int) progress) + "%..");
                    }
                });

            }
            else{
                Toast.makeText(admin.this,"Error",Toast.LENGTH_LONG).show();

            }
        }

        public void saveNote()
        {

            final String name=pname.getText().toString();
            final String p =price.getText().toString();
            String image = img_url.getText().toString();
            String dec=desc.getText().toString();
            String doc=name;

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(p) || TextUtils.isEmpty(image) || TextUtils.isEmpty(dec)) {
                Toast.makeText(admin.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
            } else {

                Map<String, Object> note = new HashMap<>();
                note.put(KEY_NAME,name );
                note.put(KEY_PRICE,p);
                note.put(KEY_IMAGE,image);
                note.put(KEY_DESC,dec);

                db.collection("Product").document(doc).set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pname.setText("");
                                price.setText("");
                                img_url.setText("");
                                desc.setText("");
                                Toast.makeText(admin.this, "Details Saved", Toast.LENGTH_LONG).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(admin.this, "Error ", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        }

    public void onClick(View view){
        if (view==buttonChoose){
            showFileChooser();
        }
        else if (view == buttonUpload){
            uploadFile();

        }
    }

}
