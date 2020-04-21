package com.example.alpha.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Profile.ChangePassword;
import com.example.alpha.R;
import com.google.android.material.textfield.TextInputEditText;
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
    LinearLayout tab1, tab4;
    private Object View;
    DatabaseReference mUser;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        myprofiletoolbar = findViewById(R.id.myprofiletoolbar);
        setSupportActionBar(myprofiletoolbar);
        mUser = FirebaseDatabase.getInstance().getReference("Users").child(selfUid);
        logout = findViewById(R.id.logout);
        mRef = FirebaseDatabase.getInstance().getReference("Users");

        tab1 = findViewById(R.id.tab1);
        tab4 = findViewById(R.id.tab4);

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

        mRef.child(selfUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mUserName = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                final String mName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                final String mEmail = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();


                userName.setText(mUserName);
                email.setText(mEmail);
                name.setText(mName);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tab1.setOnClickListener(v -> {
            // Intent intent = new Intent(MyProfile.this, EditDetails.class);
            //startActivity(intent);

            editDetails();
        });

        tab4.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfile.this, ChangePassword.class);
            startActivity(intent);
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


    private void editDetails() {
        try {
            dialog = new Dialog(MyProfile.this);
            dialog.setContentView(R.layout.edit_details_dialog);
            dialog.setCancelable(false);
            final Button finish, cancel;

            finish = dialog.findViewById(R.id.finish);
            cancel = dialog.findViewById(R.id.cancelBtn);

            TextInputEditText emailAddress, fullName;
            emailAddress = dialog.findViewById(R.id.emailAddress);
            fullName = dialog.findViewById(R.id.fullName);
            emailAddress.setEnabled(false);
            mUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String mEmail = dataSnapshot.child("email").getValue().toString();
                    String mName = dataSnapshot.child("name").getValue().toString();

                    emailAddress.setText(mEmail);
                    fullName.setText(mName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            dialog.show();
            finish.setOnClickListener(view -> {

                cancel.setEnabled(false);
                mUser.child("name").setValue(Objects.requireNonNull(fullName.getText()).toString());
                finish.setText("Updated");
                cancel.setEnabled(true);
                dialog.dismiss();

            });

            cancel.setOnClickListener(v -> dialog.dismiss());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
