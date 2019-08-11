package com.example.alpha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        mFirebase = FirebaseDatabase.getInstance().getReference("Users");

        mAuth = FirebaseAuth.getInstance();

        finish = findViewById(R.id.finsh);




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
                        else if(checkdataSnapshot.hasChild(mUserName))
                        {
                            editTextuserName.setError("Username Exists");
                            editTextuserName.requestFocus();
                        }

                        else{

                            Intent i = new Intent(getApplicationContext(), Signup_Activity.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("stuff",mUserName);

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
}
