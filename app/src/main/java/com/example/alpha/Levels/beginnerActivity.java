package com.example.alpha.Levels;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Activity.HelpActivity;
import com.example.alpha.Model.ParentClass;
import com.example.alpha.R;
import com.example.alpha.Registration.PaytmPayment;
import com.example.alpha.Utils.ViewAnimation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class beginnerActivity extends AppCompatActivity {

    private List<View> view_list = new ArrayList<>();
    private List<RelativeLayout> step_view_list = new ArrayList<>();
    private int success_step = 0;
    private int current_step = 0;
    private View parent_view;
    private Date date = null;
    private String time = null;


    final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private DatabaseReference mFirebase, mUsers, mChain;


    RelativeLayout PaymentRelative, ReferCodeRelative;
    ImageView PaymentImageView;
    ProgressBar PaymentProgress;
    TextView PaymentText;
    Button paymentButton, referCodeButton;
    ProgressBar referCodeProgressBar;
    EditText editTextReferCode;


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginner);

        mFirebase = FirebaseDatabase.getInstance().getReference();
        mUsers = FirebaseDatabase.getInstance().getReference("Users");
        mChain = FirebaseDatabase.getInstance().getReference("Chain");


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

    //ReferCode
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


    //ReferCode
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
