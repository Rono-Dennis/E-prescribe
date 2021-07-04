package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class ProductsDetailsActivity2 extends AppCompatActivity {
    private String saveCurrentDate, saveCurrentTime;
    private FloatingActionButton addToCartBtn;
    private Button addToCartButton, Buy_now;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "", state="Normal", CurrentUser;


    private  String CurrentUsername, CurrentuserPhone;

    FirebaseAuth mAuth;

    private DatabaseReference useref;
    // FirebaseAuth mAuth;
    private DatabaseReference usereference;

    MenuItem menuItem;
    int pendingNotification = 0;
    TextView badgeCounter;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details1);


        mAuth= FirebaseAuth.getInstance();
//        CurrentuserPhone= mAuth.getCurrentUser().getUid();
//
//        useref= FirebaseDatabase.getInstance().getReference().child("User").child(CurrentuserPhone);

        productID = getIntent().getStringExtra("pid");

        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);

        //addToCartBtn = (FloatingActionButton) findViewById(R.id.add_product_to_cart_btn);
        numberButton= (ElegantNumberButton) findViewById(R.id.number_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription= (TextView) findViewById(R.id.product_description_deails);
        productPrice= (TextView) findViewById(R.id.product_price_details);
        Buy_now=findViewById(R.id.buy_now);

        Paper.init(this);

        getProductDeatils(productID);

        Buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentUser= mAuth.getCurrentUser().getUid();


                Calendar calender = Calendar.getInstance();



                SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                saveCurrentDate = currentDate.format(calender.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime= currentTime.format((calender.getTime()));

                final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
                final DatabaseReference counter= FirebaseDatabase.getInstance().getReference().child("Depressants");
                final  DatabaseReference showDrug = FirebaseDatabase.getInstance().getReference().child("ShowDrug").child(CurrentUser);

                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("pid", productID);
                cartMap.put("pname", productName.getText().toString());
                cartMap.put("price", productPrice.getText().toString());
                cartMap.put("date", saveCurrentDate);
                cartMap.put("time", saveCurrentTime);
                cartMap.put("quantity", numberButton.getNumber());
                cartMap.put("discount", "");

                cartListRef.child("User View").child(CurrentUser)
                        .child("PainKillers").child(productID)
                        .updateChildren(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    cartListRef.child("Admin View").child(CurrentUser)
                                            .child("PainKillers").child(productID)
                                            .updateChildren(cartMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    counter.child(CurrentUser).child(productID).updateChildren(cartMap);
                                                    showDrug.child(productID).updateChildren(cartMap);
                                                    Toast.makeText( ProductsDetailsActivity2.this,"Buy now.", Toast.LENGTH_LONG).show();

                                                    Intent intent = new Intent( ProductsDetailsActivity2.this, CartActivity.class);
                                                    startActivity(intent);
                                                }

                                            });

                                }
                            }
                        });
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductsDetailsActivity2.this,"You can still purchase with us",Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingToCartList();
                }
            }
        });

//        getPhoneInfo();

        CurrentUser=mAuth.getCurrentUser().getUid();
        final  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("User View")
                .child(CurrentUser)
                .child("PainKillers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    try {


                        pendingNotification = (int) snapshot.getChildrenCount();

                        badgeCounter.setText(String.valueOf(pendingNotification));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProductsDetailsActivity2.this, HomeeActivity.class);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_notification, menu);

        menuItem=menu.findItem(R.id.nav_notifications);

        if (pendingNotification==0){
            menuItem.setActionView(null);
        }else
        {
            menuItem.setActionView(R.layout.notification_badge);

            View view=menuItem.getActionView();

            badgeCounter=view.findViewById(R.id.badge_counter);

            img=view.findViewById(R.id.imageBtn);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProductsDetailsActivity2.this, CartActivity.class);
                    startActivity(intent);
                }
            });
            badgeCounter.setText(String.valueOf(pendingNotification));
        }
        return super.onCreateOptionsMenu(menu);
    }
    //    private void getPhoneInfo()
//    {
//        useref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists())
//                {
//                    CurrentuserPhone=snapshot.child("Phone").getValue().toString();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


    @Override
    protected void onStart() {
        super.onStart();
        checkorderState();
    }

    private void addingToCartList()
    {
        CurrentUser= mAuth.getCurrentUser().getUid();


        Calendar calender = Calendar.getInstance();



        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format((calender.getTime()));

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final DatabaseReference counter= FirebaseDatabase.getInstance().getReference().child("Depressants");
        final  DatabaseReference showDrug = FirebaseDatabase.getInstance().getReference().child("ShowDrug").child(CurrentUser);

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(CurrentUser)
                .child("PainKillers").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(CurrentUser)
                                    .child("PainKillers").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            counter.child(CurrentUser).child(productID).updateChildren(cartMap);
                                            showDrug.child(productID).updateChildren(cartMap);
                                            Toast.makeText( ProductsDetailsActivity2.this,"Added To Cart.", Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent( ProductsDetailsActivity2.this, HomeeActivity.class);
                                            startActivity(intent);
                                        }

                                    });

                        }
                    }
                });

    }

    private void getProductDeatils(String productID)
    {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("PainKillers");

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Products products = snapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkorderState()
    {
        DatabaseReference ordersRef;
        ordersRef=FirebaseDatabase.getInstance().getReference().child("Orders").child("Phone");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String shippingState = snapshot.child("state").getValue().toString();


                    if (shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";
                    }
                    else if (shippingState.equals("not shipped"))
                    {
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}