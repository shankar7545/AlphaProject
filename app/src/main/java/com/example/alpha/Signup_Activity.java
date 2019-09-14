package com.example.alpha;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup_Activity extends AppCompatActivity {

    EditText editTextName, editTextEmail ,editTextPassword,editTextReferCode ;
    Button next;
    Button finish;
    private FirebaseAuth mAuth;
    public ProgressBar progressBar;

    private DatabaseReference mFirebase ,mReferDB ,mWallet, mTransactions , mLevel , mRef ,mLogin;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);



        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        next = (Button)findViewById(R.id.next);

        mRef = FirebaseDatabase.getInstance().getReference();
        mFirebase = FirebaseDatabase.getInstance().getReference("Users");
        mReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");
        mTransactions = FirebaseDatabase.getInstance().getReference("Transactions");
        mLevel = FirebaseDatabase.getInstance().getReference("Level");
        mLogin = FirebaseDatabase.getInstance().getReference("Login");
        mAuth = FirebaseAuth.getInstance();







        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);

                registerUser();

            }

        });
    }



    private void registerUser(){

        progressBar.setVisibility(View.VISIBLE);


        final String mName = editTextName.getText().toString().trim();
        final String mEmail = editTextEmail.getText().toString().trim();
        final String mPassword = editTextPassword.getText().toString().trim();



        Bundle bundle = getIntent().getExtras();
        final String mUserName = bundle.getString("stuff");



        if(mName.isEmpty()){
            editTextName.setError("Enter Name");
            editTextName.requestFocus();
            next.setVisibility(View.VISIBLE);



            return;
        }
        if(mEmail.isEmpty()){
            editTextEmail.setError("Enter Email");
            editTextEmail.requestFocus();
            next.setVisibility(View.VISIBLE);


            return;


        }

        if(mPassword.isEmpty()){
            editTextPassword.setError("Enter Password");
            editTextPassword.requestFocus();
            next.setVisibility(View.VISIBLE);


            return;
        }

        if(mPassword.length() <= 5){
            editTextPassword.setError("Enter valid Password");
            editTextPassword.requestFocus();
            next.setVisibility(View.VISIBLE);


            return;
        }



        mAuth.createUserWithEmailAndPassword(mEmail,mPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){


                            //UsersDB

                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(mName);
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(mEmail);
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("password").setValue(mPassword);
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(mUserName);
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("payment").setValue("false");
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("child").child("count").setValue("0");

                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p1").child("uid").setValue("0");
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p2").child("uid").setValue("0");
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p3").child("uid").setValue("0");
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p4").child("uid").setValue("0");
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p5").child("uid").setValue("0");
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p6").child("uid").setValue("0");
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p7").child("uid").setValue("0");
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p8").child("uid").setValue("0");
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p8").child("uid").setValue("0");


                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("state").setValue("false");



                            //WalletDB

                            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue("0");
                            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("withdrawable").setValue("0");
                            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("received").child("50*2").child("state").setValue("0");
                            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("received").child("100*4").child("state").setValue("0");
                            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("received").child("400*8").child("state").setValue("0");
                            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("received").child("3000*16").child("state").setValue("0");
                            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("received").child("40000*32").child("state").setValue("0");


                            //Level

                            mLevel.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("0");



                            //ReferDB

                            mReferDB.child(mUserName).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            mReferDB.child(mUserName).child("username").setValue(mUserName);
                            mReferDB.child(mUserName).child("child").setValue("0");


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);

                                    startActivity(new Intent(Signup_Activity.this, PaytmPayment.class));
                                    finish();
                                }
                            },3000);

                        }else {
                            Toast.makeText(Signup_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            next.setVisibility(View.VISIBLE);

                        }

                    }
                });



    }


}
