package com.example.alpha.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class IntroActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 500;
    DatabaseReference mData, updatenotice, maintaindb, mRef;
    FirebaseAuth.AuthStateListener mAuthListener;
    TextView textDisplay;

    Uri uriNotification;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        textDisplay = findViewById(R.id.textDisplay);

        new Handler().postDelayed(() -> {
            //startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            //finish();
            connectFirebase();
        }, SPLASH_TIME_OUT);



    }

    private void connectFirebase() {
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();


        Window window = IntroActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(IntroActivity.this, R.color.colorPrimaryDark));

    }
}
