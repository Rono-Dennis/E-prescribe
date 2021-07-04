package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.prescribe.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class Homepage extends AppCompatActivity {
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ViewFlipper viewFlipper;
    private PopularAdapter popularAdapter;
    private List<Popular> popular;
    private Button button;
    EditText inputSearch;
    private ImageView hallucinogens, depressants, Antibiotic, painKiller;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //button=findViewById(R.id.)
        viewFlipper=findViewById(R.id.imgBanner);
        inputSearch=findViewById(R.id.inputsearchs);
        inputSearch.setOnKeyListener(null);
        textView=findViewById(R.id.category);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder passwordDialog = new AlertDialog.Builder(Homepage.this);
                passwordDialog.setTitle("You need to Login First before proceeding to view the Categories");
                passwordDialog.setMessage("Will you proceed?");
                passwordDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent(Homepage.this, login2Activity.class);
                            startActivity(intent);
                        }catch (Exception e){
                            Toast.makeText(Homepage.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                passwordDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordDialog.create().show();
            }
        });
//        hallucinogens=findViewById(R.id.Hallucinogens);
//        depressants=findViewById(R.id.Depressants);
//        Antibiotic=findViewById(R.id.antibiotic);
//        painKiller=findViewById(R.id.PainKiller);

//        hallucinogens.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Homepage.this, Login.class);
//                startActivity(intent);
//            }
//        });
//        depressants.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Homepage.this, login2Activity.class);
//                startActivity(intent);
//            }
//        });

        int sliders[]={
                R.drawable.acarbosa,R.drawable.acarbosee,R.drawable.aprovel, R.drawable.acemetophen, R.drawable.adcirca, R.drawable.adizem, R.drawable.advill, R.drawable.glucobay, R.drawable.opioid, R.drawable.proventil
        };

        for (int slide:sliders){
            bannerFlipper(slide);
        }






        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

//showPopularProducts();

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null)
                {
                    onStart();


                }
            }
        });


    }

//    private void showPopularProducts()
//    {
//        recyclerView=findViewById(R.id.recyclerViewMenu);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
//
//        popular= new ArrayList<>();
//        databaseReference=FirebaseDatabase.getInstance().getReference().child("PainKillers");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//              for (DataSnapshot postSnapshot:snapshot.getChildren())
//              {
//                  Popular populars = postSnapshot.getValue(Popular.class);
//                  popular.add(populars);
//              }
//              popularAdapter= new PopularAdapter(Homepage.this, popular);
//              recyclerView.setAdapter(popularAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//                Toast.makeText(Homepage.this,error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    public void bannerFlipper(int image){

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(this, android.R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.option, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
          super.onOptionsItemSelected(item);
          if (item.getItemId()==R.id.options){
              try {
                  Intent intent =  new Intent(Homepage.this, AdminCategoryActivity.class);
                  startActivity(intent);
              }catch (Exception e)
              {
                  e.printStackTrace();
              }

          }

          return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("pname").startAt(inputSearch.getText().toString()).endAt(inputSearch.getText().toString()+"\uf8ff"), Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = ksh " + model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                         holder.addCart.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 AlertDialog.Builder passwordDialog = new AlertDialog.Builder(Homepage.this);
                                 passwordDialog.setTitle("You need to Login First before proceeding to purchase the Product");
                                 passwordDialog.setMessage("Proceed to purchase?");
                                 passwordDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         try {
                                             Intent intent = new Intent(Homepage.this, Login.class);
                                             startActivity(intent);
                                         }catch (Exception e){
                                             Toast.makeText(Homepage.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                                         }
                                     }
                                 });
                                 passwordDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {

                                     }
                                 });
                                 passwordDialog.create().show();
                             }
                         });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder passwordDialog = new AlertDialog.Builder(Homepage.this);
                                passwordDialog.setTitle("You need to Login First before proceeding to purchase the Product");
                                passwordDialog.setMessage("Proceed to purchase product");
                                passwordDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            Intent intent = new Intent(Homepage.this, Login.class);
                                            startActivity(intent);
                                        }catch (Exception e){
                                            Toast.makeText(Homepage.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                passwordDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                passwordDialog.create().show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }
}