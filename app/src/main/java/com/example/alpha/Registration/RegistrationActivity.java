package com.example.alpha.Registration;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Activity.FingerPrintActivity;
import com.example.alpha.Model.Balance_class;
import com.example.alpha.Model.TransactionCount_class;
import com.example.alpha.Model.UserClass;
import com.example.alpha.R;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class RegistrationActivity extends AppCompatActivity {

    public ProgressBar progressBar;
    TextInputEditText editTextName, editTextEmail, editTextPassword, editTextReferCode;
    Button next;
    Button finish;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebase, mReferDB, mWallet, mTransactions, mChain, mRef, mLogin, mUsers, mParent;

    TextView txtAgreeTo;
    LinearLayout login;
    private Dialog dialog;
    ProgressDialog progressDialog;

    CredentialsClient mCredentialsClient;


    private static final int RC_HINT = 100;

    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        parent_view = findViewById(android.R.id.content);

        mCredentialsClient = Credentials.getClient(this);

        login = findViewById(R.id.login);
        editTextName = findViewById(R.id.name);
        editTextName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        txtAgreeTo = findViewById(R.id.txtAgreeTo);

        txtAgreeTo.setPaintFlags(txtAgreeTo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        progressBar = findViewById(R.id.progress_bar);

        next = findViewById(R.id.next);

        mRef = FirebaseDatabase.getInstance().getReference();
        mUsers = FirebaseDatabase.getInstance().getReference("Users");
        mReferDB = FirebaseDatabase.getInstance().getReference("ReferDB");
        mTransactions = FirebaseDatabase.getInstance().getReference("Transactions");
        mLogin = FirebaseDatabase.getInstance().getReference("Login");
        mChain = FirebaseDatabase.getInstance().getReference("Chain");
        mParent = FirebaseDatabase.getInstance().getReference("Parent");
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();


        findViewById(R.id.bt_close).setOnClickListener(v -> onBackPressed());


        editTextEmail.setOnClickListener(v -> showHintRequest());

        next.setOnClickListener(view -> {
            //progressBar.setVisibility(View.VISIBLE);
            //next.setVisibility(View.GONE);
            final String mName = Objects.requireNonNull(editTextName.getText()).toString().trim();
            final String mEmail = Objects.requireNonNull(editTextEmail.getText()).toString().trim();
            final String mPassword = Objects.requireNonNull(editTextPassword.getText()).toString().trim();


            try {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                assert imm != null;
                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            } catch (Exception e) {
                Toast.makeText(this, "Exception Found :" + e, Toast.LENGTH_SHORT).show();
            }


            if (mEmail.isEmpty()) {
                //editTextEmail.setError("Enter Email");
                Snackbar("Email address is empty !");
                editTextEmail.requestFocus();
                next.setVisibility(View.VISIBLE);

                return;

            }

            if (mName.isEmpty()) {
                //editTextName.setError("Enter Name");
                Snackbar("Name is empty !");
                editTextName.requestFocus();
                next.setVisibility(View.VISIBLE);
                return;
            }

            if (mPassword.isEmpty()) {
                //editTextPassword.setError("Enter Password");
                Snackbar("Password is Empty !");
                editTextPassword.requestFocus();
                next.setVisibility(View.VISIBLE);

                return;
            }
            if (mPassword.length() <= 6) {
                //editTextPassword.setError("Minimum 6 Letters");
                Snackbar("Password should be minimum 6 letters.");
                editTextPassword.requestFocus();
                next.setVisibility(View.VISIBLE);


                return;
            }


            registerUser();

        });

    }


    private void referDialog() {
        try {
            dialog = new Dialog(RegistrationActivity.this);
            dialog.setContentView(R.layout.referdialog);
            dialog.setCancelable(true);
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            final EditText editTextuserName;
            final TextView heading;
            final Button finish, cancel;
            final ProgressBar progress_bar_dialog;

            editTextuserName = dialog.findViewById(R.id.referCode);
            mRef = FirebaseDatabase.getInstance().getReference("Users");
            mFirebase = FirebaseDatabase.getInstance().getReference();

            finish = dialog.findViewById(R.id.finish);
            cancel = dialog.findViewById(R.id.cancelBtn);
            heading = dialog.findViewById(R.id.heading);
            progress_bar_dialog = dialog.findViewById(R.id.progress_bar_dialog);

            editTextuserName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            heading.setText("CREATE UNIQUE USERNAME");
            dialog.show();


            finish.setOnClickListener(view -> {
                cancel.setVisibility(View.GONE);
                finish.setVisibility(View.GONE);
                progress_bar_dialog.setVisibility(View.VISIBLE);
                final String mUserName = editTextuserName.getText().toString().trim();


                final DatabaseReference promodb = FirebaseDatabase.getInstance().getReference("ReferDB");
                promodb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot checkdataSnapshot) {


                        if (mUserName.isEmpty()) {
                            editTextuserName.setError("Enter Username");
                            editTextuserName.requestFocus();
                            finish.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                            progress_bar_dialog.setVisibility(View.GONE);
                        } else if (mUserName.length() < 5) {
                            editTextuserName.setError("Enter 5 Letters");
                            finish.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                            progress_bar_dialog.setVisibility(View.GONE);
                            editTextuserName.requestFocus();
                        } else if (checkdataSnapshot.hasChild(mUserName)) {
                            editTextuserName.setError("Username Exists");
                            finish.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                            progress_bar_dialog.setVisibility(View.GONE);
                            editTextuserName.requestFocus();
                        } else {
                            finish.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                            progress_bar_dialog.setVisibility(View.GONE);
                            registerUser();
                            dialog.dismiss();

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            });


            cancel.setOnClickListener(v -> dialog.dismiss());
            next.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void registerUser() {

        //ProgressBar
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog_new);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);


        final String mName = Objects.requireNonNull(editTextName.getText()).toString().trim();
        final String mEmail = Objects.requireNonNull(editTextEmail.getText()).toString().trim();
        final String mPassword = Objects.requireNonNull(editTextPassword.getText()).toString().trim();


        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        final String currentdate = dateformat.format(c.getTime());
        final Date currentTime = Calendar.getInstance().getTime();



        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {


                        //UsersDB
                        UserClass user = new UserClass(
                                mEmail,
                                mName,
                                mPassword,
                                "false",
                                "0",
                                "false",
                                currentdate,
                                "0"
                        );

                        String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                        mUsers.child(selfUid).setValue(user);



                        //WalletDB

                        mWallet = FirebaseDatabase.getInstance().getReference("Wallet")
                                .child(selfUid);

                        Balance_class balance_class = new Balance_class(
                                "0",
                                "0",
                                "0",
                                "0",
                                "0",
                                "0"

                        );


                        mWallet.child("Balance").setValue(balance_class);

                        mRef.child("Status").child(selfUid).child("status").setValue("free");
                        mRef.child("Status").child(selfUid).child("usingByUID").setValue("none");


                        TransactionCount_class transactionCount_class = new TransactionCount_class(
                                "0",
                                "0",
                                "0",
                                "0"
                        );

                        mWallet.child("Transactions").child("count").setValue(transactionCount_class);


                        //Chain


                        mChain.child(selfUid).child("uid1").child("uid").setValue("null");
                        mChain.child(selfUid).child("uid1").child("username").setValue("null");

                        mChain.child(selfUid).child("uid2").child("uid").setValue("null");
                        mChain.child(selfUid).child("uid2").child("username").setValue("null");
                        mChain.child(selfUid).child("parent").child("p1").setValue("null");

                        /*AchievementsClass achievementsClass = new AchievementsClass(
                                "unlocked",
                                "locked",
                                "locked",
                                "locked",
                                "locked"
                        );

                        mUsers.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .child("Achievements").setValue(achievementsClass); */


                        //ReferDB
                       /* ReferClass referClass = new ReferClass(
                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                mUserName);

                        assert mUserName != null;
                        mReferDB.child(mUserName).setValue(referClass); */


                        progressDialog.dismiss();
                        startActivity(new Intent(this, FingerPrintActivity.class));

                    } else {
                        //Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

                        Snackbar(Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                        progressBar.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        next.setVisibility(View.VISIBLE);

                    }

                });


    }

    private void Snackbar(String text) {
        Snackbar snackbar = Snackbar.make(parent_view, Objects.requireNonNull(text), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.blue_700))
                .setAction("Okay", view -> {
                });
        snackbar.show();

    }


    private void showHintRequest() {

        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setEmailAddressIdentifierSupported(true)
                .setAccountTypes(IdentityProviders.GOOGLE)
                .build();

        PendingIntent intent = mCredentialsClient.getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RC_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException ignored) {
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);

                assert credential != null;
                editTextEmail.setText(credential.getId());
                editTextName.requestFocus();
                editTextName.setSelection(Objects.requireNonNull(editTextName.getText()).length());


            } else {
                editTextEmail.clearFocus();
            }
        }


    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();
        statusBarColor();
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


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Cancel registration")
                .setMessage("Are you sure you want to cancel registration?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", null)
                .show();

    }
}
