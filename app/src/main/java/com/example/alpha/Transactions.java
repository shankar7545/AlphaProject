package com.example.alpha;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Transactions extends AppCompatActivity {
    public RecyclerView transactionRecycler, withdrawRecycler;
    public LinearLayoutManager transactionLayout, withdrawLinear;
    FirebaseRecyclerAdapter<TransactionHistory_Class, TransactionViewHolder> trasactionAdapter;

    DatabaseReference transactionHistory, withdrawhistory;
    LinearLayout no_transaction_found;
    LinearLayout wallet_layout, withdraw_layout;
    DecimalFormat myFormatter = new DecimalFormat("#,##,###");
    Toolbar tr_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        tr_toolbar=(Toolbar)findViewById(R.id.transaction_toolbar);
        setSupportActionBar(tr_toolbar);
        getSupportActionBar().setTitle("Transactions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        transactionRecycler = (RecyclerView)findViewById(R.id.transactionRecycler);
        withdrawRecycler = (RecyclerView)findViewById(R.id.withdrawRecycler);
        withdrawRecycler.hasFixedSize();
        withdrawLinear = new LinearLayoutManager(this);
        withdrawRecycler.setLayoutManager(withdrawLinear);
        withdrawLinear.setReverseLayout(true);
        withdrawLinear.setStackFromEnd(true);
        withdrawLinear.setSmoothScrollbarEnabled(true);
        transactionRecycler.hasFixedSize();
        transactionLayout = new LinearLayoutManager(this);
        transactionLayout.setReverseLayout(true);
        transactionLayout.setStackFromEnd(true);
        transactionLayout.setSmoothScrollbarEnabled(true);
        transactionRecycler.setLayoutManager(transactionLayout);
        no_transaction_found = (LinearLayout)findViewById(R.id.no_transaction_found);
        wallet_layout = (LinearLayout)findViewById(R.id.wallet_linear);
        withdraw_layout = (LinearLayout)findViewById(R.id.withdraw_linear);
        transactionHistory = FirebaseDatabase.getInstance().getReference("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        transactionHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("TransactionsList").exists()) {
                    wallet_layout.setVisibility(View.VISIBLE);
                    withdraw_layout.setVisibility(View.GONE);
                    no_transaction_found.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        loadTransaction();
    }
    private void loadTransaction() {
        Query transactionList = transactionHistory.child("history").orderByChild("transactionDate");
        FirebaseRecyclerOptions<TransactionHistory_Class> options = new FirebaseRecyclerOptions.Builder<TransactionHistory_Class>()
                .setQuery(transactionList, TransactionHistory_Class.class)
                .build();
        trasactionAdapter = new FirebaseRecyclerAdapter<TransactionHistory_Class, TransactionViewHolder>(options) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onBindViewHolder(@NonNull TransactionViewHolder holder, int position, @NonNull TransactionHistory_Class model) {
                int amount=Integer.parseInt(model.getTransactionamount());
                holder.transactionAmount.setText(String.valueOf(myFormatter.format(amount)));
                holder.transactiontime.setText(model.getTransactionTime());
                holder.transactiondate.setText(model.getTransactionDate());
                holder.transactionAmount.setTextColor(getResources().getColor(R.color.green_400));
                holder.transaction_status.setText("Added");
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {



                    }
                });


                try
                {
                    if (model.getTransactionStatus().isEmpty())
                    {
                        holder.transaction_status.setText("Added");
                        holder.transactionAmount.setTextColor(getResources().getColor(R.color.green_400));
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }





            }

            @NonNull
            @Override
            public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.transaction_history_layout, viewGroup, false);
                return new TransactionViewHolder(itemView);
            }
        };
        transactionRecycler.setAdapter(trasactionAdapter);
        trasactionAdapter.startListening();
    }

}
