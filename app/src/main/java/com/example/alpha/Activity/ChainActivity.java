package com.example.alpha.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alpha.R;
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
        node1 = new Node("Node1");

        mRef = FirebaseDatabase.getInstance().getReference();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("Chain").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                    final String child1 = dataSnapshot.child("Chain").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("child1").getValue().toString();
                    String child2 = dataSnapshot.child("Chain").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("child2").getValue().toString();

                    node2 = new Node(child1);
                    graph.addEdge(node1, node2);
                    node3 = new Node(child2);
                    graph.addEdge(node1, node3);
                } else {
                    node2 = new Node("?");
                    graph.addEdge(node1, node2);
                    node3 = new Node("?");
                    graph.addEdge(node1, node3);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        node4 = new Node("Node4");
        graph.addEdge(node2, node4);
        node5 = new Node("Node5");
        graph.addEdge(node2, node5);
        node6 = new Node("node6");
        graph.addEdge(node4, node6);
        node7 = new Node("Node7");
        graph.addEdge(node4, node7);


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


}


