package com.example.alpha.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Activity.ChainActivity;
import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.example.alpha.Wallet.TransactionFilterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.NachoTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BronzeFragment extends Fragment {

    private View mView;

    TextView referCount, transactionCount, upgradeTextView;
    ImageView referImageView, transactionImageView;
    RelativeLayout referRelative, transactionRelative;
    LinearLayout upgradeLayoutBronze, transactionButton, referButton, usernameDisplay;

    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
    private SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss aa");
    private String timeformat = time.format(c.getTime());
    private String datetime = dateformat.format(c.getTime());


    private DatabaseReference mUsers, mFirebase, mTransactions, mWallet, mChain;
    private final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    ProgressDialog progressDialog;
    ProgressBar upgradeProgressBar;

    public BronzeFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        Bundle args = new Bundle();

        BronzeFragment fragment = new BronzeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_bronze, container, false);

        upgradeLayoutBronze = mView.findViewById(R.id.upgradeLayoutBronze);
        usernameDisplay = mView.findViewById(R.id.usernameDisplay);

        referRelative = mView.findViewById(R.id.referRelative);
        referButton = mView.findViewById(R.id.referButton);
        referCount = mView.findViewById(R.id.referCount);
        referImageView = mView.findViewById(R.id.referImageView);


        transactionRelative = mView.findViewById(R.id.transactionRelative);
        transactionButton = mView.findViewById(R.id.transactionButton);
        transactionCount = mView.findViewById(R.id.transactionCount);
        transactionImageView = mView.findViewById(R.id.transactionImageView);


        upgradeTextView = mView.findViewById(R.id.upgradeTextView);
        upgradeProgressBar = mView.findViewById(R.id.upgradeProgressBar);

        mFirebase = FirebaseDatabase.getInstance().getReference();
        mUsers = FirebaseDatabase.getInstance().getReference("Users");
        mChain = FirebaseDatabase.getInstance().getReference("Chain");
        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");
        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet").child(selfUid).child("Transactions");


        ReferralFunction();
        TransactionFunction();
        checkUpgrade();


        return mView;
    }


    private void ReferralFunction() {

        try {

            mUsers.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String childCount = Objects.requireNonNull(dataSnapshot.child("childCount").getValue()).toString();


                    if (childCount.equals("2")) {
                        referRelative.setBackgroundResource(R.drawable.shape_round_solid_green);
                        referRelative.removeAllViews();
                        ImageButton img = new ImageButton(getContext());
                        img.setImageResource(R.drawable.ic_done);
                        img.setBackgroundColor(Color.TRANSPARENT);
                        img.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                        referRelative.addView(img);

                        String a = "two";
                        usernameDisplayMethod(a);

                        referImageView.setImageResource(R.drawable.green_chechk);
                        referCount.setText("2/2");
                        referButton.setVisibility(View.GONE);
                        referButton.setOnClickListener(v -> {
                            startActivity(new Intent(getContext(), ChainActivity.class));
                        });


                    } else if (childCount.equals("1")) {

                        referImageView.setImageResource(R.drawable.red_check);
                        referCount.setText("1/2");
                        referButton.setVisibility(View.VISIBLE);
                        String a = "one";
                        usernameDisplayMethod(a);
                        referButton.setOnClickListener(v -> {
                            try {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Alpha");
                                String shareMessage = "\nInterested to earn Money? Want to make some cash out of it?? Try out DreamWinner, an eSports Platform. Join Daily PUBG Matches & Get Rewards on Each Kill you Score. Get Huge Prize on Getting Chicken Dinner. Just Download the DreamWinner Android App & Register and Prove your Skills \n\n";
                                shareMessage = shareMessage + "Download Link:\n" + "\n https://dreamwinner.in";
                                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                startActivity(Intent.createChooser(shareIntent, "choose one"));
                            } catch (Exception e) {
                                //e.toString();
                            }
                        });


                    } else {
                        referImageView.setImageResource(R.drawable.red_check);
                        referCount.setText("0/2");
                        usernameDisplay.setVisibility(View.GONE);
                        referButton.setVisibility(View.VISIBLE);
                        referButton.setOnClickListener(v -> {
                            try {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Alpha");
                                String shareMessage = "\nInterested to earn Money? Want to make some cash out of it?? Try out DreamWinner, an eSports Platform. Join Daily PUBG Matches & Get Rewards on Each Kill you Score. Get Huge Prize on Getting Chicken Dinner. Just Download the DreamWinner Android App & Register and Prove your Skills \n\n";
                                shareMessage = shareMessage + "Download Link:\n" + "\n https://dreamwinner.in";
                                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                startActivity(Intent.createChooser(shareIntent, "choose one"));
                            } catch (Exception e) {
                                //e.toString();
                            }
                        });

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

    private void usernameDisplayMethod(String a) {

        usernameDisplay.setVisibility(View.VISIBLE);

        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (a.equals("one")) {

                    String usernameOne = dataSnapshot.child(selfUid).child("uid1").child("username").getValue().toString();
                    NachoTextView et_tag = mView.findViewById(R.id.et_tag);
                    List<String> items = new ArrayList<>();
                    items.add(usernameOne);
                    et_tag.setText(items);
                }
                if (a.equals("two")) {
                    String usernameOne = dataSnapshot.child(selfUid).child("uid1").child("username").getValue().toString();
                    String usernameTwo = dataSnapshot.child(selfUid).child("uid2").child("username").getValue().toString();

                    NachoTextView et_tag = mView.findViewById(R.id.et_tag);
                    List<String> items = new ArrayList<>();
                    items.add(usernameOne);
                    items.add(usernameTwo);
                    et_tag.setText(items);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void TransactionFunction() {
        try {

            mTransactions.child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String countLevelOne = dataSnapshot.child("level1").getValue().toString();

                    if (countLevelOne.equals("2")) {
                        transactionRelative.setBackgroundResource(R.drawable.shape_round_solid_green);
                        transactionRelative.removeAllViews();
                        ImageButton img1 = new ImageButton(getContext());
                        img1.setImageResource(R.drawable.ic_done);
                        img1.setBackgroundColor(Color.TRANSPARENT);
                        img1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                        transactionRelative.addView(img1);


                        transactionImageView.setImageResource(R.drawable.green_chechk);
                        transactionCount.setText("2/2");
                        transactionButton.setVisibility(View.VISIBLE);


                        transactionButton.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), TransactionFilterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("filterWord", "level1");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });


                        checkUpgrade();

                    } else if (countLevelOne.equals("1")) {
                        transactionImageView.setImageResource(R.drawable.red_check);
                        transactionCount.setText("1/2");
                        transactionButton.setVisibility(View.VISIBLE);

                        transactionButton.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), TransactionFilterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("filterWord", "level1");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });


                    } else {
                        transactionImageView.setImageResource(R.drawable.red_check);
                        transactionCount.setText("0/2");
                        transactionButton.setVisibility(View.GONE);
                        transactionButton.setOnClickListener(v -> {
                            TransactionFunction();
                            Toast.makeText(getContext(), "Refresh Success", Toast.LENGTH_SHORT).show();
                        });


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

    private void checkUpgrade() {

        upgradeLayoutBronze.setEnabled(true);
        upgradeLayoutBronze.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        upgradeLayoutBronze.setOnClickListener(v -> upgradeMethod());


    }

    private void upgradeMethod() {


        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String p2 = dataSnapshot.child("Users").child(selfUid).child("Chain").child("parent").child("p2").getValue().toString();
                String mAchievement = dataSnapshot.child("Users").child(selfUid).child("Achievement").getValue().toString();
                String user_bal = dataSnapshot.child("Wallet").child(selfUid).child("balance").getValue().toString();
                String p2Status = dataSnapshot.child("Wallet").child(p2).child("Status").child("status").getValue().toString();
                String userStatusUid = dataSnapshot.child("Wallet").child(p2).child("Status").child("uid").getValue().toString();

                //ProgressBar
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog_new);
                Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                TextView Ptext;
                Ptext = progressDialog.findViewById(R.id.Ptext);
                Ptext.setVisibility(View.VISIBLE);

                if ((mAchievement.equals("Bronze")) && (user_bal.equals("100"))) {
                    progressDialog.show();

                    if (p2Status.equals("free")) {

                        mWallet.child(p2).child("Status").child("status").setValue("busy");
                        mWallet.child(p2).child("Status").child("uid").setValue(selfUid);

                        //reducing money in user wallet
                        int user_bal_Int = Integer.parseInt(user_bal);

                        String user_updated_bal = Integer.toString(user_bal_Int - 100);

                        mWallet.child(selfUid).child("balance").setValue(user_updated_bal);

                        //adding money to parent1
                        String p2_bal = Objects.requireNonNull(dataSnapshot.child("Wallet").child(p2).child("balance").getValue()).toString();
                        int p2_bal_Int = Integer.parseInt(p2_bal);

                        String p2_updated_bal = Integer.toString(p2_bal_Int + 100);

                        mWallet.child(p2).child("balance").setValue(p2_updated_bal);


                        //AutoLoginCode
                        //String p2_email = dataSnapshot.child("Users").child(p2).child("email").getValue().toString();
                        //String p2_password = dataSnapshot.child("Users").child(p2).child("password").getValue().toString();
                        //String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                        //int i = Integer.parseInt(mEnd);

                        //mLogin.child("user" + i).child("uid").setValue(p2);
                        //mLogin.child("user" + i).child("email").setValue(p2_email);
                        //mLogin.child("user" + i).child("password").setValue(p2_password);

                        //String endCount = Integer.toString(i + 1);
                        //mLogin.child("end").setValue(endCount);

                        //MainTransactions
                        String user_userName = Objects.requireNonNull(dataSnapshot.child("Users").child(selfUid).child("username").getValue()).toString();
                        String p2_userName = Objects.requireNonNull(dataSnapshot.child("Users").child(p2).child("username").getValue()).toString();
                        String id = UUID.randomUUID().toString();
                        String childid = "PW" + id.substring(0, 5).toUpperCase();
                        String extraid = id.substring(0, 4).toUpperCase();


                        //Main Transactions
                        if (dataSnapshot.child("Transactions").child(childid).exists()) {


                            Transaction_Class send_transaction_class = new Transaction_Class(
                                    "debited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p2_userName,
                                    childid,
                                    "100",
                                    1,
                                    "level2"
                            );
                            mFirebase.child("Transactions").child(childid + extraid).setValue(send_transaction_class);

                        } else {

                            Transaction_Class send_transaction_class = new Transaction_Class(
                                    "debited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p2_userName,
                                    childid,
                                    "100",
                                    1,
                                    "level2"

                            );
                            mFirebase.child("Transactions").child(childid).setValue(send_transaction_class);


                        }


                        //sendTransaction in user
                        if (dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                                .child("history").exists()) {
                            long countR = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                                    .child("history").getChildrenCount();

                            long sizeR = countR + 1;


                            Transaction_Class send_transaction_class = new Transaction_Class(
                                    "debited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p2_userName,
                                    childid,
                                    "100",
                                    sizeR,
                                    "level2"
                            );
                            mWallet.child(selfUid).child("Transactions").child("history").child(childid).setValue(send_transaction_class);

                        } else {

                            Transaction_Class send_transaction_class = new Transaction_Class(
                                    "debited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p2_userName,
                                    childid,
                                    "100",
                                    1,
                                    "level2"
                            );
                            mWallet.child(selfUid).child("Transactions").child("history").child(childid).setValue(send_transaction_class);


                        }


                        //receivedTransaction in parent1
                        if (dataSnapshot.child("Wallet").child(p2).child("Transactions")
                                .child("history").exists()) {
                            long countP = dataSnapshot.child("Wallet").child(p2).child("Transactions")
                                    .child("history").getChildrenCount();

                            long sizeP = countP + 1;
                            Transaction_Class received_transaction_class = new Transaction_Class(
                                    "credited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p2_userName,
                                    childid,
                                    "100",
                                    sizeP,
                                    "level2"


                            );
                            mWallet.child(p2).child("Transactions").child("history").child(childid).setValue(received_transaction_class);

                            String p2_tran_count = dataSnapshot.child("Wallet").child(p2).child("Transactions").child("count").child("level2").getValue().toString();
                            int p2_tran_count_Int = Integer.parseInt(p2_tran_count);
                            String updated_p2_tran_count = Integer.toString(p2_tran_count_Int + 1);

                            mWallet.child(p2).child("Transactions").child("count").child("level2").setValue(updated_p2_tran_count);


                        } else {
                            Transaction_Class received_transaction_class = new Transaction_Class(
                                    "credited",
                                    datetime,
                                    timeformat,
                                    user_userName,
                                    p2_userName,
                                    childid,
                                    "100",
                                    1,
                                    "level2"

                            );
                            mWallet.child(p2).child("Transactions").child("history").child(childid).setValue(received_transaction_class);

                            String p2_tran_count = dataSnapshot.child("Wallet").child(p2).child("Transactions").child("count").child("level2").getValue().toString();
                            int p2_tran_count_Int = Integer.parseInt(p2_tran_count);
                            String updated_p2_tran_count = Integer.toString(p2_tran_count_Int + 1);

                            mWallet.child(p2).child("Transactions").child("count").child("level2").setValue(updated_p2_tran_count);
                        }


                        //Upgrading level
                        mUsers.child(selfUid).child("level").setValue("2");


                        mUsers.child(selfUid).child("Achievement").setValue("Silver");


                        mWallet.child(p2).child("Status").child("status").setValue("free");
                        mWallet.child(p2).child("Status").child("uid").setValue("null");
                        new Handler().postDelayed(() -> {
                            progressDialog.dismiss();
                            upgradeProgressBar.setVisibility(View.GONE);
                            upgradeTextView.setVisibility(View.VISIBLE);
                            upgradeTextView.setText("U P G R A D E D");

                        }, 1000);


                    } else {
                        Toast.makeText(getContext(), " Parent wallet is busy , try again in 15 seconds", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        upgradeProgressBar.setVisibility(View.GONE);
                        upgradeTextView.setVisibility(View.VISIBLE);


                        /*new CountDownTimer(110000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                //progress_bar_text.setText("Refreshing in: " + millisUntilFinished / 1000);
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                //progress_bar_text.setText("done!");
                            }

                        }.start();

                        new Handler().postDelayed(() -> {


                        }, 0); */

                    }


                } else {
                    progressDialog.dismiss();
                    upgradeProgressBar.setVisibility(View.GONE);
                    upgradeTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Invlaid level or balance", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
