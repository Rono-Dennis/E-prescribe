package com.example.prescribe;



import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prescribe.ViewHolder.ProductViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;



/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

private View v;
private TextView tvNotif;
    private RecyclerView recyclerView;
private FirebaseAuth mAuth;
    private DatabaseReference myUsersDatabase;

    private String userId;
    EditText inputSearch;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_notification, container, false);
        mAuth=FirebaseAuth.getInstance();
        //myUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        myUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Products");
userId=mAuth.getCurrentUser().getUid();
        recyclerView=v.findViewById(R.id.recyclerViewMenu);
        inputSearch=v.findViewById(R.id.inputsearch);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);

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

        setupUploader();
    return v;
    }
    private void setupUploader() {
        myUsersDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {

                    String name=dataSnapshot.child("name").getValue().toString();

                    tvNotif.setText("Hello,  "+name+". Thank you for being part of Pharmacy, we look forward to make this place a better platform to sell your products as you maximize your profits . We guarantee you a free platform to sell all your products and new customers as well. Stay tuned to have more of our services in future.");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

       final DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");



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
                                Intent intent= new Intent(getContext(), ProductDetailsActivity.class);
                                intent.putExtra("pid",model.getPid());

                                startActivity(intent);
                            }
                        });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent= new Intent(getContext(), ProductDetailsActivity.class);
                                intent.putExtra("pid",model.getPid());

                                startActivity(intent);
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
