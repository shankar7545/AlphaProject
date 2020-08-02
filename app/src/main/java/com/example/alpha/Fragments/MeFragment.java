package com.example.alpha.Fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.example.alpha.ViewHolder.TransactionView;
import com.example.alpha.Wallet.TransactionExtender;
import com.example.alpha.Wallet.TransactionFilterActivity;
import com.example.alpha.Wallet.TransactionsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MeFragment extends Fragment {
    public FirebaseAuth mAuth, mAuthListener;
    private TextView transactionDate, transactionTime, transferredFrom, transactionAmount, transactionType, transactionStatus, transactionId;
    TextView profileName, wallet_bal, recieved, withdrawable, email;
    private View mView;
    private DatabaseReference mRef, mTransactions, mTransactionsRecycler;
    private FirebaseRecyclerAdapter<Transaction_Class, TransactionView> TransactionsAdapter;
    private FirebaseRecyclerAdapter<Transaction_Class, TransactionView> dialogAdapter;
    private LinearLayout transactions_linear, no_matches_found;
    private RecyclerView transactionsRecycler;
    private LinearLayoutManager transactionsLinearLayout;
    private MaterialRippleLayout fab;
    ProgressDialog bar;
    ProgressBar progressBar;
    private Button dateFilterButton, levelFilterButton, typeFilterButton;

    private static final String[] TRANSACTION_DATE = new String[]{
            "13-May-2020", "15-May-2020", "19-May-2020", "21-May-2020", "1-June-2020", "23-June-2020"
    };


    private static final String[] TRANSACTION_LEVEL = new String[]{
            "beginner", "level1", "level2", "level3", "level4",
    };

    private static final String[] TRANSACTION_TYPE = new String[]{
            "added", "credited", "debited"
    };

    public ArrayList<String> ar = new ArrayList<String>();
    public String[] str = new String[2];


    public MeFragment() {
        // Required empty public constructor
    }


    public static Fragment newInstance() {
        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_me, container, false);

        String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        no_matches_found = mView.findViewById(R.id.no_subjects_found);


        dateFilterButton = mView.findViewById(R.id.dateFilterButton);
        levelFilterButton = mView.findViewById(R.id.levelFilterButton);
        typeFilterButton = mView.findViewById(R.id.typeFilterButton);
        progressBar = mView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet")
                .child(selfUid).child("Transactions");



        //Recycler For Transactions
        transactions_linear = mView.findViewById(R.id.transactions_linear);
        transactionsRecycler = mView.findViewById(R.id.transactionsRecycler);
        transactionsRecycler.hasFixedSize();


        transactionsLinearLayout = new LinearLayoutManager(getContext());
        transactionsLinearLayout.setReverseLayout(true);
        transactionsLinearLayout.setStackFromEnd(true);


        transactionsRecycler.setLayoutManager(transactionsLinearLayout);


        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        mTransactions.child("Transactions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("history").exists()) {

                    transactions_linear.setVisibility(View.VISIBLE);
                    no_matches_found.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.GONE);
                    transactions_linear.setVisibility(View.GONE);
                    no_matches_found.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /*bar = new ProgressDialog(getContext());
        bar.setCancelable(false);
        bar.setMessage("Loading Transactions...");
        bar.setIndeterminate(true);
        bar.setCanceledOnTouchOutside(false);
        bar.show();*/




        loadTransactions();

        return mView;

    }
    private void loadTransactions() {
        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("history");

        Query withdrawList = mTransactions.orderByChild("position").limitToLast(10);
        FirebaseRecyclerOptions<Transaction_Class> withdrawOption = new FirebaseRecyclerOptions.Builder<Transaction_Class>()
                .setQuery(withdrawList, Transaction_Class.class)
                .build();
        TransactionsAdapter = new FirebaseRecyclerAdapter<Transaction_Class, TransactionView>(withdrawOption) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onBindViewHolder(@NonNull TransactionView holder, final int position, @NonNull final Transaction_Class model) {
                transactions_linear.setVisibility(View.VISIBLE);
                holder.transactionAmount.setText(model.getTransactionAmount());
                holder.transactionDate.setText(model.getTransactionDate());
                holder.transactionTime.setText(model.getTransactionTime());


                mView.findViewById(R.id.loadMoreButton).setVisibility(View.VISIBLE);
                mView.findViewById(R.id.loadMoreButton).setOnClickListener(v -> startActivity(new Intent(getContext(), TransactionsActivity.class)));

                String transType = model.getTransactionType();
                {
                    switch (transType) {
                        case "credited":
                            holder.transactionType.setText("Received From");
                            holder.transactionImage.setImageResource(R.drawable.ic_arrow_down_black);
                            holder.transactionStatus.setText("Credited");
                            holder.transactionImage.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.indigo_600));
                            holder.transactionName.setText(model.getTransferredFrom());
                            holder.transactionStatus.setTextColor(getResources().getColor(R.color.black));
                            holder.transactionAmount.setTextColor(getResources().getColor(R.color.black));

                            break;
                        case "debited":
                            holder.transactionType.setText("Paid To");
                            holder.transactionImage.setImageResource(R.drawable.ic_arrow_up_black);
                            holder.transactionStatus.setText("Debited");
                            holder.transactionImage.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.indigo_600));
                            holder.transactionName.setText(model.getTransferredTo());
                            holder.transactionStatus.setTextColor(getResources().getColor(R.color.black));
                            holder.transactionAmount.setTextColor(getResources().getColor(R.color.black));


                            break;
                        case "added":
                            holder.transactionType.setText("Added To");
                            holder.transactionImage.setImageResource(R.drawable.ic_wallet_transaction);
                            holder.transactionStatus.setText("Added");
                            holder.transactionName.setText("Wallet");
                            holder.transactionStatus.setTextColor(getResources().getColor(R.color.black));
                            holder.transactionAmount.setTextColor(getResources().getColor(R.color.black));
                            break;
                    }
                }


                holder.transactionLayout.setOnClickListener(v -> {


                    Intent i = new Intent(getContext(), TransactionExtender.class);

                    Bundle bundle = new Bundle();
                    i.putExtra("transactionDate", model.getTransactionDate());
                    i.putExtra("transactionTime", model.getTransactionTime());
                    i.putExtra("transactionAmount", model.getTransactionAmount());
                    i.putExtra("transferredFrom", model.getTransferredFrom());
                    i.putExtra("transferredTo", model.getTransferredTo());
                    i.putExtra("transactionId", model.getTransactionId());
                    i.putExtra("transactionType", model.getTransactionType());

                    i.putExtras(bundle);
                    startActivity(i);

                });

                progressBar.setVisibility(View.GONE);

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

    @Override
    public void onStart() {
        super.onStart();
    }

    private String single_choice_selected;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void filterDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Date");

        View v = getLayoutInflater().inflate(R.layout.layout_transaction_recycler, null);

        RecyclerView dialogRecycler = v.findViewById(R.id.transactionsDialogRecycler);
        LinearLayoutManager dialogLinearLayout = new LinearLayoutManager(builder.getContext());
        dialogRecycler.setLayoutManager(dialogLinearLayout);
        builder.setNegativeButton(R.string.CANCEL, null);
        builder.setView(v);

        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Transactions").child("history");

        Query withdrawList = mTransactions.orderByChild("position");
        FirebaseRecyclerOptions<Transaction_Class> withdrawOption = new FirebaseRecyclerOptions.Builder<Transaction_Class>()
                .setQuery(withdrawList, Transaction_Class.class)
                .build();
        dialogAdapter = new FirebaseRecyclerAdapter<Transaction_Class, TransactionView>(withdrawOption) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onBindViewHolder(@NonNull TransactionView holder, final int position, @NonNull final Transaction_Class model) {


                holder.filterText.setText(model.getTransactionDate());

                holder.filterLayout.setOnClickListener(v1 -> {

                    Intent intent = new Intent(getContext(), TransactionFilterActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("filterWord", model.getTransactionDate());
                    bundle.putString("filterType", "transactionDate");
                    intent.putExtras(bundle);
                    startActivity(intent);
                });


            }

            @NonNull
            @Override
            public TransactionView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_transaction_filter_selector, viewGroup, false);
                return new TransactionView(itemView);
            }
        };
        dialogRecycler.setAdapter(dialogAdapter);
        dialogAdapter.startListening();

        builder.show();

    }



}
