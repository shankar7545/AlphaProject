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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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


        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        final String currentdate = dateformat.format(c.getTime());
        final Date currentTime = Calendar.getInstance().getTime();


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
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("0");
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(mUserName);
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("paymentStatus").setValue("false");
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("childCount").setValue("0");
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("date").setValue(currentdate);
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("time").setValue(currentTime);
                            mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parentStatus").setValue("false");



                            //WalletDB

                            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue("0");
                            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("withdrawable").setValue("0");



                            //ReferDB

                            mReferDB.child(mUserName).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            mReferDB.child(mUserName).child("username").setValue(mUserName);


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
