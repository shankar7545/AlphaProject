package com.example.alpha.Wallet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        solotoolbar = (Toolbar) findViewById(R.id.subjecttoolbar);
        setSupportActionBar(solotoolbar);


        getSupportActionBar().setTitle("Transactions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBarLayout = (LinearLayout) findViewById(R.id.progress_bar1);

        transactions_linear = (LinearLayout) findViewById(R.id.transactions_linear);

        transactionsRecycler = findViewById(R.id.transactionsRecycler);

        transactionsRecycler.hasFixedSize();


        transactionsLinearLayout = new LinearLayoutManager(this);

        no_matches_found = (LinearLayout) findViewById(R.id.no_subjects_found);

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
                      /*  String BranchName=getIntent().getStringExtra("branchname");
                        toolBar=getIntent().getStringExtra("toolbar");
                        yearName=getIntent().getStringExtra("yearName");
                        year=getIntent().getStringExtra("year");

                        Intent sendto_single=new Intent(ActivitySubjects.this, ActivityUnits1.class);
                        sendto_single.putExtra("branchname",BranchName);
                        sendto_single.putExtra("yearName",yearName);
                        sendto_single.putExtra("year",year);
                        sendto_single.putExtra("subjectname",model.getSubjectName());
                        sendto_single.putExtra("sem","firstSem");
                        startActivity(sendto_single); */


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