package com.example.alpha;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    LinearLayout pay50, referLayout;
    public EditText mAmount;
    EditText editTextReferCode;
    Button next;
    Button paytm;
    View mView;
    DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mLevel, dbPaytm, mLogin;
    public ProgressBar progressBar;


    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        pay50 = (LinearLayout) mView.findViewById(R.id.pay50);

        paytm = mView.findViewById(R.id.paytm);


        next = (Button) mView.findViewById(R.id.next);
        editTextReferCode = mView.findViewById(R.id.referCode);
        progressBar = (ProgressBar) mView.findViewById(R.id.progress_bar);

        dbPaytm = FirebaseDatabase.getInstance().getReference("Paytm");

        mAmount = mView.findViewById(R.id.amount);


        //Paytm add money

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbPaytm.child("01").addValueEventListener(new ValueEventListener() {
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






 @Override
    public void onStart() {
     super.onStart();

     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {



             mRef = FirebaseDatabase.getInstance().getReference();


             mRef.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     String value = dataSnapshot.child("Level").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                     if (value.equals("0")) {
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


                             String p1_bal = dataSnapshot.child("Wallet").child(p1).child("balance").getValue().toString();


                             int a = Integer.parseInt(p1_bal);

                             mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("trans").setValue("1");
                             mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue("0");


                             String wallet_bal = Integer.toString(a + 50);


                             mWallet.child(p1).child("balance").setValue(wallet_bal);
                             String p1_email = dataSnapshot.child("Users").child(p1).child("email").getValue().toString();
                             String p1_password = dataSnapshot.child("Users").child(p1).child("password").getValue().toString();


                             String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                             int i = Integer.parseInt(mEnd);


                             mLogin.child("user" + i).child("uid").setValue(p1);
                             mLogin.child("user" + i).child("email").setValue(p1_email);
                             mLogin.child("user" + i).child("password").setValue(p1_password);

                             String endCount = Integer.toString(i + 1);

                             mLogin.child("end").setValue(endCount);


                         }
                         String t1 = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("trans").getValue().toString();

                         if (t1.equals("1")) {
                             mLevel.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("1");
                             Toast.makeText(getActivity(), "Upgraded to LevelOne", Toast.LENGTH_SHORT).show();

                         }


                     }
                     //Level One


                     //Level TWO

            /*String value1 = dataSnapshot.child("Level").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

             if (value1.equals("1")) {
                 String balance = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();


                 if (balance.equals("100")) {
                     String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p2").child("uid").getValue().toString();

                     String p2_bal = dataSnapshot.child("Wallet").child(p2).child("balance").getValue().toString();


                     int b = Integer.parseInt(p2_bal);


                     mTransactions.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T").setValue("2");
                     mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue("0");


                     String wallet_bal = Integer.toString(b + 100);

                     mWallet.child(p2).child("balance").setValue(wallet_bal);
                     String p2_email = dataSnapshot.child("Users").child(p2).child("email").getValue().toString();
                     String p2_password = dataSnapshot.child("Users").child(p2).child("password").getValue().toString();

                     String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                     int i = Integer.parseInt(mEnd);


                     mLogin.child("user" + i).child("uid").setValue(p2);
                     mLogin.child("user" + i).child("email").setValue(p2_email);
                     mLogin.child("user" + i).child("password").setValue(p2_password);
                     String endCount = Integer.toString(i + 1);

                     mLogin.child("end").setValue(endCount);


                 }
                 String t2 = dataSnapshot.child("Transactions").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T").getValue().toString();

                 if (t2.equals("2")) {
                     mLevel.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("2");

                 }


             }*/
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
         }
     }, 2000);






 }



}