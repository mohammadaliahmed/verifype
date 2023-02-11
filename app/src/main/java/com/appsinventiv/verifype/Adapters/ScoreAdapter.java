package com.appsinventiv.verifype.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Models.Compromise;
import com.appsinventiv.verifype.Models.ObjectModel;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.Constants;

import java.util.List;


public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {
    Context context;
    List<Compromise> itemList;

    public ScoreAdapter(Context context, List<Compromise> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemList(List<Compromise> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.compromise_item_layout, parent, false);
        ScoreAdapter.ViewHolder viewHolder = new ScoreAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Compromise item = itemList.get(position);
//        if (item.getFieldColor().equalsIgnoreCase("red")) {
//            holder.rootView.setBackground(context.getResources().getDrawable(R.drawable.compromise_red_bg));
//        } else if (item.getFieldColor().equalsIgnoreCase("green")) {
//            holder.rootView.setBackground(context.getResources().getDrawable(R.drawable.compromise_green_bg));
//        } else if (item.getFieldColor().equalsIgnoreCase("blue")) {
//            holder.rootView.setBackground(context.getResources().getDrawable(R.drawable.compromise_blue_bg));
//        } else if (item.getFieldColor().equalsIgnoreCase("orange")) {
//            holder.rootView.setBackground(context.getResources().getDrawable(R.drawable.compromise_orange_bg));
//        } else {
//            holder.rootView.setBackground(context.getResources().getDrawable(R.drawable.compromise_default_bg));
//
//        }

        holder.fieldName.setText(item.getFieldName());
        holder.fieldScore.setText(""+item.getFieldScore());
        String findings = String.join("\n", item.getFieldFindings());
        holder.fieldFindings.setText(findings);
        String tips = String.join("\n", item.getFieldTips());
        holder.fieldTips.setText(tips);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        RelativeLayout rootView;
        TextView fieldName,fieldScore,fieldTips,fieldFindings;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            rootView = itemView.findViewById(R.id.rootView);
            fieldName = itemView.findViewById(R.id.fieldName);
            fieldScore = itemView.findViewById(R.id.fieldScore);
            fieldTips = itemView.findViewById(R.id.fieldTips);
            fieldFindings = itemView.findViewById(R.id.fieldFindings);


        }
    }

}
