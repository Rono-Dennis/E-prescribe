package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.prescribe.Model.Payment;
import com.example.prescribe.ViewHolder.HistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CancelOrder extends AppCompatActivity {
    private String noOrders="";
    EditText inputSearch;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);
        inputSearch=findViewById(R.id.inputsearchs);
        recyclerView=findViewById(R.id.history_payment);
        recyclerView.setHasFixedSize(true);

        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        noOrders= getIntent().getStringExtra("orderss");
        inputSearch.setText(noOrders);

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
    protected void onStart() {
        super.onStart();

        final DatabaseReference payHistory = FirebaseDatabase.getInstance().getReference().child("Payment");



        FirebaseRecyclerOptions<com.example.prescribe.Model.Payment> options = new FirebaseRecyclerOptions.Builder<com.example.prescribe.Model.Payment>()
                .setQuery(payHistory.orderByChild("orderNo").startAt(inputSearch.getText().toString()).endAt(inputSearch.getText().toString()+"\uf8ff"), com.example.prescribe.Model.Payment.class).build();

        FirebaseRecyclerAdapter<com.example.prescribe.Model.Payment, HistoryViewHolder> adapter = new FirebaseRecyclerAdapter<com.example.prescribe.Model.Payment, HistoryViewHolder>(options) {
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