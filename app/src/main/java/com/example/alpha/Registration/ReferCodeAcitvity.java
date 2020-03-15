package com.example.alpha.Registration;

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

import com.example.alpha.Activity.HomeActivity;
import com.example.alpha.Model.ParentClass;
import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ReferCodeAcitvity extends AppCompatActivity {

    private static long back_pressed;
    public EditText editTextReferCode;
    public ProgressBar progressBar, progressBar2;
    Button finish;
    TextView textViewReferCode, skip;
    DatabaseReference mRef, mReferDB, mTransactions, mWallet, mLevel, dbPaytm, mLogin, mUsers, questionsRef, mAutoReferCode;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_code_acitvity);

        editTextReferCode = findViewById(R.id.referCode);
        skip = findViewById(R.id.skip);
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mFirebase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        finish = findViewById(R.id.finsh);
        progressBar = findViewById(R.id.progress_bar);
        progressBar2 = findViewById(R.id.progress_bar2);

        editTextReferCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mAutoReferCode = FirebaseDatabase.getInstance().getReference("AutoReferCode");

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReferCodeAcitvity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
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

                        } else if (checkdataSnapshot.hasChild(mReferCode)) {

                            Child();


                        } else if (!checkdataSnapshot.hasChild(mReferCode)) {
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

    private void Child() {

        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String mReferCode = editTextReferCode.getText().toString().trim();
                String referUid = dataSnapshot.child("ReferDB").child(mReferCode).child("uid").getValue().toString();
                String childCount = dataSnapshot.child("Users").child(referUid).child("childCount").getValue().toString();
                String parentStatus = dataSnapshot.child("Users").child(referUid).child("parentStatus").getValue().toString();

                if (parentStatus.equals("true")) {
                    if (childCount.equals("0")) {
                        referDetails();
                        mFirebase.child("Users").child(referUid).child("Chain").child("child").child("levelOne").child("leftChild").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        mFirebase.child("Users").child(referUid).child("childCount").setValue("1");

                    } else if (childCount.equals("1")) {
                        referDetails();
                        mFirebase.child("Users").child(referUid).child("Chain").child("child").child("levelOne").child("rightChild").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        mFirebase.child("Users").child(referUid).child("childCount").setValue("2");

                    } else {
                        editTextReferCode.setError("Limit Exceeded");
                        editTextReferCode.requestFocus();
                        Toast.makeText(ReferCodeAcitvity.this, "Limit exceeded , Try another Refer Code", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(ReferCodeAcitvity.this, mReferCode + " have no parent", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void referDetails() {
        try {
            mFirebase = FirebaseDatabase.getInstance().getReference();

            mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (!dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Child")
                            .child("parent").child("p2").exists()) {
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


                        Toast.makeText(ReferCodeAcitvity.this, "Success", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ReferCodeAcitvity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
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


                    } else {
                        Toast.makeText(ReferCodeAcitvity.this, "p2 Exists", Toast.LENGTH_SHORT).show();
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
        finish();

    }
}