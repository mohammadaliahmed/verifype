package com.appsinventiv.verifype.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.VerifyChat;

import java.util.List;


public class VerifyRecyclerAdapter extends RecyclerView.Adapter<VerifyRecyclerAdapter.ViewHolder> {
    Context context;
    List<String> itemList;

    public VerifyRecyclerAdapter(Context context, List<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemList(List<String> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.verify_item_layout, parent, false);
        VerifyRecyclerAdapter.ViewHolder viewHolder = new VerifyRecyclerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item=itemList.get(position);
        holder.text.setText(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(context, VerifyChat.class);
                i.putExtra("option",item);
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);


        }
    }


}
