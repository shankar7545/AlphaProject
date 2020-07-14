package com.example.alpha.Wallet;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.alpha.Activity.SupportActivity;
import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import static android.app.Activity.RESULT_OK;

public class DialogLevelwiseFragment extends DialogFragment {


    private DatabaseReference mFirebase;
    private DatabaseReference mWallet;
    private String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
    private SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss aa");
    private String timeformat = time.format(c.getTime());
    private String datetime = dateformat.format(c.getTime());


    private final int GOOGLEPAY_PAYMENT = 2;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
    }

    private static final String UPI_ID = "8464980838@ybl";
    private String GOOGLE_PAY_PACKAGE_NAME = "com.phonepe.app";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DatabaseReference myWalletAmount = FirebaseDatabase.getInstance().getReference("Wallet").child(selfUid);

        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");


        View root_view = inflater.inflate(R.layout.dialog_levlewise, container, false);
        root_view.findViewById(R.id.bt_close).setOnClickListener(v -> dismiss());
        (root_view.findViewById(R.id.addedBalanceLayout)).setOnClickListener(v ->
        {
            //startActivity(new Intent(getContext(), HelpActivity.class));
            payUsingGooglePay("1", UPI_ID, "name", "Added From GooglePay");


        });
        (root_view.findViewById(R.id.bronzeBalanceLayout)).setOnClickListener(v -> startActivity(new Intent(getContext(), SupportActivity.class)));
        return root_view;
    }


    private void payUsingGooglePay(String amount, String upiId, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        //  only google pay
       /* Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
         // check if intent resolves
        if(null != intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager())) {

            startActivityForResult(intent, GOOGLEPAY_PAYMENT);


        } else {
            Toast.makeText(getContext(),"GooglePay app not found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
*/
        // choose any UPI based app

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show link dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        startActivityForResult(chooser, GOOGLEPAY_PAYMENT);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GOOGLEPAY_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 12)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("GOOGLEPAY", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("GOOGLEPAY", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("GOOGLEPAY", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;

        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {

        String str = data.get(0);
        String status = "";
        String paymentCancel = "";
        String approvalRefNo = "";
        String[] response = str.split("&");
        for (String s : response) {
            String[] equalStr = s.split("=");
            if (equalStr.length >= 2) {
                if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalStr[1].toLowerCase();
                } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    approvalRefNo = equalStr[1];
                }
            } else {
                paymentCancel = "Payment cancelled by user.";
            }
        }

        if (status.equals("success")) {
            //Code to handle successful transaction here.
            Toast.makeText(getContext(), "Transaction successful.", Toast.LENGTH_SHORT).show();
            Log.d("UPI", "responseStr: " + approvalRefNo);
            addTransactionDetails(String.valueOf(System.currentTimeMillis()), approvalRefNo);
        } else if ("Payment cancelled by user.".equals(paymentCancel)) {
            Toast.makeText(getContext(), "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
        }
    }


    private void addTransactionDetails(String orderIdSt, String txnIdSt) {

        mFirebase = FirebaseDatabase.getInstance().getReference();
        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mWallet.child(selfUid).child("Balance").child("mainBalance").setValue("50");
                mFirebase.child("Users").child(selfUid).child("paymentStatus").setValue("true");

                String user_userName = Objects.requireNonNull(dataSnapshot.child("Users").child(selfUid)
                        .child("username").getValue()).toString();

                Transaction_Class send_transaction_class = new Transaction_Class(
                        "added",
                        datetime,
                        timeformat,
                        user_userName,
                        "Wallet",
                        txnIdSt,
                        "50",
                        1,
                        "beginner",
                        "GooglePay"
                );
                mFirebase.child("Transactions").child(txnIdSt).setValue(send_transaction_class);


                //sendTransaction in user


                long countR = dataSnapshot.child("Wallet").child(selfUid).child("Transactions")
                        .child("history").getChildrenCount();

                long sizeR = countR + 1;


                Transaction_Class send_transaction_class2 = new Transaction_Class(
                        "added",
                        datetime,
                        timeformat,
                        user_userName,
                        "Wallet",
                        txnIdSt,
                        "50",
                        sizeR,
                        "beginner",
                        "GooglePay"
                );
                mWallet.child(selfUid).child("Transactions").child("history").child(txnIdSt).setValue(send_transaction_class2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    void setRequestCode(int request_code) {
    }

    public interface CallbackResult {
        void sendResult(int requestCode);
    }

}