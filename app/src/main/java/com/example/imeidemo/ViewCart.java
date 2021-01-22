package com.example.imeidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ViewCart extends AppCompatActivity {

    private RecyclerView cartList;

    private FirebaseFirestore firebaseFirestore;

    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);


        //Instance of Firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        cartList = findViewById(R.id.cartlist);

       //CHANGE THE QUERY TO GET THE DETAILS ACCORDING TO THE CUSTOMER NAME
        Query query = firebaseFirestore.collection("Product");

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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
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
    }

    public class ProductViewHolder extends  RecyclerView.ViewHolder  {
        private TextView list_name, list_price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.list_name);
            list_price = itemView.findViewById(R.id.list_price);

        }
    }
}