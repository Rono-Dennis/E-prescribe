package com.example.prescribe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prescribe.Model.Payment;
import com.example.prescribe.ViewHolder.HistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.graphics.Typeface.DEFAULT;
import static android.graphics.Typeface.SANS_SERIF;

public class pay extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private String ToPay ="", pay="", toShipped="",noorders="";
    private String CurrentUser,saveCurrentDate,saveCurrentTime,DateTime, user,Randomnum;
    //myadpter myadpter;
    Bitmap bmp,scaledbmp;
    int pageWidth=1200;
    Date dateObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        mAuth=FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        recyclerView=(RecyclerView) findViewById(R.id.history_payment);
       recyclerView.setHasFixedSize(false);

        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        pay=getIntent().getStringExtra("topay");
        noorders=getIntent().getStringExtra("NoOrders");
        toShipped=getIntent().getStringExtra("shipped");
        ToPay = getIntent().getStringExtra("payement");

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.otherimage);
        scaledbmp = Bitmap.createScaledBitmap(bmp,1200,518, false);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(pay.this, UserPage.class);
        intent.putExtra("Amount",String.valueOf(ToPay));
        intent.putExtra("ToPay", String.valueOf(pay));
        intent.putExtra("Shippment", String.valueOf(toShipped));
        intent.putExtra("orderno", String.valueOf(noorders));
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();


        final DatabaseReference payHistory = FirebaseDatabase.getInstance().getReference().child("Report").child(CurrentUser);
        final DatabaseReference using = FirebaseDatabase.getInstance().getReference().child("User").child(CurrentUser);
        //final DatabaseReference payHistory = FirebaseDatabase.getInstance().getReference().child("Payment");
        using.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    user=snapshot.child("name").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseRecyclerOptions<Payment> options = new FirebaseRecyclerOptions.Builder<Payment>()
                .setQuery(payHistory, Payment.class).build();

        FirebaseRecyclerAdapter<Payment, HistoryViewHolder> adapter = new FirebaseRecyclerAdapter<com.example.prescribe.Model.Payment, HistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i, @NonNull Payment payment) {
                historyViewHolder.amountToBePaid.setText("Amount To Pay: "+payment.getAmountToPay());
                historyViewHolder.amountPaid.setText("Paid Amount: "+payment.getPaidAmount());
                historyViewHolder.dateAndTime.setText("Payment Date: "+payment.getDateTime());
                historyViewHolder.usersId.setText("User ID: "+payment.getUserId());
                historyViewHolder.phones.setText("PhoneNumber:  "+payment.getPhoneNumber());
                historyViewHolder.balanceRemain.setText("Balance"+payment.getBalance());

                historyViewHolder.Show_all_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calender = Calendar.getInstance();

                        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                        saveCurrentDate = currentDate.format(calender.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                        saveCurrentTime= currentTime.format((calender.getTime()));
                        DateTime = saveCurrentTime+saveCurrentDate;
                        Random random = new Random();
                        int val= random.nextInt(10000)+5;
                        Randomnum=String.valueOf(val);
                        PdfDocument myPdfDocument=new PdfDocument();
                        Paint myPaint=new Paint();
                        Paint titlepaint=new Paint();
                        PdfDocument.PageInfo myPageInfo1=new PdfDocument.PageInfo.Builder(1200,2010,1).create();
                        PdfDocument.Page myPage1=myPdfDocument.startPage(myPageInfo1);
                        Canvas canvas=myPage1.getCanvas();
                        canvas.drawBitmap(scaledbmp,0,0,myPaint);

                        titlepaint.setTextAlign(Paint.Align.CENTER);
                        titlepaint.setTypeface(Typeface.create(DEFAULT,Typeface.BOLD));
                        titlepaint.setTextSize(100);
//                        canvas.drawText("E-Prescribe Home",pageWidth/2,460,titlepaint);
                        titlepaint.setColor(Color.BLUE);

                        myPaint.setTextSize(30f);
                        myPaint.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText("Call: 0713 234 684",1160,40,myPaint);
                        canvas.drawText("0798 597 556",1160 ,80,myPaint);

                        titlepaint.setTextAlign(Paint.Align.CENTER);
                        titlepaint.setTypeface(Typeface.create(DEFAULT,Typeface.BOLD_ITALIC));
                        titlepaint.setTextSize(80);
//                        canvas.drawText("Invoice",pageWidth/2,520,titlepaint);
                        titlepaint.setColor(Color.BLACK);

                        myPaint.setTextAlign(Paint.Align.LEFT);
                        titlepaint.setTextSize(80);
                        myPaint.setColor(Color.BLACK);
                        canvas.drawText("Customer Name:"+user,20,590,myPaint);
                        canvas.drawText("Customer Phone Number:"+payment.getPhoneNumber(),20,640,myPaint);
                        canvas.drawText("Required Amount: "+payment.getAmountToPay(),20,690,myPaint);
                        canvas.drawText("Paid Amount: "+payment.getPaidAmount(),20,740,myPaint);
                        canvas.drawText("Payment Date: "+payment.getDateTime(),20,790,myPaint);

                        myPaint.setTextAlign(Paint.Align.RIGHT);
                        titlepaint.setTextSize(100);
                        canvas.drawText("Invoice No.: "+Randomnum,pageWidth-20,590,myPaint);


                        // canvas.drawText("Date "+dateFormart.format(dateObj),-20,640,myPaint);

                        canvas.drawText("Date "+saveCurrentDate,-20,640,myPaint);


                        canvas.drawText("Time "+saveCurrentTime,-20,690,myPaint);

                        myPaint.setStyle(Paint.Style.STROKE);
                        myPaint.setStrokeWidth(2);
                        canvas.drawRect(20,790,pageWidth-20,860,myPaint);

                        myPaint.setTextAlign(Paint.Align.LEFT);
                        myPaint.setStyle(Paint.Style.FILL);
                        canvas.drawText("R.NO.",40,830,myPaint);
                        canvas.drawText("P.Number",200,830,myPaint);
                        canvas.drawText("Name",400,830,myPaint);
                        canvas.drawText("PaidAmount",600,830,myPaint);
                        canvas.drawText("Amount",870,830,myPaint);
                        canvas.drawText("Balance.",1000,830,myPaint);

                        canvas.drawLine(180,800,180,840,myPaint);
                        canvas.drawLine(380,800,380,840,myPaint);
                        canvas.drawLine(580,800,580,840,myPaint);
                        canvas.drawLine(850,800,850,840,myPaint);
                        canvas.drawLine(980,800,980,840,myPaint);

                        canvas.drawText("1.",40,950,myPaint);
                        canvas.drawText(payment.getPhoneNumber(),200,950,myPaint);
                        canvas.drawText(user,400,950,myPaint);
                        canvas.drawText(payment.getAmountToPay(),600,950,myPaint);
                        canvas.drawText("10",870,950,myPaint);
                        canvas.drawText(payment.getPaidAmount(),1000,950,myPaint);

                        canvas.drawLine(580,1100,pageWidth-20,1100,myPaint);
                        canvas.drawText("Amount Paid",700,1150,myPaint);
                        canvas.drawText(":",870,1150,myPaint);
                        myPaint.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText(payment.getPaidAmount(),1000,1150,myPaint);

                        myPaint.setTextAlign(Paint.Align.LEFT);
                        canvas.drawText("Balance",700,1250,myPaint);
                        canvas.drawText(":",870,1250,myPaint);
                        myPaint.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText(payment.getBalance(),1000,1250,myPaint);
                        myPaint.setTextAlign(Paint.Align.LEFT);

                        myPaint.setColor(Color.BLUE);
                        canvas.drawRect(580,1300,pageWidth-20,1400,myPaint);

                        myPaint.setColor(Color.WHITE);
                        myPaint.setTextSize(50f);
                        myPaint.setTextAlign(Paint.Align.LEFT);
                        canvas.drawText("STATUS :  ",700,1385,myPaint);
                        myPaint.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText("APPROVED",pageWidth-40,1385,myPaint);


                        myPaint.setTextAlign(Paint.Align.LEFT);
                        myPaint.setTextSize(40f);
                        myPaint.setTypeface(SANS_SERIF);
                        myPaint.setColor(Color.BLACK);
                        canvas.drawText("Thank you for trusting our services.",40,1500,myPaint);









                        myPdfDocument.finishPage(myPage1);
                        File file=new File(Environment.getExternalStorageDirectory(),"/"+"Invoice"+Randomnum+".pdf");
                        try{
                            myPdfDocument.writeTo(new FileOutputStream(file));
                            Toast.makeText(pay.this,"file saved successfully", Toast.LENGTH_LONG).show();

                        }
                        catch(IOException e){
                            e.printStackTrace();
                            Toast.makeText(pay.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        myPdfDocument.close();

                    }
                });

            }

            @NonNull
            @Override
            public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_layout, parent, false);
                HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);
                return historyViewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    }

