package com.example.alpha;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import de.blox.graphview.BaseGraphAdapter;
import de.blox.graphview.Graph;
import de.blox.graphview.GraphView;
import de.blox.graphview.Node;
import de.blox.graphview.ViewHolder;
import de.blox.graphview.tree.BuchheimWalkerAlgorithm;
import de.blox.graphview.tree.BuchheimWalkerConfiguration;


public class ChainActivity extends AppCompatActivity {

    DatabaseReference mRef;
    RelativeLayout Chain;
    TextView parent , rightChild,leftChild;
    String selfUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private int nodeCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chain);

        GraphView graphView = findViewById(R.id.graph);

        // example tree
        final Graph graph = new Graph();
        final Node node1 = new Node(getNodeText());
        final Node node2 = new Node(getNodeText());
        final Node node3 = new Node(getNodeText());
        final Node node4 = new Node(getNodeText());
        final Node node5 = new Node(getNodeText());
        final Node node6 = new Node(getNodeText());
        final Node node8 = new Node(getNodeText());
        final Node node7 = new Node(getNodeText());
        final Node node9 = new Node(getNodeText());
        final Node node10 = new Node(getNodeText());
        final Node node11 = new Node(getNodeText());
        final Node node12 = new Node(getNodeText());
        final Node node13=new Node(getNodeText());
        final Node node14=new Node(getNodeText());
        final Node node15=new Node(getNodeText());
        final Node node16 =new Node(getNodeText());

        graph.addEdge(node1, node2);
        graph.addEdge(node1, node4);
        graph.addEdge(node2, node5);
        graph.addEdge(node2, node6);
        graph.addEdge(node6, node7);
        graph.addEdge(node6, node8);
        graph.addEdge(node4, node9);
        graph.addEdge(node4, node11);
        graph.addEdge(node5,node10);
        graph.addEdge(node5,node12);
        graph.addEdge(node11,node13);
        graph.addEdge(node11,node14);
        graph.addEdge(node9,node15);
        graph.addEdge(node9,node16);

        // you can set the graph via the constructor or use the adapter.setGraph(Graph) method
        final BaseGraphAdapter<ViewHolder> adapter = new BaseGraphAdapter<ViewHolder>(graph) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.node, parent, false);
                return new SimpleViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder viewHolder, Object data, int position) {
                ((SimpleViewHolder)viewHolder).textView.setText(data.toString());
            }
        };


        graphView.setAdapter(adapter);

        // set the algorithm here
        final BuchheimWalkerConfiguration configuration = new BuchheimWalkerConfiguration.Builder()
                .setSiblingSeparation(100)
                .setLevelSeparation(300)
                .setSubtreeSeparation(300)
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                .build();
        adapter.setAlgorithm(new BuchheimWalkerAlgorithm(configuration));
    }

    private String getNodeText() {
        return "User " + nodeCount++;
    }

        /*
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



         */


    }

    /*

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


    */


