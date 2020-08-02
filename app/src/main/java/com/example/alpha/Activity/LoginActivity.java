package com.example.alpha.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.R;
import com.example.alpha.Registration.DialogSignupFragment;
import com.example.alpha.Registration.RegistrationActivity;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {


    private static long back_pressed;
    public FirebaseAuth logAuth;
    public DatabaseReference loginDatabse;
    public TextInputEditText email, password;
    public Button signin;
    public String login_email, login_password;
    public RelativeLayout login_Relative;
    public TextView forgetPasswrod;

    ProgressDialog bar;
    LinearLayout sign_up;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mLevel, dbPaytm, mLogin;

    CredentialsClient mCredentialsClient;
    public InputMethodManager imm;



    public static final int DIALOG_QUEST_CODE = 300;

    private static final int RC_HINT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signin = findViewById(R.id.fab);
        sign_up = findViewById(R.id.sign_up);
        email = findViewById(R.id.l_email);
        password = findViewById(R.id.l_password);

        forgetPasswrod = findViewById(R.id.forgot_password1);


        imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
        assert imm != null;

        /*---------------------------------------------------------*/

        mCredentialsClient = Credentials.getClient(this);

        login_Relative = findViewById(R.id.login_Relative);

        signin = findViewById(R.id.fab);



        LoginActivity.this.bar = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
        LoginActivity.this.bar.setCancelable(false);
        LoginActivity.this.bar.setMessage("Signing in ...");
        LoginActivity.this.bar.setIndeterminate(true);
        LoginActivity.this.bar.setCanceledOnTouchOutside(false);


        email.setOnClickListener(v -> showHintRequest());

        signin.setOnClickListener(v -> {
            //String get value from edittext
            LoginActivity.this.bar.show();

            login_email = Objects.requireNonNull(email.getText()).toString();
            login_password = Objects.requireNonNull(password.getText()).toString();


            if (!login_email.isEmpty() && !login_password.isEmpty()) {

                startLogin(login_email, login_password);
            } else {

                LoginActivity.this.bar.dismiss();

                Toast.makeText(LoginActivity.this, "Please Fill all the details", Toast.LENGTH_SHORT).show();
            }

        });
        //Forget Password

        forgetPasswrod.setOnClickListener(v -> {
            final AlertDialog progressdialog = new SpotsDialog.Builder()
                    .setContext(LoginActivity.this)
                    .setMessage("Sending Email...")
                    .build();


            final Dialog dialog = new Dialog(LoginActivity.this);
            dialog.setContentView(R.layout.forgetpass_layout);
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            assert window != null;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            final TextInputEditText reset_email = dialog.findViewById(R.id.reset_email);
            final Button reset_button = dialog.findViewById(R.id.reset_button);
            final Button reset_cancel = dialog.findViewById(R.id.reset_cancel);


            reset_button.setOnClickListener(v12 -> {
                progressdialog.show();
                if (Objects.requireNonNull(reset_email.getText()).toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Registered Email", Toast.LENGTH_SHORT).show();
                    progressdialog.dismiss();
                } else {
                    logAuth.sendPasswordResetEmail(reset_email.getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            progressdialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            //dialog.dismiss();
                            progressdialog.dismiss();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        //dialog.dismiss();
                        progressdialog.dismiss();
                    });
                }
            });

            reset_cancel.setOnClickListener(v1 -> dialog.dismiss());

            dialog.show();


        });

        //Firebase Declaration;

        logAuth = FirebaseAuth.getInstance();
        loginDatabse = FirebaseDatabase.getInstance().getReference("Players");
        sign_up = findViewById(R.id.sign_up);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final String logoutState = bundle.getString("logoutState");
            assert logoutState != null;
            if (logoutState.equals("logout")) {
                sign_up.setOnClickListener(v ->
                {
                    //Toast.makeText(LoginActivity.this, "Restart app to Signup", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));

                });

            }

        } else {
            sign_up.setOnClickListener(v -> {

                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                //startActivity(registration);

                // showSignupDialog();
            });
        }


        mAuthListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {

                startActivity(new Intent(LoginActivity.this, FingerPrintActivity.class));
            }
        };



    }

    private void showHintRequest() {


        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setPhoneNumberIdentifierSupported(false)
                .setEmailAddressIdentifierSupported(true)
                .setAccountTypes(IdentityProviders.GOOGLE)
                .build();

        PendingIntent intent = mCredentialsClient.getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RC_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Exception exception;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);

                assert credential != null;
                email.setText(credential.getId());
                password.requestFocus();


            } else {
                //Toast.makeText(this, "Hint Read Failed", Toast.LENGTH_SHORT).show();
                email.clearFocus();
            }


        }


    }

    private void showSignupDialog() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogSignupFragment newFragment = new DialogSignupFragment();
        newFragment.setRequestCode(DIALOG_QUEST_CODE);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();


    }

    private void startLogin(String login_email, String login_password) {
        logAuth.signInWithEmailAndPassword(login_email, login_password).addOnSuccessListener(authResult -> {
            LoginActivity.this.bar.show();
            //signin.setAlpha(0f);
            startActivity(new Intent(LoginActivity.this, FingerPrintActivity.class));
            finish();


        }).addOnFailureListener(e -> {
            Snackbar.make(login_Relative, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
            LoginActivity.this.bar.dismiss();

        });


    }

    private void statusBarColor() {

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

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
    protected void onStart() {
        super.onStart();
        logAuth.addAuthStateListener(mAuthListener);
        statusBarColor();
    }


}
