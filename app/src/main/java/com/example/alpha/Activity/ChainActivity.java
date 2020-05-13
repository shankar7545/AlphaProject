package com.example.alpha.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import androidx.appcompat.widget.Toolbar;
import de.blox.graphview.BaseGraphAdapter;
import de.blox.graphview.Graph;
import de.blox.graphview.GraphView;
import de.blox.graphview.Node;
import de.blox.graphview.ViewHolder;
import de.blox.graphview.tree.BuchheimWalkerAlgorithm;
import de.blox.graphview.tree.BuchheimWalkerConfiguration;


public class ChainActivity extends AppCompatActivity {
    final Graph graph = new Graph();
    ProgressDialog bar;
    Context context;
    DatabaseReference mChain, mUser;
    RelativeLayout Chain;
    TextView parent, rightChild, leftChild;
    String selfUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Node node1, node2, node3, node4, node5, node6, node7, node8, node9, node10, node11, node12, node13, node14, node15, node16;
    private int nodeCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chain);
        mChain = FirebaseDatabase.getInstance().getReference("Chain");
        mUser = FirebaseDatabase.getInstance().getReference("Users");
        GraphView graphView = findViewById(R.id.graph);


        ChainActivity.this.bar = new ProgressDialog(ChainActivity.this);
        ChainActivity.this.bar.setCancelable(false);
        ChainActivity.this.bar.setMessage("Building Chain...");
        ChainActivity.this.bar.setIndeterminate(true);
        ChainActivity.this.bar.setCanceledOnTouchOutside(false);

        // example tree


        //Getting the tree


        LoadGraph();
        initToolbar();


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
                .setSiblingSeparation(300)
                .setLevelSeparation(300)
                .setSubtreeSeparation(300)
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                .build();
        adapter.setAlgorithm(new BuchheimWalkerAlgorithm(configuration));

    }

    static class SimpleViewHolder extends ViewHolder {
        TextView textView;

        SimpleViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }


    private void LoadGraph() {
        ChainActivity.this.bar.show();

        new Handler().postDelayed(() -> {
            mUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userdataSnapshot) {

                    mChain.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                            String userName = userdataSnapshot.child(selfUid).child("username").getValue().toString();
                            String uid1 = chaindataSnapshot.child(selfUid).child("uid1").child("uid").getValue().toString();
                            String uid1UN = chaindataSnapshot.child(selfUid).child("uid1").child("username").getValue().toString();

                            String uid2 = chaindataSnapshot.child(selfUid).child("uid2").child("uid").getValue().toString();
                            String uid2UN = chaindataSnapshot.child(selfUid).child("uid2").child("username").getValue().toString();

                            if (!uid1UN.equals("null") && !uid2UN.equals("null")) {
                                node1 = new Node(userName);
                                node2 = new Node(uid1UN);
                                node3 = new Node(uid2UN);

                                graph.addEdge(node1, node2);
                                graph.addEdge(node1, node3);


                                String child21UID = chaindataSnapshot.child(uid1).child("uid1").child("uid").getValue().toString();
                                String child21UN = chaindataSnapshot.child(uid1).child("uid1").child("username").getValue().toString();

                                String child22UID = chaindataSnapshot.child(uid2).child("uid2").child("uid").getValue().toString();
                                String child22UN = chaindataSnapshot.child(uid2).child("uid2").child("username").getValue().toString();

                                if (!child21UN.equals("null") && !child22UN.equals("null")) {

                                    node4 = new Node("node4");
                                    node5 = new Node("node5");

                                    graph.addEdge(node2, node4);
                                    graph.addEdge(node2, node5);
                                }


                            }
                            if (uid1UN.equals("null") && !uid2UN.equals("null")) {
                                node1 = new Node(userName);
                                node2 = new Node("No Child");
                                node3 = new Node(uid2UN);

                                graph.addEdge(node1, node2);
                                graph.addEdge(node1, node3);

                            }
                            if (!uid1UN.equals("null") && uid2UN.equals("null")) {
                                node1 = new Node(userName);
                                node2 = new Node(uid1UN);
                                node3 = new Node("No Child");

                                graph.addEdge(node1, node2);
                                graph.addEdge(node1, node3);
                            }
                            if (uid1UN.equals("null") && uid2UN.equals("null")) {
                                node1 = new Node(userName);
                                node2 = new Node("No Child");
                                node3 = new Node("No child");

                                graph.addEdge(node1, node2);
                                graph.addEdge(node1, node3);

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            ChainActivity.this.bar.dismiss();

        }, 1000);

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chain Layout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.help) {
            Intent intent = new Intent(ChainActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}



