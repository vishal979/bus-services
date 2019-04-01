package com.vishal.busservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomManageAdminAdapter extends RecyclerView.Adapter<CustomManageAdminAdapter.ViewHolder> {

    Context context,context2;
    private ArrayList<String> admins;
    FirebaseFirestore db;
    ManageAdminActivity manageAdminActivity;

    public CustomManageAdminAdapter(Context context, ArrayList<String> admins,Context context2,ManageAdminActivity manageAdminActivity){
        this.context=context;
        this.admins=admins;
        this.context2=context2;
        this.manageAdminActivity=manageAdminActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_admin_manage_admins,null);
        db=FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String email=admins.get(position);
        holder.emailTV.setText(""+email);
        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context2);
                builder.setMessage("Are you sure you want to delete the admin?");
                builder.setTitle("Delete");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("admins").document(admins.get(position)).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "admin successfully deleted!", Toast.LENGTH_SHORT).show();
//                                manageAdminActivity.notifyDataset();
                                admins=new ArrayList<>();
                                db.collection("admins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                                admins.add(documentSnapshot.get("admin").toString());
                                            }
                                        }
                                    }
                                });
                                manageAdminActivity.customManageAdminAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog1=builder.create();
                dialog1.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return admins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView emailTV;
        ImageView deleteIV;

        public ViewHolder(View itemView) {
            super(itemView);
            emailTV=(TextView) itemView.findViewById(R.id.emailTV);
            deleteIV=(ImageView) itemView.findViewById(R.id.deleteIV);
        }
    }
}
