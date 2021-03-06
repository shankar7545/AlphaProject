package com.example.alpha.Levels;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.alpha.Activity.HelpActivity;
import com.example.alpha.Fragments.MeFragment;
import com.example.alpha.Fragments.TutorialFragments;
import com.example.alpha.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class LevelActivity extends AppCompatActivity {
    private static long back_pressed;

    public ViewPager viewPager;
    public TabLayout tabLayout;

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private long mLastClickTime = 0;

    DatabaseReference mUser;
    private final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    public LevelActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        mUser = FirebaseDatabase.getInstance().getReference("Users");
//        //BottomSheet
//        View bottom_sheet = findViewById(R.id.bottom_sheet);
//        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        initToolbar();
        // statusBarColor();
        initComponent();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String strDate = sdf.format(c.getTime());
        Toast.makeText(this, strDate, Toast.LENGTH_SHORT).show();
    }



    private void initComponent() {
        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // viewPager.setOnTouchListener((arg0, arg1) -> true);  //to stop scrolling

        /*mUser.child(selfUid).child("Achievement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String mAchievement = Objects.requireNonNull(dataSnapshot.getValue()).toString();

                    switch (mAchievement) {
                        case "Bronze":
                            Objects.requireNonNull(tabLayout.getTabAt(0)).select();

                            break;
                        case "Silver":
                            Objects.requireNonNull(tabLayout.getTabAt(1)).select();

                            break;
                        case "Gold":
                            Objects.requireNonNull(tabLayout.getTabAt(2)).select();

                            break;
                        case "Diamond":
                            Objects.requireNonNull(tabLayout.getTabAt(3)).select();
                            break;

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BronzeFragment.newInstance(), "Plan");
        adapter.addFragment(MeFragment.newInstance(), "Wallet");
        adapter.addFragment(TutorialFragments.newInstance(), "Videos");
        //adapter.addFragment(DiamondFragment.newInstance(), "Transactions");

        viewPager.setAdapter(adapter);
    }


    private static class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

//    private void showBottomSheetDialog() {
//        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        }
//
//        final View view = getLayoutInflater().inflate(R.layout.sheet_info, null);
//
//
//
//        view.findViewById(R.id.paytm).setOnClickListener(v -> {
//            //mBottomSheetDialog.dismiss();
//
//            //to disable double_click
//            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
//                return;
//            }
//            mLastClickTime = SystemClock.elapsedRealtime();
//
//        });
//
//
//        view.findViewById(R.id.razorpay).setOnClickListener(v -> {
//            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
//                return;
//            }
//            mLastClickTime = SystemClock.elapsedRealtime();
//
//
//
//
//        });
//
//        mBottomSheetDialog = new BottomSheetDialog(this);
//        mBottomSheetDialog.setContentView(view);
//        Objects.requireNonNull(mBottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//        mBottomSheetDialog.show();
//        mBottomSheetDialog.setOnDismissListener(dialog -> mBottomSheetDialog = null);
//    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        Objects.requireNonNull(getSupportActionBar()).setTitle("Beginner Plan");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.help) {
            Intent intent = new Intent(LevelActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void statusBarColor() {

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
