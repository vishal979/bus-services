package com.vishal.busservices;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageAdminActivity extends AppCompatActivity implements View.OnClickListener{

    public static FirebaseFirestore db;
    public static RecyclerView recyclerView;
    public static ArrayList<String> admins;
    private Button addAdminBtn;
    public static CustomManageAdminAdapter customManageAdminAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_admin);
        admins=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        addAdminBtn=(Button) findViewById(R.id.addAdminBtn);
        addAdminBtn.setOnClickListener(this);
        db=FirebaseFirestore.getInstance();
        db.collection("admins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        admins.add(documentSnapshot.get("admin").toString());
                    }
                    customManageAdminAdapter=new CustomManageAdminAdapter(getBaseContext(),admins,ManageAdminActivity.this,ManageAdminActivity.this);
                    recyclerView.setAdapter(customManageAdminAdapter);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.addAdminBtn){
            LayoutInflater layoutInflater=LayoutInflater.from(this);
            View promptView=layoutInflater.inflate(R.layout.layout_prompt,null);
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setView(promptView);
            final EditText emailET=(EditText) promptView.findViewById(R.id.emailET);
            alertDialog.setCancelable(true).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String emailToAdd=emailET.getText().toString();
                    Map<String,Object> data=new HashMap<>();
                    WriteBatch batch=db.batch();
                    data.put("admin",emailToAdd);
                    DocumentReference documentReference=db.collection("admins").document(emailToAdd);
                    batch.set(documentReference,data);
                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ManageAdminActivity.this, "added successfully", Toast.LENGTH_SHORT).show();
                            admins=new ArrayList<>();
                            db.collection("admins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                                            admins.add(documentSnapshot.get("admin").toString());
                                        }
                                        CustomManageAdminAdapter customManageAdminAdapter=new CustomManageAdminAdapter(getBaseContext(),admins,ManageAdminActivity.this,ManageAdminActivity.this);
                                        recyclerView.setAdapter(customManageAdminAdapter);
                                    }
                                }
                            });
                        }
                    });
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog dialog1=alertDialog.create();
            dialog1.show();
        }
    }

//    public static void notifyDataset(){
//        db.collection("admins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(DocumentSnapshot documentSnapshot : task.getResult()){
//                        admins.add(documentSnapshot.get("admin").toString());
//                    }
//                }
//            }
//        });
//    }

}
