package com.example.alpha.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.alpha.Common.Common;
import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

public class FigerPrintActivity extends AppCompatActivity {
    private static long back_pressed;
    TextView topText;
    DatabaseReference mRef, mPin, x;
    LinearLayout progressBar;
    ProgressDialog progressDialog;
    TextView textU;
    LinearLayout loogut, fingerPrintLayout;
    private PinView pinView;
    Dialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figer_print);
        //authSuccess = findViewById(R.id.auth_success);
        fingerPrintLayout = findViewById(R.id.fingerPrintLayout);
        topText = findViewById(R.id.topText);
        progressBar = findViewById(R.id.progress_bar);
        pinView = findViewById(R.id.pinView);
        textU = findViewById(R.id.textView_noti);
        loogut = findViewById(R.id.logout);

        //FingerPrint Check
        Context context = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            assert fingerprintManager != null;
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                fingerPrintLayout.setVisibility(View.GONE);
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                fingerPrintLayout.setVisibility(View.GONE);

            } else {
                // Everything is ready for fingerprint authentication
                fingerPrintLayout.setVisibility(View.VISIBLE);

            }
        }

        loogut.setOnClickListener(v -> new AlertDialog.Builder(FigerPrintActivity.this)
                .setMessage(R.string.end_session)
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    FirebaseAuth.getInstance().signOut();

                    Intent i = new Intent(FigerPrintActivity.this, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("logoutState", "logout");
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                })
                .setNegativeButton("No", null)
                .show());

        /*loogut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                        .title("This is a Test")
                        .content("open now to get message")
                        .color(255, 0, 0, 255)
                        .led_color(255, 255, 255, 255)
                        .addAction(intent, "Done")
                        .large_icon(R.drawable.icon)
                        .small_icon(R.drawable.fireman)
                        .rrule("FREQ=MINUTELY;INTERVAL=5;COUNT=2")
                        .build();
            }
        });*/


        //ProgressBar
        progressDialog = new ProgressDialog(FigerPrintActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog_new);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        //Handler

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedToINternet(FigerPrintActivity.this)) {
                    mPin();

                } else {

                    checkInternet();
                }
                //handler.postDelayed(this, 5000);
            }
        }, 0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Executor executor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

        final BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor,
                new BiometricPrompt.AuthenticationCallback() {
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
                        //authSuccess= findViewById(R.id.auth_success);
                        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));

                        startlogin();

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
                .setSubtitle("Place Fingerprint to unlock")
                //.setDescription("Set the description to display")
                .setNegativeButtonText("Use PIN")
                .build();
        //biometricPrompt.authenticate(promptInfo);

        findViewById(R.id.fingerPrintLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                biometricPrompt.authenticate(promptInfo);
            }
        });
    }


    private void startlogin() {

        startActivity(new Intent(FigerPrintActivity.this, HomeActivity.class));
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();


       /* mRef = FirebaseDatabase.getInstance().getReference("Users");
        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String payment = dataSnapshot.child("paymentStatus").getValue().toString();
                String state = dataSnapshot.child("parentStatus").getValue().toString();

                if (payment.equals("false")) {

                    startActivity(new Intent(FigerPrintActivity.this, PaytmPayment.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();

                }

                if (payment.equals("true")) {

                    if (state.equals("false")) {
                        startActivity(new Intent(FigerPrintActivity.this, ReferCodeAcitvity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                }
                if (payment.equals("true") && state.equals("true")) {

                    startActivity(new Intent(FigerPrintActivity.this, HomeActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }

    private void mPin() {
        try {
            mPin = FirebaseDatabase.getInstance().getReference("Users")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            mPin.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String userName = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();

                    topText.setText("Welcome " + userName);

                    progressDialog.dismiss();


                    if (dataSnapshot.child("pin").exists()) {

                        final String mPin = Objects.requireNonNull(dataSnapshot.child("pin").getValue()).toString();

                        pinView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                final String pin = Objects.requireNonNull(pinView.getText()).toString();
                                if ((pin.length() == 4) && (pin.equals(mPin))) {
                                    pinView.setLineColor(getResources().getColor(R.color.green_800));
                                    textU.setTextColor(getResources().getColor(R.color.green_800));
                                    textU.setText("Verification Successfull");
                                    ((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE)))
                                            .hideSoftInputFromWindow(pinView.getWindowToken(), 0);

                                    // progressBar.setVisibility(View.VISIBLE);
                                    startlogin();

                                } else {
                                    pinView.setLineColor(Color.RED);
                                    textU.setText("INCORRECT PIN");
                                    textU.setTextColor(Color.RED);
                                }
                                if (pin.length() < 4) {
                                    pinView.setLineColor(getResources().getColor(R.color.colorPrimaryDark1));
                                    textU.setText("Enter 4 digit Login Pin");
                                    textU.setTextColor(getResources().getColor(R.color.colorPrimaryDark1));
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });


                    } else {
                        fingerPrintLayout.setVisibility(View.GONE);
                        topText.setText("Create Secure PIN");
                        textU.setTextColor(Color.DKGRAY);
                        textU.setText("Create 4 digit PIN");

                        pinView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                                final String pin = Objects.requireNonNull(pinView.getText()).toString();
                                if (pin.length() == 4) {
                                    mPin.child("pin").setValue(pin);
                                    textU.setText(R.string.pin_created_successfully);
                                    //progressBar.setVisibility(View.VISIBLE);
                                    startlogin();
                                }

                            }
                        });

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkInternet() {
        progressDialog.dismiss();
        dialog = new Dialog(FigerPrintActivity.this);
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(false);
        final Button wifienable = dialog.findViewById(R.id.enablewifi);
        if (Common.isConnectedToINternet(FigerPrintActivity.this)) {
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

        } else {

            dialog.show();
            wifienable.setOnClickListener(v -> {
                //dialog.dismiss();
                checkInternet();


            });
        }
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        } else {
            Toast.makeText(FigerPrintActivity.this, "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();

        }
    }

    public void sendNotification(View view) {


    }

}
