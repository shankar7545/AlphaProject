package com.example.alpha.Wallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionsActivity extends AppCompatActivity {

    public ProgressBar progressBar;
    public RecyclerView transactionsRecycler;
    public LinearLayoutManager transactionsLinearLayout;
    Toolbar solotoolbar;
    DatabaseReference mRef, mTransactions, mStaff;
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
                    if (transType.equals("credited")) {
                        holder.transactionType.setText("Received from");
                        holder.transactionImage.setImageResource(R.drawable.transaction_received);
                        holder.transactionStatus.setText("Credited");
                        holder.transactionName.setText(model.getTransferredFrom());
                        holder.transactionStatus.setTextColor(getResources().getColor(R.color.green_500));
                        holder.transactionAmount.setTextColor(getResources().getColor(R.color.green_500));

                    } else if (transType.equals("debited")) {
                        holder.transactionType.setText("Paid To");
                        holder.transactionImage.setImageResource(R.drawable.transaction_send);
                        holder.transactionStatus.setText("Debited");
                        holder.transactionName.setText(model.getTransferredTo());
                        holder.transactionStatus.setTextColor(getResources().getColor(R.color.red_500));
                        holder.transactionAmount.setTextColor(getResources().getColor(R.color.red_500));


                    }
                }


                progressBarLayout.setVisibility(View.GONE);


                holder.transactionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final Dialog dialog = new Dialog(TransactionsActivity.this);
                        dialog.setContentView(R.layout.fragment_success_request);

                        fab = dialog.findViewById(R.id.bt_action);

                        transactionDate = dialog.findViewById(R.id.success_date);
                        transactionTime = dialog.findViewById(R.id.success_time);
                        transferredFrom = dialog.findViewById(R.id.success_name);
                        transactionAmount = dialog.findViewById(R.id.success_money);
                        transactionType = dialog.findViewById(R.id.success_type);
                        transactionStatus = dialog.findViewById(R.id.status);
                        transactionId = dialog.findViewById(R.id.txnorderid);


                        String transType = model.getTransactionType();
                        {
                            if (transType.equals("credited")) {
                                transactionDate.setText(model.getTransactionDate());
                                transactionTime.setText(model.getTransactionTime());
                                transactionAmount.setText(model.getTransactionAmount());
                                transferredFrom.setText(model.getTransferredFrom());
                                transactionType.setText("RECEIVED FROM :  ");
                                transactionStatus.setText("CREDITED");
                                transactionId.setText(model.getTransactionId());
                                transactionStatus.setTextColor(getResources().getColor(R.color.green_500));

                            } else if (transType.equals("debited")) {
                                transactionDate.setText(model.getTransactionDate());
                                transactionTime.setText(model.getTransactionTime());
                                transactionAmount.setText(model.getTransactionAmount());
                                transferredFrom.setText(model.getTransferredTo());
                                transactionType.setText("TRANSFERED TO :  ");
                                transactionStatus.setText("DEBITED");
                                transactionId.setText(model.getTransactionId());
                                transactionStatus.setTextColor(getResources().getColor(R.color.red_500));


                            }
                        }


                        dialog.show();


                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                            }
                        });


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}