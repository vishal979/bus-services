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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    ImageView drawerPullIV;
    DrawerLayout drawerLayout;
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
}
