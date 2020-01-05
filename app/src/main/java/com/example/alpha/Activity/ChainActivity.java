package com.example.alpha.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alpha.Model.TreeClass;
import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    TextView parent, rightChild, leftChild;
    String selfUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Node node1, node2, node3, node4, node5, node6, node7, node8, node9, node10, node11, node12, node13, node14, node15, node16;
    private int nodeCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chain);
        mRef = FirebaseDatabase.getInstance().getReference("TreeView").child("1boZEA0VUvPW4C05cx4N7ZIQZz");

        GraphView graphView = findViewById(R.id.graph);

        // example tree
        final Graph graph = new Graph();




        //Getting the tree




        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot currentdataSnapshot) {

                if (currentdataSnapshot.exists()) {
                    TreeClass treeClass = currentdataSnapshot.getValue(TreeClass.class);
                    node1 = new Node(treeClass.getCurrentuser());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        mRef.child("Level1").child("lchild").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot level1leftdataSnapshot) {
                TreeClass lvel1leftclass=level1leftdataSnapshot.getValue(TreeClass.class);
                node2=new Node(lvel1leftclass.getUsername());
                graph.addEdge(node1, node2);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRef.child("Level1").child("rchild").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot level1rightdataSnapshot) {
                TreeClass lvel1rightclass=level1rightdataSnapshot.getValue(TreeClass.class);
                node4=new Node(lvel1rightclass.getUsername());
                graph.addEdge(node1, node4);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child("Level2Left").child("lchild").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot lvl2leftdatasnap) {

                TreeClass lvl2leftclass=lvl2leftdatasnap.getValue(TreeClass.class);
                node5=new Node(lvl2leftclass.getUsername());
                graph.addEdge(node2,node5);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRef.child("Level2Left").child("rchild").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot lvl2rightdatasnap) {

                TreeClass lvl2rightlass=lvl2rightdatasnap.getValue(TreeClass.class);
                node6=new Node(lvl2rightlass.getUsername());
                graph.addEdge(node2,node6);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mRef.child("Level2Right").child("lchild").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot lvl2righttdatasnap) {

                TreeClass lvl2rightclass=lvl2righttdatasnap.getValue(TreeClass.class);
                node7=new Node(lvl2rightclass.getUsername());
                graph.addEdge(node4,node7);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRef.child("Level2Right").child("rchild").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot lvl2rightdatasnap) {

                TreeClass lvl2rightlass=lvl2rightdatasnap.getValue(TreeClass.class);
                node8=new Node(lvl2rightlass.getUsername());
                graph.addEdge(node4,node8);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







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
                ((SimpleViewHolder) viewHolder).textView.setText(data.toString());
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


