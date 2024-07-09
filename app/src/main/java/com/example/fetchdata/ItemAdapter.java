package com.example.fetchdata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> items;

    public ItemAdapter(List<Item> items) {
        this.items = items;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView listIdTextView;
        TextView nameTextView;

        public ItemViewHolder(View view) {
            super(view);
            listIdTextView = view.findViewById(R.id.listId);
            nameTextView = view.findViewById(R.id.name);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.listIdTextView.setText("List ID: " + item.getListId());
        holder.nameTextView.setText("Name: " + item.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
