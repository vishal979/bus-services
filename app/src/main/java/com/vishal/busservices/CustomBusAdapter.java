package com.vishal.busservices;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CustomBusAdapter extends RecyclerView.Adapter<CustomBusAdapter.ViewHolder> {

    private Context context;
    private List<DetailBusClass> busList;

    public CustomBusAdapter(Context context, List<DetailBusClass> busList){
        this.context=context;
        this.busList=busList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.custom_bus_layout,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DetailBusClass bus=busList.get(position);
        holder.timeTV.setText(bus.getTime());
        holder.seatsAvailableTV.setText(""+bus.getSeats());
        holder.bookNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bus.getSeats()==0){
                    Toast.makeText(context, "Sorry all the seats are already booked!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(context,BookTicketActivity.class);
                    intent.putExtra("BusId",bus.getBusId());
                    intent.putExtra("time",bus.getTime());
                    intent.putExtra("seatsAvailable",bus.getSeats());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        
        Button bookNowBtn;
        TextView seatsAvailableTV,timeTV;
        
        public ViewHolder(View itemView) {
            super(itemView);
            seatsAvailableTV=(TextView) itemView.findViewById(R.id.seatsAvailableTextView);
            timeTV=(TextView) itemView.findViewById(R.id.timeTextView);
            bookNowBtn=(Button) itemView.findViewById(R.id.bookNowBtn);
        }
    }
}
