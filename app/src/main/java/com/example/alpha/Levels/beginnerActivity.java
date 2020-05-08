package com.example.alpha.Levels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Activity.HelpActivity;
import com.example.alpha.Model.ParentClass;
import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.example.alpha.Registration.PaytmPayment;
import com.example.alpha.Utils.ViewAnimation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class beginnerActivity extends AppCompatActivity {


    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
    private SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss aa");
    private String timeformat = time.format(c.getTime());
    private String datetime = dateformat.format(c.getTime());


    private List<View> view_list = new ArrayList<>();
    private List<RelativeLayout> step_view_list = new ArrayList<>();
    private int success_step = 0;
    private int current_step = 0;


    final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private DatabaseReference mFirebase, mUsers, mChain, mWallet, mAchievement;


    RelativeLayout PaymentRelative, ReferCodeRelative;
    LinearLayout upgrade;
    ImageView PaymentImageView;
    ProgressBar PaymentProgress, upgradeProgressBar;
    TextView PaymentText;
    Button paymentButton, referCodeButton;
    ProgressBar referCodeProgressBar;
    EditText editTextReferCode;
    TextView upgradeTextView;


    Toolbar toolbar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginner);

        mFirebase = FirebaseDatabase.getInstance().getReference();
        mUsers = FirebaseDatabase.getInstance().getReference("Users");
        mChain = FirebaseDatabase.getInstance().getReference("Chain");
        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");


        upgrade = findViewById(R.id.upgrade);
        upgrade.setEnabled(false);
        upgradeProgressBar = findViewById(R.id.upgradeProgressBar);
        upgradeTextView = findViewById(R.id.upgradeTextView);


        PaymentRelative = findViewById(R.id.PaymentRelative);
        PaymentImageView = findViewById(R.id.PaymentImageView);
        PaymentProgress = findViewById(R.id.PaymentProgress);
        PaymentText = findViewById(R.id.PaymentText);
        paymentButton = findViewById(R.id.paymentButton);


        ReferCodeRelative = findViewById(R.id.ReferCodeRelative);
        referCodeButton = findViewById(R.id.referCodeButton);
        editTextReferCode = findViewById(R.id.referCode);
        referCodeProgressBar = findViewById(R.id.referCodeProgressBar);

        editTextReferCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        toolbar();
        initComponent();
        checkStatus();
    }


    private void toolbar() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void initComponent() {
        // populate layout field
        view_list.add(findViewById(R.id.lyt_title));
        view_list.add(findViewById(R.id.lyt_description));
        view_list.add(findViewById(R.id.lyt_confirmation));

        // populate view step (circle in left)
        step_view_list.add(findViewById(R.id.PaymentRelative));
        step_view_list.add(findViewById(R.id.ReferCodeRelative));
        step_view_list.add(findViewById(R.id.step_confirmation));

        for (View v : view_list) {
            v.setVisibility(View.GONE);
        }

        view_list.get(0).setVisibility(View.VISIBLE);
        hideSoftKeyboard();

    }

    private void checkStatus() {
        // Check Payment Status

        mUsers.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String paymentStatus = Objects.requireNonNull(dataSnapshot.child("paymentStatus").getValue()).toString();

                if (paymentStatus.equals("true")) {

                    PaymentRelative.setBackgroundResource(R.drawable.shape_round_solid_green);
                    PaymentImageView.setImageResource(R.drawable.green_chechk);
                    PaymentText.setText("Payment Success");
                    paymentButton.setText("N E X T");
                    collapseAndContinue(0);
                    paymentButton.setOnClickListener(v -> {

                        collapseAndContinue(0);
                    });

                } else {
                    PaymentRelative.setBackgroundResource(R.drawable.shape_round_solid);
                    PaymentImageView.setImageResource(R.drawable.red_check);
                    PaymentText.setText("Payment Incomplete");
                    PaymentText.setTypeface(PaymentText.getTypeface(), Typeface.BOLD);
                    paymentButton.setText("PAY NOW");
                    paymentButton.setOnClickListener(v -> {
                        startActivity(new Intent(beginnerActivity.this, PaytmPayment.class));
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Check ReferCode Status

        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String parentStatus = Objects.requireNonNull(dataSnapshot.child(selfUid).child("parentStatus").getValue()).toString();

                if (parentStatus.equals("true")) {

                    String p1 = dataSnapshot.child(selfUid).child("Chain").child("parent").child("p1").getValue().toString();

                    String p1referCode = dataSnapshot.child(p1).child("username").getValue().toString();

                    ReferCodeRelative.setBackgroundResource(R.drawable.shape_round_solid_green);
                    editTextReferCode.setText(p1referCode);
                    editTextReferCode.setEnabled(false);
                    referCodeButton.setText("N E X T");
                    collapseAndContinue(1);
                    upgrade.setEnabled(true);
                    upgrade.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    upgrade.setOnClickListener(v -> {
                        upgradeMethod();
                        upgradeProgressBar.setVisibility(View.VISIBLE);
                        upgradeTextView.setVisibility(View.GONE);
                    });
                    referCodeButton.setOnClickListener(v -> collapseAndContinue(1));

                } else {
                    ReferCodeRelative.setBackgroundResource(R.drawable.shape_round_solid);
                    referCodeButton.setOnClickListener(v -> {
                        referCode();
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void upgradeMethod() {


        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String p1 = dataSnapshot.child("Users").child(selfUid).child("Chain").child("parent").child("p1").getValue().toString();
                String mAchievement = dataSnapshot.child("Users").child(selfUid).child("Achievement").getValue().toString();
                String user_bal = dataSnapshot.child("Wallet").child(selfUid).child("balance").getValue().toString();
                String p1Status = dataSnapshot.child("Wallet").child(p1).child("Status").child("status").getValue().toString();
                String userStatusUid = dataSnapshot.child("Wallet").child(p1).child("Status").child("uid").getValue().toString();

                //ProgressBar
                progressDialog = new ProgressDialog(beginnerActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog_new);
                Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                TextView Ptext;
                Ptext = progressDialog.findViewById(R.id.Ptext);
                Ptext.setVisibility(View.VISIBLE);

                if ((mAchievement.equals("Beginner")) && (user_bal.equals("50"))) {
                    progressDialog.show();

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
                        mUsers.child(selfUid).child("level").setValue("1");


                        mUsers.child(selfUid).child("Achievement").setValue("Bronze");


                        mWallet.child(p1).child("Status").child("status").setValue("free");
                        mWallet.child(p1).child("Status").child("uid").setValue("null");
                        new Handler().postDelayed(() -> {
                            progressDialog.dismiss();
                            upgradeProgressBar.setVisibility(View.GONE);
                            upgradeTextView.setVisibility(View.VISIBLE);
                            upgradeTextView.setText("U P G R A D E D");

                        }, 1000);

                        Toast.makeText(beginnerActivity.this, "Successfull", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(beginnerActivity.this, " Parent wallet is busy , try again in 15 seconds", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        upgradeProgressBar.setVisibility(View.GONE);
                        upgradeTextView.setVisibility(View.VISIBLE);


                        /*new CountDownTimer(15000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                //progress_bar_text.setText("Refreshing in: " + millisUntilFinished / 1000);
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                //progress_bar_text.setText("done!");
                            }

                        }.start();

                        new Handler().postDelayed(() -> {


                        }, 0); */

                    }


                } else {
                    progressDialog.dismiss();
                    upgradeProgressBar.setVisibility(View.GONE);
                    upgradeTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(beginnerActivity.this, "Invlaid level or balance", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void clickLabel(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_label_title:
                if (success_step >= 0 && current_step != 0) {
                    current_step = 0;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(0));
                }
                break;
            case R.id.tv_label_description:
                if (success_step >= 1 && current_step != 1) {
                    current_step = 1;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(1));
                }
                break;
            case R.id.tv_label_confirmation:
                if (success_step >= 2 && current_step != 2) {
                    current_step = 2;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(2));
                }
                break;
        }
    }

    private void collapseAndContinue(int index) {
        ViewAnimation.collapse(view_list.get(index));
        setCheckedStep(index);
        index++;
        current_step = index;
        success_step = index > success_step ? index : success_step;
        ViewAnimation.expand(view_list.get(index));
    }

    private void collapseAll() {
        for (View v : view_list) {
            ViewAnimation.collapse(v);
        }
    }

    private void setCheckedStep(int index) {
        RelativeLayout relative = step_view_list.get(index);
        relative.removeAllViews();
        ImageButton img = new ImageButton(this);
        img.setImageResource(R.drawable.ic_done);
        img.setBackgroundColor(Color.TRANSPARENT);
        img.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        relative.addView(img);
    }


    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    //ReferCode
    private void referCode() {
        try {

            final String mReferCode = editTextReferCode.getText().toString().trim();
            editTextReferCode.setEnabled(false);
            referCodeButton.setVisibility(View.GONE);
            referCodeProgressBar.setVisibility(View.VISIBLE);
            final DatabaseReference ReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");

            ReferDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot checkdataSnapshot) {

                    if (mReferCode.isEmpty()) {
                        editTextReferCode.setError("Enter ReferCode");
                        editTextReferCode.requestFocus();
                        referCodeProgressBar.setVisibility(View.GONE);
                        referCodeButton.setVisibility(View.VISIBLE);
                        editTextReferCode.setEnabled(true);

                    } else if (checkdataSnapshot.hasChild(mReferCode)) {

                        Child(mReferCode);


                    } else if (!checkdataSnapshot.hasChild(mReferCode)) {
                        editTextReferCode.setError("Invalid ReferCode");
                        editTextReferCode.requestFocus();
                        referCodeProgressBar.setVisibility(View.GONE);
                        referCodeButton.setVisibility(View.VISIBLE);
                        editTextReferCode.setEnabled(true);

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

    //ReferCodeChild
    private void Child(final String mReferCode) {

        try {
            mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String referUid = dataSnapshot.child("ReferDB").child(mReferCode).child("uid").getValue().toString();
                    String childCount = dataSnapshot.child("Users").child(referUid).child("childCount").getValue().toString();
                    String parentStatus = dataSnapshot.child("Users").child(referUid).child("parentStatus").getValue().toString();
                    String userName = dataSnapshot.child("Users").child(selfUid).child("username").getValue().toString();

                    if (!userName.equals(mReferCode)) {
                        String p1 = Objects.requireNonNull(dataSnapshot.child("Users").child(referUid).child("Chain")
                                .child("parent").child("p1").getValue()).toString();

                        if ((parentStatus.equals("true")) && !p1.equals("null")) {
                            if (childCount.equals("0")) {
                                referDetails(mReferCode);
                                mChain.child(referUid).child("uid1").child("uid").setValue(selfUid);
                                mChain.child(referUid).child("uid1").child("username").setValue(userName);
                                mFirebase.child("Users").child(referUid).child("childCount").setValue("1");

                            } else if (childCount.equals("1")) {
                                referDetails(mReferCode);
                                mChain.child(referUid).child("uid2").child("uid").setValue(selfUid);
                                mChain.child(referUid).child("uid2").child("username").setValue(userName);
                                mFirebase.child("Users").child(referUid).child("childCount").setValue("2");

                            } else {
                                //editTextReferCode.setError("Limit Exceeded");
                                //editTextReferCode.requestFocus();
                                Toast.makeText(beginnerActivity.this, "Limit exceeded , Try another Refer Code", Toast.LENGTH_SHORT).show();
                                referCodeProgressBar.setVisibility(View.GONE);
                                referCodeButton.setVisibility(View.VISIBLE);
                                editTextReferCode.setEnabled(true);
                            }
                        } else {
                            Toast.makeText(beginnerActivity.this, mReferCode + " have no parent", Toast.LENGTH_SHORT).show();
                            referCodeProgressBar.setVisibility(View.GONE);
                            referCodeButton.setVisibility(View.VISIBLE);
                            editTextReferCode.setEnabled(true);
                        }
                    } else {
                        Toast.makeText(beginnerActivity.this, "IDIOT!! Donot enter Your USERNAME", Toast.LENGTH_SHORT).show();
                        referCodeProgressBar.setVisibility(View.GONE);
                        referCodeButton.setVisibility(View.VISIBLE);
                        editTextReferCode.setEnabled(true);
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


    //ReferCodeData
    private void referDetails(final String mReferCode) {
        try {


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

                    mUsers.child(selfUid).child("parentStatus").setValue("true");


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

                    mUsers.child(selfUid).child("Chain")
                            .child("parent").setValue(parentClass);


                    Toast.makeText(beginnerActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> {
                        checkStatus();
                        referCodeProgressBar.setVisibility(View.GONE);
                        referCodeButton.setVisibility(View.VISIBLE);
                        Toast.makeText(beginnerActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();

                    }, 0);


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
            Intent intent = new Intent(beginnerActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
