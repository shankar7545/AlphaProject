package com.example.alpha.Levels;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Activity.ChainActivity;
import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.example.alpha.Wallet.TransactionFilterActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.NachoTextView;
import com.squareup.picasso.Picasso;

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
    ProgressDialog bar;


    ImageView imageRefer;

    private ImageView referImageView, bronzeTransactionImageView, silverTransactionImageView, goldTransactionImageView, diamondTransactionImageView;
    private LinearLayout bronzeUpgradeLayout, bronzeUpgradeButton, silverUpgradeLayout, silverUpgradeButton, goldUpgradeLayout, goldUpgradeButton, diamondUpgradeLayout, diamondUpgradeButton;
    private LinearLayout upgradeLayoutBronze, transactionButton, referButton, usernameDisplay, shareNowLayout;
    private TextView bronzeTransactionCount, silverTransactionCount, goldTransactionCount, diamondTransactionCount;
    private TextView referCount, upgradeTextView, statusText, shareNowText;

    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
    private SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss aa");
    private String timeformat = time.format(c.getTime());
    private String datetime = dateformat.format(c.getTime());


    private DatabaseReference mUsers, mFirebase, mTransactions, mWallet, mChain, mReferDB, mStatus;
    private final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    private ProgressDialog progressDialog;
    private ProgressBar upgradeProgressBar;
    private View parent_view;

    private static final String IMAGE_URl =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQPnSOIbs5JJGREsZN55BuTzOjaMF4Mw-8fhw&usqp=CAU";

    public BronzeFragment() {
        // Required empty public constructor
    }

    static Fragment newInstance() {
        Bundle args = new Bundle();

        BronzeFragment fragment = new BronzeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_bronze, container, false);

        parent_view = mView.findViewById(android.R.id.content);

        imageRefer = mView.findViewById(R.id.imageRefer);

        Picasso.get().load(IMAGE_URl).placeholder(R.drawable.placeholder_image).into(imageRefer);



        upgradeLayoutBronze = mView.findViewById(R.id.upgradeLayoutBronze);
        usernameDisplay = mView.findViewById(R.id.usernameDisplay);

        referButton = mView.findViewById(R.id.referButton);
        referCount = mView.findViewById(R.id.referCount);
        referImageView = mView.findViewById(R.id.referImageView);
        shareNowText = mView.findViewById(R.id.shareNowText);
        shareNowLayout = mView.findViewById(R.id.shareNowLayout);

        transactionButton = mView.findViewById(R.id.transactionButton);


        upgradeTextView = mView.findViewById(R.id.upgradeTextView);
        upgradeProgressBar = mView.findViewById(R.id.upgradeProgressBar);


        bronzeUpgradeLayout = mView.findViewById(R.id.bronzeUpgradeLayout);
        bronzeUpgradeButton = mView.findViewById(R.id.bronzeUpgradeButton);
        silverUpgradeLayout = mView.findViewById(R.id.silverUpgradeLayout);
        silverUpgradeButton = mView.findViewById(R.id.silverUpgradeButton);
        goldUpgradeLayout = mView.findViewById(R.id.goldUpgradeLayout);
        goldUpgradeButton = mView.findViewById(R.id.goldUpgradeButton);
        diamondUpgradeLayout = mView.findViewById(R.id.diamondUpgradeLayout);
        diamondUpgradeButton = mView.findViewById(R.id.diamondUpgradeButton);


        bronzeTransactionCount = mView.findViewById(R.id.bronzeTransactionCount);
        silverTransactionCount = mView.findViewById(R.id.silverTransactionCount);
        goldTransactionCount = mView.findViewById(R.id.goldTransactionCount);
        diamondTransactionCount = mView.findViewById(R.id.diamondTransactionCount);

        bronzeTransactionImageView = mView.findViewById(R.id.bronzeTransactionImageView);
        silverTransactionImageView = mView.findViewById(R.id.silverTransactionImageView);
        goldTransactionImageView = mView.findViewById(R.id.goldTransactionImageView);
        diamondTransactionImageView = mView.findViewById(R.id.diamondTransactionImageView);

        mFirebase = FirebaseDatabase.getInstance().getReference();
        mUsers = FirebaseDatabase.getInstance().getReference("Users");
        mReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
        mChain = FirebaseDatabase.getInstance().getReference("Chain");
        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");
        mStatus = FirebaseDatabase.getInstance().getReference("Status");
        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet").child(selfUid).child("Transactions");


        bar = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        bar.setCancelable(false);
        bar.setIndeterminate(true);
        bar.setMessage("Loading ...");
        bar.setCanceledOnTouchOutside(false);

        ReferralFunction();
        bronzeTransactionFunction();
        silverTransactionFunction();
        goldTransactionFunction();
        diamondTransactionFunction();

        checkLevel();

        return mView;
    }


    private void ReferralFunction() {

        try {

            bar.show();


            mUsers.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String mUserName = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();


                    mReferDB.child(mUserName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot referSnapshot) {

                            String childCount = Objects.requireNonNull(referSnapshot.child("childCount").getValue()).toString();
                            if (childCount.equals("2")) {

                                ImageButton img = new ImageButton(getContext());
                                img.setImageResource(R.drawable.ic_done);
                                img.setBackgroundColor(Color.TRANSPARENT);
                                img.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

                                String count = "two";
                                usernameDisplayMethod(count);

                                referImageView.setImageResource(R.drawable.green_chechk);
                                referCount.setText("2/2");
                                referButton.setVisibility(View.VISIBLE);
                                shareNowText.setText("Open Chain");
                                shareNowLayout.setBackgroundColor(getResources().getColor(R.color.blue_700));
                                referButton.setOnClickListener(v -> startActivity(new Intent(getContext(), ChainActivity.class)));


                            } else if (childCount.equals("1")) {

                                //referImageView.setImageResource(R.drawable.red_check);
                                referCount.setText("1/2");
                                referButton.setVisibility(View.VISIBLE);
                                String count = "one";
                                usernameDisplayMethod(count);
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
                                //referImageView.setImageResource(R.drawable.red_check);
                                referCount.setText("0/2");
                                //usernameDisplay.setVisibility(View.GONE);
                                referButton.setVisibility(View.VISIBLE);
                                String count = "zero";
                                usernameDisplayMethod(count);
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
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void usernameDisplayMethod(String count) {

        usernameDisplay.setVisibility(View.VISIBLE);

        mChain.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                switch (count) {
                    case "one": {

                        String usernameOne = dataSnapshot.child("uid1").child("username").getValue().toString();
                        NachoTextView et_tag = mView.findViewById(R.id.et_tag);
                        List<String> items = new ArrayList<>();
                        items.add(usernameOne);
                        et_tag.setText(items);
                        bar.dismiss();

                        break;
                    }
                    case "two": {
                        String usernameOne = dataSnapshot.child("uid1").child("username").getValue().toString();
                        String usernameTwo = dataSnapshot.child("uid2").child("username").getValue().toString();

                        NachoTextView et_tag = mView.findViewById(R.id.et_tag);
                        List<String> items = new ArrayList<>();
                        items.add(usernameOne);
                        items.add(usernameTwo);
                        et_tag.setText(items);
                        bar.dismiss();

                        break;
                    }
                    case "zero": {
                        String usernameOne = "None";
                        String usernameTwo = "None";

                        NachoTextView et_tag = mView.findViewById(R.id.et_tag);
                        List<String> items = new ArrayList<>();
                        items.add(usernameOne);
                        items.add(usernameTwo);
                        et_tag.setText(items);
                        bar.dismiss();
                        break;
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void bronzeTransactionFunction() {
        try {

            mTransactions.child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String countLevelOne = dataSnapshot.child("level1").getValue().toString();

                    if (countLevelOne.equals("2")) {

                        ImageButton img1 = new ImageButton(getContext());
                        img1.setImageResource(R.drawable.ic_done);
                        img1.setBackgroundColor(Color.TRANSPARENT);
                        img1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


                        bronzeTransactionImageView.setImageResource(R.drawable.green_chechk);
                        bronzeTransactionCount.setText("2/2");
                        transactionButton.setVisibility(View.VISIBLE);


                        transactionButton.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), TransactionFilterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("filterWord", "bronze");
                            bundle.putString("filterType", "transactionLevel");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });



                        ImageButton img2 = new ImageButton(getContext());
                        img2.setImageResource(R.drawable.ic_done);
                        img2.setBackgroundColor(Color.TRANSPARENT);
                        img2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


                        // checkUpgrade();
                        bronzeUpgradeButton.setBackgroundColor(getResources().getColor(R.color.amber_800));
                        bronzeUpgradeButton.setOnClickListener(v -> runForUpgrade("Silver", "100", "p2"));

                    } else if (countLevelOne.equals("1")) {
                        //transactionImageView.setImageResource(R.drawable.red_check);
                        bronzeTransactionCount.setText("1/2");
                        transactionButton.setVisibility(View.VISIBLE);

                        transactionButton.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), TransactionFilterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("filterWord", "bronze");
                            bundle.putString("filterType", "transactionLevel");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });


                    } else {
                        //transactionImageView.setImageResource(R.drawable.red_check);
                        bronzeTransactionCount.setText("0/2");
                        transactionButton.setVisibility(View.GONE);
                        transactionButton.setOnClickListener(v -> {
                            bronzeTransactionFunction();
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

    private void silverTransactionFunction() {
        try {

            mTransactions.child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String countLevelTwo = dataSnapshot.child("level2").getValue().toString();

                    int count = Integer.parseInt(countLevelTwo);
                    if (count == 4) {

                        ImageButton img1 = new ImageButton(getContext());
                        img1.setImageResource(R.drawable.ic_done);
                        img1.setBackgroundColor(Color.TRANSPARENT);
                        img1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


                        silverTransactionImageView.setImageResource(R.drawable.green_chechk);
                        silverTransactionCount.setText("4/4");
                        transactionButton.setVisibility(View.VISIBLE);


                        transactionButton.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), TransactionFilterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("filterWord", "Silver");
                            bundle.putString("filterType", "transactionLevel");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });


                        ImageButton img2 = new ImageButton(getContext());
                        img2.setImageResource(R.drawable.ic_done);
                        img2.setBackgroundColor(Color.TRANSPARENT);
                        img2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


                        // checkUpgrade();
                        silverUpgradeButton.setBackgroundColor(getResources().getColor(R.color.green_500));
                        silverUpgradeButton.setOnClickListener(v -> runForUpgrade("Gold", "400", "p3"));

                    } else if (count > 0 && count < 4) {
                        //transactionImageView.setImageResource(R.drawable.red_check);
                        silverTransactionCount.setText(count + "/4");
                        transactionButton.setVisibility(View.VISIBLE);

                        transactionButton.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), TransactionFilterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("filterWord", "Silver");
                            bundle.putString("filterType", "transactionLevel");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });


                    } else {
                        //transactionImageView.setImageResource(R.drawable.red_check);
                        silverTransactionCount.setText("0/4");
                        transactionButton.setVisibility(View.GONE);
                        transactionButton.setOnClickListener(v -> {
                            bronzeTransactionFunction();
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

    private void goldTransactionFunction() {
        try {

            mTransactions.child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String countLevelThree = dataSnapshot.child("level3").getValue().toString();
                    int count = Integer.parseInt(countLevelThree);

                    if (count == 8) {

                        ImageButton img1 = new ImageButton(getContext());
                        img1.setImageResource(R.drawable.ic_done);
                        img1.setBackgroundColor(Color.TRANSPARENT);
                        img1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


                        goldTransactionImageView.setImageResource(R.drawable.green_chechk);
                        goldTransactionCount.setText("8/8");
                        transactionButton.setVisibility(View.VISIBLE);


                        transactionButton.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), TransactionFilterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("filterWord", "Gold");
                            bundle.putString("filterType", "transactionLevel");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });


                        ImageButton img2 = new ImageButton(getContext());
                        img2.setImageResource(R.drawable.ic_done);
                        img2.setBackgroundColor(Color.TRANSPARENT);
                        img2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


                        // checkUpgrade();
                        goldUpgradeButton.setBackgroundColor(getResources().getColor(R.color.red_300));
                        goldUpgradeButton.setOnClickListener(v -> runForUpgrade("Diamond", "1000", "p4"));

                    } else if (count > 0 && count < 8) {
                        //transactionImageView.setImageResource(R.drawable.red_check);
                        goldTransactionCount.setText(count + "/8");
                        transactionButton.setVisibility(View.VISIBLE);

                        transactionButton.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), TransactionFilterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("filterWord", "Gold");
                            bundle.putString("filterType", "transactionLevel");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });


                    } else {
                        //transactionImageView.setImageResource(R.drawable.red_check);
                        goldTransactionCount.setText("0/8");
                        transactionButton.setVisibility(View.GONE);
                        transactionButton.setOnClickListener(v -> {
                            bronzeTransactionFunction();
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

    private void diamondTransactionFunction() {
        try {

            mTransactions.child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String countLevelFour = dataSnapshot.child("level4").getValue().toString();
                    int count = Integer.parseInt(countLevelFour);

                    if (count == 16) {

                        ImageButton img1 = new ImageButton(getContext());
                        img1.setImageResource(R.drawable.ic_done);
                        img1.setBackgroundColor(Color.TRANSPARENT);
                        img1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


                        diamondTransactionImageView.setImageResource(R.drawable.green_chechk);
                        diamondTransactionCount.setText("16/16");
                        transactionButton.setVisibility(View.VISIBLE);


                        transactionButton.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), TransactionFilterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("filterWord", "Diamond");
                            bundle.putString("filterType", "transactionLevel");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });


                        ImageButton img2 = new ImageButton(getContext());
                        img2.setImageResource(R.drawable.ic_done);
                        img2.setBackgroundColor(Color.TRANSPARENT);
                        img2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


                        // checkUpgrade();
                        diamondUpgradeButton.setBackgroundColor(getResources().getColor(R.color.indigo_500));
                        diamondUpgradeButton.setOnClickListener(v -> runForUpgrade("Diamond", "40000", "p5"));


                    } else if (count > 0 && count < 16) {
                        //transactionImageView.setImageResource(R.drawable.red_check);
                        diamondTransactionCount.setText(count + "/16");
                        transactionButton.setVisibility(View.VISIBLE);

                        transactionButton.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), TransactionFilterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("filterWord", "Diamond");
                            bundle.putString("filterType", "transactionLevel");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });


                    } else {
                        //transactionImageView.setImageResource(R.drawable.red_check);
                        diamondTransactionCount.setText(count + "/16");
                        transactionButton.setVisibility(View.GONE);
                        transactionButton.setOnClickListener(v -> {
                            bronzeTransactionFunction();
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


    private void checkLevel() {

        bar.show();
        mUsers.child(selfUid).child("Achievement").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String currentLevel = Objects.requireNonNull(dataSnapshot.child("currentLevel").getValue()).toString();

                    switch (currentLevel) {
                        case "Bronze":
                            bronzeUpgradeLayout.setVisibility(View.VISIBLE);
                            bronzeUpgradeButton.setBackgroundColor(getResources().getColor(R.color.grey_40));
                            //bronzeUpgradeButton.setOnClickListener(v -> runForUpgrade("Silver","100","p2"));
                            break;
                        case "Silver":
                            silverUpgradeLayout.setVisibility(View.VISIBLE);
                            silverUpgradeButton.setBackgroundColor(getResources().getColor(R.color.grey_40));
                            //silverUpgradeButton.setOnClickListener(v -> runForUpgrade("Gold","400","p3"));
                            break;
                        case "Gold":
                            goldUpgradeLayout.setVisibility(View.VISIBLE);
                            goldUpgradeButton.setBackgroundColor(getResources().getColor(R.color.grey_40));
                            //goldUpgradeButton.setOnClickListener(v -> runForUpgrade("Diamond","1000","p4"));

                            break;
                        case "Diamond":
                            diamondUpgradeLayout.setVisibility(View.VISIBLE);
                            diamondUpgradeButton.setBackgroundColor(getResources().getColor(R.color.grey_40));
                            //diamondUpgradeButton.setOnClickListener(v -> runForUpgrade("Diamond","40000","p5"));
                            break;


                    }


                    String Bronze = Objects.requireNonNull(dataSnapshot.child("Bronze").getValue()).toString();
                    String Silver = Objects.requireNonNull(dataSnapshot.child("Silver").getValue()).toString();
                    String Gold = Objects.requireNonNull(dataSnapshot.child("Gold").getValue()).toString();
                    String Diamond = Objects.requireNonNull(dataSnapshot.child("Diamond").getValue()).toString();

                    if (Bronze.equals("true")) {
                        bronzeUpgradeLayout.setVisibility(View.GONE);
                        mView.findViewById(R.id.bronzeUpgradedText).setVisibility(View.VISIBLE);

                    }

                    if (Silver.equals("true")) {
                        silverUpgradeLayout.setVisibility(View.GONE);
                        mView.findViewById(R.id.silverUpgradedText).setVisibility(View.VISIBLE);

                    }
                    if (Gold.equals("true")) {
                        goldUpgradeLayout.setVisibility(View.GONE);
                        mView.findViewById(R.id.goldUpgradedText).setVisibility(View.VISIBLE);

                    }
                    if (Diamond.equals("true")) {
                        diamondUpgradeLayout.setVisibility(View.GONE);
                        mView.findViewById(R.id.diamondUpgradedText).setVisibility(View.VISIBLE);

                    }

                } else {
                    Toast.makeText(getContext(), "Achievement not exists", Toast.LENGTH_SHORT).show();
                    bar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkTimer() {
        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {

                bar.setMessage("Parent is busy" + "\n" + "Retrying in : " + millisUntilFinished / 1000);
                bar.show();

            }

            public void onFinish() {

                bar.setMessage("done!");
                bar.dismiss();
            }
        }.start();
    }

    private void runForUpgrade(String Level, String Amount, String parentNum) {


        mChain.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chainSnapshot) {

                String parentUID = Objects.requireNonNull(chainSnapshot.child("parent").child(parentNum).getValue()).toString();

                mUsers.child(parentUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot parentSnapshot) {

                        String parentUsername = Objects.requireNonNull(parentSnapshot.child("username").getValue()).toString();


                        mStatus.child(parentUID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot statusSnapshot) {
                                bar.setMessage("Checking parent status ...");
                                bar.show();
                                //Write Codes for Status

                                //For now

                                String Status = Objects.requireNonNull(statusSnapshot.child("status").getValue()).toString();
                                String usingByUID = Objects.requireNonNull(statusSnapshot.child("usingByUID").getValue()).toString();

                                if ((Status.equals("free"))) {

                                    mFirebase.child("Status").child(parentUID).child("status").setValue("busy");
                                    mFirebase.child("Status").child(parentUID).child("usingByUID").setValue(selfUid);
                                    bar.setMessage("Parent status locked");

                                    new CountDownTimer(10000, 1000) {

                                        public void onTick(long millisUntilFinished) {

                                            bar.setMessage("Rechecking parent status in  : " + millisUntilFinished / 1000 + "\n" + "\n" + "Just Making sure no one is accessing " + parentUsername + "'s Data ");
                                            bar.show();

                                        }

                                        public void onFinish() {

                                            mStatus.child(parentUID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot statusSnapshotTwo) {
                                                    String Status = Objects.requireNonNull(statusSnapshotTwo.child("status").getValue()).toString();
                                                    String usingByUID = Objects.requireNonNull(statusSnapshotTwo.child("usingByUID").getValue()).toString();
                                                    if (Status.equals("busy") && usingByUID.equals(selfUid)) {

                                                        bar.setMessage("Parent status locked");
                                                        bar.show();
                                                        mUsers.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot selfUserSnapshot) {

                                                                String mUsername = Objects.requireNonNull(selfUserSnapshot.child("username").getValue()).toString();
                                                                String currentLevel = Objects.requireNonNull(selfUserSnapshot.child("Achievement").child("currentLevel").getValue()).toString();

                                                                //Toast.makeText(getContext(), "Success till upgradeMethod", Toast.LENGTH_SHORT).show();
                                                                upgradeMethod(mUsername, parentUID, parentUsername, Amount, Level, currentLevel);
                                                                //bar.dismiss();

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                    } else {
                                                        Toast.makeText(getContext(), "Status :" + Status + "  UID : " + usingByUID, Toast.LENGTH_SHORT).show();
                                                        Toast.makeText(getContext(), "Try again after few minutes", Toast.LENGTH_LONG).show();

                                                        bar.dismiss();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });


                                        }
                                    }.start();


                                } else if (Status.equals("busy") && usingByUID.equals(selfUid)) {

                                    mStatus.child(parentUID).child("status").setValue("free");
                                    mStatus.child(parentUID).child("usingByUID").setValue("null");
                                    Toast.makeText(getContext(), "Reset success", Toast.LENGTH_SHORT).show();
                                    runForUpgrade(Level, Amount, parentNum);
                                } else {
                                    bar.show();

                                    new CountDownTimer(30000, 1000) {

                                        public void onTick(long millisUntilFinished) {


                                            if (bar.isShowing()) {
                                                bar.setMessage("Parent is busy" + "\n" + "Retrying in : " + millisUntilFinished / 1000);
                                                bar.setCancelable(true);


                                            } else {
                                                cancel();
                                                Toast.makeText(getContext(), "Retrying Cancelled", Toast.LENGTH_SHORT).show();

                                            }

                                        }

                                        public void onFinish() {
                                            bar.setMessage("Rechecking ..");

                                            runForUpgrade(Level, Amount, parentNum);

                                        }
                                    }.start();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void upgradeMethod(String mUsername, String parentUID, String parentUsername, String upgradeAmount, String transactionLevel, String currentLevel) {


        mWallet.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userWalletSnapshot) {

                bar.setMessage("Starting Upgrade to " + transactionLevel);
                bar.show();
                //UserBalance
                String userBalance = Objects.requireNonNull(userWalletSnapshot.child("Balance").child(currentLevel).getValue()).toString();
                int user_bal_Int = Integer.parseInt(userBalance);

                int upgradeAmountInt = Integer.parseInt(upgradeAmount);


                if (user_bal_Int >= 100) {
                    String user_updated_bal = Integer.toString(user_bal_Int - upgradeAmountInt);
                    mWallet.child(selfUid).child("Balance").child(currentLevel).setValue(user_updated_bal);

                } else {
                    Toast.makeText(getContext(), "Reduction Failed", Toast.LENGTH_SHORT).show();
                }


                mWallet.child(parentUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot parentWalletSnapshot) {


                        bar.setMessage("Transferring money to " + parentUsername);

                        //ParentBalance
                        String parentBalance = Objects.requireNonNull(parentWalletSnapshot.child("Balance").child("silver").getValue()).toString();
                        int parent_bal_Int = Integer.parseInt(parentBalance);

                        String parent_updated_bal = Integer.toString(parent_bal_Int + upgradeAmountInt);

                        mWallet.child(parentUID).child("Balance").child(transactionLevel).setValue(parent_updated_bal);


                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aa");
                        String date = dateFormat.format(c.getTime());
                        String time = timeFormat.format(c.getTime());


                        String id = UUID.randomUUID().toString();
                        String idOne = "PV" + id.substring(0, 6).toUpperCase();
                        String idTwo = "EX" + id.substring(0, 6).toUpperCase();


                        bar.setMessage("Processing transactions .. ");

                        //MainTransactions
                        Transaction_Class main_transaction_class = new Transaction_Class(
                                "debited",
                                date,
                                time,
                                mUsername,
                                parentUsername,
                                idOne + idTwo,
                                upgradeAmount,
                                1,
                                transactionLevel,
                                ""
                        );
                        mFirebase.child("Transactions").child(idOne + idTwo).setValue(main_transaction_class);


                        //sendTransaction in user
                        long countR = userWalletSnapshot.child("Transactions")
                                .child("history").getChildrenCount();

                        long sizeR = countR + 1;


                        Transaction_Class send_transaction_class = new Transaction_Class(
                                "debited",
                                date,
                                time,
                                mUsername,
                                parentUsername,
                                idOne + idTwo,
                                upgradeAmount,
                                sizeR,
                                "For Upgrading to " + transactionLevel,
                                ""
                        );
                        mWallet.child(selfUid).child("Transactions").child("history").child(idOne + idTwo).setValue(send_transaction_class);


                        //receivedTransaction in parent

                        long countP = parentWalletSnapshot.child("Transactions")
                                .child("history").getChildrenCount();

                        long sizeP = countP + 1;
                        Transaction_Class received_transaction_class = new Transaction_Class(
                                "credited",
                                date,
                                time,
                                mUsername,
                                parentUsername,
                                idOne + idTwo,
                                upgradeAmount,
                                sizeP,
                                transactionLevel,
                                ""


                        );
                        mWallet.child(parentUID).child("Transactions").child("history").child(idOne + idTwo).setValue(received_transaction_class);

                        //Logs

                        mUsers.child(selfUid).child("Logs").child(transactionLevel + "Upgrade").child("date").setValue(date);
                        mUsers.child(selfUid).child("Logs").child(transactionLevel + "Upgrade").child("time").setValue(time);

                        //Parent Transaction Count
                        String p1_tran_count = parentWalletSnapshot.child("Transactions").child("count").child(transactionLevel).getValue().toString();
                        int p1_tran_count_Int = Integer.parseInt(p1_tran_count);
                        String updated_p1_tran_count = Integer.toString(p1_tran_count_Int + 1);
                        mWallet.child(parentUID).child("Transactions").child("count").child(transactionLevel).setValue(updated_p1_tran_count);


                        //Upgrading level

                        mUsers.child(selfUid).child("Achievement").child("currentLevel").setValue(transactionLevel);
                        mUsers.child(selfUid).child("Achievement").child(currentLevel).setValue("true");
                        bar.setMessage("Finishing .. ");
                        mStatus.child(parentUID).child("status").setValue("free");
                        mStatus.child(parentUID).child("usingByUID").setValue("null");

                        new Handler().postDelayed(() ->
                        {
                            showSuccessDialog(transactionLevel);
                            bar.dismiss();
                        }, 2000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showSuccessDialog(String transactionLevel) {

        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_achievement_congrat);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        TextView upgradeText = dialog.findViewById(R.id.upgradeText);
        upgradeText.setText("You just got Upgraded to " + transactionLevel);
        dialog.findViewById(R.id.bt_action).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void Snackbar(String text) {
        Snackbar snackbar = Snackbar.make(parent_view, Objects.requireNonNull(text), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.green_700))
                .setAction("Okay", view -> {
                });
        snackbar.show();

    }
}
