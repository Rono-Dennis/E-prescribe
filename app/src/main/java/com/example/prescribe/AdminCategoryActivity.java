package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView albuterol,cialis,differin,cardizem;
    private  ImageView psychotropic,requip,benedik,trey;
    private  ImageView mccutcheon,freestocks,pina,filippos;
    private ImageView logoutbtn, checkOrders, Location, History, Chat,Message, ProductHistory, PaymentHistory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

    //    albuterol=findViewById(R.id.albuterol);
        //cialis=findViewById(R.id.cialis);
        //differin=findViewById(R.id.differin);
        //cardizem=findViewById(R.id.cardizem);

   //     psychotropic=findViewById(R.id.psychotropic);
        //requip=findViewById(R.id.requip);
       // benedik=findViewById(R.id.benedik);
        //trey=findViewById(R.id.trey);

//        mccutcheon=findViewById(R.id.mccutcheon);
        checkOrders = findViewById(R.id.check_orders);
        Location=findViewById(R.id.location);
        logoutbtn=findViewById(R.id.admin_logout_btn);
        Chat=findViewById(R.id.chat);
        History=findViewById(R.id.history);
        PaymentHistory=findViewById(R.id.payment_history);
        ProductHistory=findViewById(R.id.message);

        PaymentHistory.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(AdminCategoryActivity.this, HistoryPayment.class);
                 startActivity(intent);
             }
         });
         ProductHistory.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(AdminCategoryActivity.this, ActivityProductHistory.class);
                 startActivity(intent);
             }
         });

        checkOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminOrders.class);
                startActivity(intent);
            }
        });

        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminPage.class);
                startActivity(intent);
            }
        });

        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, Sms.class);
                startActivity(intent);
            }
        });

        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, UserHistory.class);
                startActivity(intent);
            }
        });



        mccutcheon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminPage.class);
                intent.putExtra("category", "mccutcheon");
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminCategoryActivity.this, Homepage.class);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==R.id.main_logout_option){
            Intent intent =  new Intent(AdminCategoryActivity.this, Homepage.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}