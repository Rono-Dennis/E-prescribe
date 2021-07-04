package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sms extends AppCompatActivity {
    EditText txt_pNumber, txt_message;
    Button send;
    private String ToPay ="", pay="",toShipped="", admin,noorders="", useree="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        txt_message = findViewById(R.id.txt_message);
        txt_pNumber = findViewById(R.id.phonenumber);

        useree=getIntent().getStringExtra("Uid");
        pay=getIntent().getStringExtra("topay");
        noorders=getIntent().getStringExtra("NoOrders");
        toShipped=getIntent().getStringExtra("shipped");
        ToPay = getIntent().getStringExtra("payement");
        send = findViewById(R.id.sent);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionCheck = ContextCompat.checkSelfPermission(Sms.this, Manifest.permission.SEND_SMS);

                if (permissionCheck== PackageManager.PERMISSION_GRANTED){

                    MyMessage();
                }
                else
                {
                    ActivityCompat.requestPermissions(Sms.this, new String[]{Manifest.permission.SEND_SMS},0);
                }

                Intent intent = new Intent(Sms.this, AdminProductsActivity.class);
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final DatabaseReference firedata = FirebaseDatabase.getInstance().getReference().child("Admins");
        firedata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    admin=snapshot.child("Phone").getValue().toString();
                    txt_pNumber.setText(admin);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Sms.this, AdminProductsActivity.class);
//        intent.putExtra("Amount",String.valueOf(ToPay));
//        intent.putExtra("ToPay", String.valueOf(pay));
//        intent.putExtra("Shippment", String.valueOf(toShipped));
        intent.putExtra("uid", useree);
        startActivity(intent);
    }



    private void MyMessage() {

        String phoneNumber = txt_pNumber.getText().toString().trim();
        String Message = txt_message.getText().toString().trim();

        if (txt_pNumber.getText().toString().equals("") || !txt_message.getText().toString().equals("")){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, Message, null, null);

            Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
            txt_message.setText("");
            txt_pNumber.setText("");
        } else {
            Toast.makeText(this, "Please Enter Number or Message", Toast.LENGTH_SHORT).show();
        }
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