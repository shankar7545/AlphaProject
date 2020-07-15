package com.example.alpha.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.alpha.Common.Common;
import com.example.alpha.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class FingerPrintActivity extends AppCompatActivity {
    private static long back_pressed;

    public View parent_view;
    public InputMethodManager imm;
    TextView topText;
    DatabaseReference mRef, mPin, x;
    LinearLayout progressBar;
    ProgressDialog progressDialog;
    TextView textU;
    LinearLayout loogut, fingerPrintLayout;
    private PinView pinView;
    Dialog dialog;


    public ProgressDialog bar;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figer_print);
        //authSuccess = findViewById(R.id.auth_success);
        imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);


        assert imm != null;
        initComponent();
        initToolbar();

        String a = "no";
        fingerPrint(a);
    }

    private void initToolbar() {

        findViewById(R.id.backToolbar).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.helpToolbar).setOnClickListener(v -> startActivity(new Intent(this, HelpActivity.class)));
    }

    private void fingerPrint(String a) {
        parent_view = findViewById(android.R.id.content);
        //FingerPrint Check
        Context context = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            assert fingerprintManager != null;
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                findViewById(R.id.fingerPrintLayout).setOnClickListener(v -> {
                    Snackbar snackbar = Snackbar.make(parent_view, "Device doesn't support fingerprint", Snackbar.LENGTH_SHORT)
                            .setAction("Okay", view -> {
                            });
                    snackbar.show();
                });


            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                findViewById(R.id.fingerPrintLayout).setOnClickListener(v -> {

                    Snackbar snackbar = Snackbar.make(parent_view, "User hasn't enrolled any fingerprints", Snackbar.LENGTH_SHORT)
                            .setAction("Okay", view -> {
                            });
                    snackbar.show();
                });

            } else {
                // Everything is ready for fingerprint authentication
                fingerPrintLayout.setVisibility(View.VISIBLE);
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

                                runOnUiThread(() -> bar.show());


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
                if (a.equals("visible")) {
                    biometricPrompt.authenticate(promptInfo);

                }

                findViewById(R.id.fingerPrintLayout).setOnClickListener(v -> biometricPrompt.authenticate(promptInfo));

            }
        }

    }

    private void initComponent() {
        fingerPrintLayout = findViewById(R.id.fingerPrintLayout);
        topText = findViewById(R.id.topText);
        pinView = findViewById(R.id.pinView);
        textU = findViewById(R.id.textView_noti);
        loogut = findViewById(R.id.logout);


        bar = new ProgressDialog(FingerPrintActivity.this, R.style.MyAlertDialogStyle);
        bar.setCancelable(false);
        bar.setMessage("Loading ..");
        bar.setIndeterminate(true);
        bar.setCanceledOnTouchOutside(false);


        loogut.setOnClickListener(v ->
                {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss aa");
                    String timeformat = time.format(c.getTime());
                    String datetime = dateformat.format(c.getTime());

                    Toast.makeText(this, "Date : " + timeformat + ", Time : " + datetime, Toast.LENGTH_LONG).show();
                }


                /*new AlertDialog.Builder(FingerPrintActivity.this)
                .setMessage(R.string.end_session)
                .setCancelable(false)
                .setTitle("Logout")
                .setPositiveButton("Yes", (dialog, id) -> {
                    logout();
                    //openWebView();


                })
                .setNegativeButton("No", null)
                .show()*/);


        //ProgressBar
        progressDialog = new ProgressDialog(FingerPrintActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog_new);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        //Handler

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (Common.isConnectedToINternet(FingerPrintActivity.this)) {
                checkPIN();

            } else {

                checkInternet();
            }
            //handler.postDelayed(this, 5000);
        }, 0);

    }

    private void openWebView() {

        String URL = "https://www.google.com";

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(URL));
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();


        Intent i = new Intent(FingerPrintActivity.this, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("logoutState", "logout");
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        statusBarColor();
    }

    private void statusBarColor() {

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.grey_5));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    private void startlogin() {

        startActivity(new Intent(FingerPrintActivity.this, HomeActivity.class));
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

    private void checkPIN() {


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FingerPrintActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        String value = preferences.getString("PIN", null);
        if (value == null) {

            // the key does not exist
            mPin = FirebaseDatabase.getInstance().getReference("Users")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            mPin.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();

                    pinView.setCursorVisible(true);

                    String a = "visible";
                    fingerPrint(a);
                    progressDialog.dismiss();


                    if (dataSnapshot.child("pin").exists()) {
                        final String mPIN = Objects.requireNonNull(dataSnapshot.child("pin").getValue()).toString();
                        editor.putString("PIN", mPIN);
                        editor.putString("email", email);
                        editor.apply();
                        topText.setText(email);
                        mPin(mPIN);


                    } else {
                        fingerPrintLayout.setVisibility(View.GONE);
                        //topText.setText("Create Secure PIN");
                        textU.setTextColor(Color.DKGRAY);
                        textU.setText("Create 4 digit PIN");
                        topText.setText(email);

                        pinView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                pinView.setLineColor(getResources().getColor(R.color.colorPrimaryDark1));
                                textU.setTextColor(getResources().getColor(R.color.colorPrimaryDark1));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                                final String pin = Objects.requireNonNull(pinView.getText()).toString();
                                if (pin.length() == 4) {
                                    mPin.child("pin").setValue(pin);
                                    pinView.setLineColor(getResources().getColor(R.color.green_800));
                                    textU.setTextColor(getResources().getColor(R.color.green_800));
                                    textU.setText(R.string.pin_created_successfully);
                                    //progressBar.setVisibility(View.VISIBLE);
                                    bar.show();
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


        } else {
            // handle the value
            progressDialog.dismiss();
            String mPIN = value;
            String email = preferences.getString("email", null);
            topText.setText(email);
            String a = "visible";
            fingerPrint(a);
            mPin(mPIN);


        }


    }

    private void mPin(String PIN) {
        try {

            pinView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    final String pin = Objects.requireNonNull(pinView.getText()).toString();
                    if ((pin.length() == 4) && (pin.equals(PIN))) {
                        pinView.setLineColor(getResources().getColor(R.color.green_800));
                        textU.setTextColor(getResources().getColor(R.color.green_800));
                        textU.setText("Verification Successfull");
                        ((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE)))
                                .hideSoftInputFromWindow(pinView.getWindowToken(), 0);

                        // progressBar.setVisibility(View.VISIBLE);
                        bar.show();
                        startlogin();

                    } else {
                        pinView.setLineColor(Color.RED);
                        textU.setText("INCORRECT PIN");
                        textU.setTextColor(Color.RED);
                    }
                    if (pin.length() < 4) {
                        pinView.setLineColor(getResources().getColor(R.color.colorPrimaryDark1));
                        textU.setText("Enter your 4 digit password");
                        textU.setTextColor(getResources().getColor(R.color.colorPrimaryDark1));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkInternet() {
        progressDialog.dismiss();
        dialog = new Dialog(FingerPrintActivity.this);
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(false);

        final Button wifienable = dialog.findViewById(R.id.enablewifi);
        if (Common.isConnectedToINternet(FingerPrintActivity.this)) {
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            initComponent();


        } else {

            dialog.show();
            wifienable.setOnClickListener(v -> {
                dialog.dismiss();
                checkInternet();

            });
        }
    }


    public void onButtonTabClick(View v) {
        switch (v.getId()) {
            case R.id.tab_home:
                startActivity(new Intent(this, HelpActivity.class));
                break;

            case R.id.tab_top_artists:
                Toast.makeText(this, "Forgot Password", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tab_top_albums:
                startActivity(new Intent(this, SupportActivity.class));
                break;


        }
    }


   /* private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        new Handler().postDelayed(() -> {
            // open overflow menu
            ((Toolbar) findViewById(R.id.toolbar)).showOverflowMenu();
        }, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.supportItem) {
            startActivity(new Intent(this,SupportActivity.class));
        }
        if(item.getItemId() == R.id.helpItem){
            startActivity(new Intent(this,HelpActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    */


    @Override
    public void onBackPressed() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Close app")
                .setMessage("Are you sure you want to close App ? ")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    finish();
                    ActivityCompat.finishAffinity(this);
                    System.exit(0);
                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bar != null && bar.isShowing()) {
            bar.dismiss();
        }
    }
}
