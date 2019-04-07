package com.vishal.busservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BookTicketActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText nameEt,idET;
    private Button paymentBtn;
    private String name,id;
    private String busId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookticket);
        busId=getIntent().getStringExtra("busid");
        nameEt=(EditText) findViewById(R.id.nameET);
        idET=(EditText) findViewById(R.id.idET);
        paymentBtn=(Button) findViewById(R.id.paymentBtn);
        paymentBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.paymentBtn){
            if(idET.getText().toString().equals("") || nameEt.getText().toString().equals("")){
                Toast.makeText(this, "fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else{
                name=nameEt.getText().toString();
                id=idET.getText().toString();
                Intent intent=new Intent(BookTicketActivity.this, PaymentActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("id",id);
                intent.putExtra("busid",busId);
                intent.putExtra("time",getIntent().getStringExtra("time"));
                intent.putExtra("busid",getIntent().getStringExtra("BusId"));
                startActivity(intent);
            }
        }
    }
}
