package com.example.prescribe;

import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TextView userName, userNameTextView;
    String phonecurrent;
    private int overTotalPrice = 0;

    SwipeRefreshLayout swipeRefreshLayout;

    private  String CurrentUsername, CurrentuserPhone,CurrentuserEmail, CurrentUser;
    View headerView;

   // FirebaseAuth mAuth;
    private DatabaseReference usereference;

    MenuItem menuItem;
    int pendingNotification = 0;
    int pendingNotification1 = 0;
    int PendingNotification2 = 0;
    int PendingNotification3 = 0;

    TextView badgeCounter, txtTotalAmount;
    ImageView img;
    EditText inputSearch;
    RecyclerView recyclerCategories;
    RecyclerView recyclerItems;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1, tab2, tab3, tab4;
    public PagerAdapter pagerAdapter;

    private TextView profileLabel,usersLabel,notificationLabel,tvUsername, Antibiotic, show_all;
    private ViewPager mViewPager;
    private PagerViewAdapter pagerViewAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference myUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homee);

        mAuth=FirebaseAuth.getInstance();
        myUsersDatabase= FirebaseDatabase.getInstance().getReference().child("User");
        myUsersDatabase.keepSynced(true);
        profileLabel=findViewById(R.id.profileLabel);
        usersLabel=findViewById(R.id.usersLabel);
        notificationLabel=findViewById(R.id.notificationLabel);
        mViewPager=findViewById(R.id.mainViewPager);
        tvUsername=findViewById(R.id.useraname);
        Antibiotic=findViewById(R.id.antibiotic);
        mViewPager=findViewById(R.id.mainViewPager);
//        inputSearch.setOnKeyListener(null);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);

        userName=headerView.findViewById(R.id.user_profile_email);
        userNameTextView = headerView.findViewById(R.id.user_profile_name);

        swipeRefreshLayout=findViewById(R.id.refresh);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });



                String userId=mAuth.getCurrentUser().getUid();
                myUsersDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists())
                        {
                            String name=dataSnapshot.child("name").getValue().toString();
                            tvUsername.setText("You are signed in as:  "+name.toUpperCase()+ "  Sign out ? ");
                            tvUsername.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(HomeeActivity.this);
                                    builder.setTitle("Confirm Action");
                                    builder.setMessage("Do you want to Sign out?");
                                    builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            mAuth.signOut();

                                            Intent intent = new Intent(HomeeActivity.this, Homepage.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    builder.setNegativeButton("Later",null);
                                    builder.show();

                                }
                            });

                        }else {
                            startActivity(new Intent(   HomeeActivity.this,Homepage.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        Paper.init(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);


//        recyclerView = findViewById(R.id.recycler_menu);
//       recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
//        recyclerView.setLayoutManager(layoutManager);


        mAuth= FirebaseAuth.getInstance();
        usereference= FirebaseDatabase.getInstance().getReference().child("User");

        CurrentUsername=mAuth.getCurrentUser().getUid();

        usereference.child(CurrentUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {

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

                .child("Depressants").child(CurrentUser);

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






        profileLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPager.setCurrentItem(0);
            }
        });
        usersLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);

            }
        });
        notificationLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);

            }
        });
        Antibiotic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(3);
            }
        });


        pagerViewAdapter=new PagerViewAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerViewAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position ,float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {

                changeTabs(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }



    private void changeTabs(int position) {


        if (position==0)
        {
            profileLabel.setTextColor(getResources().getColor(R.color.textTabBright));
            profileLabel.setTextSize(18);
            usersLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            usersLabel.setTextSize(10);

            Antibiotic.setTextColor(getResources().getColor(R.color.textTabLight));
            Antibiotic.setTextSize(10);
            notificationLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            notificationLabel.setTextSize(10);
        }
        if (position==1)
        {

            profileLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            profileLabel.setTextSize(10);
            usersLabel.setTextColor(getResources().getColor(R.color.textTabBright));
            usersLabel.setTextSize(18);
            notificationLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            notificationLabel.setTextSize(10);

            Antibiotic.setTextColor(getResources().getColor(R.color.textTabLight));
            Antibiotic.setTextSize(10);
        }
        if (position==2)
        {

            profileLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            profileLabel.setTextSize(10);
            usersLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            usersLabel.setTextSize(10);
            Antibiotic.setTextColor(getResources().getColor(R.color.textTabLight));
            Antibiotic.setTextSize(10);

            notificationLabel.setTextColor(getResources().getColor(R.color.textTabBright));
            notificationLabel.setTextSize(18);
        }
        if (position==3)
        {
            profileLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            profileLabel.setTextSize(10);
            usersLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            usersLabel.setTextSize(10);
            notificationLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            notificationLabel.setTextSize(10);

            Antibiotic.setTextColor(getResources().getColor(R.color.textTabBright));
            Antibiotic.setTextSize(18);
        }
        verifyUserDetails();
    }




    @Override
    protected void onStart() {
        super.onStart();

//        FirebaseUser currentUser=mAuth.getCurrentUser();
//        if (currentUser==null)
//        {
//            sendToLogin();
//        }else {

//        }

    }
    private void verifyUserDetails() {
        FirebaseUser currentUser=mAuth.getCurrentUser();

    }

    private void sendToLogin() {
        startActivity(new Intent(HomeeActivity.this,login2Activity.class));

        finish();
    }




    @Override
    public void onBackPressed() {



//        mAuth.signOut();
//        Intent intent = new Intent(HomeeActivity.this, Homepage.class);
//        startActivity(intent);
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
//            menuItem.setActionView(pendingNotification);
            menuItem.setActionView(R.layout.notification_badge);

            View view=menuItem.getActionView();

            badgeCounter=view.findViewById(R.id.badge_counter);

            img=view.findViewById(R.id.imageBtn);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            });
            badgeCounter.setText(String.valueOf(pendingNotification));
        }else
        {
            menuItem.setActionView(R.layout.notification_badge);

            View view=menuItem.getActionView();

            badgeCounter=view.findViewById(R.id.badge_counter);

            img=view.findViewById(R.id.imageBtn);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeeActivity.this, CartActivity.class);
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
            Intent intent = new Intent(HomeeActivity.this, CartActivity.class);
//            intent.putExtra("Curentphone",phonecurrent);
            startActivity(intent);
        }
        else if (id == R.id.nav_orders)
        {
            Intent intent = new Intent(HomeeActivity.this, HomeActivity.class);
//            intent.putExtra("Curentphone",phonecurrent);
            startActivity(intent);
        }
        else if (id == R.id.nav_categories)
        {
            Intent intent = new Intent(HomeeActivity.this, UserPage.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_Logout)
        {
            mAuth.signOut();

            Intent intent = new Intent(HomeeActivity.this, Homepage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}