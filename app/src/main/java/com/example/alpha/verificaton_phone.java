package com.example.alpha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class verificaton_phone extends AppCompatActivity {

    Button continueBtn;
    EditText PhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificaton_phone);

        PhoneNum = findViewById(R.id.phone_number);

        continueBtn = (Button)findViewById(R.id.continueBtn);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number = PhoneNum.getText().toString().trim();
                String mName = getIntent().getStringExtra("name");
                String mEmail = getIntent().getStringExtra("email");
                String mPassword = getIntent().getStringExtra("password");
                String mReferCode = getIntent().getStringExtra("refercode");
                String mUserName = getIntent().getStringExtra("username");



                if(number.isEmpty() || number.length() <10)
                {
                    PhoneNum.setError("Enter Number");
                    PhoneNum.requestFocus();
                    return;
                }


                String mobile = "+" +"91" +number;

                Intent intent = new Intent(verificaton_phone.this, verification_code.class);
                intent.putExtra("mobile" ,mobile);
                intent.putExtra("name" ,mName);
                intent.putExtra("email" ,mEmail);
                intent.putExtra("password" ,mPassword);
                intent.putExtra("refercode" ,mReferCode);
                intent.putExtra("username" ,mUserName);
                startActivity(intent);
            }
        });
    }
}
