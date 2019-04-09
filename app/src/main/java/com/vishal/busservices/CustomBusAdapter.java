package com.vishal.busservices;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.vishal.busservices.ManageAdminActivity.recyclerView;

public class CustomBusAdapter extends RecyclerView.Adapter<CustomBusAdapter.ViewHolder> {

    private Context context;
    private List<DetailBusClass> busList;
    int mExpandedPosition = -1;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final DetailBusClass bus=busList.get(position);
        holder.timeTV.setText(bus.getTime());
        holder.seatsAvailableTV.setText(""+bus.getSeats());
        if(bus.getSeats()==0) {
            holder.name.setVisibility(View.INVISIBLE);
            holder.id.setVisibility(View.INVISIBLE);
            holder.bookNowBtn.setVisibility(View.INVISIBLE);
            holder.noSeat.setVisibility(View.VISIBLE);
        }
        holder.bookNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bus.getSeats()==0){
                    Toast.makeText(context, "Sorry all the seats are already booked!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(context,PaymentActivity.class);
                    intent.putExtra("BusId",bus.getBusId());
                    intent.putExtra("time",bus.getTime());
                    intent.putExtra("name", holder.name.getText().toString());
                    intent.putExtra("id", holder.id.getText().toString());
                    intent.putExtra("seatsAvailable",bus.getSeats());
                    context.startActivity(intent);
                }
            }
        });

        final boolean isExpanded = position==mExpandedPosition;
        holder.expandArea.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        //holder.drop_down_up.setImageResource(R.drawable.down_arrow_hide);
        holder.itemView.setActivated(isExpanded);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                if(isExpanded) {
                    holder.drop_down_up.setImageResource(R.drawable.down_arrow_hide);
                }
                else {
                    holder.drop_down_up.setImageResource(R.drawable.up_arrow_show);
                }
                //TransitionManager.beginDelayedTransition(recyclerView);
                notifyDataSetChanged();
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
        RelativeLayout expandArea;
        ImageView drop_down_up;
        EditText name, id;
        TextView noSeat;


        public ViewHolder(View itemView) {
            super(itemView);
            seatsAvailableTV=(TextView) itemView.findViewById(R.id.seatsAvailableTextView);
            timeTV=(TextView) itemView.findViewById(R.id.timeTextView);
            bookNowBtn=(Button) itemView.findViewById(R.id.bookNowBtn);
            expandArea = itemView.findViewById(R.id.expand_area);
            drop_down_up = itemView.findViewById(R.id.drop_down);
            name = itemView.findViewById(R.id.enter_name);
            id = itemView.findViewById(R.id.enter_id);
            noSeat = itemView.findViewById(R.id.no_seat);
        }
    }
}
