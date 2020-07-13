package com.example.alpha.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alpha.Common.Common;
import com.example.alpha.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class IntroActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 500;
    DatabaseReference mData, updatenotice, maintaindb, mRef;
    FirebaseAuth.AuthStateListener mAuthListener;
    TextView textDisplay;
    ProgressBar progress_indeterminate;

    Uri uriNotification;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
        //statusBarColor();
        textDisplay = findViewById(R.id.textDisplay);
        progress_indeterminate = findViewById(R.id.progress_indeterminate);

        //startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        //finish();

        checkInternet();
        //new Handler().postDelayed(() -> connectFirebase(), SPLASH_TIME_OUT);

    }

    private void connectFirebase() {
        progress_indeterminate.setIndeterminate(true);
        textDisplay.setText("Connecting Server");

        mData = FirebaseDatabase.getInstance().getReference("Data");

        mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                uriNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(IntroActivity.this, uriNotification);
                r.play();


                textDisplay.setText("Connected");
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkInternet() {

        View parent_view = findViewById(android.R.id.content);

        if (Common.isConnectedToINternet(IntroActivity.this)) {
            //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
            connectFirebase();

        } else {

            textDisplay.setText("No internet");
            progress_indeterminate.setIndeterminate(false);

            Snackbar snackbar = Snackbar.make(parent_view, "Turn on your Internet", Snackbar.LENGTH_INDEFINITE)

                    .setActionTextColor(getResources().getColor(R.color.green_700))
                    .setAction("Retry", view -> checkInternet());
            snackbar.show();


        }
    }

    private void sharedPreference() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Name", "Shankar");
        editor.apply();

        String name = preferences.getString("Name", "");
        if (!name.equalsIgnoreCase("")) {
            name = name + "  Sethi";  /* Edit the value here*/

            textDisplay.setText(name);

        }

        String value = preferences.getString("Name", null);
        if (value == null) {
            // the key does not exist
            textDisplay.setText("null");

        } else {
            // handle the value
            textDisplay.setText(value);

            editor.clear();
            editor.commit();
        }

    }

    private void statusBarColor() {

        Window window = IntroActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(IntroActivity.this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }


    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
