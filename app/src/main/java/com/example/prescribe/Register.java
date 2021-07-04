package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.measite.minidns.record.A;

public class Register extends AppCompatActivity {
    private Button register;
    private EditText editTextFullname, editTextEmail, editTextPassword, Phone, location, address;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        userRef=FirebaseDatabase.getInstance().getReference();
        register =  findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = editTextFullname.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String phone = Phone.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();
                final String Location =location.getText().toString().trim();
                final String Address = address.getText().toString().trim();

                if (username.isEmpty()) {
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    return;
                } else if (email.isEmpty()) {
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    return;
                } else if (password.isEmpty()) {
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Please provide valid email");
                    editTextEmail.requestFocus();
                    return;

                } else if (password.length() < 6) {
                    editTextPassword.setError("Password should be more than 6 characters");
                    editTextPassword.requestFocus();
                    return;
                } else if (phone.isEmpty()) {
                    Phone.setError("phone is required");
                    Phone.requestFocus();
                    return;
                } else if (Address.isEmpty()) {
                    address.setError("Address is required");
                    address.requestFocus();
                    return;
                }
                else if (Location.isEmpty()) {
                    location.setError("Location is required");
                    location.requestFocus();
                    return;
                }
                else {

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.VISIBLE);
                                String currentUserId=mAuth.getCurrentUser().getUid();

                                /*RootRef.child("Users").child(currentUserId).setValue("");*/
                                HashMap<String, String> users=new HashMap<>();
                                users.put("uid", currentUserId);
                                users.put("location", Location);
                                users.put("address", Address);
                                users.put("name", username);
                                users.put("email", email);
                                users.put("phone", phone);
                                users.put("password",password);
                                userRef.child("User").child(currentUserId).setValue(users);
                                Toast.makeText(getApplicationContext(), "Account Created Successfully",Toast.LENGTH_SHORT).show();
                                //loadingbar.dismiss();
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(Register.this,login2Activity.class);
                                intent.putExtra("currentphone", phone);
                                startActivity(intent);
                                //startActivity(new Intent(getApplicationContext(),Login.class));

                            }
                            else{
                                String message=task.getException().toString();
                                Toast.makeText(getApplicationContext(), "Error : " + message, Toast.LENGTH_SHORT).show();
                                //loadingbar.dismiss();

                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });

                }
            }
        });
        //register.setOnClickListener(this);
        editTextFullname = (EditText) findViewById(R.id.fullname);
        editTextEmail    = (EditText) findViewById(R.id.email);
        Phone=findViewById(R.id.phone);
        editTextPassword = (EditText) findViewById(R.id.password);
        location=(EditText)findViewById(R.id.Location);
        address=(EditText)findViewById(R.id.Address);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                register();
                break;

        }

    }*/

//    private void register() {
//
//    }
//});

    //savedToDatabase(username,phone,password,email);

//            progressBar.setVisibility(View.VISIBLE);
//            User user=new User(username,email,phone,password);
//            userRef.push().setValue(user);
//            Toast.makeText(getApplicationContext(),"Registered success",Toast.LENGTH_LONG).show();
//            progressBar.setVisibility(View.GONE);
//        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    User user = new User(fullname, email);
//                    FirebaseDatabase.getInstance().getReference("Users")
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                Toast.makeText(Register.this, "User has been register successfully!", Toast.LENGTH_LONG).show();
//                                progressBar.setVisibility(View.GONE);
//                                startActivity(new Intent(Register.this, login2Activity.class));
//                            }else {
//                                Toast.makeText(Register.this, "Failed to register! try again!", Toast.LENGTH_LONG).show();
//                                progressBar.setVisibility(View.GONE);
//                            }
//                        }
//                    });
//
//                }else {
//                    Toast.makeText(Register.this, "Failed to register!", Toast.LENGTH_LONG).show();
//                    progressBar.setVisibility(View.GONE);
//                }
//            }
//        });
}
//}

//    private void savedToDatabase(String username, String phone, String password, String email) {
//
//
//        HashMap<String, Object> productMap = new HashMap<>();
//        productMap.put("pid", productRandomKey);
//        productMap.put("date", saveCurrentDate);
//        productMap.put("time", saveCurrentTime);
//        productMap.put("description", Description);
//        productMap.put("image", downloadImageUrl);
//        productMap.put("category", CategoryName);
//        productMap.put("price", Price);
//        productMap.put("pname", Pname);
//
//        ProductsRef.child(productRandomKey).updateChildren(productMap)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task)
//                    {
//                        if (task.isSuccessful())
//                        {
//                            Intent intent = new Intent(Register.this, AdminCategoryActivity.class);
//                            startActivity(intent);
//
//                            //loadingBar.dismiss();
//                            progressBar.setVisibility(View.GONE);
//                            Toast.makeText(Register.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
//                           // loadingBar.dismiss();
//                            progressBar.setVisibility(View.GONE);
//                            String message = task.getException().toString();
//                            Toast.makeText(Register.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

