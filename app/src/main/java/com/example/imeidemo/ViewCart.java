package com.example.imeidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ViewCart extends AppCompatActivity {

    private RecyclerView cartList;

    private FirestoreRecyclerAdapter adapter;

    String cnameViewCart;

    Button btnOrder;

    //Instance of Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_TOTAL_PRICE = "total_price";

    int t=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);


        cartList = findViewById(R.id.cartlist);
        btnOrder=findViewById(R.id.btnConfirmOrder);

        Intent getData = getIntent();
        cnameViewCart = getData.getStringExtra("cname");

       //CHANGED THE QUERY TO GET THE DETAILS ACCORDING TO THE CUSTOMER NAME
        String s=cnameViewCart;
        Query query = db.collection(s);

        //Recycler Option
        FirestoreRecyclerOptions<ProductModel> options = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setLifecycleOwner(this)
                .setQuery(query, ProductModel.class)
                .build();

        //Recycler Adapter
        adapter = new FirestoreRecyclerAdapter<ProductModel, ViewCart.ProductViewHolder>(options) {
            @NonNull
            @Override
            public ViewCart.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single,parent,false);
                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewCart.ProductViewHolder holder, int position, @NonNull ProductModel model) {
                holder.list_name.setText(model.getName());
                holder.list_price.setText(model.getPrice());

            }

        };
        //View Holder
        cartList.setHasFixedSize(true);
        cartList.setLayoutManager(new LinearLayoutManager(this));
        cartList.setAdapter(adapter);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String customer=cnameViewCart;

                db.collection(customer)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        String orderDocName=document.getId();
                                        Toast.makeText(ViewCart.this, orderDocName, Toast.LENGTH_SHORT).show();
                                        loadNote(orderDocName);

                                    }
                                } else {
                                    Toast.makeText(ViewCart.this, "Error in Viewing Cart", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });



    }


    public class ProductViewHolder extends  RecyclerView.ViewHolder  {
        private TextView list_name, list_price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.list_name);
            list_price = itemView.findViewById(R.id.list_price);

        }
    }


    public void loadNote(String name){

        final String doc=name;
        String customer=cnameViewCart;
        DocumentReference noteRef=db.document(customer+"/"+doc);
        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String pname=documentSnapshot.getString(KEY_NAME);
                    String price=documentSnapshot.getString(KEY_PRICE);

                    totalPrice(price);

                    Map<String, Object> note = new HashMap<>();
                    note.put(KEY_NAME,pname );
                    note.put(KEY_PRICE,price);

                    SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm:ss z");
                    String time = sdf.format(new Date());


                    db.collection("Order").document(customer+"_"+name+"_"+time).set(note)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ViewCart.this, customer+"Order placed ", Toast.LENGTH_LONG).show();
                                    db.collection(customer).document(doc).delete();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ViewCart.this, "Error ", Toast.LENGTH_LONG).show();
                                }
                            });


                } else {
                    Toast.makeText(ViewCart.this,"Document does not exist !!",Toast.LENGTH_LONG).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ViewCart.this,"Error !! ",Toast.LENGTH_LONG).show();
                        //Log.d(TAG,e.toString());


                    }
                });

    }


    public void totalPrice(String price)
    {

        String customer=cnameViewCart;

        String p=price;
        String[] items = p.split("Rs.");
        int totalPrice=Integer.parseInt(items[1]);
        t =(t+totalPrice);

        String s=Integer.toString(t);
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show();

       Map<String, Object> note = new HashMap<>();
        note.put(KEY_TOTAL_PRICE,s);
        SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm:ss z");
        String newtime = sdf.format(new Date());

        db.collection("Order").document(customer+"_Total_"+newtime).set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(ViewCart.this, customer+" Total Done", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewCart.this, "Error ", Toast.LENGTH_LONG).show();
                    }
                });

    }



}