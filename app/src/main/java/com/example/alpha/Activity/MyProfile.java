package com.example.alpha.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Profile.ChangePassword;
import com.example.alpha.Profile.EditDetails;
import com.example.alpha.R;
import com.example.alpha.Tutorials.TutorialActivity;
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
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import dmax.dialog.SpotsDialog;

public class MyProfile extends AppCompatActivity {
    TextView name, email, userName, referCode;

    public FirebaseAuth mAuth, mAuthListener;
    DatabaseReference mRef;
    Toolbar myprofiletoolbar;
    Button saveprofile;
    private String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private Object View;
    DatabaseReference mUser;
    private Dialog dialogChangePassword, dialogShowAvatar;
    String useremail;
    FirebaseUser user;
    AlertDialog progressdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        myprofiletoolbar = findViewById(R.id.myprofiletoolbar);
        setSupportActionBar(myprofiletoolbar);
        mUser = FirebaseDatabase.getInstance().getReference("Users").child(selfUid);
        mRef = FirebaseDatabase.getInstance().getReference("Users");


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        useremail = user.getEmail();


        mRef.child(selfUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("username").exists()) {
                    String mUserName = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                    userName.setText(mUserName);

                }
                final String mName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                final String mEmail = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();


                email.setText(mEmail);
                name.setText(mName);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        onCLick();
        loadAvatar();

    }

    private void onCLick() {


        findViewById(R.id.help).setOnClickListener(v -> startActivity(new Intent(this, HelpActivity.class)));


        findViewById(R.id.profileLayout).setOnClickListener(v -> startActivity(new Intent(this, EditDetails.class)));
        findViewById(R.id.changePasswordLayout).setOnClickListener(v -> startActivity(new Intent(this, ChangePassword.class)));
        findViewById(R.id.supportLayout).setOnClickListener(v -> startActivity(new Intent(this, TutorialActivity.class)));
        findViewById(R.id.referalLayout).setOnClickListener(v -> startActivity(new Intent(this, ChainActivity.class)));
        findViewById(R.id.changePasswordLayout).setOnClickListener(v -> changePasswordDialog());

        findViewById(R.id.logoutLayout).setOnClickListener(v -> new AlertDialog.Builder(MyProfile.this)
                .setMessage(R.string.end_session)
                .setCancelable(false)
                .setTitle("Logout")
                .setPositiveButton("Yes", (dialog, id) -> {
                    logout();
                    //openWebView();

                })
                .setNegativeButton("No", null)
                .show());


        findViewById(R.id.avatar).setOnClickListener(v -> showAvatarDialog());

    }

    private void showAvatarDialog() {
        dialogShowAvatar = new Dialog(MyProfile.this);
        dialogShowAvatar.setContentView(R.layout.layout_profile_images);
        dialogShowAvatar.setCancelable(true);
        Window window = dialogShowAvatar.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogShowAvatar.show();


        LinearLayout imageLayoutOne, imageLayoutTwo, imageLayoutThree, imageLayoutFour, imageLayoutFive, imageLayoutSix, imageLayoutSeven, imageLayoutEight;

        dialogShowAvatar.findViewById(R.id.imageLayoutOne).setOnClickListener(v -> updateAvatar("1"));
        dialogShowAvatar.findViewById(R.id.imageLayoutTwo).setOnClickListener(v -> updateAvatar("2"));
        dialogShowAvatar.findViewById(R.id.imageLayoutThree).setOnClickListener(v -> updateAvatar("3"));
        dialogShowAvatar.findViewById(R.id.imageLayoutFour).setOnClickListener(v -> updateAvatar("4"));
        dialogShowAvatar.findViewById(R.id.imageLayoutFive).setOnClickListener(v -> updateAvatar("5"));
        dialogShowAvatar.findViewById(R.id.imageLayoutSix).setOnClickListener(v -> updateAvatar("6"));
        dialogShowAvatar.findViewById(R.id.imageLayoutSeven).setOnClickListener(v -> updateAvatar("7"));
        dialogShowAvatar.findViewById(R.id.imageLayoutEight).setOnClickListener(v -> updateAvatar("8"));


    }

    private void updateAvatar(String value) {
        mUser.child("avatar").setValue(value);
        dialogShowAvatar.dismiss();
        loadAvatar();
    }

    private void loadAvatar() {

        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CircularImageView avatar;
                avatar = findViewById(R.id.avatar);
                if (snapshot.child("avatar").exists()) {
                    String value = Objects.requireNonNull(snapshot.child("avatar").getValue()).toString();

                    switch (value) {
                        case "1":
                            avatar.setImageResource(R.drawable.avatar_one);
                            break;
                        case "2":
                            avatar.setImageResource(R.drawable.avatar_two);

                            break;
                        case "3":
                            avatar.setImageResource(R.drawable.avatar_three);

                            break;
                        case "4":
                            avatar.setImageResource(R.drawable.avatar_four);

                            break;
                        case "5":
                            avatar.setImageResource(R.drawable.avatar_five);

                            break;
                        case "6":
                            avatar.setImageResource(R.drawable.avatar_six);

                            break;
                        case "7":
                            avatar.setImageResource(R.drawable.avatar_seven);


                            break;
                        case "8":
                            avatar.setImageResource(R.drawable.avatar_eight);

                            break;
                    }

                } else {
                    avatar.setImageResource(R.drawable.avatar_one);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();


        Intent i = new Intent(MyProfile.this, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("logoutState", "logout");
        i.putExtras(bundle);
        startActivity(i);
        finish();
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


    private void changePasswordDialog() {
        try {
            dialogChangePassword = new Dialog(MyProfile.this);
            dialogChangePassword.setContentView(R.layout.dialog_change_password);
            dialogChangePassword.setCancelable(false);
            Window window = dialogChangePassword.getWindow();
            assert window != null;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            final Button finish, cancel;

            finish = dialogChangePassword.findViewById(R.id.finish);
            cancel = dialogChangePassword.findViewById(R.id.cancelBtn);

            TextInputEditText profile_old_pass, profile_new_pass;

            profile_old_pass = dialogChangePassword.findViewById(R.id.profile_old_pass);
            profile_new_pass = dialogChangePassword.findViewById(R.id.profile_new_pass);

            profile_old_pass.requestFocus();

            dialogChangePassword.show();
            finish.setOnClickListener(view -> {


                try {

                    progressdialog = new SpotsDialog.Builder()
                            .setContext(MyProfile.this)
                            .setMessage("Please wait...")
                            .build();
                    progressdialog.show();

                    if (!profile_old_pass.getText().toString().isEmpty() && !profile_new_pass.getText().toString().isEmpty()) {
                        AuthCredential authCredential = EmailAuthProvider.getCredential(useremail, profile_old_pass.getText().toString());
                        user.reauthenticate(authCredential).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                user.updatePassword(profile_new_pass.getText().toString()).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {

                                        mUser = FirebaseDatabase.getInstance().getReference("Users")
                                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

                                        mUser.child("password").setValue(profile_new_pass.getText().toString());
                                        progressdialog.dismiss();
                                        Toast toast = new Toast(getApplicationContext());
                                        toast.setDuration(Toast.LENGTH_SHORT);

                                        //inflate view
                                        android.view.View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
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
                                        android.view.View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
                                        ((TextView) custom_view.findViewById(R.id.message)).setText("Something went wrong!");
                                        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close_black_24dp);
                                        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                        toast.setView(custom_view);
                                        toast.show();

                                        // Toast.makeText(MyProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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


                } catch (Exception e) {
                    e.printStackTrace();
                }


            });

            cancel.setOnClickListener(v -> dialogChangePassword.dismiss());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
