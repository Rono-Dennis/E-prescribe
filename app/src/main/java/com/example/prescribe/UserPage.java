  package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prescribe.Model.Orders;
import com.example.prescribe.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserPage extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    private ImageView mccutcheon,freestocks,pina,filippos;
    private ImageView logoutbtn, checkOrders, Location, History, Chat;
    private  String amount ="", CurrentUser, shippment="", topPay="", using, payment, shippe, ordersno="", current;
    private Button payer;
    private FirebaseAuth mAuth;
    private int shippedTotal = 0;
    private TextView pay, shipped, DrugAmount;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MenuItem menuItem;
    int pendingNotification = 0;
    TextView badgeCounter, txtTotalAmount;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);



        Location=findViewById(R.id.location);
        logoutbtn=findViewById(R.id.admin_logout_btn);
        Chat=findViewById(R.id.chat);
        History=findViewById(R.id.history);
        pay=findViewById(R.id.pay);
        shipped=findViewById(R.id.shippment);
        DrugAmount=findViewById(R.id.DrugAmount);
        mAuth=FirebaseAuth.getInstance();
        swipeRefreshLayout=findViewById(R.id.refreher);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                badgeCounter.setText(String.valueOf(pendingNotification));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        context();


        amount=getIntent().getStringExtra("Amount");
        shippment=getIntent().getStringExtra("Shippment");
        topPay=getIntent().getStringExtra("ToPay");
        ordersno=getIntent().getStringExtra("orderno");


      pay.setText("Amount to pay =       Ksh "+ amount);
      shipped.setText("Shippment Amount= Ksh"+shippment);
      DrugAmount.setText("Drug Amount=   Ksh"+topPay);

        using=mAuth.getCurrentUser().getUid();


        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this, HomeActivity.class);
                intent.putExtra("payement", String.valueOf(amount));
                intent.putExtra("topay", String.valueOf(topPay));
                intent.putExtra("shipped", String.valueOf(shippment));
                intent.putExtra("NoOrders",String.valueOf(ordersno));
                startActivity(intent);
            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this, pay.class);
                intent.putExtra("payement", String.valueOf(amount));
                intent.putExtra("topay", String.valueOf(topPay));
                intent.putExtra("shipped", String.valueOf(shippment));
                intent.putExtra("NoOrders",String.valueOf(ordersno));
                startActivity(intent);
            }
        });

        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this, SmsActivity.class);
                intent.putExtra("payement", String.valueOf(amount));
                intent.putExtra("topay", String.valueOf(topPay));
                intent.putExtra("shipped", String.valueOf(shippment));
                intent.putExtra("NoOrders",String.valueOf(ordersno));
                startActivity(intent);
            }
        });

        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this, Payment.class);
                intent.putExtra("payement", String.valueOf(amount));
                intent.putExtra("topay", String.valueOf(topPay));
                intent.putExtra("shipped", String.valueOf(shippment));
                intent.putExtra("NoOrders",String.valueOf(ordersno));
                startActivity(intent);
            }
        });
databaseReference = FirebaseDatabase.getInstance().getReference().child("CancelOrders");
        current = mAuth.getCurrentUser().getUid();
databaseReference.child(current).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists())
        {
            try
            {
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
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.user_menu, menu);
        getMenuInflater().inflate(R.menu.notify, menu);

        menuItem=menu.findItem(R.id.nav_notifications);

        if (pendingNotification==1){
            menuItem.setActionView(pendingNotification);
        }else
        {
            menuItem.setActionView(R.layout.notification_badge);

            View view=menuItem.getActionView();

            badgeCounter=view.findViewById(R.id.badge_counter);

            img=view.findViewById(R.id.imageBtn);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserPage.this, Cancel.class);
                    startActivity(intent);
                }
            });
            badgeCounter.setText(String.valueOf(pendingNotification));
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserPage.this, HomeeActivity.class);

        startActivity(intent);
    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==R.id.user_logout_option){
            FirebaseUser currentUser=mAuth.getCurrentUser();
            mAuth.signOut();
            Intent intent =  new Intent(UserPage.this, Homepage.class);
            intent.putExtra("payement", String.valueOf(amount));
            intent.putExtra("topay", String.valueOf(topPay));
            intent.putExtra("shipped", String.valueOf(shippment));
            intent.putExtra("NoOrders",String.valueOf(ordersno));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}