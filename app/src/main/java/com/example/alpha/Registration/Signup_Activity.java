package com.example.alpha.Registration;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Model.ReferClass;
import com.example.alpha.Model.TransactionCount_class;
import com.example.alpha.Model.UserClass;
import com.example.alpha.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Signup_Activity extends AppCompatActivity {

    public ProgressBar progressBar;
    TextInputEditText editTextName, editTextEmail, editTextPassword, editTextReferCode;
    Button next;
    Button finish;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebase, mReferDB, mWallet, mTransactions, mChain, mRef, mLogin, mUsers;

    LinearLayout login;
    private Dialog dialog;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        login = findViewById(R.id.login);
        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);


        progressBar = findViewById(R.id.progress_bar);

        next = findViewById(R.id.next);

        mRef = FirebaseDatabase.getInstance().getReference();
        mUsers = FirebaseDatabase.getInstance().getReference("Users");
        mReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
        mTransactions = FirebaseDatabase.getInstance().getReference("Transactions");
        mLogin = FirebaseDatabase.getInstance().getReference("Login");
        mChain = FirebaseDatabase.getInstance().getReference("Chain");
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();


        login.setOnClickListener(v ->
                finish());

        next.setOnClickListener(view -> {
            //progressBar.setVisibility(View.VISIBLE);
            //next.setVisibility(View.GONE);
            final String mName = editTextName.getText().toString().trim();
            final String mEmail = editTextEmail.getText().toString().trim();
            final String mPassword = editTextPassword.getText().toString().trim();


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


            referDialog();

        });

    }


    private void referDialog() {
        try {
            dialog = new Dialog(Signup_Activity.this);
            dialog.setContentView(R.layout.referdialog);
            dialog.setCancelable(true);
            final EditText editTextuserName;
            final TextView heading;
            final ProgressBar progressBar;
            final Button finish, cancel;

            editTextuserName = dialog.findViewById(R.id.referCode);
            mRef = FirebaseDatabase.getInstance().getReference("Users");
            mFirebase = FirebaseDatabase.getInstance().getReference();

            finish = dialog.findViewById(R.id.finish);
            cancel = dialog.findViewById(R.id.cancelBtn);
            heading = dialog.findViewById(R.id.heading);
            progressBar = dialog.findViewById(R.id.progress_bar);

            editTextuserName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            heading.setText(R.string.enter_user_name);
            dialog.show();
            finish.setOnClickListener(view -> {


                cancel.setEnabled(true);

            });

            finish.setOnClickListener(view -> {
                final String mUserName = editTextuserName.getText().toString().trim();


                final DatabaseReference promodb = FirebaseDatabase.getInstance().getReference("ReferDB");
                promodb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot checkdataSnapshot) {


                        if (mUserName.isEmpty()) {
                            editTextuserName.setError("Enter Username");
                            editTextuserName.requestFocus();
                            return;
                        } else if (mUserName.length() < 5) {
                            editTextuserName.setError("Enter 5 Letters");
                            editTextuserName.requestFocus();
                            return;
                        } else if (checkdataSnapshot.hasChild(mUserName)) {
                            editTextuserName.setError("Username Exists");
                            editTextuserName.requestFocus();
                        } else {

                            registerUser(mUserName);
                            dialog.dismiss();

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            });


            cancel.setOnClickListener(v -> dialog.dismiss());
            next.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void registerUser(String mUserName) {

        //ProgressBar
        progressDialog = new ProgressDialog(Signup_Activity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog_new);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);


        final String mName = Objects.requireNonNull(editTextName.getText()).toString().trim();
        final String mEmail = Objects.requireNonNull(editTextEmail.getText()).toString().trim();
        final String mPassword = Objects.requireNonNull(editTextPassword.getText()).toString().trim();


        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        final String currentdate = dateformat.format(c.getTime());
        final Date currentTime = Calendar.getInstance().getTime();



        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(task -> {

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


                        mUsers.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user);
                        mUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Achievement").setValue("Beginner");
                        mUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("Chain").child("parent").child("p1").setValue("null");


                        //WalletDB
                        mWallet = FirebaseDatabase.getInstance().getReference("Wallet")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        mWallet.child("balance").setValue("0");
                        mWallet.child("withdrawable").setValue("0");
                        mWallet.child("Status").child("status").setValue("free");
                        mWallet.child("Status").child("uid").setValue("free");



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


                        //Chain


                        mChain.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid1").child("uid").setValue("null");
                        mChain.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid1").child("username").setValue("null");

                        mChain.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid2").child("uid").setValue("null");
                        mChain.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid2").child("username").setValue("null");


                        /*AchievementsClass achievementsClass = new AchievementsClass(
                                "unlocked",
                                "locked",
                                "locked",
                                "locked",
                                "locked"
                        );

                        mUsers.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .child("Achievements").setValue(achievementsClass); */


                        //ReferDB
                        ReferClass referClass = new ReferClass(
                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                mUserName);


                        assert mUserName != null;
                        mReferDB.child(mUserName).setValue(referClass);


                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(Signup_Activity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        next.setVisibility(View.VISIBLE);

                    }

                });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Cancel Registration?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", null)
                .show();
    }
}
