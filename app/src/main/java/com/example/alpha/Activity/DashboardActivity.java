package com.example.alpha.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Model.ParentClass;
import com.example.alpha.R;
import com.example.alpha.Registration.PaytmPayment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DashboardActivity extends AppCompatActivity {

    final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    Toolbar toolbar;
    private Dialog dialog;
    private ProgressBar beginnerPaymentProgress, beginnerReferProgressBar, beginnerChildProgressBar, bronzePaymentProgressBar;
    private DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mTransactionsRecycler, dbPaytm, mTransactions1, mAchievements;
    private LinearLayout beginnerLayout, beginnerExpand, bronzeLayout, bronzeExpand, silverLayout;
    private ImageView beginnerCircle, beginnerPaymentCircle, beginnerPaymentGreenCheck, beginnerReferCirle, beginnerReferGreenCheck,
            beginnerChildCirle, beginnerChildGreenCheck, bronzeCircle, bronzePaymentCircle;
    private TextView beginnerPaymentText, beginnerReferText, beginnerChildText, bronzePaymentText, beginnerPaymentClick,
            beginnerReferClick, beginnerChildClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        mAchievements = FirebaseDatabase.getInstance().getReference("Users").child(selfUid)
                .child("Achievements");
        mRef = FirebaseDatabase.getInstance().getReference("Users");

        mFirebase = FirebaseDatabase.getInstance().getReference();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //DashBoard
        beginnerLayout = findViewById(R.id.beginnerLayout);
        beginnerExpand = findViewById(R.id.beginnerExpand);
        bronzeLayout = findViewById(R.id.bronzeLayout);
        bronzeExpand = findViewById(R.id.bronzeExpand);


        beginnerCircle = findViewById(R.id.beginnerCircle);
        beginnerReferCirle = findViewById(R.id.beginnerReferCirle);
        beginnerChildCirle = findViewById(R.id.beginnerChildCirle);
        beginnerPaymentCircle = findViewById(R.id.beginnerPaymentCircle);
        beginnerReferGreenCheck = findViewById(R.id.beginnerReferGreenCheck);
        beginnerChildGreenCheck = findViewById(R.id.beginnerChildGreenCheck);
        beginnerPaymentGreenCheck = findViewById(R.id.beginnerPaymentGreenCheck);

        beginnerPaymentText = findViewById(R.id.beginnerPaymentText);
        beginnerReferText = findViewById(R.id.beginnerReferText);
        beginnerChildText = findViewById(R.id.beginnerChildText);
        beginnerPaymentClick = findViewById(R.id.beginnerPaymentClick);
        beginnerReferClick = findViewById(R.id.beginnerReferClick);


        beginnerPaymentProgress = findViewById(R.id.beginnerPaymentProgress);
        beginnerReferProgressBar = findViewById(R.id.beginnerReferProgressBar);
        beginnerChildProgressBar = findViewById(R.id.beginnerChildProgressBar);


        bronzeCircle = findViewById(R.id.bronzeCircle);
        bronzePaymentText = findViewById(R.id.bronzePaymentText);
        bronzePaymentCircle = findViewById(R.id.bronzePaymentCircle);
        bronzePaymentProgressBar = findViewById(R.id.bronzePaymentProgressBar);


        beginnerLayout.setOnClickListener(v -> {
            if (beginnerExpand.getVisibility() == View.VISIBLE) {

                beginnerExpand.setVisibility(View.GONE);

            } else {

                beginnerExpand.setVisibility(View.VISIBLE);


            }
        });

        dashboard();


    }

    private void dashboard() {


        mAchievements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String mBeginner = Objects.requireNonNull(dataSnapshot.child("beginner").getValue()).toString();
                    String mBronze = Objects.requireNonNull(dataSnapshot.child("bronze").getValue()).toString();
                    String mSilver = Objects.requireNonNull(dataSnapshot.child("silver").getValue()).toString();
                    String mGold = Objects.requireNonNull(dataSnapshot.child("gold").getValue()).toString();
                    String mDiamond = Objects.requireNonNull(dataSnapshot.child("diamond").getValue()).toString();

                    if (mBeginner.equals("unlocked")) {
                        beginnerDashboard();
                        mAchievements.child("bronze").setValue("locked");
                        mAchievements.child("silver").setValue("locked");
                        mAchievements.child("gold").setValue("locked");
                        mAchievements.child("diamond").setValue("locked");

                    } else if (mBeginner.equals("completed")) {

                        beginnerDashboard();
                    }

                    //Unlock Levels after Completing beginner
                    if (mBeginner.equals("completed") && mBronze.equals("locked")) {
                        mAchievements.child("bronze").setValue("unlocked");
                    }
                    if (mBeginner.equals("completed") && mSilver.equals("locked")) {

                        mAchievements.child("silver").setValue("unlocked");

                    }
                    if (mBeginner.equals("completed") && mGold.equals("locked")) {

                        mAchievements.child("gold").setValue("unlocked");

                    }
                    if (mBeginner.equals("completed") && mDiamond.equals("locked")) {

                        mAchievements.child("diamond").setValue("unlocked");

                    }


                    //check Remaining Levels

                    switch (mBronze) {
                        case "locked":
                            bronzeCircle.setImageResource(R.drawable.ic_lock);
                            bronzeExpand.setVisibility(View.GONE);
                            bronzeLayout.setOnClickListener(v ->
                                    Toast.makeText(DashboardActivity.this, "Complete Beginner Level", Toast.LENGTH_SHORT).show());

                            break;
                        case "unlocked":
                            bronzeExpand.setVisibility(View.VISIBLE);
                            bronzeDashboard();


                            break;
                        case "completed":
                            bronzeExpand.setVisibility(View.GONE);
                            break;
                    }


                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void beginnerDashboard() {

        mRef.child(selfUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String parentStatus = Objects.requireNonNull(dataSnapshot.child("parentStatus").getValue()).toString();
                final String paymentStatus = Objects.requireNonNull(dataSnapshot.child("paymentStatus").getValue()).toString();

                if (paymentStatus.equals("true")) {
                    beginnerPaymentProgress.setVisibility(View.GONE);
                    beginnerPaymentCircle.setVisibility(View.VISIBLE);
                    beginnerPaymentClick.setVisibility(View.GONE);
                    beginnerPaymentCircle.setImageResource(R.drawable.green_chechk);
                    beginnerPaymentText.setText("Payment Success");
                    beginnerPaymentText.setTypeface(beginnerPaymentText.getTypeface(), Typeface.BOLD);
                    beginnerPaymentText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    beginnerReferText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    beginnerReferClick.setEnabled(true);
                } else {
                    beginnerPaymentProgress.setVisibility(View.GONE);
                    beginnerPaymentCircle.setVisibility(View.VISIBLE);
                    beginnerPaymentCircle.setImageResource(R.drawable.ic_circle);
                    beginnerPaymentText.setTypeface(beginnerPaymentText.getTypeface(), Typeface.NORMAL);
                    beginnerPaymentClick.setVisibility(View.VISIBLE);
                    beginnerPaymentText.setText("to Complete Payment");
                    beginnerReferClick.setEnabled(false);
                    beginnerPaymentClick.setOnClickListener(v -> {
                        Intent intent = new Intent(DashboardActivity.this, PaytmPayment.class);
                        startActivity(intent);
                    });
                }

                if (parentStatus.equals("true")) {
                    beginnerReferProgressBar.setVisibility(View.GONE);
                    beginnerReferClick.setVisibility(View.GONE);
                    beginnerReferCirle.setVisibility(View.VISIBLE);
                    beginnerReferCirle.setImageResource(R.drawable.green_chechk);
                    beginnerReferText.setTypeface(beginnerReferText.getTypeface(), Typeface.BOLD);
                    beginnerReferText.setText("Refercode success");
                    beginnerReferText.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    beginnerReferProgressBar.setVisibility(View.GONE);
                    beginnerReferCirle.setVisibility(View.VISIBLE);
                    beginnerReferClick.setVisibility(View.VISIBLE);
                    beginnerReferCirle.setImageResource(R.drawable.ic_circle);
                    beginnerReferText.setText("to enter Refercode");
                    beginnerReferText.setTypeface(beginnerReferText.getTypeface(), Typeface.NORMAL);
                    beginnerReferClick.setOnClickListener(v -> {
                        // Intent intent = new Intent( DashboardActivity.this, ReferCodeAcitvity.class);
                        // startActivity(intent);
                        referDialog();
                    });
                }

                if ((parentStatus.equals("true")) && paymentStatus.equals("true")) {
                    beginnerCircle.setImageResource(R.drawable.green_chechk);
                    beginnerExpand.setVisibility(View.GONE);
                    mAchievements.child("beginner").setValue("completed");

                } else {
                    beginnerExpand.setVisibility(View.VISIBLE);
                    beginnerCircle.setImageResource(R.drawable.ic_circle);
                    beginnerExpand.setVisibility(View.VISIBLE);
                    mAchievements.child("beginner").setValue("unlocked");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void bronzeDashboard() {
        bronzeCircle.setImageResource(R.drawable.ic_circle);
        beginnerExpand.setVisibility(View.VISIBLE);
        bronzeLayout.setOnClickListener(v -> {

            if (bronzeExpand.getVisibility() == View.VISIBLE) {

                bronzeExpand.setVisibility(View.GONE);

            } else {
                bronzeExpand.setVisibility(View.VISIBLE);
            }
        });

        //Payment Status
        bronzePaymentProgressBar.setVisibility(View.GONE);
        bronzePaymentCircle.setVisibility(View.VISIBLE);
        bronzePaymentCircle.setImageResource(R.drawable.ic_circle);

        mRef.child(selfUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String childCount = Objects.requireNonNull(dataSnapshot.child("childCount").getValue()).toString();


                if (childCount.equals("2")) {
                    beginnerChildProgressBar.setVisibility(View.GONE);
                    beginnerChildCirle.setVisibility(View.VISIBLE);
                    beginnerChildCirle.setImageResource(R.drawable.green_chechk);
                    beginnerChildText.setText("REFERED  2/2");
                    beginnerChildText.setTypeface(beginnerChildText.getTypeface(), Typeface.BOLD);
                    beginnerChildText.setTextColor(getResources().getColor(R.color.colorPrimary));

                } else if (childCount.equals("1")) {

                    beginnerChildProgressBar.setVisibility(View.GONE);
                    beginnerChildCirle.setVisibility(View.VISIBLE);
                    beginnerChildCirle.setImageResource(R.drawable.ic_circle);
                    beginnerChildText.setText("REFER ONE MORE");
                    beginnerChildText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    beginnerChildText.setTypeface(beginnerChildText.getTypeface(), Typeface.BOLD);
                    beginnerChildText.setOnClickListener(v -> Toast.makeText(DashboardActivity.this, "Refer Clicked", Toast.LENGTH_SHORT).show());


                } else {
                    beginnerChildProgressBar.setVisibility(View.GONE);
                    beginnerChildCirle.setVisibility(View.VISIBLE);
                    beginnerChildCirle.setImageResource(R.drawable.ic_circle);
                    beginnerChildText.setText("REFER TWO USERS");
                    beginnerChildText.setTypeface(beginnerChildText.getTypeface(), Typeface.BOLD);
                    beginnerChildText.setOnClickListener(v -> Toast.makeText(DashboardActivity.this, "Refer Clicked", Toast.LENGTH_SHORT).show());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //ReferDialog
    private void referDialog() {
        dialog = new Dialog(DashboardActivity.this);
        dialog.setContentView(R.layout.referdialog);
        dialog.setCancelable(false);
        final EditText editTextReferCode;
        final ProgressBar progressBar;
        final Button finish, cancel;

        editTextReferCode = dialog.findViewById(R.id.referCode);
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mFirebase = FirebaseDatabase.getInstance().getReference();

        finish = dialog.findViewById(R.id.finish);
        cancel = dialog.findViewById(R.id.cancelBtn);
        progressBar = dialog.findViewById(R.id.progress_bar);

        editTextReferCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        dialog.show();
        finish.setOnClickListener(view -> {

            cancel.setEnabled(false);

            final String mReferCode = editTextReferCode.getText().toString().trim();

            final DatabaseReference ReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
            ReferDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot checkdataSnapshot) {

                    if (mReferCode.isEmpty()) {
                        editTextReferCode.setError("Enter ReferCode");
                        editTextReferCode.requestFocus();
                        cancel.setEnabled(true);

                    } else if (checkdataSnapshot.hasChild(mReferCode)) {

                        Child(mReferCode);


                    } else if (!checkdataSnapshot.hasChild(mReferCode)) {
                        editTextReferCode.setError("Invalid ReferCode");
                        editTextReferCode.requestFocus();
                        cancel.setEnabled(true);

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            cancel.setEnabled(true);

        });

        cancel.setOnClickListener(v -> dialog.dismiss());


    }

    //ReferCode
    private void Child(final String mReferCode) {


        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String referUid = Objects.requireNonNull(dataSnapshot.child("ReferDB").child(mReferCode).child("uid").getValue()).toString();
                String childCount = Objects.requireNonNull(dataSnapshot.child("Users").child(referUid).child("childCount").getValue()).toString();
                String parentStatus = Objects.requireNonNull(dataSnapshot.child("Users").child(referUid).child("parentStatus").getValue()).toString();
                String userName = Objects.requireNonNull(dataSnapshot.child("Users").child(selfUid).child("username").getValue()).toString();

                if (!userName.equals(mReferCode)) {
                    String p1 = Objects.requireNonNull(dataSnapshot.child("Users").child(referUid).child("Chain")
                            .child("parent").child("p1").getValue()).toString();


                    if ((parentStatus.equals("true")) && !p1.equals("null")) {
                        if (childCount.equals("0")) {
                            referDetails(mReferCode);
                            mFirebase.child("Users").child(referUid).child("Chain").child("child").child("levelOne").child("leftChild").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            mFirebase.child("Users").child(referUid).child("childCount").setValue("1");

                        } else if (childCount.equals("1")) {
                            referDetails(mReferCode);
                            mFirebase.child("Users").child(referUid).child("Chain").child("child").child("levelOne").child("rightChild").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            mFirebase.child("Users").child(referUid).child("childCount").setValue("2");

                        } else {
                            //editTextReferCode.setError("Limit Exceeded");
                            //editTextReferCode.requestFocus();
                            Toast.makeText(DashboardActivity.this, "Limit exceeded , Try another Refer Code", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(DashboardActivity.this, mReferCode + " have no parent", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DashboardActivity.this, "IDIOT!! Donot enter Your USERNAME", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //ReferCode
    private void referDetails(final String mReferCode) {
        try {


            mFirebase = FirebaseDatabase.getInstance().getReference();

            mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    String uid_p1 = dataSnapshot.child("ReferDB").child(mReferCode).child("uid").getValue().toString();

                    String uid_p2 = dataSnapshot.child("Users").child(uid_p1).child("Chain").child("parent").child("p1").getValue().toString();
                    String uid_p3 = dataSnapshot.child("Users").child(uid_p2).child("Chain").child("parent").child("p1").getValue().toString();
                    String uid_p4 = dataSnapshot.child("Users").child(uid_p3).child("Chain").child("parent").child("p1").getValue().toString();
                    String uid_p5 = dataSnapshot.child("Users").child(uid_p4).child("Chain").child("parent").child("p1").getValue().toString();
                    String uid_p6 = dataSnapshot.child("Users").child(uid_p5).child("Chain").child("parent").child("p1").getValue().toString();
                    String uid_p7 = dataSnapshot.child("Users").child(uid_p6).child("Chain").child("parent").child("p1").getValue().toString();
                    String uid_p8 = dataSnapshot.child("Users").child(uid_p7).child("Chain").child("parent").child("p1").getValue().toString();

                    mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parentStatus").setValue("true");


                    //Parent Class
                    ParentClass parentClass = new ParentClass(
                            uid_p1,
                            uid_p2,
                            uid_p3,
                            uid_p4,
                            uid_p5,
                            uid_p6,
                            uid_p7,
                            uid_p8
                    );
                    mRef = FirebaseDatabase.getInstance().getReference("Users");

                    mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain")
                            .child("parent").setValue(parentClass);


                    Toast.makeText(DashboardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(DashboardActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }, 2000);


                        /*AutoReferCode
                        String mEnd = dataSnapshot.child("AutoReferCode").child("end").getValue().toString();
                        String mStart = dataSnapshot.child("AutoReferCode").child("start").getValue().toString();
                        String mUsername = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").getValue().toString();

                        int i = Integer.parseInt(mEnd);
                        mAutoReferCode.child("user" + i).child("refercode").setValue(mUsername);

                        String endCount = Integer.toString(i + 1);

                        mAutoReferCode.child("end").setValue(endCount);
                        //AutoReferCodeEnd  */


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
