package com.example.alpha.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.example.alpha.Activity.HomeActivity;
import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.example.alpha.ViewHolder.TransactionView;
import com.example.alpha.Wallet.TransactionsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {


    public ProgressBar progressBar;
    public RecyclerView transactionsRecycler;
    public LinearLayoutManager transactionsLinearLayout;
    DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mTransactionsRecycler, dbPaytm, mLogin, mLevelTwo, mLevelThree;
    LinearLayout imageScroll;
    RelativeLayout homeFrag;
    TextView profileName, wallet_bal, level, withdrawable;
    String selfUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Calendar c = Calendar.getInstance();
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss aa");
    String timeformat = time.format(c.getTime());
    String datetime = dateformat.format(c.getTime());
    FloatingActionButton fabRefresh;
    EditText editTextReferCode;
    FirebaseRecyclerAdapter<Transaction_Class, TransactionView> TransactionsAdapter;
    LinearLayout  progressBarLayout, no_matches_found;
    NestedScrollView transactions_linear;
    Dialog dialog;
    View mView;
    private TabLayout tab_layout;
    private NestedScrollView nested_scroll_view;
    private SliderLayout mDemoSlider;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        fabRefresh = mView.findViewById(R.id.fab_refresh);

        editTextReferCode = mView.findViewById(R.id.referCode);
        //wallet_bal =(TextView)mView.findViewById(R.id.balanceH);
        //level =(TextView)mView.findViewById(R.id.levelH);
        dbPaytm = FirebaseDatabase.getInstance().getReference("Paytm");


        mFirebase = FirebaseDatabase.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");
        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet").child(selfUid).child("Transactions");

        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference();
        scoresRef.keepSynced(true);


        //Recycler For Transactions


        transactions_linear = (NestedScrollView) mView.findViewById(R.id.transactionsHome_linear);

        transactionsRecycler = mView.findViewById(R.id.transactionsHomeRecycler);

        transactionsRecycler.hasFixedSize();


        transactionsLinearLayout = new LinearLayoutManager(getContext());

        no_matches_found = (LinearLayout) mView.findViewById(R.id.no_transactions_found);

        transactionsRecycler.setLayoutManager(transactionsLinearLayout);


        mTransactionsRecycler = FirebaseDatabase.getInstance().getReference("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        mTransactionsRecycler.child("Transactions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("history").exists()) {

                    transactions_linear.setVisibility(View.VISIBLE);
                    no_matches_found.setVisibility(View.GONE);

                } else {
                    transactions_linear.setVisibility(View.GONE);
                    no_matches_found.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mRef.child(selfUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //details
                String nameH = dataSnapshot.child("username").getValue().toString();
                //String balanceH = dataSnapshot.child("balance").getValue().toString();
                String levelH = dataSnapshot.child("level").getValue().toString();

                //profileName.setText(nameH + "(Username)");
                //wallet_bal.setText(balanceH);
                //level.setText("LEVEL "+levelH);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fabRefresh.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((HomeActivity) getActivity()).refreshMyData();


                    }
                }, 500);
            }
        });


        CheckingLevelUpgrades();
        loadTransactions();
        Slider();
        return mView;

    }

    private void Slider() {
        mDemoSlider = mView.findViewById(R.id.slider);

        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();


        //image2
        listUrl.add("https://firebasestorage.googleapis.com/v0/b/jobtrackingsystem-83bad.appspot.com/o/image2.jpeg?alt=media&token=0e1e079d-18c0-4858-9ca0-f088ac32ac8d");
        listName.add("");

        //image4
        listUrl.add("https://firebasestorage.googleapis.com/v0/b/jobtrackingsystem-83bad.appspot.com/o/image4.jpg?alt=media&token=e8788eef-f8fb-4c86-be13-2d0258d43527");
        listName.add("");

        //image3
        listUrl.add("https://firebasestorage.googleapis.com/v0/b/jobtrackingsystem-83bad.appspot.com/o/image3.jpg?alt=media&token=4f248df3-b3ba-43da-8560-9cd1b2822926");
        listName.add("");

        //image1
        listUrl.add("https://firebasestorage.googleapis.com/v0/b/jobtrackingsystem-83bad.appspot.com/o/image1.jpeg?alt=media&token=eb02880d-080b-453a-bddf-39e0672cdb3e");
        listName.add("");


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();

        for (int i = 0; i < listUrl.size(); i++) {
            TextSliderView sliderView = new TextSliderView(getContext());
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            BaseSliderView baseSliderView = sliderView
                    .image(listUrl.get(i))
                    .description(listName.get(i))
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", listName.get(i));
            mDemoSlider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(5000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopCyclingWhenTouch(false);
    }

    private void loadTransactions() {
        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("history");

        Query withdrawList = mTransactions.orderByChild("position");
        FirebaseRecyclerOptions<Transaction_Class> withdrawOption = new FirebaseRecyclerOptions.Builder<Transaction_Class>()
                .setQuery(withdrawList, Transaction_Class.class)
                .build();
        TransactionsAdapter = new FirebaseRecyclerAdapter<Transaction_Class, TransactionView>(withdrawOption) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onBindViewHolder(@NonNull TransactionView holder, final int position, @NonNull final Transaction_Class model) {
                transactions_linear.setVisibility(View.VISIBLE);

                holder.transactionAmount.setText(model.getTransactionAmount());
                holder.transactionDate.setText(model.getTransactionDate());
                holder.transactionTime.setText(model.getTransactionTime());


                String transType = model.getTransactionType();
                {
                    if (transType.equals("credited")) {
                        holder.transactionType.setText("Received from");
                        holder.transactionImage.setImageResource(R.drawable.transaction_received);
                        holder.transactionStatus.setVisibility(View.GONE);
                        holder.transactionName.setText(model.getTransferredFrom());
                        //holder.transactionStatus.setTextColor(getResources().getColor(R.color.green_500));
                        holder.transactionAmount.setTextColor(getResources().getColor(R.color.green_500));

                    } else if (transType.equals("debited")) {
                        holder.transactionType.setText("Paid To");
                        holder.transactionImage.setImageResource(R.drawable.transaction_send);
                        //holder.transactionStatus.setText("Debited");
                        holder.transactionStatus.setVisibility(View.GONE);
                        holder.transactionName.setText(model.getTransferredTo());
                        //holder.transactionStatus.setTextColor(getResources().getColor(R.color.red_500));
                        holder.transactionAmount.setTextColor(getResources().getColor(R.color.red_500));


                    }
                }


                holder.transactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(), TransactionsActivity.class);
                        startActivity(intent);


                    }
                });
            }

            @NonNull
            @Override
            public TransactionView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.transaction_history_layout, viewGroup, false);
                return new TransactionView(itemView);
            }
        };
        transactionsRecycler.setAdapter(TransactionsAdapter);
        TransactionsAdapter.startListening();

    }


    public void CheckingLevelUpgrades() {


        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String user_level = dataSnapshot.child("Users").child(selfUid).child("level").getValue().toString();
                final String transactionCount_levelOne = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("levelOne").getValue().toString();
                final String transactionCount_levelTwo = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("levelTwo").getValue().toString();
                final String transactionCount_levelThree = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("levelThree").getValue().toString();
                final String transactionCount_levelFour = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("levelFour").getValue().toString();
                final String transactionCount_levelFive = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("levelFive").getValue().toString();
                final String transactionCount_levelSix = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("count").child("levelSix").getValue().toString();


                if (user_level.equals("0")) {
                    forLevelOne();
                }

                if ((user_level.equals("1")) && (transactionCount_levelOne.equals("2"))) {
                    forLevelTwo();


                }
                if ((user_level.equals("2")) && (transactionCount_levelTwo.equals("4"))) {
                    forLevelThree();
                }
                if (user_level.equals("3")) {

                }
                if (user_level.equals("4")) {

                }
                if (user_level.equals("5")) {

                }
                if (user_level.equals("6")) {

                }
                if (user_level.equals("7")) {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void forLevelOne() {

        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //LevelOne
                String level = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                if (level.equals("0")) {
                    String user_bal = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                    int user_bal_Int = Integer.parseInt(user_bal);
                    String p1 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p1").getValue().toString();
                    //String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p2").getValue().toString();
                    //String p3 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p3").getValue().toString();
                    // String p4 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p4").getValue().toString();
                    //String p5 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p5").getValue().toString();
                    //String p6 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p6").getValue().toString();
                    //String p7 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p7").getValue().toString();
                    //String p8 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p8").getValue().toString();


                    if (user_bal_Int >= 50) {

                        dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.progress_dialogue);
                        dialog.setCancelable(false);
                        final TextView mainHeading = (TextView) dialog.findViewById(R.id.mainHeading);
                        final TextView subHeading = (TextView) dialog.findViewById(R.id.subHeading);
                        dialog.show();


                        //reducing money in user wallet

                        String user_updated_bal = Integer.toString(user_bal_Int - 50);

                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(user_updated_bal);

                        //adding money to parent1
                        String p1_bal = dataSnapshot.child("Wallet").child(p1).child("balance").getValue().toString();
                        int p1_bal_Int = Integer.parseInt(p1_bal);

                        String p1_updated_bal = Integer.toString(p1_bal_Int + 50);

                        mWallet.child(p1).child("balance").setValue(p1_updated_bal);
                        subHeading.setText("Transfering money");

                        //AutoLoginCode
                        //String p1_email = dataSnapshot.child("Users").child(p1).child("email").getValue().toString();
                        //String p1_password = dataSnapshot.child("Users").child(p1).child("password").getValue().toString();
                        //String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                        //int i = Integer.parseInt(mEnd);

                        //mLogin.child("user" + i).child("uid").setValue(p1);
                        //mLogin.child("user" + i).child("email").setValue(p1_email);
                        //mLogin.child("user" + i).child("password").setValue(p1_password);

                        //String endCount = Integer.toString(i + 1);
                        //mLogin.child("end").setValue(endCount);


                        //sendTransaction in user

                        String id = UUID.randomUUID().toString();
                        String childid = "PW" + id.substring(0, 5).toUpperCase();
                        String user_userName = dataSnapshot.child("Users").child(selfUid).child("username").getValue().toString();
                        String p1_userName = dataSnapshot.child("Users").child(p1).child("username").getValue().toString();
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p1_userName);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("50");
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(user_userName);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionType").setValue("debited");


                        //receivedTransaction in parent1
                        String p1_tran_count = dataSnapshot.child("Wallet").child(p1).child("Transactions").child("count").child("levelOne").getValue().toString();
                        int p1_tran_count_Int = Integer.parseInt(p1_tran_count);

                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p1_userName);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("50");
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(user_userName);
                        mWallet.child(p1).child("Transactions").child("history").child(childid).child("transactionType").setValue("credited");

                        String updated_p1_tran_count = Integer.toString(p1_tran_count_Int + 1);
                        mWallet.child(p1).child("Transactions").child("count").child("levelOne").setValue(updated_p1_tran_count);


                        //Upgrading level
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("1");
                        subHeading.setText("Sucessfully upgraded to Level One");


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 3000);


                    }

                }
                //LevelOneEnd


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void forLevelTwo() {

        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //LevelTwo
                String level = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                if (level.equals("1")) {
                    String user_bal = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                    int user_bal_Int = Integer.parseInt(user_bal);
                    //String p1 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p1").getValue().toString();
                    String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p2").getValue().toString();
                    //String p3 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p3").getValue().toString();
                    // String p4 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p4").getValue().toString();
                    //String p5 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p5").getValue().toString();
                    //String p6 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p6").getValue().toString();
                    //String p7 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p7").getValue().toString();
                    //String p8 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p8").getValue().toString();


                    if (user_bal_Int >= 100) {

                        dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.progress_dialogue);
                        dialog.setCancelable(false);
                        final TextView mainHeading = (TextView) dialog.findViewById(R.id.mainHeading);
                        final TextView subHeading = (TextView) dialog.findViewById(R.id.subHeading);
                        dialog.show();


                        //reducing money in user wallet


                        String user_updated_bal = Integer.toString(user_bal_Int - 100);

                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(user_updated_bal);

                        //adding money to parent1
                        String p2_bal = dataSnapshot.child("Wallet").child(p2).child("balance").getValue().toString();
                        int p2_bal_Int = Integer.parseInt(p2_bal);

                        String p2_updated_bal = Integer.toString(p2_bal_Int + 100);

                        mWallet.child(p2).child("balance").setValue(p2_updated_bal);
                        subHeading.setText("Transfering money");

                        //AutoLoginCode
                        //String p1_email = dataSnapshot.child("Users").child(p1).child("email").getValue().toString();
                        //String p1_password = dataSnapshot.child("Users").child(p1).child("password").getValue().toString();
                        //String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                        //int i = Integer.parseInt(mEnd);

                        //mLogin.child("user" + i).child("uid").setValue(p1);
                        //mLogin.child("user" + i).child("email").setValue(p1_email);
                        //mLogin.child("user" + i).child("password").setValue(p1_password);

                        //String endCount = Integer.toString(i + 1);
                        //mLogin.child("end").setValue(endCount);


                        //sendTransaction in user

                        String id = UUID.randomUUID().toString();
                        String childid = "PW" + id.substring(0, 5).toUpperCase();
                        String user_userName = dataSnapshot.child("Users").child(selfUid).child("username").getValue().toString();
                        String p2_userName = dataSnapshot.child("Users").child(p2).child("username").getValue().toString();
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p2_userName);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("100");
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(user_userName);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionType").setValue("debited");


                        //receivedTransaction in parent1
                        String p2_tran_count = dataSnapshot.child("Wallet").child(p2).child("Transactions").child("count").child("levelTwo").getValue().toString();
                        int p2_tran_count_Int = Integer.parseInt(p2_tran_count);

                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p2_userName);
                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("100");
                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(user_userName);
                        mWallet.child(p2).child("Transactions").child("history").child(childid).child("transactionType").setValue("credited");

                        String updated_p2_tran_count = Integer.toString(p2_tran_count_Int + 1);
                        mWallet.child(p2).child("Transactions").child("count").child("levelTwo").setValue(updated_p2_tran_count);


                        //Upgrading level
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("2");
                        subHeading.setText("Sucessfully upgraded to Level Two");


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 0);


                    }

                }
                //LevelTwoEnd


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void forLevelThree() {

        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String level = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").getValue().toString();

                outerloop:
                if (level.equals("2")) {
                    String user_bal = dataSnapshot.child("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").getValue().toString();
                    int user_bal_Int = Integer.parseInt(user_bal);

                    //String p1 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p1").getValue().toString();
                    //String p2 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p2").getValue().toString();
                    String p3 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p3").getValue().toString();
                    // String p4 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p4").getValue().toString();
                    //String p5 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p5").getValue().toString();
                    //String p6 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p6").getValue().toString();
                    //String p7 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p7").getValue().toString();
                    //String p8 = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Chain").child("parent").child("p8").getValue().toString();


                    if (user_bal_Int >= 400) {

                        dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.progress_dialogue);
                        dialog.setCancelable(false);
                        final TextView mainHeading = (TextView) dialog.findViewById(R.id.mainHeading);
                        final TextView subHeading = (TextView) dialog.findViewById(R.id.subHeading);
                        dialog.show();


                        //reducing money in user wallet


                        String user_updated_bal = Integer.toString(user_bal_Int - 400);
                        mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(user_updated_bal);


                        //adding money to parent1
                        String p3_bal = dataSnapshot.child("Wallet").child(p3).child("balance").getValue().toString();
                        int p3_bal_Int = Integer.parseInt(p3_bal);

                        String p3_updated_bal = Integer.toString(p3_bal_Int + 400);

                        mWallet.child(p3).child("balance").setValue(p3_updated_bal);
                        subHeading.setText("Transfering money");

                        //AutoLoginCode
                        //String p1_email = dataSnapshot.child("Users").child(p1).child("email").getValue().toString();
                        //String p1_password = dataSnapshot.child("Users").child(p1).child("password").getValue().toString();
                        //String mEnd = dataSnapshot.child("Login").child("end").getValue().toString();
                        //int i = Integer.parseInt(mEnd);

                        //mLogin.child("user" + i).child("uid").setValue(p1);
                        //mLogin.child("user" + i).child("email").setValue(p1_email);
                        //mLogin.child("user" + i).child("password").setValue(p1_password);

                        //String endCount = Integer.toString(i + 1);
                        //mLogin.child("end").setValue(endCount);


                        //sendTransaction in user

                        String id = UUID.randomUUID().toString();
                        String childid = "PW" + id.substring(0, 5).toUpperCase();
                        String user_userName = dataSnapshot.child("Users").child(selfUid).child("username").getValue().toString();
                        String p3_userName = dataSnapshot.child("Users").child(p3).child("username").getValue().toString();
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p3_userName);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("400");
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(user_userName);
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).child("transactionType").setValue("debited");


                        //receivedTransaction in parent1
                        String p3_tran_count = dataSnapshot.child("Wallet").child(p3).child("Transactions").child("count").child("levelThree").getValue().toString();
                        int p3_tran_count_Int = Integer.parseInt(p3_tran_count);

                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transactionTime").setValue(timeformat);
                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transactionDate").setValue(datetime);
                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transferredTo").setValue(p3_userName);
                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transactionAmount").setValue("400");
                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transactionId").setValue(childid);
                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transferredFrom").setValue(user_userName);
                        mWallet.child(p3).child("Transactions").child("history").child(childid).child("transactionType").setValue("credited");

                        String updated_p3_tran_count = Integer.toString(p3_tran_count_Int + 1);
                        mWallet.child(p3).child("Transactions").child("count").child("levelTwo").setValue(updated_p3_tran_count);


                        //Upgrading level
                        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("level").setValue("3");
                        subHeading.setText("Sucessfully upgraded to Level Three");


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 0);


                    }

                }
                //LevelTwoEnd


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 0);
    }


    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        //Toast.makeText(getContext(), slider.getBundle().getString("extra") + "", Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(getContext(), JobsActivity.class);
        //startActivity(intent);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}