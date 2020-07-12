package com.example.alpha.Profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alpha.Activity.HelpActivity;
import com.example.alpha.R;
import com.google.android.material.snackbar.Snackbar;
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
    ProgressBar progressBar;
    LinearLayout updateChanges, displayFNLayout;
    TextView editNameText, cancelNameText;
    TextInputEditText nameD, phoneNum, userName, email;
    DatabaseReference mUser;
    private String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private Dialog dialog;
    private Context context;
    View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        parent_view = findViewById(android.R.id.content);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUser = FirebaseDatabase.getInstance().getReference("Users");

        updateChanges = findViewById(R.id.update_changes);
        displayFNLayout = findViewById(R.id.displayFNLayout);
        progressBar = findViewById(R.id.progressBar);


        nameD = findViewById(R.id.display_full_name);
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
        nameD.setKeyListener(null);
        nameD.setFocusable(false);


        progressBar.setVisibility(View.VISIBLE);
        loadDetails();
        EditDetailMethod();
    }

    private void EditDetailMethod() {

        cancelNameText.setVisibility(View.GONE);
        editNameText.setVisibility(View.VISIBLE);
        updateChanges.setVisibility(View.GONE);

        editNameText.setOnClickListener(v -> {
            editDetails();
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
                email.setText(mEmail);
                userName.setText(mUserName);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void editDetails() {
        try {
            dialog = new Dialog(EditDetails.this);
            dialog.setContentView(R.layout.edit_details_dialog);
            dialog.setCancelable(false);
            final Button finish, cancel;
            Window window = dialog.getWindow();
            assert window != null;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            finish = dialog.findViewById(R.id.finish);
            cancel = dialog.findViewById(R.id.cancelBtn);

            TextInputEditText emailAddress, fullName;
            emailAddress = dialog.findViewById(R.id.emailAddress);
            fullName = dialog.findViewById(R.id.fullName);
            emailAddress.setEnabled(false);
            mUser.child(selfUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String mEmail = dataSnapshot.child("email").getValue().toString();
                    String mName = dataSnapshot.child("name").getValue().toString();

                    emailAddress.setText(mEmail);
                    fullName.setText(mName);
                    fullName.requestFocus();
                    fullName.setSelection(fullName.getText().length());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            dialog.show();
            finish.setOnClickListener(view -> {

                cancel.setEnabled(false);
                mUser.child(selfUid).child("name").setValue(Objects.requireNonNull(fullName.getText()).toString());
                Snackbar("Name updated successful");
                finish.setText("Updated");
                cancel.setEnabled(true);
                dialog.dismiss();

            });

            cancel.setOnClickListener(v -> dialog.dismiss());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void Snackbar(String text) {
        Snackbar snackbar = Snackbar.make(parent_view, Objects.requireNonNull(text), Snackbar.LENGTH_SHORT)
                .setAction("Okay", view -> {
                });
        snackbar.show();

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
