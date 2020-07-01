package com.example.alpha.Wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Activity.HelpActivity;
import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class walletActivity extends AppCompatActivity {
    public Button buy_dreamcoins, redeem_dreamcoins;
    LinearLayout levelWiseLayout, withdrawLayout;
    TextView transactionLayout;
    public TextView total_balance;
    DatabaseReference u_walletdb, dbPaytm;
    Toolbar toolbar;
    DecimalFormat myFormatter = new DecimalFormat("#,##,###");
    TextView received_balanceT, withdraw_balanceT;
    ProgressBar progress_total, rec_prog, with_prog;
    public static final int DIALOG_QUEST_CODE = 300;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Wallet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progress_total = findViewById(R.id.progress_wallet);
        progress_total.setVisibility(View.VISIBLE);
        transactionLayout = findViewById(R.id.transactionLayout);
        levelWiseLayout = findViewById(R.id.levelWiseLayout);
        withdrawLayout = findViewById(R.id.withdrawLayout);
        received_balanceT = findViewById(R.id.received_balance);
        withdraw_balanceT = findViewById(R.id.withdrawable_balance);
        rec_prog = findViewById(R.id.progress_depo);
        with_prog = findViewById(R.id.progress_winning);
        rec_prog.setVisibility(View.VISIBLE);
        with_prog.setVisibility(View.VISIBLE);
        total_balance = findViewById(R.id.walletBalance);
        total_balance.setVisibility(View.GONE);
        received_balanceT.setVisibility(View.GONE);
        withdraw_balanceT.setVisibility(View.GONE);
        u_walletdb = FirebaseDatabase.getInstance().getReference("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        //addMoney();

        loadU_WalletCoins();
        onClick();
    }


    private void onClick() {

        transactionLayout.setOnClickListener(v -> {
            Intent viewtransActivity = new Intent(walletActivity.this, TransactionsActivity.class);
            viewtransActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(viewtransActivity);
        });


        withdrawLayout.setOnClickListener(v -> showDialogWithdraw());

        levelWiseLayout.setOnClickListener(v -> showLevelwiseDialog());

    }


    /*private void addMoney() {
        dbPaytm = FirebaseDatabase.getInstance().getReference("Paytm");
        addMoneyLayout.setOnClickListener(view -> dbPaytm.child("01").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    final PaytmKey paytmKey = dataSnapshot.getValue(PaytmKey.class);
                    try {
                        final String mgateway = dataSnapshot.child("gateway").getValue().toString();

                        if (mgateway.equals("paytm")) {
                            Intent i = new Intent(getApplicationContext(), ConfirmAmount.class);

                            Bundle bundle = new Bundle();
                            i.putExtra("MID", paytmKey.getPaytmkey());
                            i.putExtra("Amount", "1");

                            i.putExtras(bundle);
                            startActivity(i);
                        }

                        if (mgateway.equals("razorpay")) {
                            Intent i = new Intent(getApplicationContext(), RazorpaySection.class);

                            Bundle bundle = new Bundle();
                            i.putExtra("MID", paytmKey.getPaytmkey());
                            i.putExtra("Amount", "1");

                            i.putExtras(bundle);
                            startActivity(i);
                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));

    } */

    private void loadU_WalletCoins() {
        u_walletdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot udataSnapshot) {
                try {
                    String mMainBalance = udataSnapshot.child("Balance").child("mainBalance").getValue().toString();
                    String withdrawableBalance = udataSnapshot.child("Balance").child("withdrawable").getValue().toString();

                    received_balanceT.setText(mMainBalance);
                    withdraw_balanceT.setText(withdrawableBalance);
                    received_balanceT.setVisibility(View.VISIBLE);
                    withdraw_balanceT.setVisibility(View.VISIBLE);
                    rec_prog.setVisibility(View.GONE);
                    with_prog.setVisibility(View.GONE);

                    //Total balance

                    int a = Integer.parseInt(mMainBalance);
                    int b = Integer.parseInt(withdrawableBalance);
                    int c = a + b;

                    String bal = Integer.toString(c);


                    total_balance.setText(bal);
                    total_balance.setVisibility(View.VISIBLE);
                    progress_total.setVisibility(View.GONE);


                } catch (Exception e) {
                    Toast.makeText(walletActivity.this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void showDialogWithdraw() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogPaymentFragment newFragment = new DialogPaymentFragment();
        newFragment.setRequestCode(DIALOG_QUEST_CODE);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }


    private void showLevelwiseDialog() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogLevelwiseFragment newFragment = new DialogLevelwiseFragment();
        newFragment.setRequestCode(DIALOG_QUEST_CODE);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();


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
            Intent intent = new Intent(walletActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
