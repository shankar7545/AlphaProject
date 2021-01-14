package com.example.alpha.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.Model.Transaction_Class;
import com.example.alpha.R;
import com.example.alpha.ViewHolder.JSONParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ConfirmAmount extends AppCompatActivity implements PaytmPaymentTransactionCallback {
    int payamount;
    String amount = "";
    String custid = "", mid, customer = "";
    int liveorderid, uniqueid;
    DatabaseReference paytmchangedb, mWallet, mTrasaction, mUser, mRef, mAutoReferCode;
    DatabaseReference myWalletAmount, mFirebase;

    String selfUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    String id = UUID.randomUUID().toString();
    String idOne = "PV" + id.substring(0, 6).toUpperCase();
    String idTwo = "EX" + id.substring(0, 6).toUpperCase();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_amount);
        myWalletAmount = FirebaseDatabase.getInstance().getReference("Wallet").child(selfUid);
        mWallet = FirebaseDatabase.getInstance().getReference("Wallet");

        {

            Intent intent = getIntent();
            amount = Objects.requireNonNull(intent.getExtras()).getString("Amount");
            mid = intent.getExtras().getString("MID");
            customer = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            custid = customer.substring(0, 5).toUpperCase();

            if (!mid.isEmpty() && !amount.isEmpty() && !custid.isEmpty()) {
                sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
                dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            myWalletAmount = FirebaseDatabase.getInstance().getReference("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }


    }

    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum ", " respon true " + bundle.toString());

        String response = bundle.getString("RESPMSG");
        String responsecode = bundle.getString("RESPCODE");
        String paymentmode = bundle.getString("PAYMENTMODE");


        assert response != null;
        assert responsecode != null;
        if (response.equals("Txn Success") || responsecode.equals("01")) {
            try {
                mFirebase = FirebaseDatabase.getInstance().getReference();
                mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mWallet.child(selfUid).child("Balance").child("mainBalance").setValue("50");
                        mFirebase.child("Users").child(selfUid).child("paymentStatus").setValue("true");

                        String user_userName = Objects.requireNonNull(dataSnapshot.child("Users").child(selfUid)
                                .child("username").getValue()).toString();

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
                        SimpleDateFormat timeformat = new SimpleDateFormat("kk:mm:ss aa");
                        String time = timeformat.format(c.getTime());
                        String date = dateformat.format(c.getTime());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
                        String strDate = sdf.format(c.getTime());


                        Transaction_Class transaction_class = new Transaction_Class(
                                "added",
                                date,
                                time,
                                user_userName,
                                "Wallet",
                                idOne + idTwo,
                                "50",
                                strDate,
                                "beginner",
                                paymentmode
                        );
                        mFirebase.child("Transactions").child(idOne + idTwo).setValue(transaction_class);

                        //AddTransaction in user


                        Transaction_Class send_transaction_class = new Transaction_Class(
                                "added",
                                date,
                                time,
                                user_userName,
                                "Wallet",
                                idOne + idTwo,
                                "50",
                                strDate,
                                "beginner",
                                paymentmode
                        );
                        mWallet.child(selfUid).child("Transactions").child("history").child(idOne + idTwo).setValue(send_transaction_class);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //inflate view
                View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
                ((TextView) custom_view.findViewById(R.id.message)).setText("Transaction Successful!");
                ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done_black_24dp);
                ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(custom_view);
                toast.show();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }

            finish();
        } else {
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  " + s);
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true " + s + "  s1 " + s1);
        Toast.makeText(this, "onErrorLoadingWebPage", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back respon  ");
        String msg = "Transaction failed!";
        showToast(msg);
        finish();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel ");
        finish();
        showToast(s);
    }

    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {

        //private String orderId , mid, custid, amt;
        String url = "https://khelaghorbd.in/paytm/generateChecksum.php";
        String varifyurl = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + idOne + idTwo;
        // "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;
        String CHECKSUMHASH = "";
        private ProgressDialog dialog = new ProgressDialog(ConfirmAmount.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @SafeVarargs
        protected final String doInBackground(ArrayList<String>... alldata) {

            try {
                JSONParser jsonParser = new JSONParser(ConfirmAmount.this);
                String param =
                        "MID=" + mid +
                                "&ORDER_ID=" + idOne + idTwo +
                                "&CUST_ID=" + custid +
                                "&CHANNEL_ID=WAP&TXN_AMOUNT=" + amount + "&WEBSITE=DEFAULT" +
                                "&CALLBACK_URL=" + varifyurl + "&INDUSTRY_TYPE_ID=Retail";

                JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
                // yaha per checksum ke saht order id or status receive hoga..
                Log.e("CheckSum result >>", jsonObject.toString());
                if (jsonObject != null) {
                    Log.e("CheckSum result >>", jsonObject.toString());
                    try {

                        CHECKSUMHASH = jsonObject.has("CHECKSUMHASH") ? jsonObject.getString("CHECKSUMHASH") : "";
                        Log.e("CheckSum result >>", CHECKSUMHASH);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ", "  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            PaytmPGService Service = PaytmPGService.getProductionService();
            // when app is ready to publish use production service
            // PaytmPGService  Service = PaytmPGService.getProductionService();

            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("ORDER_ID", idOne + idTwo);
            paramMap.put("CUST_ID", custid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", amount);
            paramMap.put("WEBSITE", "DEFAULT");
            paramMap.put("CALLBACK_URL", varifyurl);
            //paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
            // paramMap.put( "MOBILE_NO" , "9144040888");  // no need
            paramMap.put("CHECKSUMHASH", CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");

            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param " + paramMap.toString());
            Service.initialize(Order, null);
            // start payment service call here
            Service.startPaymentTransaction(ConfirmAmount.this, true, true,
                    ConfirmAmount.this);


        }


    }

    private void showToast(String msg) {

        try {
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);

            //inflate view
            View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
            ((TextView) custom_view.findViewById(R.id.message)).setText(msg);
            ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close_black_24dp);
            ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.red_400));

            toast.setView(custom_view);
            toast.show();
            finish();

            new Handler().postDelayed(toast::cancel, 800);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
