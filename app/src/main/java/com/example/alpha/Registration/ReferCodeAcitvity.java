package com.example.alpha.Registration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Activity.HomeActivity;
import com.example.alpha.Model.ParentClass;
import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ReferCodeAcitvity extends AppCompatActivity {

    public InputMethodManager imm;

    private static long back_pressed;
    public EditText editTextReferCode;
    public ProgressBar progressBar;
    ProgressDialog bar;
    Button finish;
    TextView textViewReferCode;
    DatabaseReference mRef, mReferCodeDB, mChain, mWallet, mLevel, dbPaytm, mLogin, mUsers, questionsRef, mAutoReferCode;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebase;
    final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    private View parent_view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_code_acitvity);

        parent_view = findViewById(android.R.id.content);


        editTextReferCode = findViewById(R.id.referCode);
        mRef = FirebaseDatabase.getInstance().getReference();
        mUsers = FirebaseDatabase.getInstance().getReference("Users");
        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");

        mChain = FirebaseDatabase.getInstance().getReference("Chain");

        mFirebase = FirebaseDatabase.getInstance().getReference();


        imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);


        assert imm != null;

        mAuth = FirebaseAuth.getInstance();
        finish = findViewById(R.id.finsh);
        progressBar = findViewById(R.id.progress_bar);

        editTextReferCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mAutoReferCode = FirebaseDatabase.getInstance().getReference("AutoReferCode");

        initToolbar();
        statusBarColor();

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

        finish.setOnClickListener(view -> referCode());

        bar = new ProgressDialog(ReferCodeAcitvity.this, R.style.MyAlertDialogStyle);
        bar.setCancelable(false);
        bar.setIndeterminate(true);
        bar.setCanceledOnTouchOutside(true);
    }

    //ReferCode
    private void referCode() {
        try {

            final String mReferCode = editTextReferCode.getText().toString().trim();
            editTextReferCode.setEnabled(false);
            finish.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            mReferCodeDB = FirebaseDatabase.getInstance().getReference("ReferDB");

            mReferCodeDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot checkdataSnapshot) {

                    if (mReferCode.isEmpty()) {
                        editTextReferCode.setError("Enter ReferCode");
                        editTextReferCode.requestFocus();
                        progressBar.setVisibility(View.GONE);
                        finish.setVisibility(View.VISIBLE);
                        editTextReferCode.setEnabled(true);
                        editTextReferCode.requestFocus();
                        editTextReferCode.setSelection(editTextReferCode.getText().length());
                        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);


                    } else if (checkdataSnapshot.hasChild(mReferCode)) {

                        mWallet.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String mainBalance = snapshot.child("Balance").child("mainBalance").getValue().toString();
                                int user_bal_Int = Integer.parseInt(mainBalance);

                                if (user_bal_Int >= 50) {
                                    Child(mReferCode);

                                } else {

                                    Snackbar("Balance insufficient / Balance : " + mainBalance);
                                    progressBar.setVisibility(View.GONE);
                                    finish.setVisibility(View.VISIBLE);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else if (!checkdataSnapshot.hasChild(mReferCode)) {
                        editTextReferCode.setError("Invalid ReferCode");
                        editTextReferCode.requestFocus();
                        progressBar.setVisibility(View.GONE);
                        finish.setVisibility(View.VISIBLE);
                        editTextReferCode.setEnabled(true);
                        editTextReferCode.requestFocus();
                        editTextReferCode.setSelection(editTextReferCode.getText().length());

                        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

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

    //ReferCodeChild
    private void Child(final String mReferCode) {

        try {
            mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String referUid = Objects.requireNonNull(dataSnapshot.child("ReferDB").child(mReferCode).child("uid").getValue()).toString();
                    String enabledStatus = Objects.requireNonNull(dataSnapshot.child("ReferDB").child(mReferCode).child("enabled").getValue()).toString();
                    String userName = Objects.requireNonNull(dataSnapshot.child("Users").child(selfUid).child("username").getValue()).toString();

                    if (!userName.equals(mReferCode)) {

                        if ((enabledStatus.equals("true")) && !referUid.equals("null")) {


                            String Status = Objects.requireNonNull(dataSnapshot.child("Status").child(referUid).child("status").getValue()).toString();
                            String usingByUID = Objects.requireNonNull(dataSnapshot.child("Status").child(referUid).child("usingByUID").getValue()).toString();

                            if ((Status.equals("free"))) {
                                mFirebase.child("Status").child(referUid).child("status").setValue("busy");
                                mFirebase.child("Status").child(referUid).child("usingByUID").setValue(selfUid);
                                Snackbar("Status Free Success");

                                progressBar.setVisibility(View.GONE);
                                finish.setVisibility(View.VISIBLE);
                                editTextReferCode.setEnabled(true);

                                String childCount = Objects.requireNonNull(dataSnapshot.child("ReferDB").child(mReferCode).child("childCount").getValue()).toString();
                                childData(mReferCode, childCount, referUid, userName);
                            } else if (Status.equals("busy") && usingByUID.equals(selfUid)) {

                                mFirebase.child("Status").child(referUid).child("status").setValue("free");
                                mFirebase.child("Status").child(referUid).child("usingByUID").setValue("null");
                                Snackbar("Reset success");
                                referCode();
                            } else {
                                Toast.makeText(ReferCodeAcitvity.this, "Retry in 30 seconds", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                finish.setVisibility(View.VISIBLE);
                                editTextReferCode.setEnabled(true);
                            }


                        } else {
                            Snackbar(mReferCode + " is not Enabled/Activated");
                            progressBar.setVisibility(View.GONE);
                            finish.setVisibility(View.VISIBLE);
                            editTextReferCode.setEnabled(true);
                        }
                    } else {
                        Snackbar("IDIOT! Do not enter Your USERNAME");
                        progressBar.setVisibility(View.GONE);
                        finish.setVisibility(View.VISIBLE);
                        editTextReferCode.setEnabled(true);
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

    private void childData(String mReferCode, String childCount, String referUid, String userName) {
        if (childCount.equals("0")) {
            bar.setMessage("Joining under " + mReferCode);
            bar.show();
            referDetails(mReferCode, referUid, userName);
            mChain.child(referUid).child("uid1").child("uid").setValue(selfUid);
            mChain.child(referUid).child("uid1").child("username").setValue(userName);
            mFirebase.child("ReferDB").child(mReferCode).child("childCount").setValue("1");

        } else if (childCount.equals("1")) {
            bar.setMessage("Joining under " + mReferCode);
            bar.show();
            referDetails(mReferCode, referUid, userName);
            mChain.child(referUid).child("uid2").child("uid").setValue(selfUid);
            mChain.child(referUid).child("uid2").child("username").setValue(userName);
            mFirebase.child("ReferDB").child(mReferCode).child("childCount").setValue("2");

        } else {

            Snackbar("Limit exceeded , Try another Refer Code");
            progressBar.setVisibility(View.GONE);
            finish.setVisibility(View.VISIBLE);
            editTextReferCode.setEnabled(true);
            mFirebase.child("Status").child(referUid).child("status").setValue("free");
            mFirebase.child("Status").child(referUid).child("usingByUID").setValue("null");
        }
    }


    //ReferCodeData
    private void referDetails(final String mReferCode, String referUid, String userName) {
        try {

            bar.setMessage("Writing Parent info...");


            mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    String uid_p2 = Objects.requireNonNull(dataSnapshot.child("Chain").child(referUid).child("parent").child("p1").getValue()).toString();
                    String uid_p3 = Objects.requireNonNull(dataSnapshot.child("Chain").child(uid_p2).child("parent").child("p1").getValue()).toString();
                    String uid_p4 = Objects.requireNonNull(dataSnapshot.child("Chain").child(uid_p3).child("parent").child("p1").getValue()).toString();
                    String uid_p5 = Objects.requireNonNull(dataSnapshot.child("Chain").child(uid_p4).child("parent").child("p1").getValue()).toString();
                    String uid_p6 = Objects.requireNonNull(dataSnapshot.child("Chain").child(uid_p5).child("parent").child("p1").getValue()).toString();
                    String uid_p7 = Objects.requireNonNull(dataSnapshot.child("Chain").child(uid_p6).child("parent").child("p1").getValue()).toString();
                    String uid_p8 = Objects.requireNonNull(dataSnapshot.child("Chain").child(uid_p7).child("parent").child("p1").getValue()).toString();

                    mUsers.child(selfUid).child("parentStatus").setValue("true");


                    //Parent Class
                    ParentClass parentClass = new ParentClass(
                            referUid,
                            uid_p2,
                            uid_p3,
                            uid_p4,
                            uid_p5,
                            uid_p6,
                            uid_p7,
                            uid_p8
                    );

                    mChain.child(selfUid).child("parent").setValue(parentClass);


                    upgradeToBronze(mReferCode, referUid, userName);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upgradeToBronze(String mReferCode, String referUid, String userName) {
        bar.setMessage("Upgrading to Bronze...");

        mWallet.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //UserBalance
                String mainBalance = Objects.requireNonNull(dataSnapshot.child(selfUid).child("Balance").child("mainBalance").getValue()).toString();
                int user_bal_Int = Integer.parseInt(mainBalance);

                if (user_bal_Int >= 50) {
                    String user_updated_bal = Integer.toString(user_bal_Int - 50);
                    mWallet.child(selfUid).child("Balance").child("mainBalance").setValue(user_updated_bal);

                } else {
                    Toast.makeText(ReferCodeAcitvity.this, "Reduction Failed", Toast.LENGTH_SHORT).show();
                }

                //ParentOneBalance

                String parentBalance = Objects.requireNonNull(dataSnapshot.child(referUid).child("Balance").child("bronze").getValue()).toString();
                int parent_bal_Int = Integer.parseInt(parentBalance);

                String parent_updated_bal = Integer.toString(parent_bal_Int + 50);

                mWallet.child(referUid).child("Balance").child("bronze").setValue(parent_updated_bal);


                bar.setMessage("Updating Transactions...");

                String id = UUID.randomUUID().toString();
                String idOne = "PV" + id.substring(0, 6).toUpperCase();
                String idTwo = "EX" + id.substring(0, 6).toUpperCase();
                //MainTransactions
                Transaction_Class main_transaction_class = new Transaction_Class(
                        "debited",
                        "date",
                        "time",
                        userName,
                        mReferCode,
                        idOne + idTwo,
                        "50",
                        1,
                        "bronze",
                        ""
                );
                mFirebase.child("Transactions").child(idOne + idTwo).setValue(main_transaction_class);


                //sendTransaction in user
                long countR = dataSnapshot.child(selfUid).child("Transactions")
                        .child("history").getChildrenCount();

                long sizeR = countR + 1;


                Transaction_Class send_transaction_class = new Transaction_Class(
                        "debited",
                        "date",
                        "time",
                        userName,
                        mReferCode,
                        idOne + idTwo,
                        "50",
                        sizeR,
                        "bronze",
                        ""
                );
                mWallet.child(selfUid).child("Transactions").child("history").child(idOne + idTwo).setValue(send_transaction_class);


                //receivedTransaction in parent1

                long countP = dataSnapshot.child(referUid).child("Transactions")
                        .child("history").getChildrenCount();

                long sizeP = countP + 1;
                Transaction_Class received_transaction_class = new Transaction_Class(
                        "credited",
                        "date",
                        "time",
                        userName,
                        mReferCode,
                        idOne + idTwo,
                        "50",
                        sizeP,
                        "bronze",
                        ""


                );
                mWallet.child(referUid).child("Transactions").child("history").child(idOne + idTwo).setValue(received_transaction_class);
                //Parent Transaction Count
                String p1_tran_count = dataSnapshot.child(referUid).child("Transactions").child("count").child("level1").getValue().toString();
                int p1_tran_count_Int = Integer.parseInt(p1_tran_count);
                String updated_p1_tran_count = Integer.toString(p1_tran_count_Int + 1);
                mWallet.child(referUid).child("Transactions").child("count").child("level1").setValue(updated_p1_tran_count);


                //Upgrading level
                mUsers.child(selfUid).child("Achievement").setValue("Bronze");


                mFirebase.child("Status").child(referUid).child("status").setValue("free");
                mFirebase.child("Status").child(referUid).child("usingByUID").setValue("null");

                new Handler().postDelayed(bar::dismiss, 2000);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //AutoLoginCode
        //String p1_email = dataSnapshot.child("Users").child(referUid).child("email").getValue().toString();
        //String p1_password = dataSnapshot.child("Users").child(referUid).child("password").getValue().toString();
        //String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
        //int i = Integer.parseInt(mEnd);

        //mLogin.child("user" + i).child("uid").setValue(referUid);
        //mLogin.child("user" + i).child("email").setValue(p1_email);
        //mLogin.child("user" + i).child("password").setValue(p1_password);

        //String endCount = Integer.toString(i + 1);


    }


    private void Snackbar(String text) {
        Snackbar snackbar = Snackbar.make(parent_view, Objects.requireNonNull(text), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.green_700))
                .setAction("Okay", view -> {
                });
        snackbar.show();

    }

    private void statusBarColor() {

        Window window = ReferCodeAcitvity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(ReferCodeAcitvity.this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    private void initToolbar() {
        findViewById(R.id.backToolbar).setOnClickListener(v -> onBackPressed());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Close refercode ?")
                .setPositiveButton("Yes", (dialog, id) ->
                {
                    startActivity(new Intent(this, HomeActivity.class));
                    //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                })
                .setNegativeButton("No", null)
                .show();

    }


}