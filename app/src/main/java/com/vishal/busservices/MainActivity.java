package com.vishal.busservices;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    NavigationView navigationView;
    ImageView drawerPullIV;
    DrawerLayout drawerLayout;

    TextView nextBusTextView;

    FirebaseFirestore db=FirebaseFirestore.getInstance();

    String nextBusDate="";

    private Button bookTicket1, bookTicket2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView=(NavigationView) findViewById(R.id.app_drawer_nv);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);
        drawerPullIV=(ImageView) findViewById(R.id.drawerPullIV);
        drawerPullIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        nextBusTextView=(TextView) findViewById(R.id.nextBusTextView);

        bookTicket1=(Button) findViewById(R.id.bookTicketBtn1);
        bookTicket2=(Button) findViewById(R.id.bookTicketBtn2);

        bookTicket1.setOnClickListener(this);
        bookTicket2.setOnClickListener(this);


        DocumentReference documentReference=db.collection("busesandtimings").document("Date").collection("TIming").document("M0UmCxviWFxRglOrqmz1");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    nextBusDate=doc.get("Date").toString();
                    nextBusTextView.setText("Buses for date "+nextBusDate);
                    Toast.makeText(MainActivity.this, "Dateeeeee:  "+doc.get("Date"), Toast.LENGTH_SHORT).show();
                }
                else{
                }
            }
        });
    }
    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        LoginActivity.mGoogleSignInClient.signOut();
        Toast.makeText(MainActivity.this, "signed out", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logOut){
            signOut();
        }
        if(item.getItemId()==R.id.aboutUs){
            Toast.makeText(this, "aboutus", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId()==R.id.previousRides){
            Toast.makeText(this, "previous rides", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.bookTicketBtn1){
            Intent intent=new Intent(MainActivity.this,BookTicketActivity.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.bookTicketBtn2){
            Intent intent=new Intent(MainActivity.this,BookTicketActivity.class);
            startActivity(intent);
        }
    }
}
