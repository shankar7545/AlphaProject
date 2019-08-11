package com.example.alpha;

import android.os.Bundle;
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
        editTextReferCode = findViewById(R.id.referCode);


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
               registerUser();



            }

        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){

        }
    }

    private void registerUser(){
        final String mName = editTextName.getText().toString().trim();
        final String mEmail = editTextEmail.getText().toString().trim();
        final String mPassword = editTextPassword.getText().toString().trim();
        final String mReferCode = editTextReferCode.getText().toString().trim();



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

        if(mReferCode.isEmpty()){
            editTextReferCode.setError("Enter ReferCode");
            editTextReferCode.requestFocus();
            next.setVisibility(View.VISIBLE);

            return;

        }


        final DatabaseReference promodb = FirebaseDatabase.getInstance().getReference("ReferDB");
        promodb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot checkdataSnapshot) {
                next.setVisibility(View.GONE);

                progressBar.setVisibility(View.VISIBLE);

                if(checkdataSnapshot.hasChild(mReferCode))
                {
                    mAuth.createUserWithEmailAndPassword(mEmail,mPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);

                                        //UsersDB

                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(mName);
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(mEmail);
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("password").setValue(mPassword);
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(mUserName);
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("refercode").setValue(mReferCode);

                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p1").child("uid").setValue("0");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p2").child("uid").setValue("0");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p3").child("uid").setValue("0");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p4").child("uid").setValue("0");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p5").child("uid").setValue("0");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p6").child("uid").setValue("0");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p7").child("uid").setValue("0");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p8").child("uid").setValue("0");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p8").child("uid").setValue("0");



                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p1").child("state").setValue("false");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p2").child("state").setValue("false");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p3").child("state").setValue("false");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p4").child("state").setValue("false");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p5").child("state").setValue("false");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p6").child("state").setValue("false");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p7").child("state").setValue("false");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("p8").child("state").setValue("false");
                                        mFirebase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("state").setValue("false");







                                        //WalletDB

                                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue("0");
                                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("withdrawable").setValue("0");



                                        //Level

                                        mLevel.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("0");



                                        //Transactions
                                        mTransactions.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T1").setValue("0");
                                        mTransactions.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T2").setValue("0");
                                        mTransactions.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T3").setValue("0");
                                        mTransactions.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T4").setValue("0");
                                        mTransactions.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T5").setValue("0");
                                        mTransactions.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T6").setValue("0");
                                        mTransactions.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T7").setValue("0");
                                        mTransactions.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("T8").setValue("0");



                                        //ReferDB

                                        mReferDB.child(mUserName).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        mReferDB.child(mUserName).child("username").setValue(mUserName);


                                    }else {
                                        Toast.makeText(Signup_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

                else{
                    next.setVisibility(View.VISIBLE);

                    progressBar.setVisibility(View.GONE);
                    editTextReferCode.setError("Invalid ReferCode");
                    editTextReferCode.requestFocus();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


}
