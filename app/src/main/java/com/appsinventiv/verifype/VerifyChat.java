package com.appsinventiv.verifype;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Adapters.ChatAdapter;
import com.appsinventiv.verifype.Adapters.LogsAdapter;
import com.appsinventiv.verifype.Models.ChatModel;
import com.appsinventiv.verifype.Models.LogsModel;
import com.appsinventiv.verifype.Models.ObjectModel;
import com.appsinventiv.verifype.Models.Sms;
import com.appsinventiv.verifype.Utils.CommonUtils;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class VerifyChat extends AppCompatActivity {

    String option;
    private DatabaseReference mDatabase;
    private List<ObjectModel> objectList = new ArrayList<>();
    RecyclerView recyclerView;
    ChatAdapter adapter;
    private List<ChatModel> chatList = new ArrayList<>();
    //    HashMap<Integer, ObjectModel> sequenceMap = new HashMap<>();
    List<ObjectModel> sequenceMap = new ArrayList<>();
    int sequenceCounter = 0;
    ImageView send;
    EditText message;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_chat);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
            this.setTitle("Verify Screen");

        }
        getPermissions();

        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mDatabase = FirebaseDatabase.getInstance("https://verifipe-default-rtdb.firebaseio.com/").getReference();
        message.setEnabled(false);

        option = getIntent().getStringExtra("option");

        addUserMsg(option);//firstMsg

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().length() == 0) {
                    message.setError("Cant send empty message");
                } else {
                    msg = message.getText().toString();
                    message.setText("");

                    addUserMsg(msg);//msg from chat window
                    sequenceCounter += 1;
                    if (sequenceCounter < sequenceMap.size()) {
                        addAdminMsg(sequenceMap.get(sequenceCounter).getVarDesc(), "text", new ArrayList<>());


                    }
                    adapter.setItemList(chatList);

                    recyclerView.scrollToPosition(chatList.size() - 1);


                }
            }
        });

        adapter = new ChatAdapter(this, chatList, new ChatAdapter.ChatAdapterCallbacks() {
            @Override
            public void onSelected(ObjectModel model) {
                if (model.getSequence() == 0) {
                    addUserMsg(model.getVarDesc());
                    getSecondSequence(model);
                } else {
                    addAdminMsg(sequenceMap.get(sequenceCounter).getVarDesc(), "text", new ArrayList<>());
                    adapter.setItemList(chatList);
                    recyclerView.scrollToPosition(chatList.size() - 1);
                    sequenceCounter = 1;
                }

            }
        });
        recyclerView.setAdapter(adapter);
        getFirstSequence();
//        getCallDeatils();
//        readSMS();


    }

    private void addAdminMsg(String msgToAd, String type, List<ObjectModel> objectList) {
        chatList.add(new ChatModel(
                "" + System.currentTimeMillis(),
                msgToAd, "admin",
                "user", type, objectList,
                System.currentTimeMillis()
        ));


    }

    private void addUserMsg(String msgToAdd) {
        chatList.add(new ChatModel(
                "" + System.currentTimeMillis(), msgToAdd, SharedPrefs.getUser().getPhone(),
                "admin", "text", new ArrayList<>(),
                System.currentTimeMillis()
        ));
    }

    private void getSecondSequence(ObjectModel model) {
        mDatabase.child(option).child(model.getVarName()).child(model.getVarValue()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        ObjectModel model1 = snapshot1.getValue(ObjectModel.class);
                        if (model1 != null) {
                            sequenceMap.add(model1);

                        }

                    }
                    message.setEnabled(true);
                    if (sequenceMap.get(sequenceCounter).getVarValue().equalsIgnoreCase("auto")) {
                        if (model.getVarName().contains("sms")) {
                            readSMS();
                        } else if (model.getVarName().contains("call")) {
                            getCallDeatils();
                        }
                    } else {
                        addAdminMsg(sequenceMap.get(sequenceCounter).getVarDesc(), "text", new ArrayList<>());

                        adapter.setItemList(chatList);
                        recyclerView.scrollToPosition(chatList.size() - 1);
                        sequenceCounter += 1;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getFirstSequence() {
        mDatabase.child(option).child("main").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        ObjectModel model = snapshot1.getValue(ObjectModel.class);
                        objectList.add(model);
                    }
                    addAdminMsg("Choose Option", "object", objectList);

                    adapter.setItemList(chatList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @SuppressLint("Range")
    private void readSMS() {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri messagea = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(messagea, null, null, null, null);
        startManagingCursor(c);
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
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LogsAdapter adapter = new LogsAdapter(this, logsList, new LogsAdapter.LogsAdapterCallback() {
            @Override
            public void onLogsSelected(LogsModel model) {
                String msggg = model.getPhone() + "\n" + model.getText();
                message.setText(msggg);
                send.performClick();
                dialog.dismiss();
            }
        });

        View layout = layoutInflater.inflate(R.layout.alert_dialog_logs, null);
        dialog.setContentView(layout);
        TextView alertTitle = layout.findViewById(R.id.alertTitle);
        RecyclerView recyclerView = layout.findViewById(R.id.recyclerView);
        alertTitle.setText("SMS Logs");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        dialog.show();


    }

    private void getCallDeatils() {
        StringBuffer stringBuffer = new StringBuffer();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
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
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LogsAdapter adapter = new LogsAdapter(this, logsList, new LogsAdapter.LogsAdapterCallback() {
            @Override
            public void onLogsSelected(LogsModel model) {
                String msggg = model.getPhone();
                message.setText(msggg);
                send.performClick();
                dialog.dismiss();
            }
        });

        View layout = layoutInflater.inflate(R.layout.alert_dialog_logs, null);
        dialog.setContentView(layout);
        TextView alertTitle = layout.findViewById(R.id.alertTitle);
        RecyclerView recyclerView = layout.findViewById(R.id.recyclerView);
        alertTitle.setText("Call Logs");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        dialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CALL_LOG,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {


        }
    }

    public boolean hasPermissions(VerifyChat context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


}