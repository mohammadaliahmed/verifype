package com.appsinventiv.verifype.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Adapters.LogsAdapter;
import com.appsinventiv.verifype.Models.LogsModel;
import com.appsinventiv.verifype.Models.Sms;
import com.appsinventiv.verifype.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogsReader {

    public void readCallLogs(Activity activity,LogsCallBack callBack){
        StringBuffer stringBuffer = new StringBuffer();
        Cursor managedCursor = activity.managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);

        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        stringBuffer.append("Call Deatils");
        List<LogsModel> logsList = new ArrayList<>();
        if (managedCursor.moveToFirst()) {
            for (int i = 0; i < 50; i++) {
                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String reportDate = df.format(callDayTime);
                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";

                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }
                stringBuffer.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDate + " \nCall duration in sec :--- " + callDuration);
                stringBuffer.append("\n----------------------------------");
//                CommonUtils.showToast(phNumber);
                logsList.add(new LogsModel("" + System.currentTimeMillis(),
                        phNumber, "call", dir + "\n" + callDuration + " s", Long.parseLong(callDate)
                ));
                managedCursor.moveToNext();
            }
        }
        managedCursor.close();
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LogsAdapter adapter = new LogsAdapter(activity, logsList, new LogsAdapter.LogsAdapterCallback() {
            @Override
            public void onLogsSelected(LogsModel model) {
                callBack.OnLogsSelected(model);
                dialog.dismiss();
            }
        });

        View layout = layoutInflater.inflate(R.layout.alert_dialog_logs, null);
        dialog.setContentView(layout);
        TextView alertTitle = layout.findViewById(R.id.alertTitle);
        RecyclerView recyclerView = layout.findViewById(R.id.recyclerView);
        alertTitle.setText("Call Logs");
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        dialog.show();


    }


    @SuppressLint("Range")
    public  void readSms(Activity activity,LogsCallBack callBack){
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri messagea = Uri.parse("content://sms/");
        ContentResolver cr = activity.getContentResolver();
        Cursor c = cr.query(messagea, null, null, null, null);
        activity.startManagingCursor(c);
        int totalSMS = c.getCount();
        List<LogsModel> logsList = new ArrayList<>();
        if (c.moveToFirst()) {
            for (int i = 0; i < 50; i++) {
                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                lstSms.add(objSms);
                logsList.add(new LogsModel("" + System.currentTimeMillis(),
                        objSms.getAddress(), "sms", objSms.getMsg(), Long.parseLong(objSms.getTime())
                ));

                c.moveToNext();
            }
        }
        c.close();
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LogsAdapter adapter = new LogsAdapter(activity, logsList, new LogsAdapter.LogsAdapterCallback() {
            @Override
            public void onLogsSelected(LogsModel model) {
                callBack.OnLogsSelected(model);
//                String msggg = model.getPhone() + "\n" + model.getText();
//                message.setText(msggg);
//                send.performClick();
                dialog.dismiss();
            }
        });
        View layout = layoutInflater.inflate(R.layout.alert_dialog_logs, null);
        dialog.setContentView(layout);
        TextView alertTitle = layout.findViewById(R.id.alertTitle);
        RecyclerView recyclerView = layout.findViewById(R.id.recyclerView);
        alertTitle.setText("SMS Logs");
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        dialog.show();


    }
    public  interface  LogsCallBack{
        public void OnLogsSelected(LogsModel model);
    }
}
