package com.appsinventiv.verifype.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Models.ChatModel;
import com.appsinventiv.verifype.Models.SupportChatModel;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.CommonUtils;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.bumptech.glide.Glide;

import java.util.List;

public class SupportChatAdapter extends RecyclerView.Adapter<SupportChatAdapter.ViewHolder> {
    Context context;
    List<SupportChatModel> itemList;
    public int RIGHT_CHAT = 1;
    public int LEFT_CHAT = 0;
    public SupportChatAdapter(Context context, List<SupportChatModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemList(List<SupportChatModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        SupportChatModel model = itemList.get(position);
        if (model.getSenderId() != null) {
            if (model.getSenderId().equals(SharedPrefs.getUser().getPhone())) {
                return RIGHT_CHAT;
            } else {
                return LEFT_CHAT;
            }
        }
        return -1;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        if (viewType == RIGHT_CHAT) {
            View view = LayoutInflater.from(context).inflate(R.layout.support_right_chat_layout, parent, false);
            viewHolder = new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.support_left_chat_layout, parent, false);
            viewHolder = new ViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupportChatModel model=itemList.get(position);
        holder.message.setText(model.getMessage());
        holder.time.setText(CommonUtils.getFormattedDate(model.getTime()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView message,time;
        ImageView picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.picture);
            time = itemView.findViewById(R.id.time);
            message = itemView.findViewById(R.id.message);

        }
    }

}
