package com.example.alpha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FigerPrintActivity extends AppCompatActivity {
    DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mLevel, dbPaytm, mLogin;
    LinearLayout authSuccess;
    TextView fingerText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figer_print);
        authSuccess= findViewById(R.id.auth_success);
        fingerText=findViewById(R.id.authenticateButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Executor executor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

        final BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // user clicked negative button
                } else {
                    //TODO: Called when an unrecoverable error has been encountered and the operation is complete.
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                authSuccess= findViewById(R.id.auth_success);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        authSuccess.setVisibility(View.VISIBLE);
                        fingerText.setVisibility(View.GONE);
                    }
                });
                mRef = FirebaseDatabase.getInstance().getReference("Users");
                mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String payment = dataSnapshot.child("paymentStatus").getValue().toString();
                        String state = dataSnapshot.child("parentStatus").getValue().toString();

                        if(payment.equals("false")){

                            startActivity(new Intent(FigerPrintActivity.this, PaytmPayment.class));
                            finish();

                        }

                        if(payment.equals("true")){

                            if(state.equals("false")){
                                startActivity(new Intent(FigerPrintActivity.this, ReferCodeAcitvity.class));
                                finish();
                            }
                        }
                        if(payment.equals("true")&& state.equals("true")){

                            startActivity(new Intent(FigerPrintActivity.this, HomeActivity.class));
                            finish();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //TODO: Called when a biometric is recognized.
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //TODO: Called when a biometric is valid but not recognized.
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("FingerPrint Authentication")
                .setSubtitle("Confirm fingerprint to continue")
                //.setDescription("Set the description to display")
                .setNegativeButtonText("Cancel")
                .build();
        biometricPrompt.authenticate(promptInfo);

        findViewById(R.id.authenticateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }
}
