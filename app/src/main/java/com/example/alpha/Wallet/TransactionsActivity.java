package com.example.alpha.Wallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.alpha.Activity.HelpActivity;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionsActivity extends AppCompatActivity {

    public ProgressBar progressBar;
    public RecyclerView transactionsRecycler;
    public LinearLayoutManager transactionsLinearLayout;
    Toolbar solotoolbar;
    DatabaseReference mRef, mTransactions, mWallet;
    FirebaseRecyclerAdapter<Transaction_Class, TransactionView> TransactionsAdapter;
    LinearLayout transactions_linear, progressBarLayout, no_matches_found;
    TextView transactionDate, transactionTime, transferredFrom, transactionAmount, transactionType, transactionStatus, transactionId;
    MaterialRippleLayout fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        solotoolbar = findViewById(R.id.subjecttoolbar);
        setSupportActionBar(solotoolbar);
        no_matches_found = findViewById(R.id.no_subjects_found);


        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");
        getSupportActionBar().setTitle("Transactions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBarLayout = findViewById(R.id.progress_bar1);

        transactions_linear = findViewById(R.id.transactions_linear);

        transactionsRecycler = findViewById(R.id.transactionsRecycler);

        transactionsRecycler.hasFixedSize();


        transactionsLinearLayout = new LinearLayoutManager(this);
        transactionsLinearLayout.setReverseLayout(true);
        transactionsLinearLayout.setStackFromEnd(true);


        transactionsRecycler.setLayoutManager(transactionsLinearLayout);


        mTransactions = FirebaseDatabase.getInstance().getReference("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        mTransactions.child("Transactions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("history").exists()) {

                    progressBarLayout.setVisibility(View.GONE);
                    transactions_linear.setVisibility(View.VISIBLE);
                    no_matches_found.setVisibility(View.GONE);

                } else {
                    progressBarLayout.setVisibility(View.GONE);
                    transactions_linear.setVisibility(View.GONE);
                    no_matches_found.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        loadTransactions();


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
                            holder.transactionType.setText("Received From");
                            holder.transactionImage.setImageResource(R.drawable.ic_arrow_down_black);
                            holder.transactionStatus.setText("Credited");
                            holder.transactionImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.indigo_700));
                            holder.transactionName.setText(model.getTransferredFrom());
                            holder.transactionStatus.setTextColor(getResources().getColor(R.color.black));
                            holder.transactionAmount.setTextColor(getResources().getColor(R.color.black));

                            break;
                        case "debited":
                            holder.transactionType.setText("Paid To");
                            holder.transactionImage.setImageResource(R.drawable.ic_arrow_up_black);
                            holder.transactionStatus.setText("Debited");
                            holder.transactionImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red_600));
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

                    Intent i = new Intent(getApplicationContext(), TransactionExtender.class);

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

                    /*final Dialog dialog = new Dialog(Objects.requireNonNull(TransactionsActivity.this));
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
                                transactionStatus.setTextColor(getResources().getColor(R.color.colorPrimaryPink));


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


                    fab.setOnClickListener(v1 -> dialog.dismiss()); */


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
            Intent intent = new Intent(TransactionsActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}