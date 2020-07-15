package com.example.alpha.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.example.alpha.Activity.RazorpaySection;
import com.example.alpha.Plan.ReferCodeActivity;
import com.example.alpha.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class PaytmPayment extends AppCompatActivity {
    private static long back_pressed;
    LinearLayout pay50;
    LinearLayout completePaymentLayout;
    DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mLevel, dbPaytm, mLogin;
    Toolbar toolbar;
    ProgressDialog bar;


    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_payment);

        initComponent();
        //initToolbar();

        statusBarColor();

        PaytmPayment.this.bar = new ProgressDialog(PaytmPayment.this, R.style.MyAlertDialogStyle);
        PaytmPayment.this.bar.setCancelable(false);
        PaytmPayment.this.bar.setIndeterminate(true);
        PaytmPayment.this.bar.setCanceledOnTouchOutside(false);


        //BottomSheet
        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

    }

    private void initComponent() {

        completePaymentLayout = findViewById(R.id.completePaymentLayout);

        dbPaytm = FirebaseDatabase.getInstance().getReference("Paytm");

        completePaymentLayout.setOnClickListener(v -> showBottomSheetDialog());
       /* completePaymentLayout.setOnClickListener(view -> dbPaytm.child("01").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    final PaytmKey paytmKey = dataSnapshot.getValue(PaytmKey.class);
                    try {
                        final String mgateway = dataSnapshot.child("gateway").getValue().toString();

                        if (mgateway.equals("paytm")) {
                            Intent i = new Intent(getApplicationContext(), ConfirmAmount.class);

                            Bundle bundle = new Bundle();
                            assert paytmKey != null;
                            i.putExtra("MID", paytmKey.getPaytmkey());
                            i.putExtra("Amount", "1");
                            i.putExtras(bundle);
                            startActivity(i);
                        }

                        if (mgateway.equals("razorpay")) {
                            PaytmPayment.this.bar.setMessage("Loading Razorpay ...");
                            PaytmPayment.this.bar.show();
                            new Handler().postDelayed(() -> PaytmPayment.this.bar.dismiss(), 2000);
                            Intent i = new Intent(getApplicationContext(), RazorpaySection.class);

                            Bundle bundle = new Bundle();
                            assert paytmKey != null;
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
        })); */

    }

    private void initToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Complete Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();

        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child("paymentStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String paymentStatus = Objects.requireNonNull(dataSnapshot.getValue()).toString();

                if (paymentStatus.equals("true")) {

                    startActivity(new Intent(PaytmPayment.this, ReferCodeActivity.class));
                    finish();

                    //Toast.makeText(PaytmPayment.this, "Payment "+paymentStatus, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void statusBarColor() {

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }


    private void showBottomSheetDialog() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.sheet_info, null);



        view.findViewById(R.id.paytm).setOnClickListener(v -> {
            //mBottomSheetDialog.dismiss();

            //to disable doubleclick
            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            PaytmPayment.this.bar.setMessage("Loading Paytm ...");
            PaytmPayment.this.bar.show();
            new Handler().postDelayed(() -> PaytmPayment.this.bar.dismiss(), 2000);
            dbPaytm.child("01").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {

                        final PaytmKey paytmKey = dataSnapshot.getValue(PaytmKey.class);
                        Intent i = new Intent(getApplicationContext(), ConfirmAmount.class);

                        Bundle bundle = new Bundle();
                        assert paytmKey != null;
                        i.putExtra("MID", paytmKey.getPaytmkey());
                        i.putExtra("Amount", "1");
                        i.putExtras(bundle);
                        startActivity(i);


                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });


        view.findViewById(R.id.razorpay).setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            PaytmPayment.this.bar.setMessage("Loading Razorpay ...");
            PaytmPayment.this.bar.show();
            new Handler().postDelayed(() -> PaytmPayment.this.bar.dismiss(), 2000);
            Intent i = new Intent(getApplicationContext(), RazorpaySection.class);

            Bundle bundle = new Bundle();
            i.putExtra("Amount", "1");
            i.putExtras(bundle);
            startActivity(i);


        });

        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        Objects.requireNonNull(mBottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(dialog -> mBottomSheetDialog = null);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Cancel payment ")
                .setMessage("Are you sure you want to close payment ? ")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", null)
                .show();

    }


}
