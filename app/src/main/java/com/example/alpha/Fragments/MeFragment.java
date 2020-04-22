package com.example.alpha.Fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.example.alpha.ViewHolder.TransactionView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import androidx.annotation.NonNull;
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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseRecyclerAdapter<Transaction_Class, TransactionView> TransactionsAdapter;
    private LinearLayout transactions_linear, no_matches_found;
    private RecyclerView transactionsRecycler;
    private LinearLayoutManager transactionsLinearLayout;
    private MaterialRippleLayout fab;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_me, container, false);

        String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        no_matches_found = mView.findViewById(R.id.no_subjects_found);


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
                    transactions_linear.setVisibility(View.GONE);
                    no_matches_found.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        loadTransactions();

        return mView;

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
                    switch (transType) {
                        case "credited":
                            holder.transactionType.setText("Received from");
                            holder.transactionImage.setImageResource(R.drawable.ic_arrow_down_black);
                            holder.transactionStatus.setText("Credited");
                            holder.transactionImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.green_700));
                            holder.transactionName.setText(model.getTransferredFrom());
                            holder.transactionStatus.setTextColor(getResources().getColor(R.color.green_500));
                            holder.transactionAmount.setTextColor(getResources().getColor(R.color.green_500));

                            break;
                        case "debited":
                            holder.transactionType.setText("Paid To");
                            holder.transactionImage.setImageResource(R.drawable.ic_arrow_up_black);
                            holder.transactionStatus.setText("Debited");
                            holder.transactionImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryPink));
                            holder.transactionName.setText(model.getTransferredTo());
                            holder.transactionStatus.setTextColor(getResources().getColor(R.color.colorPrimaryPink));
                            holder.transactionAmount.setTextColor(getResources().getColor(R.color.colorPrimaryPink));


                            break;
                        case "added":
                            holder.transactionType.setText("Added To");
                            holder.transactionImage.setImageResource(R.drawable.ic_wallet);
                            holder.transactionImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.blue_600));
                            holder.transactionStatus.setText("Added");
                            holder.transactionName.setText("Wallet");
                            holder.transactionStatus.setTextColor(getResources().getColor(R.color.green_500));
                            holder.transactionAmount.setTextColor(getResources().getColor(R.color.green_500));
                            break;
                    }
                }


                holder.transactionLayout.setOnClickListener(v -> {


                    final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
                    dialog.setContentView(R.layout.fragment_success_request);

                    fab = dialog.findViewById(R.id.bt_action);

                    transactionDate = dialog.findViewById(R.id.success_date);
                    transactionTime = dialog.findViewById(R.id.success_time);
                    transferredFrom = dialog.findViewById(R.id.success_name);
                    transactionAmount = dialog.findViewById(R.id.success_money);
                    transactionType = dialog.findViewById(R.id.success_type);
                    transactionStatus = dialog.findViewById(R.id.status);
                    transactionId = dialog.findViewById(R.id.txnorderid);


                    String transType1 = model.getTransactionType();
                    {
                        switch (transType1) {
                            case "credited":
                                transactionDate.setText(model.getTransactionDate());
                                transactionTime.setText(model.getTransactionTime());
                                transactionAmount.setText(model.getTransactionAmount());
                                transferredFrom.setText(model.getTransferredFrom());
                                transactionType.setText("RECEIVED FROM :  ");
                                transactionStatus.setText("CREDITED");
                                transactionId.setText(model.getTransactionId());
                                transactionStatus.setTextColor(getResources().getColor(R.color.green_500));

                                break;
                            case "debited":
                                transactionDate.setText(model.getTransactionDate());
                                transactionTime.setText(model.getTransactionTime());
                                transactionAmount.setText(model.getTransactionAmount());
                                transferredFrom.setText(model.getTransferredTo());
                                transactionType.setText("TRANSFERED TO :  ");
                                transactionStatus.setText("DEBITED");
                                transactionId.setText(model.getTransactionId());
                                transactionStatus.setTextColor(getResources().getColor(R.color.red_500));


                                break;
                            case "added":
                                transactionDate.setText(model.getTransactionDate());
                                transactionTime.setText(model.getTransactionTime());
                                transactionAmount.setText(model.getTransactionAmount());
                                transferredFrom.setText("Wallet");
                                transactionType.setText("Added to:  ");
                                transactionStatus.setText("ADDED");
                                transactionId.setText(model.getTransactionId());
                                transactionStatus.setTextColor(getResources().getColor(R.color.green_500));

                                break;
                        }
                    }


                    dialog.show();


                    fab.setOnClickListener(v1 -> dialog.dismiss());


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

    @Override
    public void onStart() {
        super.onStart();
    }


}
