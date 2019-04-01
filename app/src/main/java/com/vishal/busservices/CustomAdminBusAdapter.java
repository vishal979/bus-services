package com.vishal.busservices;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CustomAdminBusAdapter extends RecyclerView.Adapter<CustomAdminBusAdapter.ViewHolder> {

    private Context context;
    private List<DetailBusClass> busList;

    public CustomAdminBusAdapter(Context context, List<DetailBusClass> busList){
        this.context=context;
        this.busList=busList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_admin_manage_buses,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdminBusAdapter.ViewHolder holder, final int position) {
        final DetailBusClass bus=busList.get(position);
        holder.seatsBookedTV.setText(""+bus.getSeats());
        holder.depTimeTV.setText(""+bus.getTime());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,MainActivity.class);
                intent.putExtra("busid",busList.get(position).getBusId());
                intent.putExtra("busseats",busList.get(position).getSeats());

            }
        });
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView depTimeTV,seatsBookedTV;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            depTimeTV=(TextView) itemView.findViewById(R.id.depTimeTextView);
            seatsBookedTV=(TextView) itemView.findViewById(R.id.seatsBookedTextView);
            cardView=(CardView) itemView.findViewById(R.id.manageBusCardView);
        }
    }
}
