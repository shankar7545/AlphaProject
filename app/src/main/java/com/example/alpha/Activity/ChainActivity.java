package com.example.alpha.Activity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Node;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ChainActivity extends AppCompatActivity {


    DatabaseReference mRef, mUsers, mChain;
    RelativeLayout Chain;
    TextView parent, rightChild, leftChild;
    String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    Node node1, node2, node3, node4, node5, node6, node7, node8, node9, node10, node11, node12, node13, node14, node15, node16;
    private int nodeCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chain);
        mRef = FirebaseDatabase.getInstance().getReference();
        mUsers = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mChain = FirebaseDatabase.getInstance().getReference("Chain");

        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String childCount = dataSnapshot.child("childCount").getValue().toString();

                if (childCount.equals("2")) {
                    mChain.child("uid").child("uid1").setValue("hi");
                    mChain.child("uid").child("uid2").setValue("hi , I am second");

                } else if (childCount.equals("1")) {
                    mChain.child("uid").child("uid1").setValue("hi");

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}


