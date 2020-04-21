package com.example.alpha.Activity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Node;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class ChainActivity extends AppCompatActivity {


    DatabaseReference mRef;
    RelativeLayout Chain;
    TextView parent, rightChild, leftChild;
    String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    Node node1, node2, node3, node4, node5, node6, node7, node8, node9, node10, node11, node12, node13, node14, node15, node16;
    private int nodeCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chain);
        mRef = FirebaseDatabase.getInstance().getReference("TreeView").child("1boZEA0VUvPW4C05cx4N7ZIQZz");

    }

}


