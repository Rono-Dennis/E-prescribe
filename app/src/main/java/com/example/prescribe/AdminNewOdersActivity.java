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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prescribe.Model.Cart;
import com.example.prescribe.Model.Orders;
import com.example.prescribe.Model.Payment;
import com.example.prescribe.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminNewOdersActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn, Calculate, pay, Show_all_orders;
    private  TextView txtTotalAmount, txtMsg1 ;
    private RecyclerView recyclerView;
    private int overTotalPrice = 0;

    private ListView listView;

    private TextView amountToBePaid, amountPaid, dateAndTime, usersId, phones, balanceRemain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_oders);

        amountToBePaid=findViewById(R.id.AmountToBePaid);
        amountPaid=findViewById(R.id.AmountPaid);
        dateAndTime=findViewById(R.id.DateAndTime);
        usersId=findViewById(R.id.UsersId);
        phones=findViewById(R.id.Phones);
        balanceRemain=findViewById(R.id.BalanceRemain);





        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Payment");

//
//        Order_user_name=findViewById(R.id.order_user_name);
//        Order_phone_number=findViewById(R.id.order_phone_number);
//        Order_total_price=findViewById(R.id.order_total_price);
//        Order_address=findViewById(R.id.order_address);
//        Order_city=findViewById(R.id.order_city);
//        Order_date=findViewById(R.id.order_date);
//        Order_time=findViewById(R.id.order_time);
//        Show_all_orders=findViewById(R.id.show_all_orders);
//
//        Show_all_orders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), AdminNewOdersActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        Fetchdetails();
//    }
//
//    private void Fetchdetails()
//    {
      final  DatabaseReference ordersreference = FirebaseDatabase.getInstance().getReference().child("Payment");

      ordersreference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if (snapshot.exists())
              {
                  Payment payment = snapshot.getValue(Payment.class);

                  amountToBePaid.setText(payment.getAmountToPay());
                amountPaid.setText(payment.getPaidAmount());
                dateAndTime.setText(payment.getDateTime());
                usersId.setText(payment.getUserId());
                phones.setText(payment.getPhoneNumber());
                balanceRemain.setText(payment.getBalance());

//                  Order_user_name.setText("Name: "+orders.getName());
//                  Order_phone_number.setText("Phone: "+orders.getPhone());
//                  Order_total_price.setText("TotalAmount: Ksh"+orders.getTotalAmount());
//                  Order_address.setText("Location: "+orders.getAddress());
//                  Order_city.setText("Location: "+orders.getCity());
//                  Order_date.setText("Order at: "+orders.getDate());
//                  Order_time.setText("Time: "+orders.getTime());
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
    }
}