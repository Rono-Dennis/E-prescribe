package com.example.prescribe;



import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import com.example.prescribe.R;
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
public class Antibiotics extends Fragment {

    View v;
    private RecyclerView cartList, ProductsLists;
    private DatabaseReference myCartDatabase;
    private FirebaseAuth mAuth;
    private String userId;
    private DatabaseReference myUsersDatabase;
    private int CALL_PERMISSION_CODE=345;
    EditText inputSearch;


    public Antibiotics() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_antibiotics, container, false);
        cartList=v.findViewById(R.id.productsList);
        inputSearch=v.findViewById(R.id.inputsearch);

        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();




        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1);
        cartList.setLayoutManager(gridLayoutManager);


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


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
       final DatabaseReference myCartDatabase= FirebaseDatabase.getInstance().getReference().child("Antibiotics");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(myCartDatabase.orderByChild("pname").startAt(inputSearch.getText().toString()).endAt(inputSearch.getText().toString()+"\uf8ff"), Products.class)
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
                                Intent intent= new Intent(getContext(), ProductsDetailsActivity3.class);
                                intent.putExtra("pid",model.getPid());

                                startActivity(intent);
                            }
                        });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent= new Intent(getContext(), ProductsDetailsActivity3.class);
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




        cartList.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

    }

    private void dial(String phone) {

        Intent callIntent=new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phone));
        startActivity(callIntent);
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder{
        private ImageView cart_product_imageView;
        private TextView cart_tvName,cart_tvPrice,cart_tvQty,tvCounty,tvSuCounty,tvProductUploader;
        private Button btnCall;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            cart_product_imageView=itemView.findViewById(R.id.cart_product_image_view);
            cart_tvName=itemView.findViewById(R.id.cart_name_tv);
            cart_tvPrice=itemView.findViewById(R.id.cart_price_tv);
            cart_tvQty=itemView.findViewById(R.id.cart_qty_tv);
            tvCounty=itemView.findViewById(R.id.cart_county);
            tvSuCounty=itemView.findViewById(R.id.cart_sub_county);
            btnCall=itemView.findViewById(R.id.btnCall);
            tvProductUploader=itemView.findViewById(R.id.product_cart_uploader);



        }
    }
    private void requestCallPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},CALL_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CALL_PERMISSION_CODE){

            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else{
                Toast.makeText(getContext(),"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean isCallAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }
}
