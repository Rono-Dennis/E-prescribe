package com.example.prescribe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.freddygenicho.mpesa.stkpush.Mode;
import com.freddygenicho.mpesa.stkpush.api.response.STKPushResponse;
import com.freddygenicho.mpesa.stkpush.interfaces.STKListener;
import com.freddygenicho.mpesa.stkpush.interfaces.TokenListener;
import com.freddygenicho.mpesa.stkpush.model.Mpesa;
import com.freddygenicho.mpesa.stkpush.model.STKPush;
import com.freddygenicho.mpesa.stkpush.model.Token;
import com.freddygenicho.mpesa.stkpush.model.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Payment extends AppCompatActivity implements TokenListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private EditText phoneET, amountET;
    private SweetAlertDialog sweetAlertDialog;
    private Mpesa mpesa;
    private String ToPay ="", pay="", toShipped="",noorders="", order;
    private int overTotalPrice = 0, balance=0;
    private String saveCurrentDate, saveCurrentTime, DateTime, CurrentUsing,Contacts;
    private String phone_number;
    private String amount, balancing, pricing, TotalAmount;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        phoneET = findViewById(R.id.phoneET);
        amountET = findViewById(R.id.amountET);

        mAuth= FirebaseAuth.getInstance();

        pay=getIntent().getStringExtra("topay");
        noorders=getIntent().getStringExtra("NoOrders");
        toShipped=getIntent().getStringExtra("shipped");
        ToPay = getIntent().getStringExtra("payement");
        amountET.setText(ToPay);
        CurrentUsing=mAuth.getCurrentUser().getUid();


        final DatabaseReference usereference= FirebaseDatabase.getInstance().getReference().child("User").child(CurrentUsing);

        usereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
         if (snapshot.exists()){


    Contacts=snapshot.child("phone").getValue().toString();
    //totalAmount=snapshot.child("email").getValue().toString();
    phoneET.setText(Contacts);
    //userName.setText(CurrentuserEmail);

}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mpesa = new Mpesa(Config.CONSUMER_KEY, Config.CONSUMER_SECRET, Mode.SANDBOX);

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("Connecting to Safaricom");
        sweetAlertDialog.setContentText("Please wait...");
        sweetAlertDialog.setCancelable(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Payment.this, UserPage.class);
        intent.putExtra("Amount",String.valueOf(ToPay));
        intent.putExtra("ToPay", String.valueOf(pay));
        intent.putExtra("Shippment", String.valueOf(toShipped));
        intent.putExtra("orderno", String.valueOf(noorders));
        startActivity(intent);
    }

    public void startMpesa(View view) {

        phone_number = phoneET.getText().toString();
        amount = amountET.getText().toString();

        if (phone_number.isEmpty()) {
            Toast.makeText(Payment.this, "Phone Number is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount.isEmpty()) {
            Toast.makeText(Payment.this, "Amount is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone_number.isEmpty() && !amount.isEmpty()) {
            try {
                sweetAlertDialog.show();
                mpesa.getToken(this);
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "UnsupportedEncodingException: " + e.getLocalizedMessage());
            }
        } else {
            Toast.makeText(Payment.this, "Please make sure that phone number and amount is not empty ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTokenSuccess(Token token) {
        STKPush stkPush = new STKPush();
        stkPush.setBusinessShortCode(Config.BUSINESS_SHORT_CODE);
        stkPush.setPassword(STKPush.getPassword(Config.BUSINESS_SHORT_CODE, Config.PASSKEY, STKPush.getTimestamp()));
        stkPush.setTimestamp(STKPush.getTimestamp());
        stkPush.setTransactionType(Transaction.CUSTOMER_PAY_BILL_ONLINE);
        stkPush.setAmount(amount);
        stkPush.setPartyA(STKPush.sanitizePhoneNumber(phone_number));
        stkPush.setPartyB(Config.PARTYB);
        stkPush.setPhoneNumber(STKPush.sanitizePhoneNumber(phone_number));
        stkPush.setCallBackURL(Config.CALLBACKURL);
        stkPush.setAccountReference("myApp");
        stkPush.setTransactionDesc("some description");

        mpesa.startStkPush(token, stkPush, new STKListener() {
            @Override
            public void onResponse(STKPushResponse stkPushResponse) {
                Log.e(TAG, "onResponse: " + stkPushResponse.toJson(stkPushResponse));
                String message = "Please enter your pin to complete transaction";
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Transaction started");
                sweetAlertDialog.setContentText(message);



                CurrentUsing=mAuth.getCurrentUser().getUid();

                Calendar calender = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                saveCurrentDate = currentDate.format(calender.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime= currentTime.format((calender.getTime()));
                DateTime = saveCurrentTime+saveCurrentDate;

                int BalanceTransactions = ((Integer.valueOf(ToPay))) - Integer.valueOf(amount);
                overTotalPrice = BalanceTransactions;
                TotalAmount= String.valueOf(amount);
                pricing = String.valueOf(ToPay);
                order=String.valueOf(noorders);
                balancing = String.valueOf(overTotalPrice);
                final DatabaseReference paymentRef = FirebaseDatabase.getInstance().getReference().child("Payment").child(DateTime);
                final DatabaseReference payment = FirebaseDatabase.getInstance().getReference().child("Report").child(CurrentUsing).child(DateTime);

                final HashMap<String, Object> pay = new HashMap<>();
                pay.put("paidAmount",TotalAmount);
                pay.put("amountToPay",pricing);
                pay.put("balance",balancing);
                pay.put("phoneNumber", phoneET.getText().toString());
                pay.put("dateTime", DateTime);
                pay.put("userId",CurrentUsing);
                pay.put("orderNo",order);

                paymentRef.updateChildren(pay);
                payment.updateChildren(pay);

            }




            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "stk onError: " + throwable.getMessage());
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Error");
                sweetAlertDialog.setContentText(throwable.getMessage());
            }
        });
    }

    @Override
    public void OnTokenError(Throwable throwable) {
        Log.e(TAG, "mpesa Error: " + throwable.getMessage());
        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("Error");
        sweetAlertDialog.setContentText(throwable.getMessage());
    }
}
