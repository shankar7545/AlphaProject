package com.example.alpha;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ReferCodeAcitvity extends AppCompatActivity {

    public EditText editTextReferCode;
    Button finish;
    TextView textViewReferCode;
    private static long back_pressed;
    public ProgressBar progressBar ,progressBar2;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebase;
    DatabaseReference mRef, mReferDB, mTransactions, mWallet, mLevel, dbPaytm, mLogin ,mUsers,questionsRef ,mAutoReferCode;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_code_acitvity);

        editTextReferCode = findViewById(R.id.referCode);
        textViewReferCode = findViewById(R.id.fetchReferCode);
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mFirebase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        finish = findViewById(R.id.finsh);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar2 = (ProgressBar) findViewById(R.id.progress_bar2);

        editTextReferCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mAutoReferCode = FirebaseDatabase.getInstance().getReference("AutoReferCode");

        /*textViewReferCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textViewReferCode.setVisibility(View.GONE);
                progressBar2.setVisibility(View.VISIBLE);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String mEnd = dataSnapshot.child("AutoReferCode").child("end").getValue().toString();
                                        String mStart = dataSnapshot.child("AutoReferCode").child("start").getValue().toString();


                                        if(mEnd.equals("1")&& mStart.equals("1")){

                                            Toast.makeText(ReferCodeAcitvity.this, "Sorry", Toast.LENGTH_SHORT).show();
                                            textViewReferCode.setVisibility(View.VISIBLE);
                                            progressBar2.setVisibility(View.GONE);
                                        }
                                        else {
                                            int end = Integer.parseInt(mEnd);

                                            int start = Integer.parseInt(mStart);


                                            final int random = new Random().nextInt((end-start)) + start;

                                            final String autoReferCode = "user"+random;

                                            String referCode = dataSnapshot.child("AutoReferCode").child(autoReferCode).child("refercode").getValue().toString();


                                            editTextReferCode.setText(referCode);
                                            textViewReferCode.setVisibility(View.VISIBLE);
                                            progressBar2.setVisibility(View.GONE);


                                        }

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        },2000);

            }
        });   */

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String mReferCode = editTextReferCode.getText().toString().trim();

                final DatabaseReference ReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
                ReferDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot checkdataSnapshot) {

                        if (mReferCode.isEmpty()) {
                            editTextReferCode.setError("Enter ReferCode");
                            editTextReferCode.requestFocus();

                            return;
                        }
                        else if (checkdataSnapshot.hasChild(mReferCode)) {

                            Child();


                        }
                        else if(!checkdataSnapshot.hasChild(mReferCode))
                        {
                            editTextReferCode.setError("Invalid ReferCode");
                            editTextReferCode.requestFocus();

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void Child(){

        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String mReferCode = editTextReferCode.getText().toString().trim();
                String referUid = dataSnapshot.child("ReferDB").child(mReferCode).child("uid").getValue().toString();
                String childCount = dataSnapshot.child("Users").child(referUid).child("childCount").getValue().toString();


                if(childCount.equals("0")){
                    referDetails();
                    mFirebase.child("Users").child(referUid).child("Chain").child("child").child("levelOne").child("leftChild").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mFirebase.child("Users").child(referUid).child("childCount").setValue("1");

                }
                else if(childCount.equals("1")){
                    referDetails();
                    mFirebase.child("Users").child(referUid).child("Chain").child("child").child("levelOne").child("rightChild").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mFirebase.child("Users").child(referUid).child("childCount").setValue("2");

                }
                else{
                    editTextReferCode.setError("Limit Exceeded");
                    editTextReferCode.requestFocus();
                    Toast.makeText(ReferCodeAcitvity.this, "Limit exceeded , Try another Refer Code", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void referDetails(){
        try {
            mFirebase = FirebaseDatabase.getInstance().getReference();

            mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String state = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parentStatus").getValue().toString();

                    if (state.equals("false")) {
                        final String mReferCode = editTextReferCode.getText().toString().trim();
                        String uid_p1 = dataSnapshot.child("ReferDB").child(mReferCode).child("uid").getValue().toString();
                        String uid_p2 = dataSnapshot.child("Users").child(uid_p1).child("Chain").child("parent").child("p1").getValue().toString();
                        String uid_p3 = dataSnapshot.child("Users").child(uid_p2).child("Chain").child("parent").child("p1").getValue().toString();
                        String uid_p4 = dataSnapshot.child("Users").child(uid_p3).child("Chain").child("parent").child("p1").getValue().toString();
                        String uid_p5 = dataSnapshot.child("Users").child(uid_p4).child("Chain").child("parent").child("p1").getValue().toString();
                        String uid_p6 = dataSnapshot.child("Users").child(uid_p5).child("Chain").child("parent").child("p1").getValue().toString();
                        String uid_p7 = dataSnapshot.child("Users").child(uid_p6).child("Chain").child("parent").child("p1").getValue().toString();
                        String uid_p8 = dataSnapshot.child("Users").child(uid_p7).child("Chain").child("parent").child("p1").getValue().toString();

                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parentStatus").setValue("true");

                        //p1
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p1").setValue(uid_p1);
                        //p2
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p2").setValue(uid_p2);
                        //p3
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p3").setValue(uid_p3);
                        //p4
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p4").setValue(uid_p4);
                        //p5
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p5").setValue(uid_p5);
                        //p6
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p6").setValue(uid_p6);
                        //p7
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p7").setValue(uid_p7);
                        //p8
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p8").setValue(uid_p8);



                        /*AutoReferCode
                        String mEnd = dataSnapshot.child("AutoReferCode").child("end").getValue().toString();
                        String mStart = dataSnapshot.child("AutoReferCode").child("start").getValue().toString();
                        String mUsername = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").getValue().toString();

                        int i = Integer.parseInt(mEnd);
                        mAutoReferCode.child("user" + i).child("refercode").setValue(mUsername);

                        String endCount = Integer.toString(i + 1);

                        mAutoReferCode.child("end").setValue(endCount);
                        //AutoReferCodeEnd  */


                        Toast.makeText(ReferCodeAcitvity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ReferCodeAcitvity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

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
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();

        }
    }
}