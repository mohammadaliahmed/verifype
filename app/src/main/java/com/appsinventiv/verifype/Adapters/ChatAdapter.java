package com.appsinventiv.verifype.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Models.ChatModel;
import com.appsinventiv.verifype.Models.ObjectModel;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.CommonUtils;
import com.appsinventiv.verifype.Utils.SharedPrefs;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    ChatAdapterCallbacks callbacks;
    Context context;
    List<ChatModel> itemList;
    public int RIGHT_CHAT = 1;
    public int LEFT_CHAT = 0;

    public ChatAdapter(Context context, List<ChatModel> itemList, ChatAdapterCallbacks callbacks) {
        this.callbacks = callbacks;
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemList(List<ChatModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel model = itemList.get(position);
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
            View view = LayoutInflater.from(context).inflate(R.layout.right_chat_layout, parent, false);
            viewHolder = new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.left_chat_layout, parent, false);
            viewHolder = new ViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatModel model = itemList.get(position);
        if (getItemViewType(position) == LEFT_CHAT) {
            holder.message.setText(model.getMessage());
            holder.time.setText(CommonUtils.getFormattedDate(model.getTime()));

            if (model.getMsgType().equalsIgnoreCase("object")) {
                holder.leftRecycler.setVisibility(View.VISIBLE);
                holder.leftRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                ObjectsButtonAdapter adapter = new ObjectsButtonAdapter(context, model.getObjectList(), new ObjectsButtonAdapter.ObjectsAdapterCallbacks() {
                    @Override
                    public void onOptionClicked(ObjectModel model) {
                        callbacks.onSelected(model);
                    }
                });
                holder.leftRecycler.setAdapter(adapter);
            } else {
                holder.leftRecycler.setVisibility(View.GONE);

            }

        } else {
            holder.leftRecycler.setVisibility(View.GONE);
            holder.message.setText(model.getMessage());
            holder.time.setText(CommonUtils.getFormattedDate(model.getTime()));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView message, time;
        ImageView picture;
        RecyclerView leftRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.picture);
            time = itemView.findViewById(R.id.time);
            leftRecycler = itemView.findViewById(R.id.leftRecycler);
            message = itemView.findViewById(R.id.message);

        }
    }

    public interface ChatAdapterCallbacks {
        public void onSelected(ObjectModel model);
    }
}
