package com.example.alpha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.common.internal.service.Common;
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
import com.example.alpha.JSONParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class ConfirmAmount extends AppCompatActivity implements PaytmPaymentTransactionCallback {
    int payamount;
    String amount="";
    DatabaseReference myWalletAmount;
    String id= UUID.randomUUID().toString();
    String custid="", mid,customer="";
    int liveorderid,uniqueid;
    DatabaseReference paytmchangedb ,mWallet ,mTrasaction , mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_amount);


        {


            Intent intent=getIntent();
            amount=intent.getExtras().getString("Amount");
            mid = intent.getExtras().getString("MID");
            customer= FirebaseAuth.getInstance().getCurrentUser().getUid();
            custid=customer.substring(0,5).toUpperCase();

            if(!mid.isEmpty() && !amount.isEmpty() && !custid.isEmpty())
            {
                sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
                dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else
            {

            }

            myWalletAmount= FirebaseDatabase.getInstance().getReference("Wallet").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }



    }

    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(ConfirmAmount.this);

        //private String orderId , mid, custid, amt;
        String url ="https://dreamwinner.in/paytm/generateChecksum.php";
        String varifyurl = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+"DW"+id.substring(0,5).toUpperCase();
        // "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;
        String CHECKSUMHASH ="";

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {

            try
            {
                JSONParser jsonParser = new JSONParser(ConfirmAmount.this);
                String param=
                        "MID="+mid+
                                "&ORDER_ID="+"DW"+id.substring(0,5).toUpperCase()+
                                "&CUST_ID="+custid+
                                "&CHANNEL_ID=WAP&TXN_AMOUNT="+amount+"&WEBSITE=DEFAULT"+
                                "&CALLBACK_URL="+ varifyurl+"&INDUSTRY_TYPE_ID=Retail";

                JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);
                // yaha per checksum ke saht order id or status receive hoga..
                Log.e("CheckSum result >>",jsonObject.toString());
                if(jsonObject != null){
                    Log.e("CheckSum result >>",jsonObject.toString());
                    try {

                        CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                        Log.e("CheckSum result >>",CHECKSUMHASH);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ","  signup result  " + result);
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
            paramMap.put("ORDER_ID","DW"+id.substring(0,5).toUpperCase());
            paramMap.put("CUST_ID", custid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", amount);
            paramMap.put("WEBSITE", "DEFAULT");
            paramMap.put("CALLBACK_URL" ,varifyurl);
            //paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
            // paramMap.put( "MOBILE_NO" , "9144040888");  // no need
            paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");

            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param "+ paramMap.toString());
            Service.initialize(Order,null);
            // start payment service call here
            Service.startPaymentTransaction(ConfirmAmount.this, true, true,
                    ConfirmAmount.this  );


        }

    }

    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum ", " respon true " + bundle.toString());
        Toast.makeText(this, ""+bundle.toString(), Toast.LENGTH_SHORT).show();

        Toast.makeText(this, "Done: "+ String.valueOf(payamount), Toast.LENGTH_SHORT).show();

        String response=bundle.getString("RESPMSG");
        String responsecode=bundle.getString("RESPCODE");

        if (response.equals("Txn Success") || responsecode.equals("01"))
        {
            mWallet = FirebaseDatabase.getInstance().getReference("Wallet");
            mTrasaction = FirebaseDatabase.getInstance().getReference("Transactions");
            mUser = FirebaseDatabase.getInstance().getReference("Users");


            mWallet.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue("50");

            mUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("payment").setValue("true");
            finish();
        }else
        {
            Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
            finish();
        }





    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  "+ s );
        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true "+ s + "  s1 " + s1);
        Toast.makeText(this, ""+s+" "+s1, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back respon  " );
        Toast.makeText(this, "Canceled transaction", Toast.LENGTH_SHORT).show();


        finish();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel " );
        finish();
        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
    }





}
