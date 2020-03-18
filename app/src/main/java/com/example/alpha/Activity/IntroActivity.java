package com.example.alpha.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;
    FirebaseDatabase database;
    DatabaseReference matchdb, updatenotice, maintaindb, mRef;
    FirebaseAuth.AuthStateListener mAuthListener;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();

        }, 1000);


    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(() -> {

        }, 0);

    }
}
