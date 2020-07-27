package com.example.alpha.Plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alpha.Activity.HelpActivity;
import com.example.alpha.Levels.LevelActivity;
import com.example.alpha.R;
import com.example.alpha.Registration.PaytmPayment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class OnBoardActivity extends AppCompatActivity {

    DatabaseReference mUsers;
    ProgressBar joinProgressBar;
    TextView upgradeTextView;
    String selfUid = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_onboard_plan);

        onClick();
        initComponent();

        mUsers = FirebaseDatabase.getInstance().getReference("Users");
        joinProgressBar = findViewById(R.id.joinProgressBar);


        statusBarColor();
        checkStatus();


    }

    private void initComponent() {
        upgradeTextView = findViewById(R.id.upgradeTextView);

    }

    private void onClick() {

        findViewById(R.id.backToolbar).setOnClickListener(v -> finish());
        findViewById(R.id.helpToolbar).setOnClickListener(v -> startActivity(new Intent(this, HelpActivity.class)));

    }

    private void checkStatus() {

        mUsers.child(selfUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String paymentStatus = Objects.requireNonNull(snapshot.child("paymentStatus").getValue()).toString();
                String parentStatus = Objects.requireNonNull(snapshot.child("parentStatus").getValue()).toString();
                if (snapshot.child("Achievement").exists()) {
                    String Achievement = Objects.requireNonNull(snapshot.child("Achievement").getValue()).toString();


                }


                if (paymentStatus.equals("false")) {
                    //startActivity(new Intent(OnBoardActivity.this, PaytmPayment.class));
                    findViewById(R.id.joinNowLayout).setOnClickListener(v -> startActivity(new Intent(OnBoardActivity.this, PaytmPayment.class)));


                } else {
                    upgradeTextView.setText(" PaymentStatus " + parentStatus);


                    if (parentStatus.equals("false")) {
                        //startActivity(new Intent(OnBoardActivity.this, ReferCodeAcitvity.class));
                        upgradeTextView.setText("Enter Refercode");
                        findViewById(R.id.joinNowLayout).setOnClickListener(v -> startActivity(new Intent(OnBoardActivity.this, ReferCodeActivity.class)));

                    } else {
                        upgradeTextView.setText(" Open Levels Activity");
                        findViewById(R.id.joinNowLayout).setOnClickListener(v -> startActivity(new Intent(OnBoardActivity.this, LevelActivity.class)));

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void statusBarColor() {

        Window window = OnBoardActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(OnBoardActivity.this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

}
