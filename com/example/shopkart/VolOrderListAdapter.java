package com.example.shopkart;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class VolOrderListAdapter extends RecyclerView.Adapter<VolOrderListAdapter.OrdersViewHolder> {
    public List<JSONObject> items;
    OnItemAcceptListener clickListener;

    public VolOrderListAdapter(List<JSONObject> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.volunteer_view_order,parent,false);
        return new OrdersViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        try {
            holder.title.setText(items.get(position).getString("Fname") + " " + items.get(position).getString("Sname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setClickListener(OnItemAcceptListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        private final Context context;
        Button accept;
        private VolOrderListAdapter adapter;
        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = itemView.findViewById(R.id.txtTitle);
            accept = itemView.findViewById(R.id.btnAccept);
            accept.setOnClickListener(this);
            itemView.findViewById(R.id.btnMore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, VolPreviewOrder.class);
                    int index = 0;
                    for(int j = 0;  j< adapter.items.size(); j++){
                        try {
                            String FullName = adapter.items.get(j).getString("Fname") + " " + adapter.items.get(j).getString("Sname");
                            if(FullName.equals(title.getText().toString())){
                                index = j;
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    i.putExtra("name",title.getText().toString());
                    i.putExtra("json",adapter.items.get(index).toString());
                    context.startActivity(i);
                }
            });
        }

        public OrdersViewHolder linkAdapter(VolOrderListAdapter adapter){
            this.adapter = adapter;
            return this;
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null){
                clickListener.onAccept(view, getAdapterPosition());
            }
        }
    }
}

