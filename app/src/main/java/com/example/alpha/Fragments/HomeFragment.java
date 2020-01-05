package com.example.alpha.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Registration.ConfirmAmount;
import com.example.alpha.Registration.PaytmKey;
import com.example.alpha.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment {


    DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mLevel, dbPaytm, mLogin ,mLevelTwo , mLevelThree;
    public ProgressBar progressBar;
    LinearLayout imageScroll;
    RelativeLayout homeFrag;
    TextView profileName,wallet_bal,level , withdrawable;
    private TabLayout tab_layout;
    private NestedScrollView nested_scroll_view;
    String selfUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Calendar c = Calendar.getInstance();
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat time=new SimpleDateFormat("hh:mm:ss aa");
    String timeformat=time.format(c.getTime());
    String datetime = dateformat.format(c.getTime());
    LinearLayout pay50, referLayout;
    public EditText mAmount;
    EditText editTextReferCode;
    Button next;
    Button paytm;
    Dialog dialog;
    View mView;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        pay50 = (LinearLayout) mView.findViewById(R.id.pay50);
        paytm = mView.findViewById(R.id.paytm);
        homeFrag = mView.findViewById(R.id.homeFrag);
        next = (Button) mView.findViewById(R.id.next);
        editTextReferCode = mView.findViewById(R.id.referCode);
        progressBar = (ProgressBar) mView.findViewById(R.id.progress_bar4);
        mAmount = mView.findViewById(R.id.amount);
        profileName =(TextView)mView.findViewById(R.id.usernameH);
        wallet_bal =(TextView)mView.findViewById(R.id.balanceH);
        level =(TextView)mView.findViewById(R.id.levelH);
        dbPaytm = FirebaseDatabase.getInstance().getReference("Paytm");
        //Paytm add money

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbPaytm.child("01").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            final PaytmKey paytmKey = dataSnapshot.getValue(PaytmKey.class);
                            try {
                                final String mUserName = mAmount.getText().toString().trim();

                                Intent i = new Intent(getContext(), ConfirmAmount.class);

                                Bundle bundle = new Bundle();
                                i.putExtra("Amount", mUserName);
                                i.putExtra("MID", paytmKey.getPaytmkey());
                                i.putExtras(bundle);
                                startActivity(i);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        //Paytm add money




        mFirebase = FirebaseDatabase.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");
        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet").child(selfUid).child("Transactions");

        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference();
        scoresRef.keepSynced(true);


        mFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //details
                String nameH = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue().toString();
                String balanceH = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                String value_count = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                profileName.setText(nameH);
                wallet_bal.setText(balanceH);
                level.setText("LEVEL "+value_count);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        CheckingLevelUpgrades();
        initToolbar();
        return mView;

    }



    public void CheckingLevelUpgrades(){


        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String user_level = dataSnapshot.child("Users").child(selfUid).child("level").getValue().toString();
                final String transactionCount_levelOne = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("levelOne").getValue().toString();
                final String transactionCount_levelTwo = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("levelTwo").getValue().toString();
                final String transactionCount_levelThree = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("levelThree").getValue().toString();
                final String transactionCount_levelFour = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("levelFour").getValue().toString();
                final String transactionCount_levelFive = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("levelFive").getValue().toString();
                final String transactionCount_levelSix = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("levelSix").getValue().toString();


                if(user_level.equals("0"))
                {
                    forLevelOne();
                }

                if( (user_level.equals("1")) && (transactionCount_levelOne.equals("2")))
                {
                    forLevelTwo();


                }
                if( (user_level.equals("2")) && (transactionCount_levelTwo.equals("4")))
                {
                    forLevelThree();
                }
                if(user_level.equals("3"))
                {

                }
                if(user_level.equals("4"))
                {

                }
                if(user_level.equals("5"))
                {

                }
                if(user_level.equals("6"))
                {

                }
                if(user_level.equals("7"))
                {

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void forLevelOne()
    {

        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //LevelOne
                String level = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                if (level.equals("0")) {
                    String user_bal = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                    int user_bal_Int = Integer.parseInt(user_bal);                    String p1 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p1").getValue().toString();
                    //String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p2").getValue().toString();
                    //String p3 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p3").getValue().toString();
                   // String p4 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p4").getValue().toString();
                    //String p5 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p5").getValue().toString();
                    //String p6 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p6").getValue().toString();
                    //String p7 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p7").getValue().toString();
                    //String p8 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p8").getValue().toString();


                    if (user_bal_Int>=50) {

                        dialog=new Dialog(getContext());
                        dialog.setContentView(R.layout.progress_dialogue);
                        dialog.setCancelable(false);
                        final TextView mainHeading=(TextView) dialog.findViewById(R.id.mainHeading);
                        final TextView subHeading=(TextView) dialog.findViewById(R.id.subHeading);
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


                        //sendTransaction in user

                        String id = UUID.randomUUID().toString();
                        String childid = "PW"+id.substring(0,5).toUpperCase();
                        String user_userName = dataSnapshot.child("Users").child(selfUid).child("username").getValue().toString();
                        String p1_userName = dataSnapshot.child("Users").child(p1).child("username").getValue().toString();
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p1_userName);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("50");
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(user_userName);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionType").setValue("debited");



                        //receivedTransaction in parent1
                        String p1_tran_count= dataSnapshot.child("Wallet").child(p1).child("Transactions").child("count").child("levelOne").getValue().toString();
                        int p1_tran_count_Int = Integer.parseInt(p1_tran_count);

                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p1_userName);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("50");
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(user_userName);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionType").setValue("credited");

                        String updated_p1_tran_count =Integer.toString(p1_tran_count_Int + 1);
                        mWallet.child(p1).child("Transactions").child("count").child("levelOne").setValue(updated_p1_tran_count);



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
                //LevelOneEnd


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void forLevelTwo()
    {

        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //LevelTwo
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


                    if (user_bal_Int>=100) {

                        dialog=new Dialog(getContext());
                        dialog.setContentView(R.layout.progress_dialogue);
                        dialog.setCancelable(false);
                        final TextView mainHeading=(TextView) dialog.findViewById(R.id.mainHeading);
                        final TextView subHeading=(TextView) dialog.findViewById(R.id.subHeading);
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


                        //sendTransaction in user

                        String id = UUID.randomUUID().toString();
                        String childid = "PW"+id.substring(0,5).toUpperCase();
                        String user_userName = dataSnapshot.child("Users").child(selfUid).child("username").getValue().toString();
                        String p2_userName = dataSnapshot.child("Users").child(p2).child("username").getValue().toString();
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p2_userName);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("100");
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(user_userName);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionType").setValue("debited");



                        //receivedTransaction in parent1
                        String p2_tran_count= dataSnapshot.child("Wallet").child(p2).child("Transactions").child("count").child("levelTwo").getValue().toString();
                        int p2_tran_count_Int = Integer.parseInt(p2_tran_count);

                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p2_userName);
                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("100");
                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(user_userName);
                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transactionType").setValue("credited");

                        String updated_p2_tran_count =Integer.toString(p2_tran_count_Int + 1);
                        mWallet.child(p2).child("Transactions").child("count").child("levelTwo").setValue(updated_p2_tran_count);



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
                //LevelTwoEnd


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void forLevelThree()
    {

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


                    if (user_bal_Int>=400) {

                        dialog=new Dialog(getContext());
                        dialog.setContentView(R.layout.progress_dialogue);
                        dialog.setCancelable(false);
                        final TextView mainHeading=(TextView) dialog.findViewById(R.id.mainHeading);
                        final TextView subHeading=(TextView) dialog.findViewById(R.id.subHeading);
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
                        String childid = "PW"+id.substring(0,5).toUpperCase();
                        String user_userName = dataSnapshot.child("Users").child(selfUid).child("username").getValue().toString();
                        String p3_userName = dataSnapshot.child("Users").child(p3).child("username").getValue().toString();
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p3_userName);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("400");
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(user_userName);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionType").setValue("debited");



                        //receivedTransaction in parent1
                        String p3_tran_count= dataSnapshot.child("Wallet").child(p3).child("Transactions").child("count").child("levelThree").getValue().toString();
                        int p3_tran_count_Int = Integer.parseInt(p3_tran_count);

                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p3_userName);
                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("400");
                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(user_userName);
                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transactionType").setValue("credited");

                        String updated_p3_tran_count =Integer.toString(p3_tran_count_Int + 1);
                        mWallet.child(p3).child("Transactions").child("count").child("levelTwo").setValue(updated_p3_tran_count);



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
                //LevelTwoEnd


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





    private void initToolbar() {
        Toolbar toolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

    }

}