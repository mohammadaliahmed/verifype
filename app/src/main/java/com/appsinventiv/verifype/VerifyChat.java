package com.appsinventiv.verifype;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Adapters.ChatAdapter;
import com.appsinventiv.verifype.Models.ChatModel;
import com.appsinventiv.verifype.Models.ObjectModel;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VerifyChat extends AppCompatActivity {

    String option;
    private DatabaseReference mDatabase;
    private List<ObjectModel> objectList = new ArrayList<>();
    RecyclerView recyclerView;
    ChatAdapter adapter;
    private List<ChatModel> chatList = new ArrayList<>();
    HashMap<Integer, ObjectModel> sequenceMap = new HashMap<>();
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

        chatList.add(new ChatModel(
                "" + System.currentTimeMillis(), option, SharedPrefs.getUser().getPhone(),
                "admin", "text", new ArrayList<>(),
                System.currentTimeMillis()
        ));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().length() == 0) {
                    message.setError("Cant send empty message");
                } else {
                    msg = message.getText().toString();
                    message.setText("");

                    chatList.add(new ChatModel(
                            "" + System.currentTimeMillis(), msg, SharedPrefs.getUser().getPhone(),
                            "admin", "text", new ArrayList<>(),
                            System.currentTimeMillis()
                    ));
                    if (sequenceCounter <= sequenceMap.size()) {
                        chatList.add(new ChatModel(
                                "" + System.currentTimeMillis(),
                                sequenceMap.get(sequenceCounter).getVarDesc(), "admin",
                                "user", "text", new ArrayList<>(),
                                System.currentTimeMillis()
                        ));

                    }
                    adapter.setItemList(chatList);
                    sequenceCounter += 1;
                    recyclerView.scrollToPosition(chatList.size() - 1);


                }
            }
        });

        adapter = new ChatAdapter(this, chatList, new ChatAdapter.ChatAdapterCallbacks() {
            @Override
            public void onSelected(ObjectModel model) {
                if (model.getSequence() == 0) {
                    chatList.add(new ChatModel(
                            "" + System.currentTimeMillis(), model.getVarDesc(), SharedPrefs.getUser().getPhone(),
                            "admin", "text", new ArrayList<>(),
                            System.currentTimeMillis()
                    ));
                    getSecondSequence(model);
                } else {
                    chatList.add(new ChatModel(
                            "" + System.currentTimeMillis(),
                            sequenceMap.get(sequenceCounter).getVarDesc(), "admin",
                            "user", "text", new ArrayList<>(),
                            System.currentTimeMillis()
                    ));

                }
                adapter.setItemList(chatList);
                recyclerView.scrollToPosition(chatList.size() - 1);
                sequenceCounter = 1;
            }
        });
        recyclerView.setAdapter(adapter);
        getFirstSequence();
//        readSMS();


    }

//    private void readSMS() {
//        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
//        String msgData = "";
//        if (cursor.moveToFirst()) { // must check the result to prevent exception
//            do {
//
//                for (int idx = 0; idx < 10; idx++) {
//                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
//                }
//                // use msgData
//            } while (cursor.moveToNext());
//        } else {
//            // empty box, no SMS
//        }
//
//        String sg=msgData;
//    }

    private void getSecondSequence(ObjectModel model) {
        mDatabase.child("call").child(model.getVarName()).child(model.getVarValue()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        ObjectModel model1 = snapshot1.getValue(ObjectModel.class);
                        if (model1 != null) {
                            if (model1.getSequence() > 0) {
                                sequenceMap.put(model1.getSequence(), model1);
                            }
                        }

                    }
                    message.setEnabled(true);

                    chatList.add(new ChatModel(
                            "" + System.currentTimeMillis(),
                            sequenceMap.get(sequenceCounter).getVarDesc(), "admin",
                            "user", "text", new ArrayList<>(),
                            System.currentTimeMillis()
                    ));
                    adapter.setItemList(chatList);
                    recyclerView.scrollToPosition(chatList.size() - 1);
                    sequenceCounter += 1;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getFirstSequence() {
        mDatabase.child("call").child("main").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        ObjectModel model = snapshot1.getValue(ObjectModel.class);
                        objectList.add(model);
                    }
                    chatList.add(new ChatModel(
                            "" + System.currentTimeMillis(), "Choose Option", "admin",
                            "user", "object", objectList,
                            System.currentTimeMillis()
                    ));
                    adapter.setItemList(chatList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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