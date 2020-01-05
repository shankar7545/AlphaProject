package com.example.alpha.Wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Activity.HomeActivity;
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

public class walletActivity extends AppCompatActivity {
    public Button buy_dreamcoins,redeem_dreamcoins,view_transactions;
    public TextView total_balance;
    DatabaseReference u_walletdb;
    Toolbar toolbar;
    DecimalFormat myFormatter = new DecimalFormat("#,##,###");
    TextView received_balanceT,withdraw_balanceT;
    ProgressBar progress_total,rec_prog,with_prog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        toolbar=(Toolbar)findViewById(R.id.dreamcoinWallet);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Wallet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progress_total=(ProgressBar)findViewById(R.id.progress_wallet);
        progress_total.setVisibility(View.VISIBLE);
        view_transactions=(Button)findViewById(R.id.view_transactions);
        total_balance=(TextView)findViewById(R.id.walletBalance);
        received_balanceT=(TextView)findViewById(R.id.received_balance);
        withdraw_balanceT=(TextView)findViewById(R.id.withdrawable_balance);
        rec_prog=(ProgressBar)findViewById(R.id.progress_depo);
        with_prog=(ProgressBar)findViewById(R.id.progress_winning);
        rec_prog.setVisibility(View.VISIBLE);
        with_prog.setVisibility(View.VISIBLE);
        total_balance.setVisibility(View.GONE);
        received_balanceT.setVisibility(View.GONE);
        withdraw_balanceT.setVisibility(View.GONE);
        u_walletdb= FirebaseDatabase.getInstance().getReference("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        loadU_WalletCoins();


        view_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewtransActivity=new Intent(walletActivity.this, TransactionsActivity.class);
                viewtransActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewtransActivity);
            }
        });

    }

    private void loadU_WalletCoins() {
        u_walletdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot udataSnapshot) {
                try
                {
                    String receiedBalance = udataSnapshot.child("balance").getValue().toString();
                    String withdrawableBalance = udataSnapshot.child("withdrawable").getValue().toString();

                    received_balanceT.setText(receiedBalance);
                    withdraw_balanceT.setText(withdrawableBalance);
                    received_balanceT.setVisibility(View.VISIBLE);
                    withdraw_balanceT.setVisibility(View.VISIBLE);
                    rec_prog.setVisibility(View.GONE);
                    with_prog.setVisibility(View.GONE);

                    //Total balance

                    int a = Integer.parseInt(receiedBalance);
                    int b = Integer.parseInt(withdrawableBalance);
                    int c = a+b;

                    String bal = Integer.toString(c);


                    total_balance.setText(bal);
                    total_balance.setVisibility(View.VISIBLE);
                    progress_total.setVisibility(View.GONE);




                }
                catch (Exception e)
                {
                    Toast.makeText(walletActivity.this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
       Intent gotohome=new Intent(walletActivity.this, HomeActivity.class);
       gotohome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(gotohome);
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
