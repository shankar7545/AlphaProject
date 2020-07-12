package com.example.alpha.Registration;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alpha.Model.TransactionCount_class;
import com.example.alpha.Model.UserClass;
import com.example.alpha.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DialogSignupFragment extends DialogFragment {

    private static long back_pressed;

    public ProgressBar progressBar;
    TextInputEditText editTextName, editTextEmail, editTextPassword, editTextReferCode;
    Button next;
    Button finish;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebase, mReferDB, mWallet, mTransactions, mChain, mRef, mLogin, mUsers;

    LinearLayout login;
    private Dialog dialog;
    ProgressDialog progressDialog;

    private View root_view;
    private int request_code = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.activity_signup, container, false);

        login = root_view.findViewById(R.id.login);
        editTextName = root_view.findViewById(R.id.name);
        editTextEmail = root_view.findViewById(R.id.email);
        editTextPassword = root_view.findViewById(R.id.password);


        progressBar = root_view.findViewById(R.id.progress_bar);

        next = root_view.findViewById(R.id.next);

        mRef = FirebaseDatabase.getInstance().getReference();
        mUsers = FirebaseDatabase.getInstance().getReference("Users");
        mReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
        mTransactions = FirebaseDatabase.getInstance().getReference("Transactions");
        mLogin = FirebaseDatabase.getInstance().getReference("Login");
        mChain = FirebaseDatabase.getInstance().getReference("Chain");
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();


        root_view.findViewById(R.id.bt_close).setOnClickListener(v -> dismiss());


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
            if (mPassword.length() <= 6) {
                editTextPassword.setError("Minimum 6 Letters");
                editTextPassword.requestFocus();
                next.setVisibility(View.VISIBLE);


                return;
            }


            registerUser(null);

        });

        return root_view;
    }

    private void registerUser(String mUserName) {

        //ProgressBar
        progressDialog = new ProgressDialog(getContext());
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
                                "0",
                                "false",
                                currentdate,
                                "0");


                        mUsers.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user);
                        mUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Achievement").setValue("Beginner");
                        mUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("Chain").child("parent").child("p1").setValue("null");


                        //WalletDB
                        mWallet = FirebaseDatabase.getInstance().getReference("Wallet")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        mWallet.child("Balance").child("mainBalance").setValue("0");
                        mWallet.child("Balance").child("bronzeBalance").setValue("0");
                        mWallet.child("Balance").child("silverBalance").setValue("0");
                        mWallet.child("Balance").child("goldBalance").setValue("0");
                        mWallet.child("Balance").child("diamondBalance").setValue("0");
                        mWallet.child("Balance").child("withdrawable").setValue("0");

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
                       /* ReferClass referClass = new ReferClass(
                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                mUserName);

                        assert mUserName != null;
                        mReferDB.child(mUserName).setValue(referClass); */


                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        next.setVisibility(View.VISIBLE);

                    }

                });


    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }

    public interface CallbackResult {
        void sendResult(int requestCode);
    }


}