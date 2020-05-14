package com.example.alpha.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.alpha.Activity.RazorpaySection;
import com.example.alpha.Levels.beginnerActivity;
import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class PaytmPayment extends AppCompatActivity {
    private static long back_pressed;
    public EditText mAmount;
    LinearLayout pay50, SuccessLayout;
    AppCompatButton paytm, skip;
    DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mLevel, dbPaytm, mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_payment);


        SuccessLayout = findViewById(R.id.payment_success);

        paytm = findViewById(R.id.paytm);
        skip = findViewById(R.id.skip);

        mAmount = findViewById(R.id.amount);
        dbPaytm = FirebaseDatabase.getInstance().getReference("Paytm");

        paytm.setOnClickListener(view -> dbPaytm.child("01").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    final PaytmKey paytmKey = dataSnapshot.getValue(PaytmKey.class);
                    try {
                        final String mUserName = mAmount.getText().toString().trim();
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

        skip.setOnClickListener(v -> {
            onBackPressed();
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String paymentStatus = dataSnapshot.child("paymentStatus").getValue().toString();

                if (paymentStatus.equals("true")) {

                    SuccessLayout.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(() -> {

                        Intent intent = new Intent(PaytmPayment.this, beginnerActivity.class);
                        startActivity(intent);
                        finish();
                    }, 1000);


                } else {
                    // Toast.makeText(PaytmPayment.this, "Payment Incomplete", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Close Payment?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", null)
                .show();

    }


}
