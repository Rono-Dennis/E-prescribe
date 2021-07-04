package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prescribe.ViewHolder.ProductHistoryViewHolder;
import com.example.prescribe.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ActivityProductHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_history);
        userID=getIntent().getStringExtra("uid");
        recyclerView = findViewById(R.id.recycler_mennu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
 final DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("ShowDrug").child(userID);
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductHistoryViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductHistoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductHistoryViewHolder productHistoryViewHolder, int i, @NonNull Products products) {
                        productHistoryViewHolder.txtProductName.setText(products.getPname());
                        productHistoryViewHolder.txtProductDescription.setText(products.getDescription());
                        productHistoryViewHolder.txtProductPrice.setText("Price = ksh " + products.getPrice());
                        productHistoryViewHolder.product_Id.setText("Date Uploaded: "+products.getPid());
                        Picasso.get().load(products.getImage()).into(productHistoryViewHolder.imageView);
                        productHistoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder( ActivityProductHistory.this);
                                builder.setTitle("Please confirm that this drug is out of stock before you Delete");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (i == 0)
                                        {
                                            String UID = products.getPid();

                                            RemoveOrder(UID);
                                        }
                                        else
                                        {
                                            finish();
                                        }
                                    }
                                });
                                builder.show();

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout, parent, false);
                        ProductHistoryViewHolder holder = new ProductHistoryViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActivityProductHistory.this, AdminCategoryActivity.class);

        startActivity(intent);
    }

    private void RemoveOrder(String uid) {
        final DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("Products");
        productref.child(uid).removeValue();

        Intent intent = new Intent( ActivityProductHistory.this, ActivityProductHistory.class);

        startActivity(intent);
    }
}
