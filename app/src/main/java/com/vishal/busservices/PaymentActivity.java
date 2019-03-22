package com.vishal.busservices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmClientCertificate;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    PaytmPGService service=PaytmPGService.getStagingService();
    HashMap<String,String> paramMap=new HashMap<String, String>();
    String MerchantMid="rIuKCZ25498236396834";
    String MerchantKey="UipnNh0d6Qm#SkI!";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paramMap.put("MID","rIuKCZ25498236396834");
        paramMap.put("WEBSITE","WEBSTAGING");
        paramMap.put("ORDER_ID","always_same");
        paramMap.put("CUST_ID","same_again");
        paramMap.put("CHANNEL_ID","WAP");
        paramMap.put("TXN_AMOUNT","2");
        paramMap.put("INDUSTRY_TYPE_ID" , "Retail");
        paramMap.put("CALLBACK_URL","https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=always_same");

        PaytmOrder order=new PaytmOrder(paramMap);
        service.initialize(order,null);
        service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle inResponse) {

            }

            @Override
            public void networkNotAvailable() {
                Toast.makeText(PaymentActivity.this, "Check Your Connection", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clientAuthenticationFailed(String inErrorMessage) {

            }

            @Override
            public void someUIErrorOccurred(String inErrorMessage) {

            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

            }

            @Override
            public void onBackPressedCancelTransaction() {

            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

            }
        });
//        optional ssl certificste
//        PaytmClientCertificate certificate=new PaytmClientCertificate(())
    }
}
