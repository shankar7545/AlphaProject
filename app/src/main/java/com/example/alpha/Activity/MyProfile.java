package com.example.alpha.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MyProfile extends AppCompatActivity {
    TextView name, email, userName, referCode;

    public FirebaseAuth mAuth, mAuthListener;
    DatabaseReference mRef;
    Toolbar myprofiletoolbar;
    Button saveprofile;
    TextView logout;
    private String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private Object View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        logout = findViewById(R.id.logout);
        mRef = FirebaseDatabase.getInstance().getReference("Users");

        myprofiletoolbar = findViewById(R.id.myprofiletoolbar);
        setSupportActionBar(myprofiletoolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);

        logout.setOnClickListener(v -> new AlertDialog.Builder(MyProfile.this)
                .setMessage(R.string.end_session)
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MyProfile.this, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("logoutState", "logout");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show());

        mRef.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mUserName = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                String mName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                String mEmail = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();


                userName.setText(mUserName);
                email.setText(mEmail);
                name.setText(mName);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
      /*  mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue().toString();
                String email = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").getValue().toString();
                String username = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").getValue().toString();

                mName.setText(name);
                mEmail.setText(email);
                mUserName.setText(username);

                mEmail.setTextColor(getResources().getColor(R.color.red_600));
                mUserName.setTextColor(getResources().getColor(R.color.red_600));
                mName.setTextColor(getResources().getColor(R.color.green_700));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); */

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
