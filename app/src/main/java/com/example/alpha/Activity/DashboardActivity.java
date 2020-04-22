package com.example.alpha.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Model.ParentClass;
import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.example.alpha.Registration.PaytmPayment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DashboardActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private DashboardActivity dashboardActivity;
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
    private SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss aa");
    private String timeformat = time.format(c.getTime());
    private String datetime = dateformat.format(c.getTime());
    private FloatingActionButton fabRefresh;
    final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    Toolbar toolbar;
    private Dialog dialog;
    private ProgressBar beginnerUpgradePB, beginnerPaymentProgress, beginnerReferProgressBar, beginnerChildProgressBar, bronzePaymentProgressBar;
    private DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mTransactionsRecycler, dbPaytm, mTransactions1, mAchievements;
    private LinearLayout beginnerLayout, beginnerExpand, bronzeLayout, bronzeExpand, silverLayout;
    private ImageView beginnerCircle, beginnerPaymentCircle, beginnerPaymentGreenCheck, beginnerReferCirle, beginnerReferGreenCheck,
            beginnerChildCirle, beginnerChildGreenCheck, bronzeCircle, bronzePaymentCircle;
    private TextView beginnerText, beginnerPaymentText, beginnerReferText, beginnerChildText, bronzePaymentText, beginnerPaymentClick,
            beginnerReferClick, beginnerChildClick;

    private Button beginnerUpgradeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        mAchievements = FirebaseDatabase.getInstance().getReference("Users").child(selfUid)
                .child("Achievements");
        mRef = FirebaseDatabase.getInstance().getReference("Users");

        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");
        mFirebase = FirebaseDatabase.getInstance().getReference();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //fabRefresh
        fabRefresh = findViewById(R.id.fab_refresh);

        fabRefresh.setOnClickListener(v -> {
            fabRefresh.setEnabled(false);

            fabRefresh.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.rotate));
            new Handler().postDelayed(() -> {

                dashboard();
                fabRefresh.setEnabled(true);
            }, 500);
        });

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
        beginnerText = findViewById(R.id.beginnerText);
        beginnerPaymentClick = findViewById(R.id.beginnerPaymentClick);
        beginnerReferClick = findViewById(R.id.beginnerReferClick);

        beginnerUpgradeButton = findViewById(R.id.beginnerUpgradeButton);

        beginnerPaymentProgress = findViewById(R.id.beginnerPaymentProgress);
        beginnerReferProgressBar = findViewById(R.id.beginnerReferProgressBar);
        beginnerChildProgressBar = findViewById(R.id.beginnerChildProgressBar);
        beginnerUpgradePB = findViewById(R.id.beginnerUpgradePB);

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

        beginnerDashboard();
        bronzeDashboard();


    }


    private void beginnerDashboard() {

        try {
            mAchievements.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {


                        String mBeginner = Objects.requireNonNull(dataSnapshot.child("beginner").getValue()).toString();
                        beginnerText.setText(mBeginner);

                        if (mBeginner.equals("unlocked")) {
                            mRef.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String parentStatus = Objects.requireNonNull(dataSnapshot.child("parentStatus").getValue()).toString();
                                    String paymentStatus = Objects.requireNonNull(dataSnapshot.child("paymentStatus").getValue()).toString();

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

                                        beginnerUpgradeButton.setVisibility(View.VISIBLE);
                                        beginnerUpgradeButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                beginnerUpgradeButton.setVisibility(View.GONE);
                                                //ProgressBar
                                                progressDialog = new ProgressDialog(DashboardActivity.this);
                                                progressDialog.setCancelable(false);
                                                progressDialog.setCanceledOnTouchOutside(false);
                                                progressDialog.show();
                                                progressDialog.setContentView(R.layout.progress_dialog_new);
                                                Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                                                TextView Ptext;
                                                Ptext = progressDialog.findViewById(R.id.Ptext);
                                                Ptext.setVisibility(View.VISIBLE);

                                                //upgradeFromLevel0();
                                            }
                                        });


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

                        } else if (mBeginner.equals("completed")) {

                            beginnerCircle.setImageResource(R.drawable.green_chechk);
                            beginnerExpand.setVisibility(View.GONE);
                            //Payment
                            beginnerPaymentProgress.setVisibility(View.GONE);
                            beginnerPaymentCircle.setVisibility(View.VISIBLE);
                            beginnerPaymentClick.setVisibility(View.GONE);
                            beginnerPaymentCircle.setImageResource(R.drawable.green_chechk);
                            beginnerPaymentText.setText("Payment Success");
                            beginnerPaymentText.setTypeface(beginnerPaymentText.getTypeface(), Typeface.BOLD);
                            beginnerPaymentText.setTextColor(getResources().getColor(R.color.colorPrimary));

                            //ReferCode
                            beginnerReferProgressBar.setVisibility(View.GONE);
                            beginnerReferClick.setVisibility(View.GONE);
                            beginnerReferCirle.setVisibility(View.VISIBLE);
                            beginnerReferCirle.setImageResource(R.drawable.green_chechk);
                            beginnerReferText.setTypeface(beginnerReferText.getTypeface(), Typeface.BOLD);
                            beginnerReferText.setText("Refercode success");
                            beginnerReferText.setTextColor(getResources().getColor(R.color.colorPrimary));

                        }


                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void bronzeDashboard() {
        try {
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

            mRef.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        beginnerChildText.setOnClickListener(v -> {
                            try {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Dream Winner");
                                String shareMessage = "\nInterested to play PUBG Tounarments? Want to make some cash out of it?? Try out DreamWinner, an eSports Platform. Join Daily PUBG Matches & Get Rewards on Each Kill you Score. Get Huge Prize on Getting Chicken Dinner. Just Download the DreamWinner Android App & Register and Prove your Skills \n\n";
                                shareMessage = shareMessage + "Download Link:\n" + "\n https://dreamwinner.in";
                                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                startActivity(Intent.createChooser(shareIntent, "choose one"));
                            } catch (Exception e) {
                                //e.toString();
                            }
                        });


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

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //ReferDialog
    private void referDialog() {
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //ReferCode
    private void Child(final String mReferCode) {

        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
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


    public void upgradeFromLevel0() {
        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Dialog

                String p1 = dataSnapshot.child("Users").child(selfUid).child("Chain").child("parent").child("p1").getValue().toString();
                String level = dataSnapshot.child("Users").child(selfUid).child("level").getValue().toString();
                String user_bal = dataSnapshot.child("Wallet").child(selfUid).child("balance").getValue().toString();
                String p1Status = dataSnapshot.child("Wallet").child(p1).child("Status").child("status").getValue().toString();
                String userStatusUid = dataSnapshot.child("Wallet").child(p1).child("Status").child("uid").getValue().toString();


                if ((level.equals("0")) && (user_bal.equals("50"))) {


                    if (p1Status.equals("free")) {


                        mWallet.child(p1).child("Status").child("status").setValue("busy");
                        mWallet.child(p1).child("Status").child("uid").setValue(selfUid);

                        //reducing money in user wallet
                        int user_bal_Int = Integer.parseInt(user_bal);

                        String user_updated_bal = Integer.toString(user_bal_Int - 50);

                        mWallet.child(selfUid).child("balance").setValue(user_updated_bal);

                        //adding money to parent1
                        String p1_bal = Objects.requireNonNull(dataSnapshot.child("Wallet").child(p1).child("balance").getValue()).toString();
                        int p1_bal_Int = Integer.parseInt(p1_bal);

                        String p1_updated_bal = Integer.toString(p1_bal_Int + 50);

                        mWallet.child(p1).child("balance").setValue(p1_updated_bal);


                        //AutoLoginCode
                        //String p1_email = dataSnapshot.child("Users").child(p1).child("email").getValue().toString();
                        //String p1_password = dataSnapshot.child("Users").child(p1).child("password").getValue().toString();
                        //String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                        //int i = Integer.parseInt(mEnd);

                        //mLogin.child("user" + i).child("uid").setValue(p1);
                        //mLogin.child("user" + i).child("email").setValue(p1_email);
                        //mLogin.child("user" + i).child("password").setValue(p1_password);

                        //String endCount = Integer.toString(i + 1);
                        //mLogin.child("end").setValue(endCount);

                        //MainTransactions
                        String user_userName = Objects.requireNonNull(dataSnapshot.child("Users").child(selfUid).child("username").getValue()).toString();
                        String p1_userName = Objects.requireNonNull(dataSnapshot.child("Users").child(p1).child("username").getValue()).toString();
                        String id = UUID.randomUUID().toString();
                        String childid = "PW" + id.substring(0, 5).toUpperCase();
                        String extraid = id.substring(0, 4).toUpperCase();


                        //Main Transactions
                        if (dataSnapshot.child("Transactions").child(childid).exists()) {


                            Transaction_Class send_transaction_class = new Transaction_Class(
                                    "debited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p1_userName,
                                    childid,
                                    "50",
                                    1,
                                    "level1"
                            );
                            mFirebase.child("Transactions").child(childid + extraid).setValue(send_transaction_class);

                        } else {

                            Transaction_Class send_transaction_class = new Transaction_Class(
                                    "debited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p1_userName,
                                    childid,
                                    "50",
                                    1,
                                    "level1"

                            );
                            mFirebase.child("Transactions").child(childid).setValue(send_transaction_class);


                        }

                        //sendTransaction in user


                        if (dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                                .child("history").exists()) {
                            long countR = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                                    .child("history").getChildrenCount();

                            long sizeR = countR + 1;


                            Transaction_Class send_transaction_class = new Transaction_Class(
                                    "debited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p1_userName,
                                    childid,
                                    "50",
                                    sizeR,
                                    "level1"
                            );
                            mWallet.child(selfUid).child("Transactions").child("history").child(childid).setValue(send_transaction_class);

                        } else {

                            Transaction_Class send_transaction_class = new Transaction_Class(
                                    "debited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p1_userName,
                                    childid,
                                    "50",
                                    1,
                                    "level1"
                            );
                            mWallet.child(selfUid).child("Transactions").child("history").child(childid).setValue(send_transaction_class);


                        }


                        //receivedTransaction in parent1


                        if (dataSnapshot.child("Wallet").child(p1).child("Transactions")
                                .child("history").exists()) {
                            long countP = dataSnapshot.child("Wallet").child(p1).child("Transactions")
                                    .child("history").getChildrenCount();

                            long sizeP = countP + 1;
                            Transaction_Class received_transaction_class = new Transaction_Class(
                                    "credited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p1_userName,
                                    childid,
                                    "50",
                                    sizeP,
                                    "level1"


                            );
                            mWallet.child(p1).child("Transactions").child("history").child(childid).setValue(received_transaction_class);

                            String p1_tran_count = dataSnapshot.child("Wallet").child(p1).child("Transactions").child("count").child("level1").getValue().toString();
                            int p1_tran_count_Int = Integer.parseInt(p1_tran_count);
                            String updated_p1_tran_count = Integer.toString(p1_tran_count_Int + 1);

                            mWallet.child(p1).child("Transactions").child("count").child("level1").setValue(updated_p1_tran_count);


                        } else {
                            Transaction_Class received_transaction_class = new Transaction_Class(
                                    "credited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p1_userName,
                                    childid,
                                    "50",
                                    1,
                                    "level1"

                            );
                            mWallet.child(p1).child("Transactions").child("history").child(childid).setValue(received_transaction_class);

                            String p1_tran_count = dataSnapshot.child("Wallet").child(p1).child("Transactions").child("count").child("level1").getValue().toString();
                            int p1_tran_count_Int = Integer.parseInt(p1_tran_count);
                            String updated_p1_tran_count = Integer.toString(p1_tran_count_Int + 1);

                            mWallet.child(p1).child("Transactions").child("count").child("level1").setValue(updated_p1_tran_count);
                        }


                        //Upgrading level
                        mRef.child(selfUid).child("level").setValue("1");


                        mAchievements.child("beginner").setValue("completed");
                        mAchievements.child("bronze").setValue("unlocked");
                        mAchievements.child("silver").setValue("unlocked");
                        mAchievements.child("gold").setValue("unlocked");
                        mAchievements.child("diamond").setValue("unlocked");


                        mWallet.child(p1).child("Status").child("status").setValue("free");
                        mWallet.child(p1).child("Status").child("uid").setValue("null");


                    } else if (p1Status.equals("busy")) {

                        dialog.show();

                        new CountDownTimer(15000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                //progress_bar_text.setText("Refreshing in: " + millisUntilFinished / 1000);
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                //progress_bar_text.setText("done!");
                            }

                        }.start();

                        new Handler().postDelayed(() -> {

                            dialog.dismiss();
                            upgradeFromLevel0();

                        }, 15000);

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStop() {

        super.onStop();
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
