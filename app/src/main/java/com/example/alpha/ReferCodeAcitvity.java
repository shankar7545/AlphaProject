package com.example.alpha;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
        mFirebase = FirebaseDatabase.getInstance().getReference("Users");
        mRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        finish = findViewById(R.id.finsh);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar2 = (ProgressBar) findViewById(R.id.progress_bar2);


        mAutoReferCode = FirebaseDatabase.getInstance().getReference("AutoReferCode");

        textViewReferCode.setOnClickListener(new View.OnClickListener() {
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
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String mReferCode = editTextReferCode.getText().toString().trim();

                final DatabaseReference promodb = FirebaseDatabase.getInstance().getReference("ReferDB");
                promodb.addListenerForSingleValueEvent(new ValueEventListener() {
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
        mRef = FirebaseDatabase.getInstance().getReference();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String mReferCode = editTextReferCode.getText().toString().trim();
                String referUid = dataSnapshot.child("ReferDB").child(mReferCode).child("uid").getValue().toString();
                String count = dataSnapshot.child("Users").child(referUid).child("child").child("count").getValue().toString();


                if(count.equals("0")){
                    referDetails();
                    mRef.child("Users").child(referUid).child("child").child("lchild").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mRef.child("Users").child(referUid).child("child").child("count").setValue("1");

                }
                else if(count.equals("1")){
                    referDetails();
                    mRef.child("Users").child(referUid).child("child").child("rchild").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mRef.child("Users").child(referUid).child("child").child("count").setValue("2");


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
            mRef = FirebaseDatabase.getInstance().getReference();

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String state = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("state").getValue().toString();

                    if (state.equals("false")) {
                        final String mReferCode = editTextReferCode.getText().toString().trim();
                        String uid_p1 = dataSnapshot.child("ReferDB").child(mReferCode).child("uid").getValue().toString();
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