package com.example.alpha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    TextView sign_up;
    private static long back_pressed;
    public FirebaseAuth logAuth;
    public DatabaseReference loginDatabse;
    public EditText email, password;
    public ProgressBar progressBar;

    public LinearLayout signin;
    public String login_email, login_password;
    FirebaseAuth.AuthStateListener mAuthListener;
    public LinearLayout login_Relative;
    public TextView forgetPasswrod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signin = (LinearLayout) findViewById(R.id.fab);
        sign_up = (TextView) findViewById(R.id.sign_up);
        email = (EditText) findViewById(R.id.l_email);
        password = (EditText) findViewById(R.id.l_password);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        /*---------------------------------------------------------*/


        login_Relative = (LinearLayout) findViewById(R.id.login_Relative);

        signin = (LinearLayout) findViewById(R.id.fab);



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String get value from edittext
                progressBar.setVisibility(View.VISIBLE);

                signin.setAlpha(0f);
                login_email = email.getText().toString();
                login_password = password.getText().toString();

                if (!login_email.isEmpty() && !login_password.isEmpty()) {
                    signin.setAlpha(0f);
                    progressBar.setVisibility(View.VISIBLE);

                    startLogin(login_email, login_password);
                } else {
                    signin.setAlpha(1f);
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(MainActivity.this, "Please Fill all the details", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Firebase Declaration;

        logAuth = FirebaseAuth.getInstance();
        loginDatabse = FirebaseDatabase.getInstance().getReference("Players");

        sign_up = (TextView) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registration = new Intent(MainActivity.this, self_details.class);
                startActivity(registration);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
            }
        };
    }



    private void startLogin(String login_email, String login_password) {
        logAuth.signInWithEmailAndPassword(login_email,login_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressBar.setVisibility(View.GONE);
                signin.setAlpha(0f);
                startActivity(new Intent(MainActivity.this, HomeActivity.class));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(login_Relative,e.getMessage(),Snackbar.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                signin.setAlpha(1f);

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

    @Override
    protected void onStart() {
        super.onStart();

        logAuth.addAuthStateListener(mAuthListener);


    }



}
