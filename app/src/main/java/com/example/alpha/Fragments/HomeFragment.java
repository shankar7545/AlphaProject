package com.example.alpha.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bumptech.glide.request.RequestOptions;
import com.example.alpha.Levels.LevelActivity;
import com.example.alpha.Levels.beginnerActivity;
import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.example.alpha.ViewHolder.TransactionView;
import com.example.alpha.Wallet.walletActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
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


    //Functions

    LinearLayout walletLayout, SecurityLayout, ReferLayout, courseLayout;
    //Dashboard
    private DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mTransactionsRecycler, dbPaytm, mTransactions1, mAchievements;

    LinearLayout layoutOne;
    private SliderLayout mDemoSlider;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        fabRefresh = mView.findViewById(R.id.fab_refresh);
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
        String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        mAchievements = FirebaseDatabase.getInstance().getReference("Users").child(selfUid)
                .child("Achievements");

        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference();
        scoresRef.keepSynced(true);

        layoutOne = mView.findViewById(R.id.layoutOne);


        try {

            mRef.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String Achievement = dataSnapshot.child("Achievement").getValue().toString();

                    if (Achievement.equals("Beginner")) {
                        layoutOne.setOnClickListener(v -> startActivity(new Intent(getContext(), beginnerActivity.class)));
                    } else {
                        layoutOne.setOnClickListener(v -> startActivity(new Intent(getContext(), LevelActivity.class)));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }



        //CheckingLevelUpgrades();
        //loadTransactions();
        FunctionOnclick();
        Slider();
        return mView;

    }

    private void FunctionOnclick() {
        walletLayout = mView.findViewById(R.id.walletLayout);
        walletLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), walletActivity.class);
            startActivity(intent);
        });
        SecurityLayout = mView.findViewById(R.id.securityLayout);
        SecurityLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), LevelActivity.class));
        });
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
        listUrl.add("https://firebasestorage.googleapis.com/v0/b/jobtrackingsystem-83bad.appspot.com/o/image2.jpeg?alt=media&token=0e1e079d-18c0-4858-9ca0-f088ac32ac8d");
        //listName.add("");

        //image1
        listUrl.add("https://firebasestorage.googleapis.com/v0/b/jobtrackingsystem-83bad.appspot.com/o/image4.jpg?alt=media&token=e8788eef-f8fb-4c86-be13-2d0258d43527");
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
                        dialog.show();


                        //reducing money in user wallet

                        String user_updated_bal = Integer.toString(user_bal_Int - 50);

                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(user_updated_bal);

                        //adding money to parent1
                        String p1_bal = dataSnapshot.child("Wallet").child(p1).child("balance").getValue().toString();
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
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("1");


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
                        dialog.show();


                        //reducing money in user wallet


                        String user_updated_bal = Integer.toString(user_bal_Int - 100);

                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(user_updated_bal);

                        //adding money to parent1
                        String p2_bal = dataSnapshot.child("Wallet").child(p2).child("balance").getValue().toString();
                        int p2_bal_Int = Integer.parseInt(p2_bal);

                        String p2_updated_bal = Integer.toString(p2_bal_Int + 100);

                        mWallet.child(p2).child("balance").setValue(p2_updated_bal);

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
                                    1,
                                    "level2"
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
                                    1,
                                    "level2"
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
                                sizeR,
                                "level2"
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
                                sizeP,
                                "level2"


                        );
                        mWallet.child(p2).child("Transactions").child("history").child(childid).setValue(received_transaction_class);

                        String p2_tran_count = dataSnapshot.child("Wallet").child(p2).child("Transactions").child("count").child("level2").getValue().toString();
                        int p2_tran_count_Int = Integer.parseInt(p2_tran_count);
                        String updated_p2_tran_count = Integer.toString(p2_tran_count_Int + 1);

                        mWallet.child(p2).child("Transactions").child("count").child("level2").setValue(updated_p2_tran_count);


                        //Upgrading level
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("2");


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
                        dialog.show();


                        //reducing money in user wallet


                        String user_updated_bal = Integer.toString(user_bal_Int - 400);
                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(user_updated_bal);


                        //adding money to parent1
                        String p3_bal = dataSnapshot.child("Wallet").child(p3).child("balance").getValue().toString();
                        int p3_bal_Int = Integer.parseInt(p3_bal);

                        String p3_updated_bal = Integer.toString(p3_bal_Int + 400);

                        mWallet.child(p3).child("balance").setValue(p3_updated_bal);

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
                        String user_userName = Objects.requireNonNull(dataSnapshot.child("Users").child(selfUid).child("username").getValue()).toString();
                        String p3_userName = Objects.requireNonNull(dataSnapshot.child("Users").child(p3).child("username").getValue()).toString();


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
                                sizeR,
                                "level3"
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
                                sizeP,
                                "level3"


                        );
                        mWallet.child(p3).child("Transactions").child("history").child(childid).setValue(received_transaction_class);

                        String p3_tran_count = dataSnapshot.child("Wallet").child(p3).child("Transactions").child("count").child("level3").getValue().toString();
                        int p3_tran_count_Int = Integer.parseInt(p3_tran_count);
                        String updated_p3_tran_count = Integer.toString(p3_tran_count_Int + 1);

                        mWallet.child(p3).child("Transactions").child("count").child("level3").setValue(updated_p3_tran_count);


                        //Upgrading level
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("3");


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

        new Handler().postDelayed(() -> {


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