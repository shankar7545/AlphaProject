package com.example.alpha.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
    Node node0, node1, node2, node3, node4, node5, node6, node7, node8, node9, node10, node11, node12, node13, node14, node15, node16;
    private int nodeCount = 1;


    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;

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


        //BottomSheet
        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
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

                String a = data.toString();

                ((SimpleViewHolder) viewHolder).textView.setText(data.toString());

                ((SimpleViewHolder) viewHolder).chainLayoutUser.setOnClickListener(v -> {
                    //  Toast.makeText(ChainActivity.this, data.toString(), Toast.LENGTH_SHORT).show();


                    if (a.equals("No Child(21)") || a.equals("No Child(22)") || a.equals("No Child(23)") || a.equals("No Child(24)")) {


                    } else {
                        showBottomSheetDialog(a);

                    }

                });


                //Change Color

                if (a.equals("No Child(21)") || a.equals("No Child(22)") || a.equals("No Child(23)") || a.equals("No Child(24)")) {
                    ((SimpleViewHolder) viewHolder).chainLayoutUser.setBackgroundColor(getResources().getColor(R.color.red_800));
                }
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
        LinearLayout chainLayoutUser;

        SimpleViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            chainLayoutUser = itemView.findViewById(R.id.chainLayoutUser);
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
                                node0 = new Node(userName);
                                node1 = new Node(uid1UN);
                                node2 = new Node(uid2UN);


                                graph.addEdge(node0, node1);
                                graph.addEdge(node0, node2);

                                loadGraphOneOne(uid1);
                                loadGraphOneTwo(uid2);

                            }
                            if (uid1UN.equals("null") && uid2UN.equals("null")) {
                                node0 = new Node(userName);
                                node1 = new Node("No Child");
                                node2 = new Node("No child");


                                graph.addEdge(node0, node1);
                                graph.addEdge(node0, node2);
                                ChainActivity.this.bar.dismiss();

                            }

                            if (!uid1UN.equals("null") && uid2UN.equals("null")) {
                                node0 = new Node(userName);
                                node1 = new Node(uid1UN);
                                node2 = new Node("No child");


                                graph.addEdge(node0, node1);
                                graph.addEdge(node0, node2);

                                loadGraphOneOne(uid1);
                            }

                            if (uid1UN.equals("null") && !uid2UN.equals("null")) {
                                node0 = new Node(userName);
                                node1 = new Node("No Child");
                                node2 = new Node(uid2UN);


                                graph.addEdge(node0, node1);
                                graph.addEdge(node0, node2);

                                loadGraphOneTwo(uid2);
                            }

                           /* else {
                                node0 = new Node(userName);
                                node1 = new Node("No Child");

                                graph.addEdge(node0, node1);
                                ChainActivity.this.bar.dismiss();

                            }
                            if(!uid2UN.equals("null"))
                            {
                                node2 = new Node(uid2UN);

                                graph.addEdge(node0, node2);
                                loadGraphOneTwo(uid2);
                                ChainActivity.this.bar.dismiss();
                            }
                            else
                            {
                                node2 = new Node("No child");

                                graph.addEdge(node0, node2);
                                ChainActivity.this.bar.dismiss();
                            }  */


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


        }, 0);

    }


    private void loadGraphOneOne(String uid1) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child21UID = chaindataSnapshot.child(uid1).child("uid1").child("uid").getValue().toString();
                String child21UN = chaindataSnapshot.child(uid1).child("uid1").child("username").getValue().toString();

                String child22UID = chaindataSnapshot.child(uid1).child("uid2").child("uid").getValue().toString();
                String child22UN = chaindataSnapshot.child(uid1).child("uid2").child("username").getValue().toString();


                if (!child21UN.equals("null") && !child22UN.equals("null")) {
                    node3 = new Node(child21UN);
                    node4 = new Node(child22UN);


                    graph.addEdge(node1, node3);
                    graph.addEdge(node1, node4);

                    ChainActivity.this.bar.dismiss();
                }
                if (child21UN.equals("null") && child22UN.equals("null")) {
                    node3 = new Node("No Child(21)");
                    node4 = new Node("No Child(22)");


                    graph.addEdge(node1, node3);
                    graph.addEdge(node1, node4);
                    ChainActivity.this.bar.dismiss();

                }

                if (!child21UN.equals("null") && child22UN.equals("null")) {
                    node3 = new Node(child21UN);
                    node4 = new Node("No Child(22)");


                    graph.addEdge(node1, node3);
                    graph.addEdge(node1, node4);
                    ChainActivity.this.bar.dismiss();
                }

                if (child21UN.equals("null") && !child22UN.equals("null")) {
                    node3 = new Node("No Child(21)");
                    node4 = new Node(child22UN);


                    graph.addEdge(node1, node3);
                    graph.addEdge(node1, node4);
                    ChainActivity.this.bar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphOneTwo(String uid2) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child23UID = chaindataSnapshot.child(uid2).child("uid1").child("uid").getValue().toString();
                String child23UN = chaindataSnapshot.child(uid2).child("uid1").child("username").getValue().toString();

                String child24UID = chaindataSnapshot.child(uid2).child("uid2").child("uid").getValue().toString();
                String child24UN = chaindataSnapshot.child(uid2).child("uid2").child("username").getValue().toString();


                if (!child23UN.equals("null") && !child24UN.equals("null")) {
                    node5 = new Node(child23UN);
                    node6 = new Node(child24UN);


                    graph.addEdge(node2, node5);
                    graph.addEdge(node2, node6);

                    ChainActivity.this.bar.dismiss();
                }
                if (child23UN.equals("null") && child24UN.equals("null")) {
                    node5 = new Node("No Child(23)");
                    node6 = new Node("No Child(24)");


                    graph.addEdge(node2, node5);
                    graph.addEdge(node2, node6);


                    ChainActivity.this.bar.dismiss();

                }

                if (!child23UN.equals("null") && child24UN.equals("null")) {
                    node5 = new Node(child23UN);
                    node6 = new Node("No Child(24)");


                    graph.addEdge(node2, node5);
                    graph.addEdge(node2, node6);

                    ChainActivity.this.bar.dismiss();
                }

                if (child23UN.equals("null") && !child24UN.equals("null")) {
                    node5 = new Node("No Child(23)");
                    node6 = new Node(child24UN);


                    graph.addEdge(node2, node5);
                    graph.addEdge(node2, node6);
                    ChainActivity.this.bar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void showBottomSheetDialog(String a) {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.sheet_info, null);

        TextView sheet = view.findViewById(R.id.textViewSheet);

        view.findViewById(R.id.lyt_preview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Preview '" + a + "' clicked", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.lyt_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Share '" + a + "' clicked", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.lyt_get_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Get link '" + a + "' clicked", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.lyt_make_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Make a copy '" + a + "' clicked", Toast.LENGTH_SHORT).show();
            }
        });

        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(dialog -> mBottomSheetDialog = null);
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



