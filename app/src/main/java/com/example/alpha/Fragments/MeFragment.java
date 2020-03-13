package com.example.alpha.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alpha.Activity.ChainActivity;
import com.example.alpha.Activity.LoginActivity;
import com.example.alpha.Activity.MainActivity;
import com.example.alpha.Activity.MyProfile;
import com.example.alpha.R;
import com.example.alpha.Wallet.walletActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


public class MeFragment extends Fragment {
    public FirebaseAuth mAuth, mAuthListener;
    LinearLayout logout, myProfile, myWallet, walletLayout, chainActivity;
    TextView profileName, wallet_bal, recieved, withdrawable;
    View mView;
    DatabaseReference mRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_me, container, false);


        profileName = (TextView) mView.findViewById(R.id.profilename);
        wallet_bal = (TextView) mView.findViewById(R.id.wallet_bal);
        recieved = (TextView) mView.findViewById(R.id.level);
        withdrawable = (TextView) mView.findViewById(R.id.withdrawable);

        myWallet = (LinearLayout) mView.findViewById(R.id.mywallet);
        walletLayout = (LinearLayout) mView.findViewById(R.id.walletLayout);
        chainActivity = (LinearLayout) mView.findViewById(R.id.chain);


        logout = (LinearLayout) mView.findViewById(R.id.logout);
        myProfile = (LinearLayout) mView.findViewById(R.id.myprofile);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                ActivityCompat.finishAffinity(getActivity());

            }

        });

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyProfile.class);
                startActivity(intent);

            }
        });

        myWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), walletActivity.class);
                startActivity(intent);

            }
        });
        walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), walletActivity.class);
                startActivity(intent);
            }
        });
        chainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChainActivity.class);
                startActivity(intent);
            }
        });
        return mView;

    }

    @Override
    public void onStart() {
        super.onStart();
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue().toString();
                String balance = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                String withdraw_balance = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("withdrawable").getValue().toString();
                String value_count = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                profileName.setText(name);
                wallet_bal.setText(balance);
                recieved.setText(value_count);
                withdrawable.setText(withdraw_balance);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
