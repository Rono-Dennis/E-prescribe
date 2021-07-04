package com.example.prescribe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

public class Welcome extends AppCompatActivity {
    Button button1, button2;
    CircleMenu circleMenu;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);
        relativeLayout=(RelativeLayout) findViewById(R.id.relative);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.icon_menu, R.mipmap.icon_cancel)
                .addSubMenu(Color.parseColor("#258CFF"), R.mipmap.icon_home)
                .addSubMenu(Color.parseColor("#30A400"), R.mipmap.icon_search)
                .addSubMenu(Color.parseColor("#FF4B32"), R.mipmap.icon_notify)
                .addSubMenu(Color.parseColor("#8A39FF"), R.mipmap.icon_setting)
                .addSubMenu(Color.parseColor("#FF6A00"), R.mipmap.icon_gps)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index)
                    {
                        switch (index)
                        {
                            case 0:
                                Intent intent= new Intent(Welcome.this, AdminLoginActivity.class);
                                startActivity(intent);
                                relativeLayout.setBackgroundColor(Color.parseColor("#FF6A00"));
                                break;
                            case 1:
                                Intent intentt= new Intent(Welcome.this, Homepage.class);
                                startActivity(intentt);
                                relativeLayout.setBackgroundColor(Color.parseColor("#8A39FF"));
                                break;
                            case 2:
                                Intent intennt= new Intent(Welcome.this, HomeActivity.class);
                                startActivity(intennt);
                                relativeLayout.setBackgroundColor(Color.parseColor("#FF4B32"));
                                break;
                            case 3:
                                Intent inteent= new Intent(Welcome.this, login2Activity.class);
                                startActivity(inteent);
                                relativeLayout.setBackgroundColor(Color.parseColor("#30A400"));
                                break;
                            case 4:
                                Intent inttent= new Intent(Welcome.this, Register.class);
                                startActivity(inttent);
                                relativeLayout.setBackgroundColor(Color.parseColor("#FF6A00"));
                                break;
                        }
                    }

                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened() {}

            @Override
            public void onMenuClosed() {}

        });

//        button1=findViewById(R.id.register1);
//        button2=findViewById(R.id.register2);

//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),(login2Activity.class)));
//            }
//        });
//
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),(Login.class)));
//            }
//        });
    }
}