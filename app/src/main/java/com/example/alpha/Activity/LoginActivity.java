package com.example.alpha.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.R;
import com.example.alpha.Registration.DialogSignupFragment;
import com.example.alpha.Registration.Signup_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {


    private static long back_pressed;
    public FirebaseAuth logAuth;
    public DatabaseReference loginDatabse;
    public TextInputEditText email, password;
    public FloatingActionButton signin;
    public String login_email, login_password;
    public RelativeLayout login_Relative;
    public TextView forgetPasswrod;

    ProgressDialog bar;
    LinearLayout sign_up;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mRef, mReferDB, mFirebase, mTransactions, mWallet, mLevel, dbPaytm, mLogin;

    public static final int DIALOG_QUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signin = findViewById(R.id.fab);
        sign_up = findViewById(R.id.sign_up);
        email = findViewById(R.id.l_email);
        password = findViewById(R.id.l_password);

        forgetPasswrod = findViewById(R.id.forgot_password1);

        /*---------------------------------------------------------*/


        login_Relative = findViewById(R.id.login_Relative);

        signin = findViewById(R.id.fab);


        LoginActivity.this.bar = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
        LoginActivity.this.bar.setCancelable(false);
        LoginActivity.this.bar.setMessage("Signing in...");
        LoginActivity.this.bar.setIndeterminate(true);
        LoginActivity.this.bar.setCanceledOnTouchOutside(false);



        signin.setOnClickListener(v -> {
            //String get value from edittext
            LoginActivity.this.bar.show();

            login_email = email.getText().toString();
            login_password = password.getText().toString();

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
                            dialog.dismiss();
                            progressdialog.dismiss();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
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
                sign_up.setOnClickListener(v -> Toast.makeText(LoginActivity.this, "Restart app to Signup", Toast.LENGTH_SHORT).show());
            }

        } else {
            sign_up.setOnClickListener(v -> {

                startActivity(new Intent(LoginActivity.this, Signup_Activity.class));
                //startActivity(registration);

                // showSignupDialog();
            });
        }


        mAuthListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {

                startActivity(new Intent(LoginActivity.this, FigerPrintActivity.class));
            }
        };


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
            signin.setAlpha(0f);
            startActivity(new Intent(LoginActivity.this, FigerPrintActivity.class));
            finish();


        }).addOnFailureListener(e -> {
            Snackbar.make(login_Relative, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
            LoginActivity.this.bar.dismiss();

        });
    }

    @Override
    public void onBackPressed() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage("Close app?")
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

    }

}
