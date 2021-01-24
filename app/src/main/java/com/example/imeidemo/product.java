package com.example.imeidemo;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class product extends AppCompatActivity  {


    private RecyclerView firestoreList;

    private FirebaseFirestore firebaseFirestore;

    private FirestoreRecyclerAdapter adapter;

    private Button btn_cart;
    private String cnameMain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        btn_cart=findViewById(R.id.btnViewCart);


        Intent getData = getIntent();
        cnameMain = getData.getStringExtra("cname");//Getting the Customer Name From Main Activity if he is already Registered

        //Instance of Firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        firestoreList = findViewById(R.id.firestore_list);

        //Query
        Query query = firebaseFirestore.collection("Product");

        //Recycler Option
        FirestoreRecyclerOptions<ProductModel> options = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setLifecycleOwner(this)
                .setQuery(query, ProductModel.class)
                .build();

        //Recycler Adapter
        adapter = new FirestoreRecyclerAdapter<ProductModel, ProductViewHolder>(options) {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductModel model) {
                holder.list_name.setText(model.getName());
                holder.list_price.setText(model.getPrice());

            }

        };
            //View Holder
        firestoreList.setHasFixedSize(true);
        firestoreList.setLayoutManager(new LinearLayoutManager(this));
        firestoreList.setAdapter(adapter);


        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cnameMain.isEmpty())
                {
                    Intent intent=new Intent(product.this,MainActivity.class);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(product.this, "Via Main Activity", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(product.this,ViewCart.class);
                    intent.putExtra("cname",cnameMain);
                    startActivity(intent);

                }



            }
        });


    }


    public class ProductViewHolder extends  RecyclerView.ViewHolder  {
        private TextView list_name, list_price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.list_name);
            list_price = itemView.findViewById(R.id.list_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pnm=list_name.getText().toString();
                    Toast.makeText(product.this, pnm, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(product.this,ViewProduct.class);
                    intent.putExtra("PRODUCT",pnm);
                    intent.putExtra("cname",cnameMain);
                    startActivity(intent);


                }
            });
        }
    }


      /*  @Override
        public FirestoreAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single,parent,false);
            return new FirestoreAdapter.ProductViewHolder(view);
        }*/





  /*
  No Need for Manual call
  @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }*/

    @Override
    public void onBackPressed()
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(product.this);
        builder.setMessage("Do you want to exit ?");
        builder.setTitle("Alert !");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });


        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
