package com.vishal.busservices;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageBusActivity extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView recyclerView;
    private ArrayList<DetailBusClass> busList;
    TextView nextBusTV;
    CustomAdminBusAdapter customAdminBusAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);
        db= FirebaseFirestore.getInstance();
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nextBusTV=(TextView) findViewById(R.id.nextBusTextView);

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
                    customAdminBusAdapter=new CustomAdminBusAdapter(getBaseContext(),busList);
                    recyclerView.setAdapter(customAdminBusAdapter);
                }
            }
        });
    }
}
