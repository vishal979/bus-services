package com.vishal.busservices;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private Button paytmBtn;
//    private Button tejBtn;
    //    private String MerchantMid="rIuKCZ25498236396834";
//    private String MerchantKey="UipnNh0d6Qm#SkI!";
    FirebaseFirestore db;
    String seatAvailable;
    String seatNumber;
    String name;
    String id;
    String busId;
    private static final String TAG = "PaymentActivity";
    public String merchantId, orderId, customerId, channelId, website, callbackUrl, txnAmount, industryType, merchantKey, checkSum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        paytmBtn = (Button) findViewById(R.id.paytmBtn);
//        tejBtn = (Button) findViewById(R.id.tejBtn);
        paytmBtn.setOnClickListener(this);
//        tejBtn.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        busId = getIntent().getStringExtra("busid");
        DocumentReference documentReference = db.collection("busDate").document("wpZLISKskWTXq5K6CvDr");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    txnAmount = documentSnapshot.get("Price").toString();
                    txnAmount = txnAmount + ".00";
                }
            }
        });
    }

    public void addToDatabase(final String busID, final String Name, final String userid, final String depTime) {

        final String timeStamp = new SimpleDateFormat("yyyy/MM/dd : HH:mm:ss").format(Calendar.getInstance().getTime());

        final DocumentReference documentReference = db.collection("buses").document(getIntent().getStringExtra("busid"));
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    seatAvailable = document.getData().get("seats_available").toString();
                    String totalSeats = document.getData().get("seats").toString();
//                    String totalSeats="50";
                    seatNumber = String.valueOf(Integer.parseInt(totalSeats) - Integer.parseInt(seatAvailable) + 1);
                    Map<String, Object> data = new HashMap<>();
                    data.put("seat_number", seatNumber);
                    data.put("bus_id", busID);
                    data.put("dep_time", depTime);
                    data.put("user_name", Name);
                    data.put("book_time", timeStamp);

////                                     Get a new write batch
                    WriteBatch batch = db.batch();
//
                    DocumentReference usersRef = db.collection("Users/" + FirebaseAuth.getInstance().getCurrentUser().getEmail() + "/BookedTickets").document(userid);
                    batch.set(usersRef, data);

                    DocumentReference busRef = db.collection("buses").document(busID);
                    batch.update(busRef, "seats_available", String.valueOf(Integer.parseInt(seatAvailable) - 1));

                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(PaymentActivity.this, MainActivity.class);
                                startActivity(i);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());

                                Toast.makeText(getApplicationContext(), "some this went wrong!! try again" + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Error getting documents", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Error getting documents: ", task.getException());

                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.paytmBtn) {
            getPaytmPayment();
        }
    }




    private void getPaytmPayment(){

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(id) && id.length()==8 && id.charAt(0)=='1' && id.charAt(1)=='1'){


            merchantId = "sNUXVx29909317083112";
            merchantKey = "AaQBvILsmgNdgb4h";
            customerId = generateStringID(name);
            orderId = orderId();
            channelId = "WAP";
            website = "DEFAULT";
            Toast.makeText(this, "transaction amount "+txnAmount, Toast.LENGTH_SHORT).show();
            callbackUrl = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId;
            industryType = "Retail";
            String url = "https://phppayment.herokuapp.com/checksum.php";
            final HashMap<String, String> paytmParams = new HashMap<String, String>();
            paytmParams.put("MID",merchantId);
            paytmParams.put("ORDER_ID",orderId);
            paytmParams.put("CHANNEL_ID",channelId);
            paytmParams.put("CUST_ID",customerId);
            paytmParams.put("TXN_AMOUNT",txnAmount);
            paytmParams.put("WEBSITE",website);
            paytmParams.put("INDUSTRY_TYPE_ID",industryType);
            paytmParams.put("CALLBACK_URL", callbackUrl);

            JSONObject param = new JSONObject(paytmParams);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, param, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse( JSONObject response ) {
                    Log.d("getResponse", String.valueOf(response));
                    checkSum = response.optString("CHECKSUMHASH");

                    if(checkSum.trim().length() != 0){

                        Toast.makeText(getApplicationContext(), "Payment processing!!", Toast.LENGTH_SHORT).show();
                        PaytmPGService Service = PaytmPGService.getProductionService();
                        Map<String, String> paramMap = new HashMap<String, String>();

                        // these are mandatory parameters
                        paramMap.put("CALLBACK_URL", callbackUrl);
                        paramMap.put("CHANNEL_ID",channelId);
                        paramMap.put("CHECKSUMHASH",checkSum);
                        paramMap.put("CUST_ID",customerId);
                        paramMap.put("INDUSTRY_TYPE_ID", industryType);
                        paramMap.put("MID", merchantId);
                        paramMap.put("ORDER_ID", orderId);
                        paramMap.put("TXN_AMOUNT", txnAmount);
                        paramMap.put("WEBSITE", website);


                        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) paramMap);


                        Service.initialize(Order, null);

                        Service.startPaymentTransaction(PaymentActivity.this, true, true,
                                new PaytmPaymentTransactionCallback() {
                                    @Override
                                    public void someUIErrorOccurred(String inErrorMessage) {
                                        Toast.makeText(getApplicationContext(), "some ui error occured ", Toast.LENGTH_LONG).show();
                                        // Some UI Error Occurred in Payment Gateway Activity.
                                        // // This may be due to initialization of views in
                                        // Payment Gateway Activity or may be due to //
                                        // initialization of webview. // Error Message details
                                        // the error occurred.
                                    }


                                    @Override
                                    public void onTransactionResponse( final Bundle inResponse) {
                                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
//                                        if(inResponse.get("STATUS")=="TXN_SUCCESS"){
                                            //work
                                            addToDatabase(getIntent().getStringExtra("busid"), name, id, getIntent().getStringExtra("time"));
                                            Toast.makeText(getApplicationContext(), "Transaction successful generating ticket.", Toast.LENGTH_LONG).show();
//                                        }
//                                        else{
//                                            Toast.makeText(PaymentActivity.this, "the transaction was unsuccessful due to some reason", Toast.LENGTH_SHORT).show();
//                                        }
                                    }

                                    @Override
                                    public void networkNotAvailable() { // If network is not

                                        Toast.makeText(getApplicationContext(), "network not available", Toast.LENGTH_LONG).show();
                                        // method gets called.
                                    }

                                    @Override
                                    public void clientAuthenticationFailed(String inErrorMessage) {
                                        Toast.makeText(getApplicationContext(), "client authentication failed", Toast.LENGTH_LONG).show();
                                        // This method gets called if client authentication
                                        // failed. // Failure may be due to following reasons //
                                        // 1. Server error or downtime. // 2. Server unable to
                                        // generate checksum or checksum response is not in
                                        // proper format. // 3. Server failed to authenticate
                                        // that client. That is value of payt_STATUS is 2. //
                                        // Error Message describes the reason for failure.
                                    }

                                    @Override
                                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                                      String inErrorMessage, String inFailingUrl) {

                                        Toast.makeText(getApplicationContext(), "error loading webpage", Toast.LENGTH_LONG).show();

                                    }

                                    // had to be added: NOTE
                                    @Override
                                    public void onBackPressedCancelTransaction() {

                                        Toast.makeText(PaymentActivity.this,"Back pressed. Transaction cancelled",Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                                        Toast.makeText(getApplicationContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                                    }

                                });
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse( VolleyError error ) {

                    Toast.makeText(getApplicationContext(), "some error has occurred", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            });
            Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
        }

        else {
            Toast.makeText(this,"Please provide Valid Details",Toast.LENGTH_SHORT).show();
        }
    }

    public String orderId() {
        Random r = new Random(System.currentTimeMillis());
        return "ORDER" + (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
    }

    private String generateStringID(String custName) {
        String uuid = UUID.randomUUID().toString() + custName;
        return uuid.replaceAll("-", "");
    }
}
