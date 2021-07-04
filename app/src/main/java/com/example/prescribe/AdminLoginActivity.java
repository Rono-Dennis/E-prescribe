package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prescribe.Model.Admins;
import com.example.prescribe.Model.Users;
import com.example.prescribe.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;

     String correctPhoneNumber = "0713234684";
     String correctPassword = "0713234684";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        loadingBar = new ProgressDialog(this);


        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);



        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final String number = InputPhoneNumber.getText().toString();
                final String password = InputPassword.getText().toString();
                if (TextUtils.isEmpty(number))
                {
                    InputPhoneNumber.setError("phone is required");
                    InputPhoneNumber.requestFocus();
                    return;

                }
                else if(number.length() < 10) {
                    InputPhoneNumber.setError("phone should be more be 10 characters");
                    InputPhoneNumber.requestFocus();
                    return;}
                else if (TextUtils.isEmpty(password))
                {
                    InputPassword.setError("password is required");
                    InputPassword.requestFocus();
                    return;
                }

                else if (password.length() < 6) {
                    InputPassword.setError("Password should be more than 6 characters");
                    InputPassword.requestFocus();
                    return;}

                else if (InputPhoneNumber.getText().toString().equals(correctPhoneNumber))
                {
                    if (InputPassword.getText().toString().equals(correctPassword))
                    {
                        try {
                            Intent intent = new Intent(AdminLoginActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                            Toast.makeText(AdminLoginActivity.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
//                    Intent intent = new Intent(AdminLoginActivity.this, AdminCategoryActivity.class);
//                    startActivity(intent);
//                    Toast.makeText(AdminLoginActivity.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AdminLoginActivity.this, "Phone number correct", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(AdminLoginActivity.this, "Please enter the right Password", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(AdminLoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AdminLoginActivity.this, "Fail to Login", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }







    }
