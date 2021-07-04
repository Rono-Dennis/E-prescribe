package com.example.prescribe;

import   androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ChatActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE =1;
    private FirebaseListAdapter<ChatMessage> adapter;
   private FirebaseListOptions<ChatMessage> options;
    RelativeLayout activity_chat;
    FloatingActionButton fab;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_chat, "You have been sign out", Snackbar.LENGTH_SHORT).show();
                   finish();
                }
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Snackbar.make(activity_chat, "Successfully signed in Welcome", Snackbar.LENGTH_SHORT).show();
             displayChatMessage();
            }
            else
            {
                Snackbar.make(activity_chat, "We couldn't sign you in please try again later", Snackbar.LENGTH_SHORT).show();
              finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //activity_chat = (RelativeLayout) findViewById(R.id.activity_chat);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  EditText input = findViewById(R.id.input_message);
//                FirebaseDatabase.getInstance().getReference().child("Chats").push().setValue(new ChatMessage(input.getText().toString(),
//                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
//                input.setText("");
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
          Snackbar.make(activity_chat, "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();

         displayChatMessage();
        }
        

    }

    private void displayChatMessage()
    {
    }

//    private void displayChatMessage() {
//        ListView listOfMessage = (ListView) findViewById(R.id.list_of_message);
//        //Suppose you want to retrieve "chats" in your Firebase DB:
//        final DatabaseReference query = FirebaseDatabase.getInstance().getReference().child("Chats");
////The error said the constructor expected FirebaseListOptions - here you create them:
//        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
//                .setQuery(query, ChatMessage.class)
//                .setLayout(R.layout.list_item)
//                .build();
//        //Finally you pass them to the constructor here:
//
//        FirebaseListAdapter<ChatMessage> adapter = new FirebaseListAdapter<ChatMessage>(options){
//            @Override
//            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {
//
//                TextView messageText, messageUser, messageTime;
//                messageText = findViewById(R.id.message_text);
//                messageUser = findViewById(R.id.message_user);
//                messageTime = findViewById(R.id.message_time);
//
//
//                messageText.setText(model.getMessageText());
//                messageUser.setText(model.getMessageUser());
//                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
//            }
//
//
//        };
//
////        FirebaseListAdapter<ChatMessage> adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
////                R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
////@Override
////            protected void populateView(View v, ChatMessage model, int position) {
////                TextView messageText = (TextView) v.findViewById(R.id.message_text);
////                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
////                TextView messageTime = (TextView) v.findViewById(R.id.message_time);
////                messageText.setText(model.getMessageText());
////                messageUser.setText(model.getMessageUser());
////                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
////                        model.getMessageTime()));
////            }
////        };
//
//        listOfMessage.setAdapter(adapter);
//    }
}