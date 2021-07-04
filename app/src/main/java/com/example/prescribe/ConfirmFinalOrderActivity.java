package com.example.prescribe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEditText, phoneEditText,Shippment_fee;
    private TextView cityEditText,addressEdiText, select;
    private Button confirmOrderBtn, sendbtn, Prceed;
    private Spinner spinner, spinner_parent,spinner_child;
    private  String totalAmount ="";
    private int shippedTotal = 0;
    private String saveCurrentDate, saveCurrentTime, DateTime,ordernum;
    FirebaseAuth mAuth;
    private DatabaseReference usereference;
    private  String CurrentUsername, CurrentuserPhone,CurrentuserEmail, Location, Address;
    ArrayList<String> arrayList_parent;
    ArrayAdapter<String> arrayAdapter_parent;
    ArrayList<String> arrayList_feeds,arrayList_vaccines,arrayList_medication;
    ArrayAdapter<String> arrayAdapter_child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total price");

        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText) findViewById(R.id.shippment_name);
        phoneEditText = (EditText) findViewById(R.id.shippment_phone_number);
        addressEdiText =  findViewById(R.id.Shippment_address);
        cityEditText=findViewById(R.id.Shippment_location);
        select=findViewById(R.id.selector);




        Prceed=findViewById(R.id.proceed);


        mAuth= FirebaseAuth.getInstance();
        usereference= FirebaseDatabase.getInstance().getReference().child("User");

        spinner_child = findViewById(R.id.spinner);


        arrayList_parent=new ArrayList<>();
        arrayList_parent.add("Select Location");
        arrayList_parent.add("Kieni East");
        arrayList_parent.add("Kieni West");
        arrayList_parent.add("Mathira East");
        arrayList_parent.add("Mathira West");
        arrayList_parent.add("Nyeri Central");
        arrayList_parent.add("Mukurweini");
        arrayList_parent.add("Tetu");
        arrayList_parent.add("Othaya");
        arrayAdapter_parent=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_parent);
        spinner_child.setAdapter(arrayAdapter_parent);




        spinner_child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==1){
                    cityEditText.setText("100");
                    addressEdiText.setText("");
                    select.setText("Kieni East");
                }
                if (position==2) {
                    cityEditText.setText("120");
                    addressEdiText.setText("Kieni West");
                    select.setText("Kieni West");
                }
                if (position==3) {
                    cityEditText.setText("150");
                    addressEdiText.setText("Mathira East");
                    select.setText("Mathira East");
                }
                if (position==4) {
                    cityEditText.setText("200");
                    addressEdiText.setText("Mathira West");
                    select.setText("Mathira West");
                }
                if (position==5) {
                    cityEditText.setText("100");
                    addressEdiText.setText("Nyeri Central");
                    select.setText("Nyeri Central");
                }
                if (position==6) {
                    cityEditText.setText("150");
                    addressEdiText.setText("Mukurweini");
                    select.setText("Mukurweini");
                }
                if (position==7) {
                    cityEditText.setText("200");
                    addressEdiText.setText("Tetu");
                    select.setText("Tetu");
                }
                if (position==8) {
                    cityEditText.setText("50");
                    addressEdiText.setText("Othaya");
                    select.setText("Othaya");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        List<Userr> userList = new ArrayList<>();
//        List<Userr> UserList1 = new ArrayList<>();
//        Userr user1 = new Userr("Kieni East", 50 );
//        userList.add(user1);
//        Userr user2 = new Userr("Kieni West", 100 );
//        userList.add(user2);
//        Userr user3 = new Userr("Mathira East", 100 );
//        userList.add(user3);
//        Userr user4 = new Userr("Mathira West", 150);
//        userList.add(user4);
//        Userr user5 = new Userr("Nyeri Central", 50);
//        userList.add(user5);
//        Userr user6 = new Userr("Mukurweini", 100);
//        userList.add(user6);
//        Userr user7 = new Userr("Tetu", 100);
//        userList.add(user7);
//        Userr user8 = new Userr("Othaya", 100);
//        userList.add(user8);
//        UserList1=userList;
//        ArrayAdapter<Userr> adapter = new ArrayAdapter<Userr>(this,
//                android.R.layout.simple_spinner_item, UserList1);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Userr user = (Userr) parent.getSelectedItem();
//                displayUserData(user);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        CurrentUsername=mAuth.getCurrentUser().getUid();


        Prceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proceed();

            }
        });
        usereference.child(CurrentUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    //User user = snapshot.getValue(User.class);


//
//                    userNameTextView.setText(user.email);
                    CurrentuserPhone=snapshot.child("phone").getValue().toString();
                    CurrentuserEmail=snapshot.child("name").getValue().toString();
                    Address=snapshot.child("address").getValue().toString();
                    Location=snapshot.child("location").getValue().toString();

                    phoneEditText.setText(CurrentuserPhone);
                    nameEditText.setText(CurrentuserEmail);
                    //addressEdiText.setText(Address);
                    //cityEditText.setText(Location);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayUserData();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ConfirmFinalOrderActivity.this, CartActivity.class);

        startActivity(intent);
    }

    private void proceed()
    {

        Intent intent = new Intent(ConfirmFinalOrderActivity.this, Payment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        startActivity(intent);
        finish();

    }



    private void displayUserData()
    {


        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEdiText.getText().toString();
        String city = cityEditText.getText().toString();
//        String fee = Shippment_fee.getText().toString();



        if (name.isEmpty()) {
            nameEditText.setError("FullName is required");
            nameEditText.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            phoneEditText.setError("PhoneNumber is required");
            phoneEditText.requestFocus();
            return;
        }
        if (address.isEmpty()) {
            addressEdiText.setError("Address is required");
            addressEdiText.requestFocus();
            return;
        }
        if (city.isEmpty()) {
            cityEditText.setError("Location is required");
            cityEditText.requestFocus();
            return;
        }else
        {
            confirm();
        }





    }

    private void confirm()
    {
        Calendar calender = Calendar.getInstance();



        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format((calender.getTime()));
        DateTime = saveCurrentTime+saveCurrentDate;

        CurrentUsername=mAuth.getCurrentUser().getUid();
        final DatabaseReference ordersListRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(DateTime);
        final DatabaseReference counter= FirebaseDatabase.getInstance().getReference().child("Depressants");
        final DatabaseReference userViewer = FirebaseDatabase.getInstance().getReference().child("UserView").child(CurrentUsername).child(DateTime);


        Random random = new Random();
        int value=random.nextInt(10000)+5;
        ordernum=String.valueOf(value);
        int shipped = Integer.parseInt(cityEditText.getText().toString());

        int shippedTotal = (Integer.valueOf(totalAmount) + Integer.valueOf (shipped));


        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("uid",CurrentUsername);
        orderMap.put("pid",cityEditText.getText().toString());
        orderMap.put("name", nameEditText.getText().toString());
        orderMap.put("phone", phoneEditText.getText().toString());
        orderMap.put("address", addressEdiText.getText().toString());
        orderMap.put("city", shippedTotal);
        orderMap.put("date", DateTime);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("orderNo", ordernum);

        final HashMap<String, Object> orderView = new HashMap<>();
        orderView.put("totalAmount", totalAmount);
        orderView.put("uid",CurrentUsername);
        orderView.put("pid",cityEditText.getText().toString());
        orderView.put("name", nameEditText.getText().toString());
        orderView.put("phone", phoneEditText.getText().toString());
        orderView.put("address", cityEditText.getText().toString());
        orderView.put("city", shippedTotal);
        orderView.put("date", DateTime);
        orderView.put("time", saveCurrentTime);
        orderMap.put("orderNo", ordernum);

        userViewer.updateChildren(orderView);

        ordersListRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {


                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(CurrentUsername)
                            .child("Antibiotics")
                            .removeValue();

                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(CurrentUsername)
                            .child("Hallucinogen")
                            .removeValue();

                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(CurrentUsername)
                            .child("PainKillers")
                            .removeValue();

                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(CurrentUsername)
                            .child("Products")
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){


                                        counter.child(CurrentUsername).removeValue();

                                        check(shippedTotal,totalAmount);



                                    }
                                }
                            });


                }
            }
        });
    }

    private void check(int shippedTotal, String totalAmount)
    {
        int shipped = Integer.parseInt(cityEditText.getText().toString());
        Toast.makeText(ConfirmFinalOrderActivity.this, "Your Order has been placed successfully", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(ConfirmFinalOrderActivity.this, UserPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("ToPay", String.valueOf(totalAmount));
        intent.putExtra("Amount",String.valueOf(shippedTotal));
        intent.putExtra("Shippment", String.valueOf(shipped));
        intent.putExtra("orderno", String.valueOf(ordernum));
        startActivity(intent);
        finish();
    }


     }



