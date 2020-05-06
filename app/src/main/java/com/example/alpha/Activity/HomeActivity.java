package com.example.alpha.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Fragments.EarnFragment;
import com.example.alpha.Fragments.HomeFragment;
import com.example.alpha.Fragments.MeFragment;
import com.example.alpha.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class HomeActivity extends AppCompatActivity {
    private static long back_pressed;
    DatabaseReference mRef;
    TextView userName;

    RelativeLayout home_Relative;
    AppBarLayout appbar;
    ActionBar actionBar;
    ImageButton help;
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
            Intent intent = new Intent(HomeActivity.this, ChainActivity.class);
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

