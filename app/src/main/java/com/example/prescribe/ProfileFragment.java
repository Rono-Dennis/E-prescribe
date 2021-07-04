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

import com.example.prescribe.Model.Products;
import com.example.prescribe.ViewHolder.ProductViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
public class ProfileFragment extends Fragment {
    private static final int GALLERY_REQUEST_CODE = 2345;
    private FloatingActionButton btnUpdate;
    private EditText productName,productCategory,productQuantity,productPrice;
    private RecyclerView productsList;
    private FirebaseAuth mAuth;
    private String userId;
    private View v;
    private Dialog d;
    private Uri imageUri=null;
    private ImageView profileImage;
    private DatabaseReference myProductsDatabase;
    private StorageReference mStorageReference;
    private ProgressDialog pd;
    EditText inputSearch;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v= inflater.inflate(R.layout.fragment_profile, container, false);


        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        //myProductsDatabase= FirebaseDatabase.getInstance().getReference().child("Products");

        mStorageReference= FirebaseStorage.getInstance().getReference().child("Product Images");

        btnUpdate=v.findViewById(R.id.fb);
        inputSearch=v.findViewById(R.id.inputsearch);
        d=new Dialog(getContext());
        d.setContentView(R.layout.post_dialog);
         profileImage=d.findViewById(R.id.productImage);
        productName=d.findViewById(R.id.product_title);
        productCategory=d.findViewById(R.id.product_category);
        productQuantity=d.findViewById(R.id.product_quantity);
        productPrice=d.findViewById(R.id.product_price);
        productsList=v.findViewById(R.id.productsList);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1);
        productsList.setLayoutManager(gridLayoutManager);

pd=new ProgressDialog(getContext());
pd.setTitle("Uploading Product");
pd.setMessage("Please wait...");
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bringPostDialog();
            }
        });

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

    private void bringPostDialog() {
        Button btnPost=d.findViewById(R.id.btnUpload);




        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST_CODE);
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=productName.getText().toString().trim();
                String category=productCategory.getText().toString().trim();
                String quantity=productQuantity.getText().toString().trim();
                String price=productPrice.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(category) && !TextUtils.isEmpty(quantity) &&!TextUtils.isEmpty(price))
                {

                    if (imageUri!=null)
                    {

                        //toast("Data is ready");
                        pd.show();
                        postData(name,category,quantity,price,imageUri);
                    }else {
                        toast("Tap the avatar to select product Image...");
                    }
                }else {
                    toast("Check your Product Inputs...some fileds are blank...");
                }
            }
        });


        d.show();






    }

    private void postData(final String name, final String category, final String quantity, final String price, final Uri myUri) {

       final StorageReference filePath=mStorageReference.child(myUri.getLastPathSegment()+".jpg");
       filePath.putFile(myUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

               if (task.isSuccessful())
               {
                   filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           String downloadLink=uri.toString();

                           DateFormat dateFormat=new SimpleDateFormat("HH:mm:ss ");
                           Date date=new Date();
                           String time=dateFormat.format(date);
                           String productKey=myProductsDatabase.push().getKey();
                           DatabaseReference newProduct=myProductsDatabase.child(productKey);
                           HashMap<String,Object> myMap=new HashMap<>();
                           myMap.put("post_id",productKey);
                           myMap.put("product_name",name);
                           myMap.put("product_category",category);
                           myMap.put("product_quantity",quantity);
                           myMap.put("product_price",price);
                           myMap.put("post_time",time);
                           myMap.put("product_poster",userId);
//                           myMap.put("product_views","0");
//                           myMap.put("product_likes","0");

                           myMap.put("post_image",downloadLink);

                           newProduct.updateChildren(myMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful())
                                   {
                                       toast("Uploaded successfully");
                                       pd.dismiss();
                                       d.dismiss();
                                       productName.setText("");
                                       productCategory.setText("");
                                       productQuantity.setText("");
                                       productPrice.setText("");
                                       //imageUri=null;
                                       profileImage.setImageURI(null);

                                   }else {
                                       pd.dismiss();

                                       toast(task.getException().getMessage());
                                   }
                               }
                           });



                       }
                   });
               }else {
                   toast(task.getException().getMessage());
               }
           }
       });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (GALLERY_REQUEST_CODE==requestCode && resultCode==RESULT_OK)
        {
            imageUri=data.getData();
            profileImage.setImageURI(imageUri);
        }


    }
    private void toast(String s) {
        Toast.makeText(getContext(), "Message: "+s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStart() {
        //myProductsDatabase = FirebaseDatabase.getInstance().getReference().child("Hallucinogen");
        super.onStart();
       // myProductsDatabase.keepSynced(true);
        myProductsDatabase= FirebaseDatabase.getInstance().getReference().child("Hallucinogen");
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(myProductsDatabase.orderByChild("pname").startAt(inputSearch.getText().toString()).endAt(inputSearch.getText().toString()+"\uf8ff"), Products.class)
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
                                Intent intent= new Intent(getContext(), ProductsDetailsActivity1.class);
                                intent.putExtra("pid",model.getPid());

                                startActivity(intent);
                            }
                        });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent= new Intent(getContext(), ProductsDetailsActivity1.class);
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


        productsList.setAdapter(adapter);
        adapter.startListening();

        productsList.smoothScrollToPosition(adapter.getItemCount()+1);


    }


}
