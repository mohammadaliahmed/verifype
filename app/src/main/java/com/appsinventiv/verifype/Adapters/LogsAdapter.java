package com.appsinventiv.verifype.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Models.LogsModel;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.CommonUtils;
import com.appsinventiv.verifype.VerifyChat;

import java.util.List;


public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {
    Context context;
    List<LogsModel> itemList;
    LogsAdapterCallback callback;

    public LogsAdapter(Context context, List<LogsModel> itemList, LogsAdapterCallback callback) {
        this.context = context;
        this.callback = callback;
        this.itemList = itemList;
    }

    public void setItemList(List<LogsModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.logs_item_layout, parent, false);
        LogsAdapter.ViewHolder viewHolder = new LogsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogsModel item = itemList.get(position);
        holder.phone.setText(item.getPhone());
        holder.details.setText(item.getText());
        holder.time.setText(CommonUtils.getFormattedDate(item.getTime()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onLogsSelected(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView phone, details, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.phone);
            details = itemView.findViewById(R.id.details);
            time = itemView.findViewById(R.id.time);


        }
    }

    public interface LogsAdapterCallback {
        public void onLogsSelected(LogsModel model);
    }


}
