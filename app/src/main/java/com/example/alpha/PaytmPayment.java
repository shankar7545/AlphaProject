package com.example.alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class PaytmPayment extends AppCompatActivity {
    LinearLayout pay50, referLayout;
    Button paytm;
    public EditText mAmount;
    private static long back_pressed;
    DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mLevel, dbPaytm, mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_payment);

        pay50 = (LinearLayout)findViewById(R.id.pay50);

        paytm = findViewById(R.id.paytm);
        mAmount = findViewById(R.id.amount);
        dbPaytm = FirebaseDatabase.getInstance().getReference("Paytm");

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbPaytm.child("01").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            final PaytmKey paytmKey = dataSnapshot.getValue(PaytmKey.class);
                            try {
                                final String mUserName = mAmount.getText().toString().trim();

                                Intent i = new Intent(getApplicationContext(), ConfirmAmount.class);

                                Bundle bundle = new Bundle();
                                i.putExtra("Amount", mUserName);
                                i.putExtra("MID", paytmKey.getPaytmkey());
                                i.putExtras(bundle);
                                startActivity(i);


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
                });

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String payment = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("payment").getValue().toString();

                if(payment.equals("true")){


                    String state = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("parent").child("state").getValue().toString();

                    if(state.equals("false")){
                        Intent intent = new Intent(PaytmPayment.this, ReferCodeAcitvity.class);
                        startActivity(intent);
                        finish();
                    }


                }
                else {
                    Toast.makeText(PaytmPayment.this, "You need to Complete Payment", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();

        }
    }
}
