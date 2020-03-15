package com.example.alpha.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MyProfile extends AppCompatActivity {
    public FirebaseAuth mAuth, mAuthListener;
    DatabaseReference mRef;
    Toolbar myprofiletoolbar;
    Button saveprofile;
    TextView logout;
    EditText mName, mEmail, mUserName, mReferCode;
    private Object View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        logout = findViewById(R.id.logout);
        myprofiletoolbar = findViewById(R.id.myprofiletoolbar);
        setSupportActionBar(myprofiletoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MyProfile.this)
                        .setMessage(R.string.end_session)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(MyProfile.this, LoginActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("logoutState", "logout");
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
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
