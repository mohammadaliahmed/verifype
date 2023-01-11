package com.appsinventiv.verifype.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Activites.PsychologyQuestions;
import com.appsinventiv.verifype.Models.LogsModel;
import com.appsinventiv.verifype.Models.PsychologyQuestion;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PsychologyQuestionsAdapter extends RecyclerView.Adapter<PsychologyQuestionsAdapter.ViewHolder> {
    Context context;
    List<PsychologyQuestion> itemList;

    public PsychologyQuestionsAdapter(Context context, List<PsychologyQuestion> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemList(List<PsychologyQuestion> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.psy_question_item_layout, parent, false);
        PsychologyQuestionsAdapter.ViewHolder viewHolder = new PsychologyQuestionsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PsychologyQuestion item = itemList.get(position);
        holder.question.setText(item.getQuestion());
        holder.serial.setText((position + 1) + ") ");
        int buttons = item.getOptions().size();

        if (item.getOptionsType().equalsIgnoreCase("radio_button")) {
            try {
                for (int i = 0; i <= buttons; i++) {
                    RadioButton rbn = new RadioButton(context);
                    rbn.setId(View.generateViewId());
                    rbn.setText(item.getOptions().get(i));
                    holder.radioGroup.addView(rbn);
                    int finalI = i;
                    rbn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            List<String> list  = new ArrayList<>();
                            list.add("" + (finalI + 1));
                            PsychologyQuestions.finalMap.put("question" + (position + 1), list);
                        }

                    });
                }
            } catch (Exception e) {

            }
        } else if (item.getOptionsType().equalsIgnoreCase("checkbox")) {
            try {
                for (int i = 0; i <= buttons; i++) {
                    CheckBox rbn = new CheckBox(context);
                    rbn.setId(View.generateViewId());
                    rbn.setText(item.getOptions().get(i));
                    holder.checkboxView.addView(rbn);
                    int finalI = i;
                    rbn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                List<String> list = PsychologyQuestions.finalMap.get("question" + (position + 1));
                                if (list == null) {
                                    list = new ArrayList<>();
                                }
                                list.add("" + (finalI + 1));
                                PsychologyQuestions.finalMap.put("question" + (position + 1), list);

                            } else {
                                List<String> list = PsychologyQuestions.finalMap.get("" + (position + 1));
                                if (list != null) {
                                    list.remove("" + (finalI + 1));
                                    PsychologyQuestions.finalMap.put("question" + (position + 1), list);
                                }
                            }

                        }
                    });
                }
            } catch (Exception e) {

            }
        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serial, question;
        RecyclerView recycler;
        RadioGroup radioGroup;
        LinearLayout checkboxView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            serial = itemView.findViewById(R.id.serial);
            recycler = itemView.findViewById(R.id.recycler);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            checkboxView = itemView.findViewById(R.id.checkboxView);


        }
    }


}
