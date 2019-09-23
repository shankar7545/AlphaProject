package com.example.alpha;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class ChainActivity extends AppCompatActivity {

    DatabaseReference mRef;
    RelativeLayout Chain;
    TextView parent , rightChild,leftChild;
    String selfUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chain);

        parent = findViewById(R.id.parentText);
        rightChild = findViewById(R.id.childRight);
        leftChild = findViewById(R.id.childLeft);


        mRef = FirebaseDatabase.getInstance().getReference();

        leftChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftChain();
            }
        });
        rightChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightChain();
            }
        });
        chain();


    }

    public void chain(){

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //details
                String parentName = dataSnapshot.child("Users").child(selfUid).child("name").getValue().toString();
                String rightChildUid = dataSnapshot.child("Users").child(selfUid).child("child").child("rchild").getValue().toString();
                String leftChildUid = dataSnapshot.child("Users").child(selfUid).child("child").child("lchild").getValue().toString();

                String rightChildName = dataSnapshot.child("Users").child(rightChildUid).child("username").getValue().toString();
                String leftChildName = dataSnapshot.child("Users").child(leftChildUid).child("username").getValue().toString();

                parent.setText(parentName);
                rightChild.setText(rightChildName);
                leftChild.setText(leftChildName);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void leftChain() {
        final String parentName = leftChild.getText().toString();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //details
                String parentUid = dataSnapshot.child("ReferDB").child(parentName).child("uid").getValue().toString();
                String childCount = dataSnapshot.child("Users").child(parentUid).child("child").child("count").getValue().toString();

                if(childCount.equals("1")){
                    String rightChildUid = dataSnapshot.child("Users").child(parentUid).child("child").child("rchild").getValue().toString();
                    String leftChildUid = dataSnapshot.child("Users").child(parentUid).child("child").child("lchild").getValue().toString();
                    String rightChildName = dataSnapshot.child("Users").child(rightChildUid).child("username").getValue().toString();
                    String leftChildName = dataSnapshot.child("Users").child(leftChildUid).child("username").getValue().toString();

                    parent.setText(parentName);
                    rightChild.setText("?");
                    rightChild.setClickable(false);
                    leftChild.setText(leftChildName);


                }
                else if(childCount.equals("2"))
                {
                    String rightChildUid = dataSnapshot.child("Users").child(parentUid).child("child").child("rchild").getValue().toString();
                    String leftChildUid = dataSnapshot.child("Users").child(parentUid).child("child").child("lchild").getValue().toString();

                    String rightChildName = dataSnapshot.child("Users").child(rightChildUid).child("username").getValue().toString();
                    String leftChildName = dataSnapshot.child("Users").child(leftChildUid).child("username").getValue().toString();

                    parent.setText(parentName);
                    rightChild.setText(rightChildName);
                    leftChild.setText(leftChildName);
                }
                else
                {
                    parent.setText(parentName);
                    rightChild.setText("?");
                    rightChild.setClickable(false);
                    leftChild.setText("?");
                    leftChild.setClickable(false);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void rightChain()
    {
        final String parentName = rightChild.getText().toString();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //details
                String parentUid = dataSnapshot.child("ReferDB").child(parentName).child("uid").getValue().toString();
                String rightChildUid = dataSnapshot.child("Users").child(parentUid).child("child").child("rchild").getValue().toString();
                String leftChildUid = dataSnapshot.child("Users").child(parentUid).child("child").child("lchild").getValue().toString();
                String childCount = dataSnapshot.child("Users").child(parentUid).child("child").child("count").getValue().toString();

                if(childCount.equals("1")){
                    String rightChildName = dataSnapshot.child("Users").child(rightChildUid).child("username").getValue().toString();
                    String leftChildName = dataSnapshot.child("Users").child(leftChildUid).child("username").getValue().toString();

                    parent.setText(parentName);
                    rightChild.setText("?");
                    rightChild.setClickable(false);
                    leftChild.setText(leftChildName);


                }
                else if(childCount.equals("2"))
                {
                    String rightChildName = dataSnapshot.child("Users").child(rightChildUid).child("username").getValue().toString();
                    String leftChildName = dataSnapshot.child("Users").child(leftChildUid).child("username").getValue().toString();

                    parent.setText(parentName);
                    rightChild.setText(rightChildName);
                    leftChild.setText(leftChildName);
                }
                else
                {
                    parent.setText(parentName);
                    rightChild.setText("?");
                    rightChild.setClickable(false);
                    leftChild.setText("?");
                    leftChild.setClickable(false);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
