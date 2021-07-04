package com.example.prescribe;

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

import com.example.prescribe.Model.Payment;
import com.example.prescribe.ViewHolder.HistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoryPayment extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    //myadpter myadpter;
   // public TextView amountToPay, phoneNumber, dateTime, userId, paidAmount, balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_payment);


        recyclerView=findViewById(R.id.history_payment);
        recyclerView.setHasFixedSize(true);

        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




 ;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HistoryPayment.this, AdminCategoryActivity.class);

        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference payHistory = FirebaseDatabase.getInstance().getReference().child("Payment");



        FirebaseRecyclerOptions<Payment> options = new FirebaseRecyclerOptions.Builder<Payment>()
                .setQuery(payHistory, Payment.class).build();

        FirebaseRecyclerAdapter<Payment, HistoryViewHolder> adapter = new FirebaseRecyclerAdapter<Payment, HistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i, @NonNull Payment payment) {
                historyViewHolder.amountToBePaid.setText("Amount To Pay: "+payment.getAmountToPay());
                historyViewHolder.amountPaid.setText("Paid Amount: "+payment.getPaidAmount());
                historyViewHolder.dateAndTime.setText("Payment Date: "+payment.getDateTime());
                historyViewHolder.usersId.setText("User ID: "+payment.getUserId());
                historyViewHolder.phones.setText("PhoneNumber: "+payment.getPhoneNumber());
                historyViewHolder.balanceRemain.setText("Balance: "+payment.getBalance()+ "\n\n Order No: "+ payment.getOrderNo());

            }

            @NonNull
            @Override
            public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout, parent, false);
                HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);
                return historyViewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}