package com.example.alpha.Profile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import dmax.dialog.SpotsDialog;

public class ChangePassword extends AppCompatActivity {
    TextInputEditText profile_old_pass, profile_new_pass, profile_old_pin, profile_new_pin;
    AlertDialog progressdialog;
    FirebaseUser user;
    Button changePassword, changePin;
    DatabaseReference userdetails, mUser;
    Toolbar toolbar;
    String useremail;

    private String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUser = FirebaseDatabase.getInstance().getReference("Users");
        userdetails = FirebaseDatabase.getInstance().getReference();
        profile_old_pass = findViewById(R.id.profile_old_pass);
        profile_new_pass = findViewById(R.id.profile_new_pass);

        profile_old_pin = findViewById(R.id.profile_old_pin);
        profile_new_pin = findViewById(R.id.profile_new_pin);

        changePassword = findViewById(R.id.changePassword);
        changePin = findViewById(R.id.changePin);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        useremail = user.getEmail();

       /* if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        } */

        try {


            changePassword.setOnClickListener(v -> {
                progressdialog = new SpotsDialog.Builder()
                        .setContext(ChangePassword.this)
                        .setMessage("Please wait...")
                        .build();
                progressdialog.show();

                if (!profile_old_pass.getText().toString().isEmpty() && !profile_new_pass.getText().toString().isEmpty()) {
                    AuthCredential authCredential = EmailAuthProvider.getCredential(useremail, profile_old_pass.getText().toString());
                    user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(profile_new_pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            mUser = FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

                                            mUser.child("password").setValue(profile_new_pass.getText().toString());
                                            progressdialog.dismiss();
                                            Toast toast = new Toast(getApplicationContext());
                                            toast.setDuration(Toast.LENGTH_SHORT);

                                            //inflate view
                                            View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
                                            ((TextView) custom_view.findViewById(R.id.message)).setText("Password Updated Successfully");
                                            ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done_black_24dp);
                                            ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

                                            toast.setView(custom_view);
                                            toast.show();
                                            //Toast.makeText(MyProfile.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressdialog.dismiss();

                                            Toast toast = new Toast(getApplicationContext());
                                            toast.setDuration(Toast.LENGTH_LONG);

                                            //inflate view
                                            View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
                                            ((TextView) custom_view.findViewById(R.id.message)).setText("Something went wrong!");
                                            ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close_black_24dp);
                                            ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                            toast.setView(custom_view);
                                            toast.show();

                                            // Toast.makeText(MyProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            } else {
                                progressdialog.dismiss();
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_SHORT);

                                //inflate view
                                View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
                                ((TextView) custom_view.findViewById(R.id.message)).setText("Old password is incorrect!");
                                ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close_black_24dp);
                                ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                toast.setView(custom_view);
                                toast.show();

                                //Toast.makeText(MyProfile.this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    progressdialog.dismiss();
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);

                    //inflate view
                    View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
                    ((TextView) custom_view.findViewById(R.id.message)).setText("Please fill all the details");
                    ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close_black_24dp);
                    ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.red_400));

                    toast.setView(custom_view);
                    toast.show();

                    // Toast.makeText(MyProfile.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }


            });


            changePin.setOnClickListener(v -> {
                progressdialog = new SpotsDialog.Builder()
                        .setContext(ChangePassword.this)
                        .setMessage("Please wait...")
                        .build();
                progressdialog.show();

                if (!Objects.requireNonNull(profile_old_pin.getText()).toString().isEmpty() && !Objects.requireNonNull(profile_new_pin.getText()).toString().isEmpty()) {

                    mUser.child(selfUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            final String mPin = Objects.requireNonNull(dataSnapshot.child("pin").getValue()).toString();

                            if (profile_old_pass.equals(mPin)) {

                                mUser.child(selfUid).child("pin").setValue(Objects.requireNonNull(profile_new_pass.getText()).toString());
                                progressdialog.dismiss();
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_SHORT);

                                //inflate view
                                View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
                                ((TextView) custom_view.findViewById(R.id.message)).setText("Pin Updated Successfully");
                                ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done_black_24dp);
                                ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

                                toast.setView(custom_view);
                                toast.show();

                            } else {
                                progressdialog.dismiss();
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_SHORT);

                                //inflate view
                                View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
                                ((TextView) custom_view.findViewById(R.id.message)).setText("Old Pin is Incorrect");
                                ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close_black_24dp);
                                ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.red_400));

                                toast.setView(custom_view);
                                toast.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    progressdialog.dismiss();
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);

                    //inflate view
                    View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
                    ((TextView) custom_view.findViewById(R.id.message)).setText("Please fill all the details");
                    ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close_black_24dp);
                    ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.red_400));

                    toast.setView(custom_view);
                    toast.show();

                    // Toast.makeText(MyProfile.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
