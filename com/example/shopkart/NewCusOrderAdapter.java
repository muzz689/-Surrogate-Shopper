package com.example.shopkart;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NewCusOrderAdapter extends RecyclerView.Adapter<OrderViewAdapter>{

    List<newItem> items;

    public NewCusOrderAdapter(List<newItem> items){
        this.items = items;
    }
    @NonNull
    @Override
    public OrderViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_order_item, parent, false);
        return new OrderViewAdapter(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewAdapter holder, int position) {
        holder.item.setText(items.get(position).getContent());
        holder.quantity.setText(items.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class OrderViewAdapter extends RecyclerView.ViewHolder {
    private  NewCusOrderAdapter adapter;
    TextView item;
    TextView quantity;
    public OrderViewAdapter(@NonNull View itemView) {
        super(itemView);
        item = itemView.findViewById(R.id.txtItem);
        quantity = itemView.findViewById(R.id.txtQuantity);
        itemView.findViewById(R.id.btnRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.items.remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
            }
        });
   }

    public OrderViewAdapter linkAdapter(NewCusOrderAdapter adapter){
        this.adapter = adapter;
        return this;
    }
}
