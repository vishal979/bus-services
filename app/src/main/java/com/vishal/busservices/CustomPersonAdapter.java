package com.vishal.busservices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CustomPersonAdapter extends RecyclerView.Adapter<CustomPersonAdapter.ViewHolder> {

    private Context context;
    private List<Person> personList;

    public CustomPersonAdapter(Context context,List<Person> personList){
        this.context=context;
        this.personList=personList;
    }

    @NonNull
    @Override
    public CustomPersonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.custom_bus_layout,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomPersonAdapter.ViewHolder holder, int position) {
        Person person=personList.get(position);
        holder.idTV.setText(person.getId());
        holder.nameTV.setText(person.getName());
        holder.depTimeTV.setText(person.getDepartureTime());
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView idTV,nameTV,depTimeTV;
        public ViewHolder(View itemView) {
            super(itemView);
            idTV=(TextView) itemView.findViewById(R.id.IDTextView);
            nameTV=(TextView) itemView.findViewById(R.id.nameTextView);
            depTimeTV=(TextView) itemView.findViewById(R.id.depTimeTextView);
        }
    }
}
