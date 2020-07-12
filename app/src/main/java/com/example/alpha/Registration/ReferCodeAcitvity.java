package com.example.alpha.Registration;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Model.ParentClass;
import com.example.alpha.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ReferCodeAcitvity extends AppCompatActivity {

    public InputMethodManager imm;

    private static long back_pressed;
    public EditText editTextReferCode;
    public ProgressBar progressBar;
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
        mRef = FirebaseDatabase.getInstance().getReference("Users");
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

        finish.setOnClickListener(view -> {


            referCode();

        });

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

                        Child(mReferCode);


                        //Toast.makeText(ReferCodeAcitvity.this, "Refercode Found  "+mReferCode, Toast.LENGTH_SHORT).show();


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
                    String childCount = Objects.requireNonNull(dataSnapshot.child("ReferDB").child(mReferCode).child("childCount").getValue()).toString();
                    String enabledStatus = Objects.requireNonNull(dataSnapshot.child("ReferDB").child(mReferCode).child("enabled").getValue()).toString();
                    String userName = Objects.requireNonNull(dataSnapshot.child("Users").child(selfUid).child("username").getValue()).toString();

                    if (!userName.equals(mReferCode)) {
                        String p1 = Objects.requireNonNull(dataSnapshot.child("Chain").child(referUid)
                                .child("parent").child("p1").getValue()).toString();

                        if ((enabledStatus.equals("true")) && !p1.equals("null")) {
                            if (childCount.equals("0")) {
                                referDetails(mReferCode);

                                mChain.child(referUid).child("uid1").child("uid").setValue(selfUid);
                                mChain.child(referUid).child("uid1").child("username").setValue(userName);
                                mFirebase.child("ReferDB").child(mReferCode).child("childCount").setValue("1");

                            } else if (childCount.equals("1")) {
                                referDetails(mReferCode);
                                mChain.child(referUid).child("uid2").child("uid").setValue(selfUid);
                                mChain.child(referUid).child("uid2").child("username").setValue(userName);
                                mFirebase.child("ReferDB").child(mReferCode).child("childCount").setValue("2");

                            } else {
                                //editTextReferCode.setError("Limit Exceeded");
                                //editTextReferCode.requestFocus();

                                Snackbar("Limit exceeded , Try another Refer Code");
                                progressBar.setVisibility(View.GONE);
                                finish.setVisibility(View.VISIBLE);
                                editTextReferCode.setEnabled(true);
                            }
                        } else {
                            Snackbar(mReferCode + " have no parent");
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


    //ReferCodeData
    private void referDetails(final String mReferCode) {
        try {


            mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    String uid_p1 = dataSnapshot.child("ReferDB").child(mReferCode).child("uid").getValue().toString();

                    String uid_p2 = dataSnapshot.child("Chain").child(uid_p1).child("parent").child("p1").getValue().toString();
                    String uid_p3 = dataSnapshot.child("Chain").child(uid_p2).child("parent").child("p1").getValue().toString();
                    String uid_p4 = dataSnapshot.child("Chain").child(uid_p3).child("parent").child("p1").getValue().toString();
                    String uid_p5 = dataSnapshot.child("Chain").child(uid_p4).child("parent").child("p1").getValue().toString();
                    String uid_p6 = dataSnapshot.child("Chain").child(uid_p5).child("parent").child("p1").getValue().toString();
                    String uid_p7 = dataSnapshot.child("Chain").child(uid_p6).child("parent").child("p1").getValue().toString();
                    String uid_p8 = dataSnapshot.child("Chain").child(uid_p7).child("parent").child("p1").getValue().toString();

                    mUsers.child(selfUid).child("parentStatus").setValue("true");


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

                    mChain.child(selfUid).child("parent").setValue(parentClass);


                    Toast.makeText(ReferCodeAcitvity.this, "Success", Toast.LENGTH_SHORT).show();


                        /*AutoReferCode
                        String mEnd = dataSnapshot.child("AutoReferCode").child("end").getValue().toString();
                        String mStart = dataSnapshot.child("AutoReferCode").child("start").getValue().toString();
                        String mUsername = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").getValue().toString();

                        int i = Integer.parseInt(mEnd);
                        mAutoReferCode.child("user" + i).child("refercode").setValue(mUsername);

                        String endCount = Integer.toString(i + 1);

                        mAutoReferCode.child("end").setValue(endCount);
                        //AutoReferCodeEnd  */


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Snackbar(String text) {
        Snackbar snackbar = Snackbar.make(parent_view, Objects.requireNonNull(text), Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(getResources().getColor(R.color.green_700))
                .setAction("Okay", view -> {
                });
        snackbar.show();

    }


    private void initToolbar() {
        findViewById(R.id.backToolbar).setOnClickListener(v -> finish());

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
        finish();

    }
}