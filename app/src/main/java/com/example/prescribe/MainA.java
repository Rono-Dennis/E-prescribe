package com.example.prescribe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prescribe.Model.User;

import java.util.ArrayList;
import java.util.List;

public class MainA extends AppCompatActivity {
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        spinner = findViewById(R.id.spinner);
        List<Userr> userList = new ArrayList<>();
        Userr user1 = new Userr("Kieni East", 50 );
        userList.add(user1);
        Userr user2 = new Userr("Kieni West", 100 );
        userList.add(user2);
        Userr user3 = new Userr("Mathira East", 100 );
        userList.add(user3);
        Userr user4 = new Userr("Mathira West", 150);
        userList.add(user4);
        Userr user5 = new Userr("Nyeri Central", 50);
        userList.add(user5);
        Userr user6 = new Userr("Mukurweini", 100);
        userList.add(user6);
        Userr user7 = new Userr("Tetu", 100);
        userList.add(user7);
        Userr user8 = new Userr("Othaya", 100);
        userList.add(user8);
        ArrayAdapter<Userr> adapter = new ArrayAdapter<Userr>(this,
                android.R.layout.simple_spinner_item, userList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Userr user = (Userr) parent.getSelectedItem();
                displayUserData(user);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void getSelectedUser(View v) {
        Userr user = (Userr) spinner.getSelectedItem();
        displayUserData(user);
    }
    private void displayUserData(Userr user) {
        String location = user.getLocation();
        int shippment = user.getShippment();

        String userData = "Location: " + location + "\nShippment: " + shippment;
        Toast.makeText(this, userData, Toast.LENGTH_LONG).show();
    }
}

