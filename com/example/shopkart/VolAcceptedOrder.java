package com.example.shopkart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class VolAcceptedOrder extends RecyclerView.Adapter<VolAcceptedOrder.AcceptedOrderViewHolder> {
    public List<JSONObject> items;
    private OnItemClickListener clickListener;

    public VolAcceptedOrder(List<JSONObject> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public AcceptedOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.volunteer_accepted_order,parent,false);
        return new AcceptedOrderViewHolder(view).linkAdapter(this);
    }

    public void setClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedOrderViewHolder holder, int position) {
        String sItems = "";
        try {
            for(int i = 0; i < items.size()-1; i++){
                JSONObject temp = items.get(i);
                sItems += temp.getString("Content") + ": " + temp.getString("Quantity") + "\n";
            }
            holder.items.setText(sItems);
            JSONObject cust = items.get(items.size()-1);
            String title, address, instructions;
            title = cust.getString("Fname") + " " + cust.getString("Sname");
            address = cust.getString("Address");
            instructions = cust.getString("Instruction");
            holder.title.setText(title);
            holder.address.setText(address);
            holder.instructions.setText(instructions);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class AcceptedOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView items;
        TextView address;
        TextView instructions;
        Button completeOrder;
        private final Context context;

        private VolAcceptedOrder adapter;
        public AcceptedOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = itemView.findViewById(R.id.txtTitle);
            items = itemView.findViewById(R.id.txtPreviewItems);
            address = itemView.findViewById(R.id.txtAddress);
            instructions = itemView.findViewById(R.id.txtInstructions);
            completeOrder = itemView.findViewById(R.id.btnComplete);
            completeOrder.setOnClickListener(this);
        }

        public AcceptedOrderViewHolder linkAdapter(VolAcceptedOrder adapter){
            this.adapter = adapter;
            return this;
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null){
                clickListener.onClick(view, 0);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}


