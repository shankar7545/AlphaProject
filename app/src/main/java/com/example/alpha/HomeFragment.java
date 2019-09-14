package com.example.alpha;

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

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    View mView;
    DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mLevel, dbPaytm, mLogin;
    public ProgressBar progressBar;
    LinearLayout imageScroll;
    RelativeLayout homeFrag;
    TextView profileName,wallet_bal,level , withdrawable;
    private TabLayout tab_layout;
    private NestedScrollView nested_scroll_view;


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


        initToolbar();

        /*Paytm add money

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


        Paytm add money*/


        mTransactions = FirebaseDatabase.getInstance().getReference("Transactions");
        mFirebase = FirebaseDatabase.getInstance().getReference("Users");
        mReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");
        mLevel = FirebaseDatabase.getInstance().getReference("Level");
        mLogin = FirebaseDatabase.getInstance().getReference("Login");
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference();
        scoresRef.keepSynced(true);


        return mView;


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

    }




 @Override
    public void onStart() {
     super.onStart();

     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {


             mRef = FirebaseDatabase.getInstance().getReference();

             mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     //details
                     String nameH = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue().toString();
                     String balanceH = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                     String value_count = dataSnapshot.child("Level").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                     profileName.setText(nameH);
                     wallet_bal.setText(balanceH);
                     level.setText("LEVEL "+value_count);



                     //LevelOne
                     String levelOne = dataSnapshot.child("Level").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                     if (levelOne.equals("0")) {
                         String balance = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                         String p1 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p1").child("uid").getValue().toString();
                         String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p2").child("uid").getValue().toString();
                         String p3 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p3").child("uid").getValue().toString();
                         String p4 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p4").child("uid").getValue().toString();
                         String p5 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p5").child("uid").getValue().toString();
                         String p6 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p6").child("uid").getValue().toString();
                         String p7 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p7").child("uid").getValue().toString();
                         String p8 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p8").child("uid").getValue().toString();


                         if (balance.equals("50")) {

                             String tranState = dataSnapshot.child("Wallet").child(p1).child("Transactions").child("received").child("50*2").child("state").getValue().toString();
                             int tranStateInt = Integer.parseInt(tranState);

                             if( tranStateInt < 2)
                             {
                                 String p1_bal = dataSnapshot.child("Wallet").child(p1).child("balance").getValue().toString();

                                 //converting string to integer
                                 int a = Integer.parseInt(p1_bal);

                                 //reducing money in user wallet
                                 mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue("0");

                                 //adding money to parent1
                                 String bal = Integer.toString(a + 50);
                                 mWallet.child(p1).child("balance").setValue(bal);
                                 Toast.makeText(getActivity(), "Transferred 50 to p1", Toast.LENGTH_SHORT).show();

                                 //AutoLoginCode
                                 String p1_email = dataSnapshot.child("Users").child(p1).child("email").getValue().toString();
                                 String p1_password = dataSnapshot.child("Users").child(p1).child("password").getValue().toString();
                                 String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                                 int i = Integer.parseInt(mEnd);

                                 mLogin.child("user" + i).child("uid").setValue(p1);
                                 mLogin.child("user" + i).child("email").setValue(p1_email);
                                 mLogin.child("user" + i).child("password").setValue(p1_password);

                                 String endCount = Integer.toString(i + 1);
                                 mLogin.child("end").setValue(endCount);


                                 //sendTransaction in user
                                 mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("send").child("50").child("uid").setValue(p1);
                                 mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("send").child("50").child("state").setValue("true");


                                 //receivedTransaction in parent1

                                 String uidInt = Integer.toString(tranStateInt + 1);
                                 String uid = "uid"+uidInt;
                                 mWallet.child(p1).child("Transactions").child("received").child("50*2").child(uid).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                 mWallet.child(p1).child("Transactions").child("received").child("50*2").child("state").setValue(uidInt);

                                 //Upgrading level
                                 mLevel.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("1");
                                 Toast.makeText(getActivity(), "Upgraded to LevelOne", Toast.LENGTH_SHORT).show();

                             }

                             else
                             {
                                 Toast.makeText(getActivity(), "Unable to transfer money", Toast.LENGTH_SHORT).show();

                             }


                         }

                     }
                     //LevelOneEnd


                     //LevelTWO
                     String twoTransCount = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("received").child("50*2").child("state").getValue().toString();

                     //When user gets two transactions in 50*2
                     if(twoTransCount.equals("2"))
                     {
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


                                 //Main work
                                 if(tranStateInt < 4)
                                 {
                                     String p2_bal = dataSnapshot.child("Wallet").child(p2).child("balance").getValue().toString();

                                     //converting string to integer
                                     int a = Integer.parseInt(p2_bal);

                                     //reducing money in user wallet
                                     mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue("0");

                                     //adding money to parent2
                                     String bal = Integer.toString(a + 100);
                                     mWallet.child(p2).child("balance").setValue(bal);
                                     Toast.makeText(getActivity(), "Transferred money to p2", Toast.LENGTH_SHORT).show();

                                     //AutoLoginCode
                                     String p1_email = dataSnapshot.child("Users").child(p2).child("email").getValue().toString();
                                     String p1_password = dataSnapshot.child("Users").child(p2).child("password").getValue().toString();
                                     String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                                     int i = Integer.parseInt(mEnd);

                                     mLogin.child("user" + i).child("uid").setValue(p2);
                                     mLogin.child("user" + i).child("email").setValue(p1_email);
                                     mLogin.child("user" + i).child("password").setValue(p1_password);

                                     String endCount = Integer.toString(i + 1);
                                     mLogin.child("end").setValue(endCount);


                                     //sendTransaction in user
                                     mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("send").child("100").child("uid").setValue(p2);
                                     mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("send").child("100").child("state").setValue("true");


                                     //receivedTransaction in parent2
                                     String uidInt = Integer.toString(tranStateInt + 1);
                                     String uid = "uid"+uidInt;
                                     mWallet.child(p2).child("Transactions").child("received").child("100*4").child(uid).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                     mWallet.child(p2).child("Transactions").child("received").child("100*4").child("state").setValue(uidInt);

                                     //Upgrading level
                                     mLevel.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("2");
                                     Toast.makeText(getActivity(), "Upgraded to LevelTwo", Toast.LENGTH_SHORT).show();

                                 }

                                 else
                                 {
                                     Toast.makeText(getActivity(), "Unable to transfer money", Toast.LENGTH_SHORT).show();

                                 }

                             }
                         }
                     }
                     //LevelTwoEnd

                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
         }
     }, 0);

 }

}