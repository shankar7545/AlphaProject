package com.example.alpha.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.example.alpha.Activity.HomeActivity;
import com.example.alpha.Activity.MyProfile;
import com.example.alpha.Model.ParentClass;
import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.example.alpha.Registration.PaytmPayment;
import com.example.alpha.ViewHolder.TransactionView;
import com.example.alpha.Wallet.TransactionsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {


    private String selfUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
    private SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss aa");
    private String timeformat = time.format(c.getTime());
    private String datetime = dateformat.format(c.getTime());
    private FloatingActionButton fabRefresh;
    private EditText editTextReferCode;
    FirebaseRecyclerAdapter<Transaction_Class, TransactionView> TransactionsAdapter;
    private NestedScrollView transactions_linear;
    private Dialog dialog;
    private View mView;
    private RecyclerView transactionsRecycler;
    private LinearLayoutManager transactionsLinearLayout;

    CircularImageView profilePic;
    //Dashboard
    private ProgressBar beginnerPaymentProgress, beginnerReferProgressBar, beginnerChildProgressBar, bronzePaymentProgressBar;
    private DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mTransactionsRecycler, dbPaytm, mTransactions1;
    private LinearLayout beginnerLayout, beginnerExpand, bronzeLayout, bronzeExpand, silverLayout;
    private ImageView beginnerCircle, beginnerPaymentCircle, beginnerPaymentGreenCheck, beginnerReferCirle, beginnerReferGreenCheck,
            beginnerChildCirle, beginnerChildGreenCheck, bronzeCircle, bronzePaymentCircle;
    private TextView beginnerPaymentText, beginnerReferText, beginnerChildText, bronzePaymentText, beginnerPaymentClick,
            beginnerReferClick, beginnerChildClick;
    private SliderLayout mDemoSlider;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        fabRefresh = mView.findViewById(R.id.fab_refresh);
        profilePic = mView.findViewById(R.id.ProfilePic);
        editTextReferCode = mView.findViewById(R.id.referCode);
        //wallet_bal =(TextView)mView.findViewById(R.id.balanceH);
        //level =(TextView)mView.findViewById(R.id.levelH);
        dbPaytm = FirebaseDatabase.getInstance().getReference("Paytm");
        mFirebase = FirebaseDatabase.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");
        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet")
                .child(selfUid).child("Transactions");

        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference();
        scoresRef.keepSynced(true);


        //DashBoard
        beginnerLayout = mView.findViewById(R.id.beginnerLayout);
        beginnerExpand = mView.findViewById(R.id.beginnerExpand);
        bronzeLayout = mView.findViewById(R.id.bronzeLayout);
        bronzeExpand = mView.findViewById(R.id.bronzeExpand);
        silverLayout = mView.findViewById(R.id.silverLayout);


        beginnerCircle = mView.findViewById(R.id.beginnerCircle);
        beginnerReferCirle = mView.findViewById(R.id.beginnerReferCirle);
        beginnerChildCirle = mView.findViewById(R.id.beginnerChildCirle);
        beginnerPaymentCircle = mView.findViewById(R.id.beginnerPaymentCircle);
        beginnerReferGreenCheck = mView.findViewById(R.id.beginnerReferGreenCheck);
        beginnerChildGreenCheck = mView.findViewById(R.id.beginnerChildGreenCheck);
        beginnerPaymentGreenCheck = mView.findViewById(R.id.beginnerPaymentGreenCheck);

        beginnerPaymentText = mView.findViewById(R.id.beginnerPaymentText);
        beginnerReferText = mView.findViewById(R.id.beginnerReferText);
        beginnerChildText = mView.findViewById(R.id.beginnerChildText);
        beginnerPaymentClick = mView.findViewById(R.id.beginnerPaymentClick);
        beginnerReferClick = mView.findViewById(R.id.beginnerReferClick);


        beginnerPaymentProgress = mView.findViewById(R.id.beginnerPaymentProgress);
        beginnerReferProgressBar = mView.findViewById(R.id.beginnerReferProgressBar);
        beginnerChildProgressBar = mView.findViewById(R.id.beginnerChildProgressBar);


        bronzeCircle = mView.findViewById(R.id.bronzeCircle);
        bronzePaymentText = mView.findViewById(R.id.bronzePaymentText);
        bronzePaymentCircle = mView.findViewById(R.id.bronzePaymentCircle);
        bronzePaymentProgressBar = mView.findViewById(R.id.bronzePaymentProgressBar);


        beginnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beginnerExpand.getVisibility() == View.VISIBLE) {

                    beginnerExpand.setVisibility(View.GONE);

                } else {

                    beginnerExpand.setVisibility(View.VISIBLE);


                }
            }
        });

        dashboard();


        //Recycler For Transactions


        transactions_linear = mView.findViewById(R.id.transactionsHome_linear);

        transactionsRecycler = mView.findViewById(R.id.transactionsHomeRecycler);

        transactionsRecycler.hasFixedSize();


        transactionsLinearLayout = new LinearLayoutManager(getContext());
        transactionsLinearLayout.setReverseLayout(true);
        transactionsLinearLayout.setStackFromEnd(true);

        transactionsRecycler.setLayoutManager(transactionsLinearLayout);


        mTransactionsRecycler = FirebaseDatabase.getInstance().getReference("Wallet")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        mTransactionsRecycler.child("Transactions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("history").exists()) {

                    transactions_linear.setVisibility(View.VISIBLE);

                } else {
                    transactions_linear.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabRefresh.setEnabled(false);

                fabRefresh.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((HomeActivity) getActivity()).refreshMyData();
                        fabRefresh.setEnabled(true);
                    }
                }, 500);
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyProfile.class);
                startActivity(intent);
            }
        });


        CheckingLevelUpgrades();
        //loadTransactions();
        Slider();
        return mView;

    }

    private void dashboard() {

        try {

            mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final String transactionCount_level1 = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                            .child("count").child("level1").getValue().toString();
                    final String transactionCount_level2 = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                            .child("count").child("level2").getValue().toString();
                    final String transactionCount_level3 = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                            .child("count").child("level3").getValue().toString();
                    final String transactionCount_level4 = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                            .child("count").child("level4").getValue().toString();
                    final String transactionCount_level5 = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                            .child("count").child("level5").getValue().toString();
                    final String transactionCount_level6 = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                            .child("count").child("level6").getValue().toString();

                    final String user_level = dataSnapshot.child("Users").child(selfUid).child("level").getValue().toString();
                    int count = Integer.parseInt(user_level);


                    mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    String parentStatus = dataSnapshot.child("parentStatus").getValue().toString();
                                    final String paymentStatus = dataSnapshot.child("paymentStatus").getValue().toString();
                                    String childCount = dataSnapshot.child("childCount").getValue().toString();


                                    if ((parentStatus.equals("true")) && paymentStatus.equals("true") && childCount.equals("2")) {
                                        beginnerCircle.setImageResource(R.drawable.green_chechk);
                                        beginnerExpand.setVisibility(View.GONE);


                                    } else {
                                        beginnerExpand.setVisibility(View.VISIBLE);
                                        beginnerCircle.setImageResource(R.drawable.ic_circle);
                                        beginnerExpand.setVisibility(View.VISIBLE);
                                        bronzeCircle.setImageResource(R.drawable.ic_lock);


                                    }
                                    if (paymentStatus.equals("true")) {
                                        beginnerPaymentProgress.setVisibility(View.GONE);
                                        beginnerPaymentCircle.setVisibility(View.VISIBLE);
                                        beginnerPaymentClick.setVisibility(View.GONE);
                                        beginnerPaymentCircle.setImageResource(R.drawable.green_chechk);
                                        beginnerPaymentText.setText("PAYMENT COMPLETED");
                                        beginnerPaymentText.setTypeface(beginnerPaymentText.getTypeface(), Typeface.BOLD);
                                        beginnerPaymentText.setTextColor(getResources().getColor(R.color.green_800));
                                        beginnerReferText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        beginnerReferClick.setEnabled(true);
                                    } else {
                                        beginnerPaymentProgress.setVisibility(View.GONE);
                                        beginnerPaymentCircle.setVisibility(View.VISIBLE);
                                        beginnerPaymentCircle.setImageResource(R.drawable.ic_circle);
                                        beginnerPaymentText.setTypeface(beginnerPaymentText.getTypeface(), Typeface.NORMAL);
                                        beginnerPaymentClick.setVisibility(View.VISIBLE);
                                        beginnerPaymentText.setText("to Complete Payment");
                                        beginnerReferClick.setTextColor(getResources().getColor(R.color.grey_40));
                                        beginnerReferClick.setEnabled(false);


                                        beginnerPaymentClick.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getContext(), PaytmPayment.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    if (parentStatus.equals("true")) {
                                        beginnerReferProgressBar.setVisibility(View.GONE);
                                        beginnerReferClick.setVisibility(View.GONE);
                                        beginnerReferCirle.setVisibility(View.VISIBLE);
                                        beginnerReferCirle.setImageResource(R.drawable.green_chechk);
                                        beginnerReferText.setTypeface(beginnerReferText.getTypeface(), Typeface.BOLD);
                                        beginnerReferText.setText("REFERCODE FOUND");
                                        beginnerReferText.setTextColor(getResources().getColor(R.color.green_800));
                                    } else {
                                        beginnerReferProgressBar.setVisibility(View.GONE);
                                        beginnerReferCirle.setVisibility(View.VISIBLE);
                                        beginnerReferClick.setVisibility(View.VISIBLE);
                                        beginnerReferCirle.setImageResource(R.drawable.ic_circle);
                                        beginnerReferText.setText("to enter Refercode");
                                        beginnerReferText.setTypeface(beginnerReferText.getTypeface(), Typeface.NORMAL);

                                        beginnerReferClick.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Intent intent = new Intent(getContext(), ReferCodeAcitvity.class);
                                                // startActivity(intent);
                                                referDialog();


                                            }
                                        });
                                    }

                                    if (childCount.equals("2")) {
                                        beginnerChildProgressBar.setVisibility(View.GONE);
                                        beginnerChildCirle.setVisibility(View.VISIBLE);
                                        beginnerChildCirle.setImageResource(R.drawable.green_chechk);
                                        beginnerChildText.setText("REFERED  2/2");
                                        beginnerChildText.setTypeface(beginnerChildText.getTypeface(), Typeface.BOLD);
                                        beginnerChildText.setTextColor(getResources().getColor(R.color.green_800));
                                    } else if (childCount.equals("1")) {

                                        beginnerChildProgressBar.setVisibility(View.GONE);
                                        beginnerChildCirle.setVisibility(View.VISIBLE);
                                        beginnerChildCirle.setImageResource(R.drawable.ic_circle);
                                        beginnerChildText.setText("REFER ONE MORE");
                                        beginnerChildText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        beginnerChildText.setTypeface(beginnerChildText.getTypeface(), Typeface.BOLD);
                                        if (parentStatus.equals("true")) {
                                            beginnerChildText.setTextColor(getResources().getColor(R.color.colorPrimary));

                                            beginnerChildText.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    Toast.makeText(getContext(), "Refer Clicked", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            beginnerChildText.setTextColor(getResources().getColor(R.color.grey_40));


                                        }

                                    } else {
                                        beginnerChildProgressBar.setVisibility(View.GONE);
                                        beginnerChildCirle.setVisibility(View.VISIBLE);
                                        beginnerChildCirle.setImageResource(R.drawable.ic_circle);
                                        beginnerChildText.setText("REFER TWO USERS");
                                        beginnerChildText.setTypeface(beginnerChildText.getTypeface(), Typeface.BOLD);

                                        if (parentStatus.equals("true")) {
                                            beginnerChildText.setTextColor(getResources().getColor(R.color.colorPrimary));

                                            beginnerChildText.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Toast.makeText(getContext(), "Refer Clicked", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            beginnerChildText.setTextColor(getResources().getColor(R.color.grey_40));


                                        }

                                    }

                                    if ((parentStatus.equals("true")) && paymentStatus.equals("true")) {
                                        forlevel1();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                    //for Unlocking Levels
                    if (count >= 1) {
                        bronzeCircle.setImageResource(R.drawable.ic_circle);


                        bronzeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (bronzeExpand.getVisibility() == View.VISIBLE) {

                                    bronzeExpand.setVisibility(View.GONE);

                                } else {

                                    bronzeExpand.setVisibility(View.VISIBLE);

                                }
                            }
                        });


                        //BronzeExpand
                        if (transactionCount_level1.equals("2")) {
                            bronzePaymentCircle.setImageResource(R.drawable.green_chechk);
                            bronzePaymentCircle.setVisibility(View.GONE);
                            bronzePaymentProgressBar.setVisibility(View.GONE);
                            bronzePaymentText.setText(transactionCount_level1 + " Payment received");
                            bronzeCircle.setImageResource(R.drawable.green_chechk);

                        } else {
                            bronzePaymentProgressBar.setVisibility(View.GONE);
                            bronzePaymentCircle.setVisibility(View.VISIBLE);
                            bronzePaymentCircle.setImageResource(R.drawable.ic_circle);
                            bronzePaymentText.setText(transactionCount_level1 + " Payment received");
                            bronzeCircle.setImageResource(R.drawable.ic_circle);

                        }


                    } else {
                        bronzeCircle.setImageResource(R.drawable.ic_lock);


                        bronzeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(getContext(), "Complete Beginner Stage to Unlock", Toast.LENGTH_SHORT).show();

                                bronzeLayout.setEnabled(false);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        bronzeLayout.setEnabled(true);
                                    }
                                }, 2000);

                            }
                        });

                    }

                    //AutoExpand Current Level

                    if (count == 1) {
                        bronzeExpand.setVisibility(View.VISIBLE);
                    } else {
                        bronzeExpand.setVisibility(View.GONE);

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


    public void referDialog() {
        dialog = new Dialog(getContext());
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
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    //ReferCode
    private void Child(final String mReferCode) {


        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String referUid = dataSnapshot.child("ReferDB").child(mReferCode).child("uid").getValue().toString();
                String childCount = dataSnapshot.child("Users").child(referUid).child("childCount").getValue().toString();
                String parentStatus = dataSnapshot.child("Users").child(referUid).child("parentStatus").getValue().toString();
                String userName = dataSnapshot.child("Users").child(referUid).child("username").getValue().toString();

                if (!userName.equals(mReferCode)) {
                    String p1 = dataSnapshot.child("Users").child(referUid).child("Chain")
                            .child("parent").child("p1").getValue().toString();


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
                            Toast.makeText(getContext(), "Limit exceeded , Try another Refer Code", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), mReferCode + " have no parent", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "IDIOT!! Donot enter Your USERNAME", Toast.LENGTH_SHORT).show();
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


                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), HomeActivity.class);
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


    //SLider
    private void Slider() {
        mDemoSlider = mView.findViewById(R.id.slider);

        ArrayList<String> listUrl = new ArrayList<>();
        //ArrayList<String> listName = new ArrayList<>();


        //image2
        listUrl.add("https://firebasestorage.googleapis.com/v0/b/jobtrackingsystem-83bad.appspot.com/o/image2.jpeg?alt=media&token=0e1e079d-18c0-4858-9ca0-f088ac32ac8d");
        // listName.add("");

        //image4
        listUrl.add("https://firebasestorage.googleapis.com/v0/b/jobtrackingsystem-83bad.appspot.com/o/image4.jpg?alt=media&token=e8788eef-f8fb-4c86-be13-2d0258d43527");
        //listName.add("");

        //image3
        listUrl.add("https://firebasestorage.googleapis.com/v0/b/jobtrackingsystem-83bad.appspot.com/o/image3.jpg?alt=media&token=4f248df3-b3ba-43da-8560-9cd1b2822926");
        //listName.add("");

        //image1
        listUrl.add("https://firebasestorage.googleapis.com/v0/b/jobtrackingsystem-83bad.appspot.com/o/image1.jpeg?alt=media&token=eb02880d-080b-453a-bddf-39e0672cdb3e");
        //listName.add("");


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();

        for (int i = 0; i < listUrl.size(); i++) {
            TextSliderView sliderView = new TextSliderView(getContext());
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            BaseSliderView baseSliderView = sliderView
                    .image(listUrl.get(i))
                    //.description(listName.get(i))
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());
            // sliderView.getBundle().putString("extra", listName.get(i));
            mDemoSlider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        //mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopCyclingWhenTouch(false);
    }

    private void loadTransactions() {
        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("history");
        mTransactions1 = FirebaseDatabase.getInstance().getReference("Transactions");


        long one = 1;
        Query withdrawList = mTransactions.orderByChild("position");
        FirebaseRecyclerOptions<Transaction_Class> withdrawOption = new FirebaseRecyclerOptions.Builder<Transaction_Class>()
                .setQuery(withdrawList, Transaction_Class.class)
                .build();
        TransactionsAdapter = new FirebaseRecyclerAdapter<Transaction_Class, TransactionView>(withdrawOption) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onBindViewHolder(@NonNull TransactionView holder, final int position, @NonNull final Transaction_Class model) {
                transactions_linear.setVisibility(View.VISIBLE);

                holder.transactionAmount.setText(model.getTransactionAmount());
                holder.transactionDate.setText(model.getTransactionDate());
                holder.transactionTime.setText(model.getTransactionTime());


                String transType = model.getTransactionType();
                {
                    if (transType.equals("credited")) {
                        holder.transactionType.setText("Received from");
                        holder.transactionImage.setImageResource(R.drawable.transaction_received);
                        holder.transactionStatus.setVisibility(View.GONE);
                        holder.transactionName.setText(model.getTransferredFrom());
                        //holder.transactionStatus.setTextColor(getResources().getColor(R.color.green_500));
                        holder.transactionAmount.setTextColor(getResources().getColor(R.color.green_500));

                    } else if (transType.equals("debited")) {
                        holder.transactionType.setText("Paid To");
                        holder.transactionImage.setImageResource(R.drawable.transaction_send);
                        //holder.transactionStatus.setText("Debited");
                        holder.transactionStatus.setVisibility(View.GONE);
                        holder.transactionName.setText(model.getTransferredTo());
                        //holder.transactionStatus.setTextColor(getResources().getColor(R.color.red_500));
                        holder.transactionAmount.setTextColor(getResources().getColor(R.color.red_500));


                    }
                }


                holder.transactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(), TransactionsActivity.class);
                        startActivity(intent);


                    }
                });
            }

            @NonNull
            @Override
            public TransactionView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.transaction_history_layout, viewGroup, false);
                return new TransactionView(itemView);
            }
        };
        transactionsRecycler.setAdapter(TransactionsAdapter);
        TransactionsAdapter.startListening();

    }


    private void CheckingLevelUpgrades() {


        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String user_level = dataSnapshot.child("Users").child(selfUid).child("level").getValue().toString();
                final String transactionCount_level1 = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("level1").getValue().toString();
                final String transactionCount_level2 = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("level2").getValue().toString();
                final String transactionCount_level3 = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("level3").getValue().toString();
                final String transactionCount_level4 = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("level4").getValue().toString();
                final String transactionCount_level5 = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("level5").getValue().toString();
                final String transactionCount_level6 = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("level6").getValue().toString();


                if (user_level.equals("0")) {
                    forlevel1();
                }

                if ((user_level.equals("1")) && (transactionCount_level1.equals("2"))) {
                    forlevel2();

                }
                if ((user_level.equals("2")) && (transactionCount_level2.equals("4"))) {
                    forlevel3();
                }
                if (user_level.equals("3")) {

                }
                if (user_level.equals("4")) {

                }
                if (user_level.equals("5")) {

                }
                if (user_level.equals("6")) {

                }
                if (user_level.equals("7")) {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void forlevel1() {

        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //level1
                String level = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                if (level.equals("0")) {
                    String user_bal = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                    int user_bal_Int = Integer.parseInt(user_bal);
                    String p1 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p1").getValue().toString();
                    //String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p2").getValue().toString();
                    //String p3 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p3").getValue().toString();
                    //String p4 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p4").getValue().toString();
                    //String p5 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p5").getValue().toString();
                    //String p6 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p6").getValue().toString();
                    //String p7 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p7").getValue().toString();
                    //String p8 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p8").getValue().toString();


                    if (user_bal_Int >= 50) {

                        dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.progress_dialogue);
                        dialog.setCancelable(false);
                        final TextView mainHeading = dialog.findViewById(R.id.mainHeading);
                        final TextView subHeading = dialog.findViewById(R.id.subHeading);
                        dialog.show();


                        //reducing money in user wallet

                        String user_updated_bal = Integer.toString(user_bal_Int - 50);

                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(user_updated_bal);

                        //adding money to parent1
                        String p1_bal = dataSnapshot.child("Wallet").child(p1).child("balance").getValue().toString();
                        int p1_bal_Int = Integer.parseInt(p1_bal);

                        String p1_updated_bal = Integer.toString(p1_bal_Int + 50);

                        mWallet.child(p1).child("balance").setValue(p1_updated_bal);
                        subHeading.setText("Transfering money");

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
                        String user_userName = dataSnapshot.child("Users").child(selfUid).child("username").getValue().toString();
                        String p1_userName = dataSnapshot.child("Users").child(p1).child("username").getValue().toString();
                        String id = UUID.randomUUID().toString();
                        String childid = "PW" + id.substring(0, 5).toUpperCase();
                        String extraid = id.substring(0, 4).toUpperCase();


                        if (dataSnapshot.child("Transactions").child(childid).exists()) {


                            Transaction_Class send_transaction_class = new Transaction_Class(
                                    "debited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p1_userName,
                                    childid,
                                    "50",
                                    1
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
                                    1

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
                                    sizeR
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
                                    1
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
                                    sizeP


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
                                    1

                            );
                            mWallet.child(p1).child("Transactions").child("history").child(childid).setValue(received_transaction_class);

                            String p1_tran_count = dataSnapshot.child("Wallet").child(p1).child("Transactions").child("count").child("level1").getValue().toString();
                            int p1_tran_count_Int = Integer.parseInt(p1_tran_count);
                            String updated_p1_tran_count = Integer.toString(p1_tran_count_Int + 1);

                            mWallet.child(p1).child("Transactions").child("count").child("level1").setValue(updated_p1_tran_count);
                        }

                        //Upgrading level
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("1");
                        subHeading.setText("Sucessfully upgraded to Level One");


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 3000);


                    }

                }
                //level1End


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void forlevel2() {

        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //level2
                String level = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                if (level.equals("1")) {
                    String user_bal = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                    int user_bal_Int = Integer.parseInt(user_bal);
                    //String p1 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p1").getValue().toString();
                    String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p2").getValue().toString();
                    //String p3 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p3").getValue().toString();
                    // String p4 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p4").getValue().toString();
                    //String p5 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p5").getValue().toString();
                    //String p6 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p6").getValue().toString();
                    //String p7 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p7").getValue().toString();
                    //String p8 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p8").getValue().toString();


                    if (user_bal_Int >= 100) {

                        dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.progress_dialogue);
                        dialog.setCancelable(false);
                        final TextView mainHeading = dialog.findViewById(R.id.mainHeading);
                        final TextView subHeading = dialog.findViewById(R.id.subHeading);
                        dialog.show();


                        //reducing money in user wallet


                        String user_updated_bal = Integer.toString(user_bal_Int - 100);

                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(user_updated_bal);

                        //adding money to parent1
                        String p2_bal = dataSnapshot.child("Wallet").child(p2).child("balance").getValue().toString();
                        int p2_bal_Int = Integer.parseInt(p2_bal);

                        String p2_updated_bal = Integer.toString(p2_bal_Int + 100);

                        mWallet.child(p2).child("balance").setValue(p2_updated_bal);
                        subHeading.setText("Transfering money");

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
                        String id = UUID.randomUUID().toString();
                        String childid = "PW" + id.substring(0, 5).toUpperCase();
                        String extraid = id.substring(0, 4).toUpperCase();
                        String user_userName = dataSnapshot.child("Users").child(selfUid).child("username").getValue().toString();
                        String p2_userName = dataSnapshot.child("Users").child(p2).child("username").getValue().toString();


                        if (dataSnapshot.child("Transactions").child(childid).exists()) {


                            Transaction_Class send_transaction_class = new Transaction_Class(
                                    "debited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p2_userName,
                                    childid,
                                    "100",
                                    1
                            );
                            mFirebase.child("Transactions").child(childid + extraid).setValue(send_transaction_class);

                        } else {

                            Transaction_Class send_transaction_class = new Transaction_Class(
                                    "debited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p2_userName,
                                    childid,
                                    "100",
                                    1
                            );
                            mFirebase.child("Transactions").child(childid).setValue(send_transaction_class);


                        }


                        //sendTransaction in user


                        long countR = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                                .child("history").getChildrenCount();

                        long sizeR = countR + 1;


                        Transaction_Class send_transaction_class = new Transaction_Class(
                                "debited",
                                datetime,
                                timeformat,
                                user_userName,
                                p2_userName,
                                childid,
                                "100",
                                sizeR
                        );
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).setValue(send_transaction_class);


                        //receivedTransaction in parent1

                        long countP = dataSnapshot.child("Wallet").child(p2).child("Transactions")
                                .child("history").getChildrenCount();

                        long sizeP = countP + 1;
                        Transaction_Class received_transaction_class = new Transaction_Class(
                                "credited",
                                datetime,
                                timeformat,
                                user_userName,
                                p2_userName,
                                childid,
                                "100",
                                sizeP


                        );
                        mWallet.child(p2).child("Transactions").child("history").child(childid).setValue(received_transaction_class);

                        String p2_tran_count = dataSnapshot.child("Wallet").child(p2).child("Transactions").child("count").child("level2").getValue().toString();
                        int p2_tran_count_Int = Integer.parseInt(p2_tran_count);
                        String updated_p2_tran_count = Integer.toString(p2_tran_count_Int + 1);

                        mWallet.child(p2).child("Transactions").child("count").child("level2").setValue(updated_p2_tran_count);


                        //Upgrading level
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("2");
                        subHeading.setText("Sucessfully upgraded to Level Two");


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 0);


                    }

                }
                //level2End


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void forlevel3() {

        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String level = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                outerloop:
                if (level.equals("2")) {
                    String user_bal = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                    int user_bal_Int = Integer.parseInt(user_bal);

                    //String p1 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p1").getValue().toString();
                    //String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p2").getValue().toString();
                    String p3 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p3").getValue().toString();
                    // String p4 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p4").getValue().toString();
                    //String p5 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p5").getValue().toString();
                    //String p6 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p6").getValue().toString();
                    //String p7 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p7").getValue().toString();
                    //String p8 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p8").getValue().toString();


                    if (user_bal_Int >= 400) {

                        dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.progress_dialogue);
                        dialog.setCancelable(false);
                        final TextView mainHeading = dialog.findViewById(R.id.mainHeading);
                        final TextView subHeading = dialog.findViewById(R.id.subHeading);
                        dialog.show();


                        //reducing money in user wallet


                        String user_updated_bal = Integer.toString(user_bal_Int - 400);
                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(user_updated_bal);


                        //adding money to parent1
                        String p3_bal = dataSnapshot.child("Wallet").child(p3).child("balance").getValue().toString();
                        int p3_bal_Int = Integer.parseInt(p3_bal);

                        String p3_updated_bal = Integer.toString(p3_bal_Int + 400);

                        mWallet.child(p3).child("balance").setValue(p3_updated_bal);
                        subHeading.setText("Transfering money");

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


                        //sendTransaction in user

                        String id = UUID.randomUUID().toString();
                        String childid = "PW" + id.substring(0, 5).toUpperCase();
                        String user_userName = dataSnapshot.child("Users").child(selfUid).child("username").getValue().toString();
                        String p3_userName = dataSnapshot.child("Users").child(p3).child("username").getValue().toString();


                        long countR = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                                .child("history").getChildrenCount();

                        long sizeR = countR + 1;


                        Transaction_Class send_transaction_class = new Transaction_Class(
                                "debited",
                                datetime,
                                timeformat,
                                user_userName,
                                p3_userName,
                                childid,
                                "400",
                                sizeR
                        );
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).setValue(send_transaction_class);


                        //receivedTransaction in parent1

                        long countP = dataSnapshot.child("Wallet").child(p3).child("Transactions")
                                .child("history").getChildrenCount();

                        long sizeP = countP + 1;
                        Transaction_Class received_transaction_class = new Transaction_Class(
                                "credited",
                                datetime,
                                timeformat,
                                user_userName,
                                p3_userName,
                                childid,
                                "400",
                                sizeP


                        );
                        mWallet.child(p3).child("Transactions").child("history").child(childid).setValue(received_transaction_class);

                        String p3_tran_count = dataSnapshot.child("Wallet").child(p3).child("Transactions").child("count").child("level3").getValue().toString();
                        int p3_tran_count_Int = Integer.parseInt(p3_tran_count);
                        String updated_p3_tran_count = Integer.toString(p3_tran_count_Int + 1);

                        mWallet.child(p3).child("Transactions").child("count").child("level3").setValue(updated_p3_tran_count);


                        //Upgrading level
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("3");
                        subHeading.setText("Sucessfully upgraded to Level Three");


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 0);


                    }

                }
                //level2End


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 0);
    }


    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        //Toast.makeText(getContext(), slider.getBundle().getString("extra") + "", Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(getContext(), JobsActivity.class);
        //startActivity(intent);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}