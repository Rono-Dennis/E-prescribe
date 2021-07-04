package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.prescribe.Model.Orders;
import com.example.prescribe.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private static int SPlASH_SCREEN = 5000;

    Animation toAnim, bottomAnim;
    ImageView image, image1;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private String ToPay ="", pay="",toShipped="", noorders="";
    private  String Amount ="", CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAuth=FirebaseAuth.getInstance();
        noorders=getIntent().getStringExtra("NoOrders");
        pay=getIntent().getStringExtra("topay");
        toShipped=getIntent().getStringExtra("shipped");
        ToPay = getIntent().getStringExtra("payement");

//        toAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
//        bottomAnim=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
//
//        image=findViewById(R.id.imageview);
//        image1=findViewById(R.id.slogan);
//
//        image.setAnimation(toAnim);
//        image1.setAnimation(bottomAnim);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(HomeActivity.this, Homepage.class);
//                startActivity(intent);
//                finish();
//            }
//        }, SPlASH_SCREEN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HomeActivity.this, UserPage.class);
        intent.putExtra("Amount",String.valueOf(ToPay));
        intent.putExtra("ToPay", String.valueOf(pay));
        intent.putExtra("Shippment", String.valueOf(toShipped));
        intent.putExtra("orderno", String.valueOf(noorders));
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CurrentUser=mAuth.getCurrentUser().getUid();
        final DatabaseReference ordersreference = FirebaseDatabase.getInstance().getReference().child("UserView").child(CurrentUser);

        FirebaseRecyclerOptions<Orders> options = new  FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(ordersreference, Orders.class)
                .build();

        FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull Orders orders) {
                orderViewHolder.Order_user_name.setText("Name: " + orders.getName());
                orderViewHolder.Order_phone_number.setText("Phone: " + orders.getPhone());
                orderViewHolder.Order_total_price.setText("DrugAmount: Ksh" + orders.getTotalAmount()+ "\n\nShippmentFee: Ksh" + orders.getPid());
                orderViewHolder.Order_address.setText("Location: " + orders.getAddress());
                orderViewHolder.Order_city.setText("TotalPayment: " + orders.getCity());
                orderViewHolder.Order_date.setText("Order at: " + orders.getDate());
                orderViewHolder.Order_time.setText("Time: " + orders.getTime());


                orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes", "No"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder( HomeActivity.this);
                        builder.setTitle("Please confirm that you've Shipped this order before you Delete");
                        builder.setMessage("Proceed to Delete");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (i == 0)
                                {
                                    String UID = getRef(which).getKey();

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
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view, parent, false);
                OrderViewHolder holder = new OrderViewHolder(view);
                return holder;
            }
        };



        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private void RemoveOrder(String uid)
    {
        final DatabaseReference ordersreference = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersreference.child(uid).removeValue();


    }
    }
