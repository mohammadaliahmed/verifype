package com.appsinventiv.verifype.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Models.ObjectModel;
import com.appsinventiv.verifype.R;

import java.util.List;


public class ObjectsButtonAdapter extends RecyclerView.Adapter<ObjectsButtonAdapter.ViewHolder> {
    Context context;
    List<ObjectModel> itemList;
    ObjectsAdapterCallbacks callbacks;

    public ObjectsButtonAdapter(Context context, List<ObjectModel> itemList,ObjectsAdapterCallbacks callbacks) {
        this.context = context;
        this.callbacks = callbacks;
        this.itemList = itemList;
    }

    public void setItemList(List<ObjectModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.object_btn_item_layout, parent, false);
        ObjectsButtonAdapter.ViewHolder viewHolder = new ObjectsButtonAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ObjectModel item=itemList.get(position);
        holder.button.setText(item.getVarDesc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbacks.onOptionClicked(item);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);


        }
    }
    public interface ObjectsAdapterCallbacks {
        public void onOptionClicked(ObjectModel model);
    }


}
