package com.example.alpha.Levels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Activity.ChainActivity;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BronzeFragment extends Fragment {

    private View mView;
    ProgressDialog bar;

    private TextView referCount, transactionCount, upgradeTextView, statusText, shareNowText;
    private ImageView referImageView, transactionImageView;
    private LinearLayout upgradeLayoutBronze, transactionButton, referButton, usernameDisplay, shareNowLayout;

    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
    private SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss aa");
    private String timeformat = time.format(c.getTime());
    private String datetime = dateformat.format(c.getTime());


    private DatabaseReference mUsers, mFirebase, mTransactions, mWallet, mChain, mReferDB, mStatus;
    private final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    private ProgressDialog progressDialog;
    private ProgressBar upgradeProgressBar;

    private static final String IMAGE_URl =
            "https://www.khelaghorbd.in/imagesTesting/2784130.jpg";

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

        upgradeLayoutBronze = mView.findViewById(R.id.upgradeLayoutBronze);
        usernameDisplay = mView.findViewById(R.id.usernameDisplay);

        referButton = mView.findViewById(R.id.referButton);
        referCount = mView.findViewById(R.id.referCount);
        referImageView = mView.findViewById(R.id.referImageView);
        shareNowText = mView.findViewById(R.id.shareNowText);
        shareNowLayout = mView.findViewById(R.id.shareNowLayout);

        transactionButton = mView.findViewById(R.id.transactionButton);
        transactionCount = mView.findViewById(R.id.transactionCount);
        transactionImageView = mView.findViewById(R.id.transactionImageView);



        upgradeTextView = mView.findViewById(R.id.upgradeTextView);
        upgradeProgressBar = mView.findViewById(R.id.upgradeProgressBar);

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
        TransactionFunction();



        return mView;
    }


    private void ReferralFunction() {

        try {

            bar.show();

            //Picasso.get().load(IMAGE_URl).fit().centerCrop().into(imageRefer);

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

                if (count.equals("one")) {

                    String usernameOne = dataSnapshot.child("uid1").child("username").getValue().toString();
                    NachoTextView et_tag = mView.findViewById(R.id.et_tag);
                    List<String> items = new ArrayList<>();
                    items.add(usernameOne);
                    et_tag.setText(items);
                    bar.dismiss();

                } else if (count.equals("two")) {
                    String usernameOne = dataSnapshot.child("uid1").child("username").getValue().toString();
                    String usernameTwo = dataSnapshot.child("uid2").child("username").getValue().toString();

                    NachoTextView et_tag = mView.findViewById(R.id.et_tag);
                    List<String> items = new ArrayList<>();
                    items.add(usernameOne);
                    items.add(usernameTwo);
                    et_tag.setText(items);
                    bar.dismiss();

                } else if (count.equals("zero")) {
                    String usernameOne = "None";
                    String usernameTwo = "None";

                    NachoTextView et_tag = mView.findViewById(R.id.et_tag);
                    List<String> items = new ArrayList<>();
                    items.add(usernameOne);
                    items.add(usernameTwo);
                    et_tag.setText(items);
                    bar.dismiss();
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

                        ImageButton img1 = new ImageButton(getContext());
                        img1.setImageResource(R.drawable.ic_done);
                        img1.setBackgroundColor(Color.TRANSPARENT);
                        img1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


                        transactionImageView.setImageResource(R.drawable.green_chechk);
                        transactionCount.setText("2/2");
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




                        checkUpgrade();

                    } else if (countLevelOne.equals("1")) {
                        //transactionImageView.setImageResource(R.drawable.red_check);
                        transactionCount.setText("1/2");
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


        mUsers.child(selfUid).child("Achievement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String mAchievement = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                    if (!mAchievement.equals("Bronze")) {

                        runForBronze();

                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void runForBronze() {


        mChain.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chainSnapshot) {

                String parent = Objects.requireNonNull(chainSnapshot.child("parent").child("p2").getValue()).toString();

                mStatus.child(parent).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot statusSnapshot) {

                        //Write Codes for Status

                        //For now

                        mUsers.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot selfUserSnapshot) {

                                String mUsername = Objects.requireNonNull(selfUserSnapshot.child("username").getValue()).toString();
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

    private void upgradeMethod(String parent) {


    }


}
