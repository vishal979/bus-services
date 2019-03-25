package com.vishal.busservices;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PreviousTicketActivity extends AppCompatActivity {

    TextView notBookedTV;
    List<Person> personList;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    private static final String TAG = "PreviousTicketActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previousticket);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db=FirebaseFirestore.getInstance();
        db.collection("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/BookedTickets").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    personList=new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        Log.d(TAG, documentSnapshot.toString());
                        personList.add(new Person(documentSnapshot.getId(), documentSnapshot.getData().get("user_name").toString(), documentSnapshot.getData().get("dep_time").toString(), documentSnapshot.getData().get("seat_number").toString(), documentSnapshot.getData().get("book_time").toString(), documentSnapshot.getData().get("bus_id").toString()));
                    }
                    CustomPersonAdapter adapter=new CustomPersonAdapter(getApplicationContext(),personList);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
//        notBookedTV=(TextView) findViewById(R.id.notBookedTV);
//        notBookedTV.setVisibility(View.INVISIBLE);
    }
}
