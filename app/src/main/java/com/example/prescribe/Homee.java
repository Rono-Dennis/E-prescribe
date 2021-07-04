package com.example.prescribe;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prescribe.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import static androidx.fragment.app.FragmentPagerAdapter.*;

public class Homee extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TextView userName, userNameTextView;
    String phonecurrent;
    private int overTotalPrice = 0;

    private  String CurrentUsername, CurrentuserPhone,CurrentuserEmail, CurrentUser;
    View headerView;

    FirebaseAuth mAuth;
    private DatabaseReference usereference;

    MenuItem menuItem;
    int pendingNotification = 0;
    TextView badgeCounter, txtTotalAmount;
    ImageView img;
    EditText inputSearch;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1, tab2, tab3, tab4;
    public PagerAdapter pagerAdapter;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager pager;
    TabLayout mTabLayout;
    TabItem firstItem,secondItem,thirdItem;
    PagerAdapter adapter;
    PageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homee);

//        pager = findViewById(R.id.viewpager);
       // mTabLayout = findViewById(R.id.tablayout);

        firstItem = findViewById(R.id.firstItem);
        secondItem = findViewById(R.id.secondItem);
        thirdItem = findViewById(R.id.thirditem);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        inputSearch=findViewById(R.id.inputsearch);
        userName=headerView.findViewById(R.id.user_profile_email);
        userNameTextView = headerView.findViewById(R.id.user_profile_name);

        //phonecurrent=getIntent().getStringExtra("currentphones");




        pageAdapter = new PageAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mTabLayout.getTabCount());
        pager.setAdapter(pageAdapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homee.this, CartActivity.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setNavigationItemSelectedListener(this);

        //View headerView = navigationView.getHeaderView(0);
//        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

//        userNameTextView.setText(Prevalent.currentOnlineUser.getName());
//        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);


        recyclerView = findViewById(R.id.recycler_menu);
       recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        //userName=findViewById(R.id.user_profile_name);

        mAuth= FirebaseAuth.getInstance();
        usereference= FirebaseDatabase.getInstance().getReference().child("User");

        CurrentUsername=mAuth.getCurrentUser().getUid();
       // TextView finalUserNameTextView = userNameTextView;
        usereference.child(CurrentUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    //User user = snapshot.getValue(User.class);


//
//                    userNameTextView.setText(user.email);
                    CurrentuserPhone=snapshot.child("name").getValue().toString();
                    CurrentuserEmail=snapshot.child("email").getValue().toString();
                    userNameTextView.setText(CurrentuserPhone);
                    userName.setText(CurrentuserEmail);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        CurrentUser=mAuth.getCurrentUser().getUid();
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





    @Override
    protected void onStart()
    {
       // Query query = ProductsRef.orderByChild("pname").startAt(inputSearch.getText().toString()).endAt(inputSearch.getText().toString()+"\uf8ff");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Hallucinogen");

        super.onStart();

//        FirebaseRecyclerOptions<Products> options =
//                new FirebaseRecyclerOptions.Builder<Products>()
//                        .setQuery(ProductsRef.orderByChild("pname").startAt(inputSearch.getText().toString()).endAt(inputSearch.getText().toString()+"\uf8ff"), Products.class)
//                        .build();
//
//
//        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
//                    {
//                        holder.txtProductName.setText(model.getPname());
//                        holder.txtProductDescription.setText(model.getDescription());
//                        holder.txtProductPrice.setText("Price = ksh " + model.getPrice());
//                        Picasso.get().load(model.getImage()).into(holder.imageView);
//
//
//                        holder.addCart.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent= new Intent(Homee.this, ProductsDetailsActivity1.class);
//                                intent.putExtra("pid",model.getPid());
//                                intent.putExtra("Curentphone",phonecurrent);
//                                startActivity(intent);
//                            }
//                        });
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent= new Intent(Homee.this, ProductsDetailsActivity1.class);
//                                intent.putExtra("pid",model.getPid());
//                                intent.putExtra("Curentphone",phonecurrent);
//                                startActivity(intent);
//                            }
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
//                    {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
//                        ProductViewHolder holder = new ProductViewHolder(view);
//                        return holder;
//                    }
//                };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                    Intent intent = new Intent(Homee.this, CartActivity.class);
                    startActivity(intent);
                }
            });
            badgeCounter.setText(String.valueOf(pendingNotification));
        }
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart)
        {
            Intent intent = new Intent(Homee.this, CartActivity.class);
//            intent.putExtra("Curentphone",phonecurrent);
            startActivity(intent);
        }
        else if (id == R.id.nav_orders)
        {

        }
        else if (id == R.id.nav_categories)
        {
            Intent intent = new Intent(Homee.this, UserPage.class);
            startActivity(intent);
        }
         
        else if (id == R.id.nav_Logout)
        {
            Paper.book().destroy();

            Intent intent = new Intent(Homee.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}