package com.example.alpha.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Fragments.EarnFragment;
import com.example.alpha.Fragments.HomeFragment;
import com.example.alpha.Fragments.MeFragment;
import com.example.alpha.Model.ReferClass;
import com.example.alpha.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeActivity extends AppCompatActivity {
    private static long back_pressed;
    DatabaseReference mRef, mReferDB, mUser;
    TextView userName, nameM, userNameM;
    private Dialog dialog;

    RelativeLayout home_Relative;

    private ActionBar actionBar;
    private Toolbar toolbar;

    ProgressBar progressUsername;
    private SwipeRefreshLayout swipe_refresh;


    ImageButton help, menu;
    private final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    CircularImageView profilePic;
    @SuppressLint("NonConstantResourceId")
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

                if (Objects.equals(selectedFragment, new HomeFragment())) {
                    Toast.makeText(this, "Selected Home Fragment", Toast.LENGTH_SHORT).show();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                        selectedFragment).commit();


                return true;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_home);


        //initNavigationMenu();
        initComponent();
        loadAvatar();
        //menuOnclick();

    }

    private void initComponent() {
        profilePic = findViewById(R.id.ProfilePic);
        home_Relative = findViewById(R.id.home_relative);
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        userName = findViewById(R.id.userName);
        help = findViewById(R.id.help);
        progressUsername = findViewById(R.id.progressUsername);
        progressUsername.setVisibility(View.VISIBLE);
        userName.setVisibility(View.GONE);
        profilePic.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyProfile.class);
            startActivity(intent);
        });

        help.setOnClickListener(v ->
        {
            Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
            startActivity(intent);
        });


        mRef.child(selfUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("username").exists()) {
                    String mUserName = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                    String mName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();

                    userName.setText(mUserName);
                    userName.setVisibility(View.VISIBLE);
                    progressUsername.setVisibility(View.GONE);
                } else {

                    usernameDialog();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Swipe Refresh


    }

    private void usernameDialog() {
        try {
            dialog = new Dialog(HomeActivity.this);
            dialog.setContentView(R.layout.referdialog);
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            assert window != null;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            final TextInputEditText editTextuserName;
            final TextView heading;
            final Button finish, cancel;
            final ProgressBar progress_bar_dialog;

            editTextuserName = dialog.findViewById(R.id.referCode);

            editTextuserName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            mRef = FirebaseDatabase.getInstance().getReference("Users");
            DatabaseReference mFirebase = FirebaseDatabase.getInstance().getReference();

            finish = dialog.findViewById(R.id.finish);
            cancel = dialog.findViewById(R.id.cancelBtn);
            heading = dialog.findViewById(R.id.heading);
            progress_bar_dialog = dialog.findViewById(R.id.progress_bar_dialog);

            editTextuserName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            heading.setText("CREATE UNIQUE USERNAME");
            dialog.show();


            finish.setOnClickListener(view -> {
                cancel.setVisibility(View.GONE);
                finish.setVisibility(View.GONE);
                progress_bar_dialog.setVisibility(View.VISIBLE);
                final String mUserName = editTextuserName.getText().toString().trim();


                final DatabaseReference promodb = FirebaseDatabase.getInstance().getReference("ReferDB");
                promodb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot checkdataSnapshot) {


                        if (mUserName.isEmpty()) {
                            editTextuserName.setError("Enter Username");
                            editTextuserName.requestFocus();
                            finish.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                            progress_bar_dialog.setVisibility(View.GONE);
                        } else if (mUserName.length() < 5) {
                            editTextuserName.setError("Enter 5 Letters");
                            finish.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                            progress_bar_dialog.setVisibility(View.GONE);
                            editTextuserName.requestFocus();
                        } else if (mUserName.length() > 10) {
                            editTextuserName.setError("Maximum 10 Letters");
                            finish.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                            progress_bar_dialog.setVisibility(View.GONE);
                            editTextuserName.requestFocus();
                        } else if (checkdataSnapshot.hasChild(mUserName)) {
                            editTextuserName.setError("Username Exists");
                            finish.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                            progress_bar_dialog.setVisibility(View.GONE);
                            editTextuserName.requestFocus();
                        } else {
                            finish.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                            progress_bar_dialog.setVisibility(View.GONE);
                            mRef.child(selfUid).child("username").setValue(mUserName);

                            //ReferDB
                            mReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
                            ReferClass referClass = new ReferClass(
                                    selfUid,
                                    mUserName,
                                    "0",
                                    "false");

                            mReferDB.child(mUserName).setValue(referClass);

                            mUser = FirebaseDatabase.getInstance().getReference("Users").child(selfUid);

                            mUser.child("Achievement").child("currentLevel").setValue("Beginner");
                            mUser.child("Achievement").child("BronzeA").setValue("false");
                            mUser.child("Achievement").child("SilverA").setValue("false");
                            mUser.child("Achievement").child("GoldA").setValue("false");
                            mUser.child("Achievement").child("DiamondA").setValue("false");


                            dialog.dismiss();

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            });


            cancel.setOnClickListener(v -> logout());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();


        Intent i = new Intent(this, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("logoutState", "logout");
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    private void loadAvatar() {
        mUser = FirebaseDatabase.getInstance().getReference("Users").child(selfUid);

        mUser.child("avatar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()) {
                    String value = Objects.requireNonNull(snapshot.getValue()).toString();

                    switch (value) {
                        case "1":
                            profilePic.setImageResource(R.drawable.avatar_one);
                            break;
                        case "2":
                            profilePic.setImageResource(R.drawable.avatar_two);

                            break;
                        case "3":
                            profilePic.setImageResource(R.drawable.avatar_three);

                            break;
                        case "4":
                            profilePic.setImageResource(R.drawable.avatar_four);

                            break;
                        case "5":
                            profilePic.setImageResource(R.drawable.avatar_five);

                            break;
                        case "6":
                            profilePic.setImageResource(R.drawable.avatar_six);

                            break;
                        case "7":
                            profilePic.setImageResource(R.drawable.avatar_seven);

                            break;
                        case "8":
                            profilePic.setImageResource(R.drawable.avatar_eight);

                            break;
                    }

                } else {
                    profilePic.setImageResource(R.drawable.avatar_one);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        //statusBarColor();

    }

    private void statusBarColor() {

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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

