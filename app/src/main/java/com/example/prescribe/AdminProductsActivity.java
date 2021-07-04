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
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prescribe.Model.Cart;
import com.example.prescribe.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminProductsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartlistRef;
    private String userID = "", Phone = "", productId="",saveCurrentDate,saveCurrentTime,DateTime, orderrsno="";
    private FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference productsRef, showDrug;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products);

        //mAuth= FirebaseAuth.getInstance();
        //currentUserId=mAuth.getCurrentUser().getUid();
        orderrsno=getIntent().getStringExtra("orderno");
        productId = getIntent().getStringExtra("productid");
        Phone= getIntent().getStringExtra("phone");
        userID=getIntent().getStringExtra("uid");
        //in the line below u need to pass userID mahali kwa currentuserId
        //cartlistRef= FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(userID).child("Products");
        showDrug = FirebaseDatabase.getInstance().getReference().child("ShowDrug").child(userID);
        recyclerView = findViewById(R.id.products_lists);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminProductsActivity.this,AdminOrders.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(showDrug, Cart.class)
                        .build();

        adapter =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {

                cartViewHolder.txtProductQuantity.setText("Quantity = "+cart.getQuantity());
                cartViewHolder.txtProductPrice.setText("Price = ksh "+cart.getPrice());
                cartViewHolder.txtProductName.setText(cart.getPname());

                cartViewHolder.Show_all_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminProductsActivity.this,CancelOrder.class);
                        intent.putExtra("orderss",orderrsno);
                        startActivity(intent);
                    }
                });

                cartViewHolder.Show_all_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Calendar calender = Calendar.getInstance();



                        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                        saveCurrentDate = currentDate.format(calender.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                        saveCurrentTime= currentTime.format((calender.getTime()));
                        DateTime = saveCurrentTime+saveCurrentDate;
                        final DatabaseReference ordersListRef = FirebaseDatabase.getInstance().getReference().child("CancelOrders").child(userID).child(DateTime);

                        final HashMap<String, Object> cartMap = new HashMap<>();
                        cartMap.put("pid", DateTime);
                        cartMap.put("pname", cart.getPname());
                        cartMap.put("price", cart.getPrice());
                        cartMap.put("date", saveCurrentDate);
                        cartMap.put("time", orderrsno);
                        cartMap.put("quantity", cart.getQuantity());
                        cartMap.put("discount", "");
                        ordersListRef.updateChildren(cartMap);

                        CharSequence options[] = new CharSequence[]
                                {

                                        "No","Yes"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder( AdminProductsActivity.this);
                        builder.setTitle("Cancel order");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent( AdminProductsActivity.this, AdminProductsActivity.class);
                                    intent.putExtra("pid", cart.getPid());
                                    startActivity(intent);

                                }
                                if (i==1) {
                                    final  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ShowDrug");
                                    databaseReference.child(userID)
                                            .child(cart.getPid())
                                            .removeValue();


                                }
                            }
                        });
                        builder.show();


                        int permissionCheck = ContextCompat.checkSelfPermission(AdminProductsActivity.this, Manifest.permission.SEND_SMS);

                        if (permissionCheck== PackageManager.PERMISSION_GRANTED){

                            MyMessage();
                        }
                        else
                        {
                            ActivityCompat.requestPermissions(AdminProductsActivity.this, new String[]{Manifest.permission.SEND_SMS},0);
                        }

                    }
                });

                cartViewHolder.Show_all_orders.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminProductsActivity.this, Sms.class);
                        intent.putExtra("Uid", userID);
                        intent.putExtra("enterPhone", Phone);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_product, parent, false);
                CartViewHolder cartViewHolder =new CartViewHolder(view);
                return cartViewHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }

    private void RemoveOrder(String uid)
    {
        final DatabaseReference ordersreference = FirebaseDatabase.getInstance().getReference().child("ShowDrug").child(userID).child(productId);

        ordersreference.child(uid).removeValue();

        Intent intent = new Intent( AdminProductsActivity.this, AdminProductsActivity.class);

        startActivity(intent);
    }

    private void MyMessage()
    {
        String phoneNumber = Phone;
        String Message = "Your Order have been cancelled\n\n\n Thank you for Choosing us:)";


            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, Message, null, null);

            //Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 0:
                if (grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    MyMessage();
                }
                else {
                    Toast.makeText(this,"You don't have permission", Toast.LENGTH_SHORT).show();
                }
        }
    }
}