package com.example.alpha.Wallet;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alpha.Activity.HelpActivity;
import com.example.alpha.Activity.SupportActivity;
import com.example.alpha.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class TransactionExtender extends AppCompatActivity {

    LinearLayout copyTransactionID;
    TextView txnorderid;
    TextView transactionDate, transactionTime, transferredFrom, transactionAmount, transactionType, transactionStatus, transactionId;

    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_extender);


        parent_view = findViewById(android.R.id.content);
        initToolbar();
        initComponent();
    }

    private void initComponent() {
        copyTransactionID = findViewById(R.id.copyTransactionID);

        findViewById(R.id.contactLayout).setOnClickListener(v -> startActivity(new Intent(this, SupportActivity.class)));


        transactionId = findViewById(R.id.txnorderid);
        transactionDate = findViewById(R.id.dateToolbar);
        transferredFrom = findViewById(R.id.success_name);
        transactionAmount = findViewById(R.id.success_money);
        transactionType = findViewById(R.id.success_type);
        transactionStatus = findViewById(R.id.status);


        Bundle bundle = getIntent().getExtras();

        final String transactionDateS = bundle.getString("transactionDate");
        final String transactionTimeS = bundle.getString("transactionTime");
        final String transactionAmountS = bundle.getString("transactionAmount");
        final String transferredFromS = bundle.getString("transferredFrom");
        final String transferredToS = bundle.getString("transferredTo");
        final String transactionIdS = bundle.getString("transactionId");
        final String transactionTypeS = bundle.getString("transactionType");

        assert transactionTypeS != null;
        switch (transactionTypeS) {
            case "credited":
                transactionDate.setText(transactionTimeS + "  on  " + transactionDateS);
                transactionAmount.setText(transactionAmountS);
                transferredFrom.setText(transferredFromS);
                transactionType.setText("Received From :  ");
                //transactionStatus.setText("CREDITED");
                transactionId.setText(transactionIdS);
                //transactionStatus.setTextColor(getResources().getColor(R.color.green_500));

                break;
            case "debited":
                transactionDate.setText(transactionTimeS + "  on  " + transactionDateS);
                transactionAmount.setText(transactionAmountS);
                transferredFrom.setText(transferredToS);
                transactionType.setText("Paid To :  ");
                //transactionStatus.setText("DEBITED");
                transactionId.setText(transactionIdS);
                //transactionStatus.setTextColor(getResources().getColor(R.color.colorPrimaryPink));


                break;
            case "added":
                transactionDate.setText(transactionTimeS + "  on  " + transactionDateS);
                transactionAmount.setText(transactionAmountS);
                transferredFrom.setText("Wallet");
                transactionType.setText("Added to :  ");
                //transactionStatus.setText("ADDED");
                transactionId.setText(transactionIdS);
                //transactionStatus.setTextColor(getResources().getColor(R.color.green_500));

                break;
        }


        copyTransactionID.setOnClickListener(v -> {
            ClipboardManager clipman = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            String text = transactionId.getText().toString();
            Objects.requireNonNull(clipman).setText(text);

            Snackbar snackbar = Snackbar.make(parent_view, "Transaction ID Copied", Snackbar.LENGTH_SHORT)
                    .setAction("Okay", view -> {
                    });
            snackbar.show();
        });


    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        statusBarColor();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void statusBarColor() {

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green_700));
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
            Intent intent = new Intent(TransactionExtender.this, HelpActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
