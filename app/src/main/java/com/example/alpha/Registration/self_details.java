package com.example.alpha.Registration;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alpha.Activity.MainActivity;
import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class self_details extends AppCompatActivity {

    public EditText editTextuserName;
    Button finish;


    private FirebaseAuth mAuth;
    private DatabaseReference mFirebase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_details);

        editTextuserName = findViewById(R.id.userName);
        editTextuserName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mFirebase = FirebaseDatabase.getInstance().getReference("Users");

        mAuth = FirebaseAuth.getInstance();

        finish = findViewById(R.id.finsh);
        FirebaseAuth.getInstance().signOut();

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mUserName = editTextuserName.getText().toString().trim();


                final DatabaseReference promodb = FirebaseDatabase.getInstance().getReference("ReferDB");
                promodb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot checkdataSnapshot) {


                        if (mUserName.isEmpty()) {
                            editTextuserName.setError("Enter Username");
                            editTextuserName.requestFocus();
                            return;

                        }
                        if (mUserName.length() < 5) {
                            editTextuserName.setError("Enter 5 Letters");
                            editTextuserName.requestFocus();

                            return;
                        } else if (checkdataSnapshot.hasChild(mUserName)) {
                            editTextuserName.setError("Username Exists");
                            editTextuserName.requestFocus();
                        } else {

                            Intent i = new Intent(getApplicationContext(), Signup_Activity.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("stuff", mUserName);

                            i.putExtras(bundle);
                            startActivity(i);
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }


        });
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Cancel Registration?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(self_details.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
