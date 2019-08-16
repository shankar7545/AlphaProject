package com.example.alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
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

public class ReferCodeAcitvity extends AppCompatActivity {

    public EditText editTextReferCode;
    Button finish;
    TextView textViewReferCode;
    private static long back_pressed;
    public ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebase;
    DatabaseReference mRef, mReferDB, mTransactions, mWallet, mLevel, dbPaytm, mLogin ,mUsers,questionsRef;



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




        textViewReferCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String mEnd = dataSnapshot.child("AutoReferCode").child("end").getValue().toString();
                        String mStart = dataSnapshot.child("AutoReferCode").child("start").getValue().toString();


                        int end = Integer.parseInt(mEnd);

                        int start = Integer.parseInt(mStart);
                        final int random = new Random().nextInt((end-start)+1) + start;

                        final String autoReferCode = "user"+random;
                        String referCode = dataSnapshot.child("AutoReferCode").child(autoReferCode).child("refercode").getValue().toString();

                        editTextReferCode.setText(referCode);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish.setVisibility(View.GONE);

                progressBar.setVisibility(View.VISIBLE);
                final String mReferCode = editTextReferCode.getText().toString().trim();

                final DatabaseReference promodb = FirebaseDatabase.getInstance().getReference("ReferDB");
                promodb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot checkdataSnapshot) {


                        if (checkdataSnapshot.hasChild(mReferCode)) {

                            Child();
                            progressBar.setVisibility(View.GONE);
                            finish.setVisibility(View.VISIBLE);


                        }
                        else if(!checkdataSnapshot.hasChild(mReferCode))
                        {
                            editTextReferCode.setError("Invalid Username");
                            editTextReferCode.requestFocus();
                            finish.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                        if (mReferCode.isEmpty()) {
                            editTextReferCode.setError("Enter ReferCode");
                            editTextReferCode.requestFocus();
                            finish.setVisibility(View.VISIBLE);
                            return;
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
                    mRef.child("Users").child(referUid).child("child").child("rchild").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mRef.child("Users").child(referUid).child("child").child("count").setValue("1");

                }
                else if(count.equals("1")){
                    referDetails();
                    mRef.child("Users").child(referUid).child("child").child("lchild").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mRef.child("Users").child(referUid).child("child").child("count").setValue("2");

                }
                else{
                    editTextReferCode.setError("Limit Exceeded");
                    editTextReferCode.requestFocus();
                    finish.setVisibility(View.VISIBLE);
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

            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                    long childrenCount = dataSnapshot.getChildrenCount();
                    int count = (int) childrenCount;
                    int randomNumber = new Random().nextInt(count);
                    int i=0;
                    String themeTune; //Your random themeTune will be stored here
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        if(i == randomNumber) {
                            themeTune = snap.getValue(String.class);
                            break;
                        }
                        i++;
                    }
                    referDetails();


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