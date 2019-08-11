package com.example.alpha;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    LinearLayout pay50, referLayout;
    public EditText mAmount;

    Button paytm;
    View mView;
    DatabaseReference mRef, mReferDB ,mFirebase,mTransactions ,mWallet , mLevel ,dbPaytm,mLogin;




    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        pay50 = (LinearLayout) mView.findViewById(R.id.pay50);

        paytm = mView.findViewById(R.id.paytm);

        dbPaytm = FirebaseDatabase.getInstance().getReference("Paytm");

        mAmount =mView.findViewById(R.id.amount);




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
        mRef = FirebaseDatabase.getInstance().getReference();


        ReferDetails();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //Level One

                String value = dataSnapshot.child("Level").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                if (value.equals("0")) {
                    pay50 = (LinearLayout) mView.findViewById(R.id.pay50);
                    pay50.setVisibility(View.VISIBLE);
                    String balance = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                    String p1 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p1").child("uid").getValue().toString();
                    String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p2").child("uid").getValue().toString();
                    String p3 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p3").child("uid").getValue().toString();
                    String p4 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p4").child("uid").getValue().toString();
                    String p5 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p5").child("uid").getValue().toString();
                    String p6 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p6").child("uid").getValue().toString();
                    String p7 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p7").child("uid").getValue().toString();
                    String p8 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p8").child("uid").getValue().toString();



                    if(balance.equals("50"))
                    {

                        String p1_bal = dataSnapshot.child("Wallet").child(p1).child("balance").getValue().toString();


                        int a = Integer.parseInt(p1_bal.replaceAll("[\\D]",""));


                         mTransactions.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T1").setValue("1");
                         mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue("0");
                         mWallet.child(p1).child("balance").setValue(a+50);

                        String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                        int i = Integer.parseInt(mEnd.replaceAll("[\\D]",""));


                        mLogin.child("user"+i).child("uid").setValue(p1);
                        mLogin.child("end").setValue(i+1);



                    }
                    String t1 = dataSnapshot.child("Transactions").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T1").getValue().toString();

                    if(t1.equals("1"))
                    {
                        mLevel.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("1");

                    }


                }
                //Level One





                //Level TWO

                String value1 = dataSnapshot.child("Level").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                if (value1.equals("1")){
                    String balance = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();


                    if(balance.equals("100"))
                    {
                        String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p2").child("uid").getValue().toString();

                        String p2_bal = dataSnapshot.child("Wallet").child(p2).child("balance").getValue().toString();


                        int b = Integer.parseInt(p2_bal.replaceAll("[\\D]",""));


                        mTransactions.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T2").setValue("1");
                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue("0");
                        mWallet.child(p2).child("balance").setValue(b+100);

                        String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                        int i = Integer.parseInt(mEnd.replaceAll("[\\D]",""));


                        mLogin.child("user"+i).child("uid").setValue(p2);
                        mLogin.child("end").setValue(i+1);


                    }
                    String t2 = dataSnapshot.child("Transactions").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T2").getValue().toString();

                    if(t2.equals("1"))
                    {
                        mLevel.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("2");

                    }


                }
            }

                  //Level TWO



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    public void ReferDetails(){
        try {

            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String state =dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("state").getValue().toString();

                    if(state.equals("false")) {
                        String mReferCode = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("refercode").getValue().toString();
                        String uid_p1 =dataSnapshot.child("ReferDB").child(mReferCode).child("uid").getValue().toString();
                        String uid_p2 = dataSnapshot.child("Users").child(uid_p1).child("parent").child("p1").child("uid").getValue().toString();
                        String uid_p3 = dataSnapshot.child("Users").child(uid_p2).child("parent").child("p1").child("uid").getValue().toString();
                        String uid_p4 = dataSnapshot.child("Users").child(uid_p3).child("parent").child("p1").child("uid").getValue().toString();
                        String uid_p5 = dataSnapshot.child("Users").child(uid_p4).child("parent").child("p1").child("uid").getValue().toString();
                        String uid_p6 = dataSnapshot.child("Users").child(uid_p5).child("parent").child("p1").child("uid").getValue().toString();
                        String uid_p7 = dataSnapshot.child("Users").child(uid_p6).child("parent").child("p1").child("uid").getValue().toString();
                        String uid_p8 = dataSnapshot.child("Users").child(uid_p7).child("parent").child("p1").child("uid").getValue().toString();

                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("state").setValue("true");

                        //p1
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p1").child("uid").setValue(uid_p1);
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p1").child("state").setValue("true");

                        //p2
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p2").child("uid").setValue(uid_p2);
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p2").child("state").setValue("true");

                        //p3
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p3").child("uid").setValue(uid_p3);
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p3").child("state").setValue("true");


                        //p4
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p4").child("uid").setValue(uid_p4);
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p4").child("state").setValue("true");


                        //p5
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p5").child("uid").setValue(uid_p5);
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p5").child("state").setValue("true");


                        //p6
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p6").child("uid").setValue(uid_p6);
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p6").child("state").setValue("true");


                        //p7
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p7").child("uid").setValue(uid_p7);
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p7").child("state").setValue("true");


                        //p8
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p8").child("uid").setValue(uid_p8);
                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p8").child("state").setValue("true");

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

