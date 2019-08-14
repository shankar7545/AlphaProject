package com.example.alpha;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MeFragment extends Fragment {
    LinearLayout logout ,myProfile;
    TextView profileName,wallet_bal,recieved , withdrawable;
    View mView;
    DatabaseReference mRef;
    public FirebaseAuth mAuth ,mAuthListener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference noteRef = db.document("Users/NrZMhC49o5OpplMjzinO7Ik5pFF3");



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_me, container, false);


        profileName =(TextView)mView.findViewById(R.id.profilename);
        wallet_bal =(TextView)mView.findViewById(R.id.wallet_bal);
        recieved =(TextView)mView.findViewById(R.id.level);
        withdrawable =(TextView)mView.findViewById(R.id.withdrawable);

        logout=(LinearLayout) mView.findViewById(R.id.logout);
        myProfile=(LinearLayout) mView.findViewById(R.id.myprofile);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

        });

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MyProfile.class);
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
                String value_count = dataSnapshot.child("Level").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

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
