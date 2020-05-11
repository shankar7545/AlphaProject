package com.example.alpha.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alpha.Activity.HelpActivity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditDetails extends AppCompatActivity {
    Toolbar toolbar;
    private KeyListener listener;

    LinearLayout updateChanges, editFNLayout, displayFNLayout;
    TextView editNameText, cancelNameText;
    TextInputEditText nameD, nameE, phoneNum, userName, email;
    DatabaseReference mUser;
    private String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUser = FirebaseDatabase.getInstance().getReference("Users");

        updateChanges = findViewById(R.id.update_changes);
        editFNLayout = findViewById(R.id.editFNLayout);
        displayFNLayout = findViewById(R.id.displayFNLayout);


        nameD = findViewById(R.id.display_full_name);
        nameE = findViewById(R.id.edit_full_name);
        phoneNum = findViewById(R.id.profile_phone);
        userName = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        editNameText = findViewById(R.id.editNameText);
        cancelNameText = findViewById(R.id.cancelNameText);


        // userName.setEnabled(false);
        userName.setFocusable(false);
        userName.setKeyListener(null);
        email.setKeyListener(null);
        email.setFocusable(false);


        loadDetails();
        EditDetailMethod();
    }

    private void EditDetailMethod() {

        nameD.setClickable(false);
        nameD.setFocusable(false);
        nameD.setFocusableInTouchMode(false);

        cancelNameText.setVisibility(View.GONE);
        editNameText.setVisibility(View.VISIBLE);

        updateChanges.setVisibility(View.GONE);
        editNameText.setOnClickListener(v -> {

            editNameText.setVisibility(View.GONE);
            cancelNameText.setVisibility(View.VISIBLE);
            updateChanges.setVisibility(View.VISIBLE);

            editFNLayout.setVisibility(View.VISIBLE);
            displayFNLayout.setVisibility(View.GONE);

        });

        cancelNameText.setOnClickListener(v -> {

            updateChanges.setVisibility(View.GONE);
            editNameText.setVisibility(View.VISIBLE);
            cancelNameText.setVisibility(View.GONE);

            displayFNLayout.setVisibility(View.VISIBLE);
            editFNLayout.setVisibility(View.GONE);

            editFNLayout.requestFocus();

        });


    }

    private void loadDetails() {

        mUser.child(selfUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String mName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                String mUserName = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                String mEmail = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();

                nameD.setText(mName);
                nameE.setText(mName);
                email.setText(mEmail);
                userName.setText(mUserName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.help) {
            Intent intent = new Intent(EditDetails.this, HelpActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
