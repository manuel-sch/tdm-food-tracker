package com.example.foodtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductListAdapter  extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {
    private List<DataModel> list;
    private ItemClickListener clickListener;


    public ProductListAdapter(List<DataModel> list, ItemClickListener clickListener) {
        this.list = list;
        this.clickListener  = clickListener;

    }
    @NonNull
    @Override
    public ProductListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.MyViewHolder holder, int position) {
        holder.titleTextView.setText(list.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public MyViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.title);
        }

    }

    public interface ItemClickListener {

        public void onItemClick(DataModel dataModel);
    }
}
