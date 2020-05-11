package com.example.alpha.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Fragments.EarnFragment;
import com.example.alpha.Fragments.HomeFragment;
import com.example.alpha.Fragments.MeFragment;
import com.example.alpha.Profile.EditDetails;
import com.example.alpha.R;
import com.example.alpha.Wallet.walletActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class HomeActivity extends AppCompatActivity {
    private static long back_pressed;
    DatabaseReference mRef;
    TextView userName;

    RelativeLayout home_Relative;

    private ActionBar actionBar;
    private Toolbar toolbar;

    LinearLayout profileLayoutM, walletLayoutM, logoutLayoutM;

    ImageButton help, menu;
    private String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    CircularImageView profilePic;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            menuItem -> {


                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {

                    case R.id.nav_home:

                        selectedFragment = new HomeFragment();

                        break;
                    case R.id.nav_me:
                        selectedFragment = new MeFragment();

                        break;

                    case R.id.nav_earn:
                        selectedFragment = new EarnFragment();
                        break;

                }
                assert selectedFragment != null;

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                        selectedFragment).commit();

                return true;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        profilePic = findViewById(R.id.ProfilePic);

        home_Relative = findViewById(R.id.home_relative);

        mRef = FirebaseDatabase.getInstance().getReference("Users");

        userName = findViewById(R.id.userName);
        help = findViewById(R.id.help);


        profilePic.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyProfile.class);
            startActivity(intent);
        });

        help.setOnClickListener(v ->
        {
            Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
            startActivity(intent);
        });


        mRef.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mUserName = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();

                userName.setText(mUserName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_home);


        initNavigationMenu();

        menuOnclick();



    }


    private void initNavigationMenu() {
        NavigationView nav_view = findViewById(R.id.nav_view);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        menu = findViewById(R.id.menu);
        menu.setOnClickListener(v -> {

            drawer.openDrawer(GravityCompat.START);

        });

    }


    private void menuOnclick() {
        profileLayoutM = findViewById(R.id.profileLayoutM);
        walletLayoutM = findViewById(R.id.walletLayoutM);
        logoutLayoutM = findViewById(R.id.logoutLayoutM);

        profileLayoutM.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, EditDetails.class)));

        walletLayoutM.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, walletActivity.class)));

        logoutLayoutM.setOnClickListener(v -> new AlertDialog.Builder(HomeActivity.this)
                .setMessage(R.string.end_session)
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("logoutState", "logout");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show());

    }


    /*public void refreshMyData() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_home);

        //Snackbar.make(home_Relative,"Refresh Successfull",Snackbar.LENGTH_SHORT).show();

        final Snackbar snackbar = Snackbar.make(home_Relative, "", Snackbar.LENGTH_SHORT);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.snackbar_icon_text, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);

        ((TextView) custom_view.findViewById(R.id.message)).setText("Refresh Successfull");
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        (custom_view.findViewById(R.id.parent_view)).setBackgroundColor(getResources().getColor(R.color.green_700));
        snackBarView.addView(custom_view, 0);
        //snackbar.show();


    } */


    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        } else {
            Toast.makeText(this, "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();

        }
    }


}

