package com.example.alpha.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class IntroActivity extends AppCompatActivity {

    int SPLASH_TIME_OUT = 1000;


    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);



        mAuthListener = firebaseAuth -> {

            if (firebaseAuth.getCurrentUser() != null) {

                new Handler().postDelayed(() ->
                {
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                }, SPLASH_TIME_OUT);
            }
            else
            {
                new Handler().postDelayed(() ->
                {
                    startActivity(new Intent(this, LoginActivity.class));

                    finish();

                }, SPLASH_TIME_OUT);
            }
        };

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth logAuth = FirebaseAuth.getInstance();
        logAuth.addAuthStateListener(mAuthListener);


        Window window = IntroActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(IntroActivity.this, R.color.black));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        //statusBarColor();

    }
}
