package com.example.prescribe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.prescribe.Model.Cart;
import com.example.prescribe.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn, Calculate, pay;
    private  TextView txtTotalAmount, txtMsg1 ;
    private RecyclerView recyclerView;
    private int overTotalPrice = 0, Price = 0;
    private String Currentuser,CurrentUser;
    private FirebaseAuth firebaseAuth;
    //FirebaseAuth mAuth;
    private DatabaseReference usereference;

    MenuItem menuItem;
    int pendingNotification = 0;
    TextView badgeCounter;
    ImageView img;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        firebaseAuth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Calculate=findViewById(R.id.calculate);
        NextProcessBtn= findViewById(R.id.next);
        txtTotalAmount=findViewById(R.id.total_price);
        txtMsg1=findViewById(R.id.msg1);
//       pay=findViewById(R.id.calculate);

       txtTotalAmount.setText("Total Price = Ksh " + String.valueOf(overTotalPrice));

//        pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                txtTotalAmount.setText("Total Price = Ksh " + String.valueOf(overTotalPrice));
//            }
//        });



        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //txtTotalAmount.setText("Total Price = Ksh " + String.valueOf(overTotalPrice));

                Intent intent = new Intent( CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total price", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });

        CurrentUser=firebaseAuth.getCurrentUser().getUid();
        final  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("User View")
                .child(CurrentUser)
                .child("Products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
//                    Cart product = snapshot.getValue(Cart.class);
//                    int ProductionPrice = ((Integer.valueOf(product.getPrice()))) * Integer.valueOf(product.getQuantity());
//                    Price = Price + ProductionPrice;
//                    txtTotalAmount.setText("Total Price = Ksh"+ String.valueOf(Price));
                    try {


                        pendingNotification = (int) snapshot.getChildrenCount();

                        badgeCounter.setText(String.valueOf(pendingNotification));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CartActivity.this, HomeeActivity.class);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);

        menuItem=menu.findItem(R.id.nav_notifications);

        if (pendingNotification==0){
            menuItem.setActionView(null);
        }else
        {
            menuItem.setActionView(R.layout.notification_badge);

            View view=menuItem.getActionView();

            badgeCounter=view.findViewById(R.id.badge_counter);

            img=view.findViewById(R.id.imageBtn);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CartActivity.this, CartActivity.class);
                    //intent.putExtra("Total price", String.valueOf(overTotalPrice));
                    startActivity(intent);
                }
            });
            badgeCounter.setText(String.valueOf(pendingNotification));
        }
        return super.onCreateOptionsMenu(menu);

        //CurrentUser=firebaseAuth.getCurrentUser().getUid();


    }



    @Override
    protected void onStart() {
        super.onStart();





        Currentuser= firebaseAuth.getCurrentUser().getUid();


        checkorderState();

        final  DatabaseReference cartLIstRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("User View")
                .child(CurrentUser)
                .child("Products");

        final  DatabaseReference cartLIstReff = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("User View")
                .child(CurrentUser)
                .child("Antibiotics");

        final  DatabaseReference cartLIstRefff = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("User View")
                .child(CurrentUser)
                .child("Hallucinogen");
        final  DatabaseReference cartLIstReffff = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("User View")
                .child(CurrentUser)
                .child("PainKillers");
        final DatabaseReference counter= FirebaseDatabase.getInstance().getReference().child("Depressants").child(Currentuser);

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(counter

                        , Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {

                cartViewHolder.txtProductQuantity.setText("Quantity = "+cart.getQuantity());
                cartViewHolder.txtProductPrice.setText("Price = ksh "+cart.getPrice());
                cartViewHolder.txtProductName.setText(cart.getPname());

                int oneTypeProductionPrice = ((Integer.valueOf(cart.getPrice()))) * Integer.valueOf(cart.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductionPrice;
                txtTotalAmount.setText("Total Price = Ksh " + String.valueOf(overTotalPrice));
                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder( CartActivity.this);
                        builder.setTitle("CartOptions");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent( CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", cart.getPid());
                                    startActivity(intent);

                                }
                                if (i==1) {
                                    cartLIstRef.child("User View")
                                            .child(Currentuser)
                                            .child("Products")
                                            .child(cart.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        counter.child("Depressants").child(CurrentUser).removeValue();
                                                        cartLIstReff.child("Antibiotics").child(cart.getPid()).removeValue();
                                                        cartLIstRefff.child("Hallucinogen").child(cart.getPid()).removeValue();
                                                        cartLIstReffff.child("PainKillers").child(cart.getPid()).removeValue();
                                                        Toast.makeText( CartActivity.this, "item removed successfully.", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent( CartActivity.this, CartActivity.class);
                                                        txtTotalAmount.setText("");
                                                        intent.putExtra("pid", cart.getPid());
                                                        startActivity(intent);
                                                    }
                                                }
                                            });

                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout, viewGroup, false);
                CartViewHolder cartViewHolder =new CartViewHolder(view);
                return cartViewHolder;
            }


        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkorderState()
    {
//     DatabaseReference ordersRef;
//     ordersRef=FirebaseDatabase.getInstance().getReference().child("User View").child("Phone").child("Products").child(ca);
//
//     ordersRef.
    }
}