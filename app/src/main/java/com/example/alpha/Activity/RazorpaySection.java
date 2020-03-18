package com.example.alpha.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RazorpaySection extends AppCompatActivity implements PaymentResultListener {

    String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    String amount = "";
    int payamount;
    DatabaseReference myWalletAmount, mFirebase, mWallet;
    String id = UUID.randomUUID().toString();
    String childid = "PW" + id.substring(0, 5).toUpperCase();
    String extraid = id.substring(0, 4).toUpperCase();
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
    private SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss aa");
    private String timeformat = time.format(c.getTime());
    private String datetime = dateformat.format(c.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay_section);
        if (getIntent().getExtras() != null) {
            amount = getIntent().getStringExtra("Amount");
            startPayment();
            myWalletAmount = FirebaseDatabase.getInstance().getReference("Wallet").child(selfUid);
            mWallet = FirebaseDatabase.getInstance().getReference("Wallet");
        }
    }

    private void startPayment() {
        payamount = Integer.parseInt(amount);
        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.refer);
        final Activity activity = this;


        try {
            JSONObject options = new JSONObject();
            options.put("description", childid);
            options.put("currency", "INR");
            options.put("amount", payamount * 100);
            checkout.open(activity, options);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPaymentSuccess(String s) {

        try {
            mFirebase = FirebaseDatabase.getInstance().getReference();
            mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mWallet.child(selfUid).child("balance").setValue("50");
                    mFirebase.child("Users").child(selfUid).child("paymentStatus").setValue("true");

                    String user_userName = Objects.requireNonNull(dataSnapshot.child("Users").child(selfUid)
                            .child("username").getValue()).toString();

                    if (dataSnapshot.child("Transactions").child(childid).exists()) {


                        Transaction_Class send_transaction_class = new Transaction_Class(
                                "added",
                                datetime,
                                timeformat,
                                user_userName,
                                "Wallet",
                                childid,
                                "50",
                                1,
                                "beginner"
                        );
                        mFirebase.child("Transactions").child(childid + extraid).setValue(send_transaction_class);

                    } else {

                        Transaction_Class send_transaction_class = new Transaction_Class(
                                "added",
                                datetime,
                                timeformat,
                                user_userName,
                                "Wallet",
                                childid,
                                "50",
                                1,
                                "beginner"

                        );
                        mFirebase.child("Transactions").child(childid).setValue(send_transaction_class);


                    }

                    //sendTransaction in user


                    if (dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                            .child("history").exists()) {
                        long countR = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                                .child("history").getChildrenCount();

                        long sizeR = countR + 1;


                        Transaction_Class send_transaction_class = new Transaction_Class(
                                "added",
                                datetime,
                                timeformat,
                                user_userName,
                                "Wallet",
                                childid,
                                "50",
                                sizeR,
                                "beginner"
                        );
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).setValue(send_transaction_class);

                    } else {

                        Transaction_Class send_transaction_class = new Transaction_Class(
                                "added",
                                datetime,
                                timeformat,
                                user_userName,
                                "Wallet",
                                childid,
                                "50",
                                1,
                                "beginner"
                        );
                        mWallet.child(selfUid).child("Transactions").child("history").child(childid).setValue(send_transaction_class);


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            //inflate view
            View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
            ((TextView) custom_view.findViewById(R.id.message)).setText("Transaction Successful!");
            ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done_black_24dp);
            ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(custom_view);
            toast.show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onPaymentError(int i, String s) {

        try {
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);

            //inflate view
            View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
            ((TextView) custom_view.findViewById(R.id.message)).setText("Transaction failed!");
            ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close_black_24dp);
            ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.red_400));

            toast.setView(custom_view);
            toast.show();
            finish();

            new Handler().postDelayed(toast::cancel, 800);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
