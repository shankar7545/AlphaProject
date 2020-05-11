package com.example.alpha.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BronzeFragment extends Fragment {

    private View mView;

    Button referButton;
    TextView referCount, transactionCount;
    ImageView referImageView, transactionImageView;
    RelativeLayout referRelative, transactionRelative;
    LinearLayout upgradeLayout, transactionButton;


    private DatabaseReference mUsers, mFirebase, mTransactions;
    private final String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


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

        upgradeLayout = mView.findViewById(R.id.upgradeLayout);

        referRelative = mView.findViewById(R.id.referRelative);
        referButton = mView.findViewById(R.id.referButton);
        referCount = mView.findViewById(R.id.referCount);
        referImageView = mView.findViewById(R.id.referImageView);


        transactionRelative = mView.findViewById(R.id.transactionRelative);
        transactionButton = mView.findViewById(R.id.transactionButton);
        transactionCount = mView.findViewById(R.id.transactionCount);
        transactionImageView = mView.findViewById(R.id.transactionImageView);


        mFirebase = FirebaseDatabase.getInstance().getReference();
        mUsers = FirebaseDatabase.getInstance().getReference("Users");
        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet").child(selfUid).child("Transactions");


        ReferralFunction();
        TransactionFunction();
        checkUpgrade();


        return mView;
    }


    private void ReferralFunction() {

        try {

            mUsers.child(selfUid).addValueEventListener(new ValueEventListener() {
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

                        NachoTextView et_tag = mView.findViewById(R.id.et_tag);
                        List<String> items = new ArrayList<>();
                        items.add("SHANKAR");
                        items.add("DINESH");
                        et_tag.setText(items);

                        referImageView.setImageResource(R.drawable.green_chechk);
                        referCount.setText("2/2");
                        referButton.setVisibility(View.GONE);
                        referButton.setText("Chain Activity");
                        referButton.setOnClickListener(v -> {
                            startActivity(new Intent(getContext(), ChainActivity.class));
                        });


                    } else if (childCount.equals("1")) {

                        referImageView.setImageResource(R.drawable.red_check);
                        referCount.setText("1/2");
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


                    } else {
                        referImageView.setImageResource(R.drawable.red_check);
                        referCount.setText("0/2");
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

    private void TransactionFunction() {
        try {

            mTransactions.child("count").addValueEventListener(new ValueEventListener() {
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
                        transactionButton.setVisibility(View.VISIBLE);
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

        upgradeLayout.setEnabled(true);
        upgradeLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        upgradeLayout.setOnClickListener(v -> upgradeMethod());


    }

    private void upgradeMethod() {

        Toast.makeText(getContext(), "Upgrade Clicked", Toast.LENGTH_SHORT).show();

    }

}
