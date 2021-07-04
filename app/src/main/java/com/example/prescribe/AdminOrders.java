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
import android.widget.Button;

import com.example.prescribe.Model.Orders;
import com.example.prescribe.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminOrders extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    //private Button Show_all_orders;
    private String CurrentName;
    private FirebaseAuth firebaseAuth;
    private int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        recyclerView=findViewById(R.id.order_list);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Show_all_orders=findViewById(R.id.show_all_orders);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference ordersreference = FirebaseDatabase.getInstance().getReference().child("Orders");

        FirebaseRecyclerOptions<Orders> options = new  FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(ordersreference, Orders.class)
                .build();

        FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull Orders orders) {
                orderViewHolder.Order_user_name.setText("Name: " + orders.getName());
                orderViewHolder.Order_phone_number.setText("Phone: " + orders.getPhone());
                orderViewHolder.Order_total_price.setText("\nTotalAmount: Ksh" + orders.getTotalAmount() + "\n\nShippment fee: ksh" + orders.getPid());
                orderViewHolder.Order_address.setText("\nLocation: " + orders.getAddress());
                orderViewHolder.Order_city.setText("\nShippmentTotal: " + orders.getCity());
                orderViewHolder.Order_date.setText("\nOrder at: " + orders.getDate());
                orderViewHolder.Order_time.setText("\nTime: " + orders.getTime());


                int oneTypeProductionPrice = ((Integer.valueOf(orders.getTotalAmount()))) + Integer.valueOf(orders.getCity());
                overTotalPrice = overTotalPrice + oneTypeProductionPrice;

                orderViewHolder.Show_all_orders.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //String uID = getRef(i).getKey();

                        Intent intent = new Intent(AdminOrders.this, AdminProductsActivity.class);
                        intent.putExtra("uid", orders.getUid());
                        intent.putExtra("phone", orders.getPhone());
                        intent.putExtra("productid",orders.getDate());
                        intent.putExtra("orderno",orders.getOrderNo());
                        startActivity(intent);
                    }
                });

                orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes", "No"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder( AdminOrders.this);
                        builder.setTitle("Please confirm that you've Shipped this order before you Delete");
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                OrderViewHolder holder = new OrderViewHolder(view);
                return holder;
            }
        };



        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminOrders.this, AdminCategoryActivity.class);

        startActivity(intent);
    }

    private void RemoveOrder(String uid)
    {
        final DatabaseReference ordersreference = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersreference.child(uid).removeValue();

        Intent intent = new Intent( AdminOrders.this, AdminOrders.class);

        startActivity(intent);
    }
}