package com.vishal.busservices;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    ImageView drawerPullIV;
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;

    FirebaseFirestore db;

    List<DetailBusClass> busList;
    TextView nextBusTV,UserTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView=(NavigationView) findViewById(R.id.app_drawer_nv);
        View headerView=navigationView.getHeaderView(0);
        UserTV=(TextView) headerView.findViewById(R.id.navigationUsernameTV);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);
        drawerPullIV=(ImageView) findViewById(R.id.drawerPullIV);
        drawerPullIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        nextBusTV=(TextView) findViewById(R.id.nextBusTextView);

        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String[] parts=email.split("@");
        String username=parts[0];
        UserTV.setText(""+username);

        db=FirebaseFirestore.getInstance();

        DocumentReference documentReference=db.collection("busDate").document("wpZLISKskWTXq5K6CvDr");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot=task.getResult();
                    nextBusTV.setText("Buses For Date "+snapshot.get("Date"));
                }
            }
        });

        db.collection("buses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    busList=new ArrayList<>();
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        busList.add(new DetailBusClass(documentSnapshot.getData().get("time").toString(),Integer.parseInt(documentSnapshot.getData().get("seats_available").toString()),documentSnapshot.getId()));
                    }
                    CustomBusAdapter customBusAdapter=new CustomBusAdapter(getBaseContext(),busList);
                    recyclerView.setAdapter(customBusAdapter);

                }
            }
        });


    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        LoginActivity.mGoogleSignInClient.signOut();
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
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("This app was made by Vishal Sharma ID-11641150 3rd year BTECH IIT BHILAI");
            builder.setTitle("About");
            builder.setIcon(R.drawable.ic_bubble).show();
        }
        if(item.getItemId()==R.id.previousRides){
            Intent intent=new Intent(MainActivity.this,PreviousTicketActivity.class);
            startActivity(intent);
        }
        return true;
    }


}
