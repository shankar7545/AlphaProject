package com.example.alpha.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alpha.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

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
    Node node0, node1, node2, node3, node4, node5, node6, node7, node8, node9, node10, node11, node12, node13, node14, node15, node16, node17, node18, node19, node20, node21, node22, node23, node24, node25, node26, node27, node28, node29, node30, node31, node32, node33, node34, node35, node36, node37, node38, node39, node40;
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


        ChainActivity.this.bar = new ProgressDialog(ChainActivity.this, R.style.MyAlertDialogStyle);
        ChainActivity.this.bar.setCancelable(false);
        ChainActivity.this.bar.setMessage("Building Chain...");
        ChainActivity.this.bar.setIndeterminate(true);
        ChainActivity.this.bar.setCanceledOnTouchOutside(true);




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


                    if (a.equals("No Child") || a.equals("No child") || a.equals("No Child(21)") || a.equals("No Child(22)") || a.equals("No Child(23)") || a.equals("No Child(24)") ||
                            a.equals("No Child(31)") || a.equals("No Child(32)") || a.equals("No Child(33)") || a.equals("No Child(34)") ||
                            a.equals("No Child(35)") || a.equals("No Child(36)") || a.equals("No Child(37)") || a.equals("No Child(38)") ||
                            a.equals("No Child(41)") || a.equals("No Child(42)") || a.equals("No Child(43)") || a.equals("No Child(44)") ||
                            a.equals("No Child(45)") || a.equals("No Child(46)") || a.equals("No Child(47)") || a.equals("No Child(48)") ||
                            a.equals("No Child(49)") || a.equals("No Child(410)") || a.equals("No Child(411)") || a.equals("No Child(412)") ||
                            a.equals("No Child(413)") || a.equals("No Child(414)") || a.equals("No Child(415)") || a.equals("No Child(416)")) {


                    } else {
                        showDialog(a);
                        /*Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                                .title("Money Added Successfully")
                                .content("50 has been added to your wallet ")
                                .color(255, 0, 0, 255)
                                .led_color(255, 255, 255, 255)
                                .addAction(intent, "Done")
                                .large_icon(R.drawable.ic_wallet_add)
                                .small_icon(R.drawable.fireman)
                                .rrule("FREQ=0;INTERVAL=0;COUNT=1")
                                .build(); */
                    }

                });


                //Change Color

                if (a.equals("No Child") || a.equals("No child") || a.equals("No Child(21)") || a.equals("No Child(22)") || a.equals("No Child(23)") || a.equals("No Child(24)") ||
                        a.equals("No Child(31)") || a.equals("No Child(32)") || a.equals("No Child(33)") || a.equals("No Child(34)") ||
                        a.equals("No Child(35)") || a.equals("No Child(36)") || a.equals("No Child(37)") || a.equals("No Child(38)") ||
                        a.equals("No Child(41)") || a.equals("No Child(42)") || a.equals("No Child(43)") || a.equals("No Child(44)") ||
                        a.equals("No Child(45)") || a.equals("No Child(46)") || a.equals("No Child(47)") || a.equals("No Child(48)") ||
                        a.equals("No Child(49)") || a.equals("No Child(410)") || a.equals("No Child(411)") || a.equals("No Child(412)") ||
                        a.equals("No Child(413)") || a.equals("No Child(414)") || a.equals("No Child(415)") || a.equals("No Child(416)")) {
                    ((SimpleViewHolder) viewHolder).chainLayoutUser.setBackgroundColor(getResources().getColor(R.color.red_400));
                } else {
                    ((SimpleViewHolder) viewHolder).chainLayoutUser.setBackgroundColor(getResources().getColor(R.color.indigo_700));

                }
            }
        };


        graphView.setAdapter(adapter);

        // set the algorithm here
        final BuchheimWalkerConfiguration configuration = new BuchheimWalkerConfiguration.Builder()
                .setSiblingSeparation(400)
                .setLevelSeparation(400)
                .setSubtreeSeparation(400)
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

                    loadGraphTwoOne(child21UID);
                    loadGraphTwoTwo(child22UID);

                }
                if (child21UN.equals("null") && child22UN.equals("null")) {
                    node3 = new Node("No Child(21)");
                    node4 = new Node("No Child(22)");


                    graph.addEdge(node1, node3);
                    graph.addEdge(node1, node4);
                    //ChainActivity.this.bar.dismiss();

                }

                if (!child21UN.equals("null") && child22UN.equals("null")) {
                    node3 = new Node(child21UN);
                    node4 = new Node("No Child(22)");


                    graph.addEdge(node1, node3);
                    graph.addEdge(node1, node4);
                    loadGraphTwoOne(child21UID);
                }

                if (child21UN.equals("null") && !child22UN.equals("null")) {
                    node3 = new Node("No Child(21)");
                    node4 = new Node(child22UN);


                    graph.addEdge(node1, node3);
                    graph.addEdge(node1, node4);
                    loadGraphTwoTwo(child22UID);
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

                    loadGraphTwoThree(child23UID);
                    loadGraphTwoFour(child24UID);
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

                    loadGraphTwoThree(child23UID);
                }

                if (child23UN.equals("null") && !child24UN.equals("null")) {
                    node5 = new Node("No Child(23)");
                    node6 = new Node(child24UN);


                    graph.addEdge(node2, node5);
                    graph.addEdge(node2, node6);

                    loadGraphTwoFour(child24UID);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphTwoOne(String child21UID) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child31UID = chaindataSnapshot.child(child21UID).child("uid1").child("uid").getValue().toString();
                String child31UN = chaindataSnapshot.child(child21UID).child("uid1").child("username").getValue().toString();

                String child32UID = chaindataSnapshot.child(child21UID).child("uid2").child("uid").getValue().toString();
                String child32UN = chaindataSnapshot.child(child21UID).child("uid2").child("username").getValue().toString();


                if (!child31UN.equals("null") && !child32UN.equals("null")) {
                    node7 = new Node(child31UN);
                    node8 = new Node(child32UN);


                    graph.addEdge(node3, node7);
                    graph.addEdge(node3, node8);

                    loadGraphThreeOne(child31UID);
                    loadGraphThreeTwo(child32UID);

                }
                if (child31UN.equals("null") && child32UN.equals("null")) {
                    node7 = new Node("No Child(31)");
                    node8 = new Node("No Child(32)");


                    graph.addEdge(node3, node7);
                    graph.addEdge(node3, node8);


                    //ChainActivity.this.bar.dismiss();

                }

                if (!child31UN.equals("null") && child32UN.equals("null")) {
                    node7 = new Node(child31UN);
                    node8 = new Node("No Child(32)");


                    graph.addEdge(node3, node7);
                    graph.addEdge(node3, node8);

                    loadGraphThreeOne(child31UID);

                }

                if (child31UN.equals("null") && !child32UN.equals("null")) {
                    node7 = new Node("No Child(31)");
                    node8 = new Node(child32UN);


                    graph.addEdge(node3, node7);
                    graph.addEdge(node3, node8);

                    loadGraphThreeTwo(child32UID);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphTwoTwo(String child22UID) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child33UID = chaindataSnapshot.child(child22UID).child("uid1").child("uid").getValue().toString();
                String child33UN = chaindataSnapshot.child(child22UID).child("uid1").child("username").getValue().toString();

                String child34UID = chaindataSnapshot.child(child22UID).child("uid2").child("uid").getValue().toString();
                String child34UN = chaindataSnapshot.child(child22UID).child("uid2").child("username").getValue().toString();


                if (!child33UN.equals("null") && !child34UN.equals("null")) {
                    node9 = new Node(child33UN);
                    node10 = new Node(child34UN);


                    graph.addEdge(node4, node9);
                    graph.addEdge(node4, node10);

                    loadGraphThreeThree(child33UID);
                    loadGraphThreeFour(child34UID);

                }
                if (child33UN.equals("null") && child34UN.equals("null")) {
                    node9 = new Node("No Child(33)");
                    node10 = new Node("No Child(34)");


                    graph.addEdge(node4, node9);
                    graph.addEdge(node4, node10);


                    //ChainActivity.this.bar.dismiss();

                }

                if (!child33UN.equals("null") && child34UN.equals("null")) {
                    node9 = new Node(child33UN);
                    node10 = new Node("No Child(34)");


                    graph.addEdge(node4, node9);
                    graph.addEdge(node4, node10);

                    loadGraphThreeThree(child33UID);

                }

                if (child33UN.equals("null") && !child34UN.equals("null")) {
                    node9 = new Node("No Child(33)");
                    node10 = new Node(child34UN);


                    graph.addEdge(node4, node9);
                    graph.addEdge(node4, node10);

                    loadGraphThreeFour(child34UID);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphTwoThree(String child23UID) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child35UID = chaindataSnapshot.child(child23UID).child("uid1").child("uid").getValue().toString();
                String child35UN = chaindataSnapshot.child(child23UID).child("uid1").child("username").getValue().toString();

                String child36UID = chaindataSnapshot.child(child23UID).child("uid2").child("uid").getValue().toString();
                String child36UN = chaindataSnapshot.child(child23UID).child("uid2").child("username").getValue().toString();


                if (!child35UN.equals("null") && !child36UN.equals("null")) {
                    node11 = new Node(child35UN);
                    node12 = new Node(child36UN);


                    graph.addEdge(node5, node11);
                    graph.addEdge(node5, node12);

                    loadGraphThreeFive(child35UID);
                    loadGraphThreeSix(child36UID);



                }
                if (child35UN.equals("null") && child36UN.equals("null")) {
                    node11 = new Node("No Child(35)");
                    node12 = new Node("No Child(36)");


                    graph.addEdge(node5, node11);
                    graph.addEdge(node5, node12);

                    //ChainActivity.this.bar.dismiss();

                }

                if (!child35UN.equals("null") && child36UN.equals("null")) {
                    node11 = new Node(child35UN);
                    node12 = new Node("No Child(36)");


                    graph.addEdge(node5, node11);
                    graph.addEdge(node5, node12);

                    loadGraphThreeFive(child35UID);
                }

                if (child35UN.equals("null") && !child36UN.equals("null")) {
                    node11 = new Node("No Child(35)");
                    node12 = new Node(child36UN);


                    graph.addEdge(node5, node11);
                    graph.addEdge(node5, node12);

                    loadGraphThreeSix(child36UID);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphTwoFour(String child24UID) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child37UID = chaindataSnapshot.child(child24UID).child("uid1").child("uid").getValue().toString();
                String child37UN = chaindataSnapshot.child(child24UID).child("uid1").child("username").getValue().toString();

                String child38UID = chaindataSnapshot.child(child24UID).child("uid2").child("uid").getValue().toString();
                String child38UN = chaindataSnapshot.child(child24UID).child("uid2").child("username").getValue().toString();


                if (!child37UN.equals("null") && !child38UN.equals("null")) {
                    node13 = new Node(child37UN);
                    node14 = new Node(child38UN);


                    graph.addEdge(node6, node13);
                    graph.addEdge(node6, node14);

                    loadGraphThreeSeven(child37UID);
                    loadGraphThreeEight(child38UID);

                }
                if (child37UN.equals("null") && child38UN.equals("null")) {
                    node13 = new Node("No Child(37)");
                    node14 = new Node("No Child(38)");


                    graph.addEdge(node6, node13);
                    graph.addEdge(node6, node14);

                    ChainActivity.this.bar.dismiss();

                }

                if (!child37UN.equals("null") && child38UN.equals("null")) {
                    node13 = new Node(child37UN);
                    node14 = new Node("No Child(38)");


                    graph.addEdge(node6, node13);
                    graph.addEdge(node6, node14);

                    loadGraphThreeSeven(child37UID);
                }

                if (child37UN.equals("null") && !child38UN.equals("null")) {
                    node13 = new Node("No Child(37)");
                    node14 = new Node(child38UN);


                    graph.addEdge(node6, node13);
                    graph.addEdge(node6, node14);

                    loadGraphThreeEight(child38UID);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphThreeOne(String child31UID) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child41UID = chaindataSnapshot.child(child31UID).child("uid1").child("uid").getValue().toString();
                String child41UN = chaindataSnapshot.child(child31UID).child("uid1").child("username").getValue().toString();

                String child42UID = chaindataSnapshot.child(child31UID).child("uid2").child("uid").getValue().toString();
                String child42UN = chaindataSnapshot.child(child31UID).child("uid2").child("username").getValue().toString();


                if (!child41UN.equals("null") && !child42UN.equals("null")) {
                    node15 = new Node(child41UN);
                    node16 = new Node(child42UN);


                    graph.addEdge(node7, node15);
                    graph.addEdge(node7, node16);

                    ChainActivity.this.bar.dismiss();
                }
                if (child41UN.equals("null") && child42UN.equals("null")) {
                    node15 = new Node("No Child(41)");
                    node16 = new Node("No Child(42)");


                    graph.addEdge(node7, node15);
                    graph.addEdge(node7, node16);

                    //ChainActivity.this.bar.dismiss();

                }

                if (!child41UN.equals("null") && child42UN.equals("null")) {
                    node15 = new Node(child41UN);
                    node16 = new Node("No Child(42)");


                    graph.addEdge(node7, node15);
                    graph.addEdge(node7, node16);

                    ChainActivity.this.bar.dismiss();
                }

                if (child41UN.equals("null") && !child42UN.equals("null")) {
                    node15 = new Node("No Child(41)");
                    node16 = new Node(child42UN);


                    graph.addEdge(node7, node15);
                    graph.addEdge(node7, node16);
                    ChainActivity.this.bar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphThreeTwo(String child32UID) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child43UID = chaindataSnapshot.child(child32UID).child("uid1").child("uid").getValue().toString();
                String child43UN = chaindataSnapshot.child(child32UID).child("uid1").child("username").getValue().toString();

                String child44UID = chaindataSnapshot.child(child32UID).child("uid2").child("uid").getValue().toString();
                String child44UN = chaindataSnapshot.child(child32UID).child("uid2").child("username").getValue().toString();


                if (!child43UN.equals("null") && !child44UN.equals("null")) {
                    node17 = new Node(child43UN);
                    node18 = new Node(child44UN);


                    graph.addEdge(node8, node17);
                    graph.addEdge(node8, node18);

                    ChainActivity.this.bar.dismiss();
                }
                if (child43UN.equals("null") && child44UN.equals("null")) {
                    node17 = new Node("No Child(43)");
                    node18 = new Node("No Child(44)");


                    graph.addEdge(node8, node17);
                    graph.addEdge(node8, node18);

                    ChainActivity.this.bar.dismiss();

                }

                if (!child43UN.equals("null") && child44UN.equals("null")) {
                    node17 = new Node(child43UN);
                    node18 = new Node("No Child(44)");


                    graph.addEdge(node8, node17);
                    graph.addEdge(node8, node18);

                    ChainActivity.this.bar.dismiss();
                }

                if (child43UN.equals("null") && !child44UN.equals("null")) {
                    node17 = new Node("No Child(43)");
                    node18 = new Node(child44UN);


                    graph.addEdge(node8, node17);
                    graph.addEdge(node8, node18);
                    ChainActivity.this.bar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphThreeThree(String child33UID) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child45UID = chaindataSnapshot.child(child33UID).child("uid1").child("uid").getValue().toString();
                String child45UN = chaindataSnapshot.child(child33UID).child("uid1").child("username").getValue().toString();

                String child46UID = chaindataSnapshot.child(child33UID).child("uid2").child("uid").getValue().toString();
                String child46UN = chaindataSnapshot.child(child33UID).child("uid2").child("username").getValue().toString();


                if (!child45UN.equals("null") && !child46UN.equals("null")) {
                    node19 = new Node(child45UN);
                    node20 = new Node(child46UN);


                    graph.addEdge(node9, node19);
                    graph.addEdge(node9, node20);

                    ChainActivity.this.bar.dismiss();
                }
                if (child45UN.equals("null") && child46UN.equals("null")) {
                    node19 = new Node("No Child(45)");
                    node20 = new Node("No Child(46)");


                    graph.addEdge(node9, node19);
                    graph.addEdge(node9, node20);

                    ChainActivity.this.bar.dismiss();

                }

                if (!child45UN.equals("null") && child46UN.equals("null")) {
                    node19 = new Node(child45UN);
                    node20 = new Node("No Child(46)");


                    graph.addEdge(node9, node19);
                    graph.addEdge(node9, node20);

                    ChainActivity.this.bar.dismiss();
                }

                if (child45UN.equals("null") && !child46UN.equals("null")) {
                    node19 = new Node("No Child(45)");
                    node20 = new Node(child46UN);


                    graph.addEdge(node9, node19);
                    graph.addEdge(node9, node20);
                    ChainActivity.this.bar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphThreeFour(String child34UID) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child47UID = chaindataSnapshot.child(child34UID).child("uid1").child("uid").getValue().toString();
                String child47UN = chaindataSnapshot.child(child34UID).child("uid1").child("username").getValue().toString();

                String child48UID = chaindataSnapshot.child(child34UID).child("uid2").child("uid").getValue().toString();
                String child48UN = chaindataSnapshot.child(child34UID).child("uid2").child("username").getValue().toString();


                if (!child47UID.equals("null") && !child48UN.equals("null")) {
                    node21 = new Node(child47UID);
                    node22 = new Node(child48UN);


                    graph.addEdge(node10, node21);
                    graph.addEdge(node10, node22);

                    ChainActivity.this.bar.dismiss();
                }
                if (child47UID.equals("null") && child48UN.equals("null")) {
                    node21 = new Node("No Child(47)");
                    node22 = new Node("No Child(48)");


                    graph.addEdge(node10, node21);
                    graph.addEdge(node10, node22);

                    ChainActivity.this.bar.dismiss();

                }

                if (!child47UID.equals("null") && child48UN.equals("null")) {
                    node21 = new Node(child47UN);
                    node22 = new Node("No Child(48)");


                    graph.addEdge(node10, node21);
                    graph.addEdge(node10, node22);

                    ChainActivity.this.bar.dismiss();
                }

                if (child47UID.equals("null") && !child48UN.equals("null")) {
                    node21 = new Node("No Child(47)");
                    node22 = new Node(child48UN);


                    graph.addEdge(node10, node21);
                    graph.addEdge(node10, node22);
                    ChainActivity.this.bar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphThreeFive(String child35UID) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child49UID = chaindataSnapshot.child(child35UID).child("uid1").child("uid").getValue().toString();
                String child49UN = chaindataSnapshot.child(child35UID).child("uid1").child("username").getValue().toString();

                String child410UID = chaindataSnapshot.child(child35UID).child("uid2").child("uid").getValue().toString();
                String child410UN = chaindataSnapshot.child(child35UID).child("uid2").child("username").getValue().toString();


                if (!child49UN.equals("null") && !child410UN.equals("null")) {
                    node23 = new Node(child49UN);
                    node24 = new Node(child410UN);


                    graph.addEdge(node11, node23);
                    graph.addEdge(node11, node24);

                    ChainActivity.this.bar.dismiss();
                }
                if (child49UN.equals("null") && child410UN.equals("null")) {
                    node23 = new Node("No Child(49)");
                    node24 = new Node("No Child(410)");


                    graph.addEdge(node11, node23);
                    graph.addEdge(node11, node24);

                    ChainActivity.this.bar.dismiss();

                }

                if (!child49UN.equals("null") && child410UN.equals("null")) {
                    node23 = new Node(child49UN);
                    node24 = new Node("No Child(410)");


                    graph.addEdge(node11, node23);
                    graph.addEdge(node11, node24);

                    ChainActivity.this.bar.dismiss();
                }

                if (child49UN.equals("null") && !child410UN.equals("null")) {
                    node23 = new Node("No Child(49)");
                    node24 = new Node(child410UN);


                    graph.addEdge(node11, node23);
                    graph.addEdge(node11, node24);
                    ChainActivity.this.bar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphThreeSix(String child36UID) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child411UID = chaindataSnapshot.child(child36UID).child("uid1").child("uid").getValue().toString();
                String child411UN = chaindataSnapshot.child(child36UID).child("uid1").child("username").getValue().toString();

                String child412UID = chaindataSnapshot.child(child36UID).child("uid2").child("uid").getValue().toString();
                String child412UN = chaindataSnapshot.child(child36UID).child("uid2").child("username").getValue().toString();


                if (!child411UN.equals("null") && !child412UN.equals("null")) {
                    node25 = new Node(child411UN);
                    node26 = new Node(child412UN);


                    graph.addEdge(node12, node23);
                    graph.addEdge(node12, node26);

                    ChainActivity.this.bar.dismiss();
                }
                if (child411UN.equals("null") && child412UN.equals("null")) {
                    node25 = new Node("No Child(411)");
                    node26 = new Node("No Child(412)");


                    graph.addEdge(node12, node25);
                    graph.addEdge(node12, node26);

                    ChainActivity.this.bar.dismiss();

                }

                if (!child411UN.equals("null") && child412UN.equals("null")) {
                    node25 = new Node(child411UN);
                    node26 = new Node("No Child(412)");


                    graph.addEdge(node12, node25);
                    graph.addEdge(node12, node26);

                    ChainActivity.this.bar.dismiss();
                }

                if (child411UN.equals("null") && !child412UN.equals("null")) {
                    node25 = new Node("No Child(411)");
                    node26 = new Node(child412UN);


                    graph.addEdge(node12, node25);
                    graph.addEdge(node12, node26);
                    ChainActivity.this.bar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphThreeSeven(String child37UID) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child413UID = chaindataSnapshot.child(child37UID).child("uid1").child("uid").getValue().toString();
                String child413UN = chaindataSnapshot.child(child37UID).child("uid1").child("username").getValue().toString();

                String child414UID = chaindataSnapshot.child(child37UID).child("uid2").child("uid").getValue().toString();
                String child414UN = chaindataSnapshot.child(child37UID).child("uid2").child("username").getValue().toString();


                if (!child413UN.equals("null") && !child414UN.equals("null")) {
                    node25 = new Node(child413UN);
                    node26 = new Node(child414UN);


                    graph.addEdge(node12, node23);
                    graph.addEdge(node12, node26);

                    ChainActivity.this.bar.dismiss();
                }
                if (child413UN.equals("null") && child414UN.equals("null")) {
                    node25 = new Node("No Child(413)");
                    node26 = new Node("No Child(414)");


                    graph.addEdge(node12, node25);
                    graph.addEdge(node12, node26);

                    ChainActivity.this.bar.dismiss();

                }

                if (!child413UN.equals("null") && child414UN.equals("null")) {
                    node25 = new Node(child413UN);
                    node26 = new Node("No Child(414)");


                    graph.addEdge(node12, node25);
                    graph.addEdge(node12, node26);

                    ChainActivity.this.bar.dismiss();
                }

                if (child413UN.equals("null") && !child414UN.equals("null")) {
                    node25 = new Node("No Child(413)");
                    node26 = new Node(child414UN);


                    graph.addEdge(node12, node25);
                    graph.addEdge(node12, node26);
                    ChainActivity.this.bar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadGraphThreeEight(String child38UID) {
        mChain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot chaindataSnapshot) {

                String child415UID = chaindataSnapshot.child(child38UID).child("uid1").child("uid").getValue().toString();
                String child415UN = chaindataSnapshot.child(child38UID).child("uid1").child("username").getValue().toString();

                String child416UID = chaindataSnapshot.child(child38UID).child("uid2").child("uid").getValue().toString();
                String child416UN = chaindataSnapshot.child(child38UID).child("uid2").child("username").getValue().toString();


                if (!child415UN.equals("null") && !child416UN.equals("null")) {
                    node25 = new Node(child415UN);
                    node26 = new Node(child416UN);


                    graph.addEdge(node12, node23);
                    graph.addEdge(node12, node26);

                    ChainActivity.this.bar.dismiss();
                }
                if (child415UN.equals("null") && child416UN.equals("null")) {
                    node25 = new Node("No Child(415)");
                    node26 = new Node("No Child(416)");


                    graph.addEdge(node12, node25);
                    graph.addEdge(node12, node26);

                    ChainActivity.this.bar.dismiss();

                }

                if (!child415UN.equals("null") && child416UN.equals("null")) {
                    node25 = new Node(child415UN);
                    node26 = new Node("No Child(416)");


                    graph.addEdge(node12, node25);
                    graph.addEdge(node12, node26);

                    ChainActivity.this.bar.dismiss();
                }

                if (child415UN.equals("null") && !child416UN.equals("null")) {
                    node25 = new Node("No Child(415)");
                    node26 = new Node(child416UN);


                    graph.addEdge(node12, node25);
                    graph.addEdge(node12, node26);
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

        TextView userNameSheet = view.findViewById(R.id.userNameSheet);

        userNameSheet.setText(a);

        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(dialog -> mBottomSheetDialog = null);
    }

    private void showDialog(String a) {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_contact_dark);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        dialog.setCancelable(true);
        TextView usernameDialog = dialog.findViewById(R.id.usernameDialog);
        usernameDialog.setText(a);

        (dialog.findViewById(R.id.bt_close)).setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chain");
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



