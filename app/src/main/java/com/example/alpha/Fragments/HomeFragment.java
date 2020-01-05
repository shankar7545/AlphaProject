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

    LinearLayout pay50, referLayout;
    public EditText mAmount;
    EditText editTextReferCode;
    Button next;
    Button paytm;
    Dialog dialog;

    View mView;
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


        //LevelTwo
        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String level = dataSnapshot.child("level").getValue().toString();
                if(level.equals("0"))
                {
                    forLevelOne();
                }
                if(level.equals("1"))
                {


                }
                if(level.equals("2"))
                {

                }
                if(level.equals("3"))
                {

                }
                if(level.equals("4"))
                {

                }
                if(level.equals("5"))
                {

                }
                if(level.equals("6"))
                {

                }
                if(level.equals("7"))
                {

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void forLevelOne() {

        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //LevelOne
                String level = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                if (level.equals("0")) {
                    String balance = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                    String p1 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p1").getValue().toString();
                    //String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p2").getValue().toString();
                    //String p3 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p3").getValue().toString();
                   // String p4 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p4").getValue().toString();
                    //String p5 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p5").getValue().toString();
                    //String p6 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p6").getValue().toString();
                    //String p7 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p7").getValue().toString();
                    //String p8 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p8").getValue().toString();


                    if (balance.equals("50")) {

                        dialog=new Dialog(getContext());
                        dialog.setContentView(R.layout.progress_dialogue);
                        dialog.setCancelable(false);
                        final TextView mainHeading=(TextView) dialog.findViewById(R.id.mainHeading);
                        final TextView subHeading=(TextView) dialog.findViewById(R.id.subHeading);
                        dialog.show();



                        //reducing money in user wallet

                        String user_bal = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                        int user_bal_Int = Integer.parseInt(user_bal);

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
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p1);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("50");
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(selfUid);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionType").setValue("debited");



                        //receivedTransaction in parent1
                        String p1_tran_count= dataSnapshot.child("Wallet").child(p1).child("Transactions").child("count").child("levelOne").getValue().toString();
                        int p1_tran_count_Int = Integer.parseInt(p1_tran_count);

                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p1);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("50");
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(selfUid);
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

    public void LevelTwo(){
        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //LevelTWO

                    String levelTwo = dataSnapshot.child("Level").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                    //if user level equal to one
                    if (levelTwo.equals("1"))
                    {
                        String balance = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                        String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p2").child("uid").getValue().toString();

                        //if user balance equals to 100
                        if (balance.equals("100")) {

                            String tranState = dataSnapshot.child("Wallet").child(p2).child("Transactions").child("received").child("100*4").child("state").getValue().toString();
                            int tranStateInt = Integer.parseInt(tranState);

                            String p2_bal = dataSnapshot.child("Wallet").child(p2).child("balance").getValue().toString();
                            String user_bal = dataSnapshot.child("Wallet").child(selfUid).child("balance").getValue().toString();

                            //converting string to integer
                            int a = Integer.parseInt(p2_bal);
                            int userBal = Integer.parseInt(user_bal);

                            //reducing money in user wallet
                            String redUserBal = Integer.toString(userBal - 100);
                            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(redUserBal);

                            //adding money to parent2
                            String bal = Integer.toString(a + 100);
                            mWallet.child(p2).child("balance").setValue(bal);
                            Toast.makeText(getActivity(), "Transferred money to p2", Toast.LENGTH_SHORT).show();

                            //AutoLoginCode
                            String p2_email = dataSnapshot.child("Users").child(p2).child("email").getValue().toString();
                            String p2_password = dataSnapshot.child("Users").child(p2).child("password").getValue().toString();
                            String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                            int i = Integer.parseInt(mEnd);

                            mLogin.child("user" + i).child("uid").setValue(p2);
                            mLogin.child("user" + i).child("email").setValue(p2_email);
                            mLogin.child("user" + i).child("password").setValue(p2_password);

                            String endCount = Integer.toString(i + 1);
                            mLogin.child("end").setValue(endCount);


                            //sendTransaction in user
                            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("send").child("100").child("uid").setValue(p2);
                            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("send").child("100").child("state").setValue("true");


                            //receivedTransaction in parent2
                            String uidInt = Integer.toString(tranStateInt + 1);
                            String uid = "uid"+uidInt;
                            mWallet.child(p2).child("Transactions").child("received").child("100*4").child("state").setValue(uidInt);
                            mWallet.child(p2).child("Transactions").child("received").child("100*4").child(selfUid).setValue(datetime);


                            //Send TransactionList in user
                            mWallet.child(selfUid).child("TransactionsList").child(p2).child("email").setValue(p2_email);
                            mWallet.child(selfUid).child("TransactionsList").child(p2).child("transactionType").setValue("Send");
                            mWallet.child(selfUid).child("TransactionsList").child(p2).child("transactionTime").setValue(timeformat);
                            mWallet.child(selfUid).child("TransactionsList").child(p2).child("transactionDate").setValue(datetime);
                            mWallet.child(selfUid).child("TransactionsList").child(p2).child("transactionAmount").setValue("100");

                            //receivedTransactionList in parent1
                            mWallet.child(p2).child("TransactionsList").child(selfUid).child("uid").setValue(selfUid);
                            mWallet.child(p2).child("TransactionsList").child(selfUid).child("transactionTime").setValue(timeformat);
                            mWallet.child(p2).child("TransactionsList").child(selfUid).child("transactionDate").setValue(datetime);
                            mWallet.child(p2).child("TransactionsList").child(selfUid).child("transactionAmount").setValue("100");
                            mWallet.child(p2).child("TransactionsList").child(selfUid).child("transactionType").setValue("Received");

                            //Upgrading level
                            mLevel.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("2");
                            Toast.makeText(getActivity(), "Upgraded to LevelTwo", Toast.LENGTH_SHORT).show();


                        }
                    }

                //LevelTwoEnd

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void LevelThree(){
        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //LevelThree

                String levelThree = dataSnapshot.child("Level").child(selfUid).child("level").getValue().toString();

                //if user level equal to one
                if (levelThree.equals("2"))
                {
                    String balance = dataSnapshot.child("Wallet").child(selfUid).child("balance").getValue().toString();
                    String p3 = dataSnapshot.child("Users").child(selfUid).child("parent").child("p3").child("uid").getValue().toString();

                    //if user balance equals to 100
                    if (balance.equals("400")) {

                        String tranState = dataSnapshot.child("Wallet").child(p3).child("Transactions").child("received").child("100*4").child("state").getValue().toString();
                        int tranStateInt = Integer.parseInt(tranState);

                        String p3_bal = dataSnapshot.child("Wallet").child(p3).child("balance").getValue().toString();
                        String user_bal = dataSnapshot.child("Wallet").child(selfUid).child("balance").getValue().toString();


                        //converting string to integer
                        int a = Integer.parseInt(p3_bal);
                        int userBal = Integer.parseInt(user_bal);

                        //reducing money in user wallet

                        String redUserBal = Integer.toString(userBal - 400);
                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(redUserBal);

                        //adding money to parent2
                        String bal = Integer.toString(a + 400);
                        mWallet.child(p3).child("balance").setValue(bal);
                        Toast.makeText(getActivity(), "Transferred money to p3", Toast.LENGTH_SHORT).show();

                        //AutoLoginCode
                        String p3_email = dataSnapshot.child("Users").child(p3).child("email").getValue().toString();
                        String p3_password = dataSnapshot.child("Users").child(p3).child("password").getValue().toString();
                        String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                        int i = Integer.parseInt(mEnd);

                        mLogin.child("user" + i).child("uid").setValue(p3);
                        mLogin.child("user" + i).child("email").setValue(p3_email);
                        mLogin.child("user" + i).child("password").setValue(p3_password);

                        String endCount = Integer.toString(i + 1);
                        mLogin.child("end").setValue(endCount);


                        //sendTransaction in user
                        mWallet.child(selfUid).child("Transactions").child("send").child("400").child("uid").setValue(p3);
                        mWallet.child(selfUid).child("Transactions").child("send").child("400").child("state").setValue("true");


                        //receivedTransaction in parent2
                        String uidInt = Integer.toString(tranStateInt + 1);
                        String uid = "uid"+uidInt;
                        mWallet.child(p3).child("Transactions").child("received").child("400*8").child("state").setValue(uidInt);
                        mWallet.child(p3).child("Transactions").child("received").child("400*8").child(selfUid).setValue(datetime);


                        //Send TransactionList in user
                        mWallet.child(selfUid).child("TransactionsList").child(p3).child("email").setValue(p3_email);
                        mWallet.child(selfUid).child("TransactionsList").child(p3).child("transactionType").setValue("Send");
                        mWallet.child(selfUid).child("TransactionsList").child(p3).child("transactionTime").setValue(timeformat);
                        mWallet.child(selfUid).child("TransactionsList").child(p3).child("transactionDate").setValue(datetime);
                        mWallet.child(selfUid).child("TransactionsList").child(p3).child("transactionAmount").setValue("400");

                        //receivedTransactionList in parent1
                        mWallet.child(p3).child("TransactionsList").child(selfUid).child("uid").setValue(selfUid);
                        mWallet.child(p3).child("TransactionsList").child(selfUid).child("transactionTime").setValue(timeformat);
                        mWallet.child(p3).child("TransactionsList").child(selfUid).child("transactionDate").setValue(datetime);
                        mWallet.child(p3).child("TransactionsList").child(selfUid).child("transactionAmount").setValue("400");
                        mWallet.child(p3).child("TransactionsList").child(selfUid).child("transactionType").setValue("Received");

                        //Upgrading level
                        mLevel.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("3");
                        Toast.makeText(getActivity(), "Upgraded to Level Three", Toast.LENGTH_SHORT).show();


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