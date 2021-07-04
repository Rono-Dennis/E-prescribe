package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prescribe.Model.Cart;
import com.example.prescribe.ViewHolder.CartViewHolder;
import com.example.prescribe.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Cancel extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EditText inputSearch;
    DatabaseReference productsRef;
    private String ToPay ="", pay="",toShipped="", admin,noorders="";
    private FirebaseAuth mAuth;
    String using;
    private FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        inputSearch=findViewById(R.id.inputsearchs);
        recyclerView = findViewById(R.id.recycler_menuu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        mAuth=FirebaseAuth.getInstance();
        inputSearch.setOnKeyListener(null);
        pay=getIntent().getStringExtra("topay");
        noorders=getIntent().getStringExtra("NoOrders");
        toShipped=getIntent().getStringExtra("shipped");
        ToPay = getIntent().getStringExtra("payement");

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null)
                {
                    onStart();


                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Cancel.this,UserPage.class);
        intent.putExtra("Amount",String.valueOf(ToPay));
        intent.putExtra("ToPay", String.valueOf(pay));
        intent.putExtra("Shippment", String.valueOf(toShipped));
        intent.putExtra("orderno", String.valueOf(noorders));
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        using=mAuth.getCurrentUser().getUid();
        productsRef = FirebaseDatabase.getInstance().getReference().child("CancelOrders").child(using);

        FirebaseRecyclerOptions<com.example.prescribe.Model.Cart> options =
                new FirebaseRecyclerOptions.Builder<com.example.prescribe.Model.Cart>()
                        .setQuery(productsRef.orderByChild("pname").startAt(inputSearch.getText().toString()).endAt(inputSearch.getText().toString() + "\uf8ff"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {

                cartViewHolder.txtProductQuantity.setText("Quantity = "+cart.getQuantity());
                cartViewHolder.txtProductPrice.setText("Price = ksh "+cart.getPrice());
                cartViewHolder.txtProductName.setText(cart.getPname());

                int oneTypeProductionPrice = ((Integer.valueOf(cart.getPrice()))) * Integer.valueOf(cart.getQuantity());

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                                                                 "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder( Cancel.this);
                        builder.setTitle("CartOptions");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent( Cancel.this, AdminProductsActivity.class);
                                    intent.putExtra("pid", cart.getPid());
                                    startActivity(intent);

                                }
                                if (i==1) {
                                    final  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("CancelOrders");
                                    databaseReference.child(using)
                                            .child(cart.getPid())
                                            .removeValue();


                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout, viewGroup, false);
                CartViewHolder cartViewHolder =new CartViewHolder(view);
                return cartViewHolder;
            }


        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }}