package com.example.alpha.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class MeFragment extends Fragment {
    public FirebaseAuth mAuth, mAuthListener;
    LinearLayout logout, myProfile, myWallet, walletLayout, chainActivity;
    TextView profileName, wallet_bal, recieved, withdrawable, email;
    View mView;
    DatabaseReference mRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_me, container, false);


        myProfile = mView.findViewById(R.id.myprofile);
        wallet_bal = mView.findViewById(R.id.wallet_bal);
        recieved = mView.findViewById(R.id.level);
        withdrawable = mView.findViewById(R.id.withdrawable);

        return mView;

    }

    @Override
    public void onStart() {
        super.onStart();
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").getValue().toString();
                String mEmail = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").getValue().toString();
                String balance = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                String withdraw_balance = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("withdrawable").getValue().toString();
                String value_count = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();


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
