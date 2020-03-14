package com.example.alpha.Registration;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alpha.Model.ReferClass;
import com.example.alpha.Model.TransactionCount_class;
import com.example.alpha.Model.UserClass;
import com.example.alpha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Signup_Activity extends AppCompatActivity {

    public ProgressBar progressBar;
    EditText editTextName, editTextEmail, editTextPassword, editTextReferCode;
    Button next;
    Button finish;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebase, mReferDB, mWallet, mTransactions, mLevel, mRef, mLogin, mUsers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);


        progressBar = findViewById(R.id.progress_bar);

        next = findViewById(R.id.next);

        mRef = FirebaseDatabase.getInstance().getReference();
        mUsers = FirebaseDatabase.getInstance().getReference("Users");
        mReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
        mTransactions = FirebaseDatabase.getInstance().getReference("Transactions");
        mLevel = FirebaseDatabase.getInstance().getReference("Level");
        mLogin = FirebaseDatabase.getInstance().getReference("Login");
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);

                registerUser();

            }

        });

    }


    private void registerUser() {

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


        if (mName.isEmpty()) {
            editTextName.setError("Enter Name");
            editTextName.requestFocus();
            next.setVisibility(View.VISIBLE);


            return;
        }
        if (mEmail.isEmpty()) {
            editTextEmail.setError("Enter Email");
            editTextEmail.requestFocus();
            next.setVisibility(View.VISIBLE);


            return;


        }

        if (mPassword.isEmpty()) {
            editTextPassword.setError("Enter Password");
            editTextPassword.requestFocus();
            next.setVisibility(View.VISIBLE);

            return;
        }
        if (mPassword.length() <= 5) {
            editTextPassword.setError("Enter valid Password");
            editTextPassword.requestFocus();
            next.setVisibility(View.VISIBLE);


            return;
        }


        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {


                            //UsersDB
                            UserClass user = new UserClass(
                                    mEmail,
                                    mName,
                                    mPassword,
                                    "false",
                                    mUserName,
                                    "0",
                                    "false",
                                    currentdate,
                                    "0"
                            );


                            mUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);


                            mUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("Chain").child("parent").child("p1").setValue("null");


                            //WalletDB
                            mWallet = FirebaseDatabase.getInstance().getReference("Wallet")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            mWallet.child("balance").setValue("0");
                            mWallet.child("withdrawable").setValue("0");


                            TransactionCount_class transactionCount_class = new TransactionCount_class(
                                    "0",
                                    "0",
                                    "0",
                                    "0",
                                    "0",
                                    "0",
                                    "0",
                                    "0"
                            );

                            mWallet.child("Transactions").child("count").setValue(transactionCount_class);





                            //ReferDB
                            ReferClass referClass = new ReferClass(
                                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                    mUserName);


                            mReferDB.child(mUserName).setValue(referClass);



                        } else {
                            Toast.makeText(Signup_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            next.setVisibility(View.VISIBLE);

                        }

                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();


    }
}
