package com.appsinventiv.verifype.Activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Adapters.ChatAdapter;
import com.appsinventiv.verifype.Models.ApiResponse;
import com.appsinventiv.verifype.Models.ChatModel;
import com.appsinventiv.verifype.Models.InputModel;
import com.appsinventiv.verifype.Models.LogsModel;
import com.appsinventiv.verifype.Models.ObjectModel;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.AppConfig;
import com.appsinventiv.verifype.Utils.CommonUtils;
import com.appsinventiv.verifype.Utils.CompressImage;
import com.appsinventiv.verifype.Utils.Constants;
import com.appsinventiv.verifype.Utils.KeyboardUtils;
import com.appsinventiv.verifype.Utils.LogsReader;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.appsinventiv.verifype.Utils.UserClient;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyChat extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE = 23;
    String option;
    private DatabaseReference mDatabase;
    private List<ObjectModel> objectList = new ArrayList<>();
    RecyclerView recyclerView;
    ChatAdapter adapter;
    private List<ChatModel> chatList = new ArrayList<>();
    List<ObjectModel> sequenceMap = new ArrayList<>();
    int sequenceCounter = 0;
    ImageView send;
    EditText message;
    String msg;
    private ArrayList<String> mSelected = new ArrayList<>();
    private String imageUrl;
    private boolean objecListSizeGreaterThanOne;
    private ObjectModel randomObjModel;
    private boolean calledSecondSequence;
    HashMap<String, String> apiMap = new HashMap<>();

    int mapCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_chat);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
            this.setTitle("Verify Screen");
        }


        apiMap.put("type", "verify");
        apiMap.put("channel_type", "");
        apiMap.put("channel_sender", "");
        apiMap.put("channel_entity", "");
        apiMap.put("channel_message", "");
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mDatabase = FirebaseDatabase.getInstance("https://verifipe-default-rtdb.firebaseio.com/").getReference();
        message.setEnabled(false);
        option = getIntent().getStringExtra("option");
        if (option.equalsIgnoreCase("SMS")) {
            getSMSPermissions();

        } else if (option.equalsIgnoreCase("Call")) {
            getCallPermissions();
        } else if (option.equalsIgnoreCase("QR Code")) {
            getCameraPermissions();
        }
        Constants.OPTION_CLICKED = false;
        addUserMsg(option, "text");//firstMsg
        getFirstSequence();
        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                recyclerView.scrollToPosition(chatList.size() - 1);


            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().length() == 0) {
                    message.setError("Cant send empty message");
                } else {
                    msg = message.getText().toString();
                    message.setText("");
                    addUserMsg(msg, "text");//msg from chat window

                    if (mapCounter == 0) {
                        apiMap.put("channel_sender", msg);
                    } else if (mapCounter == 1) {
                        apiMap.put("channel_entity", msg);
                    } else if (mapCounter == 2) {
                        apiMap.put("channel_message", msg);
                    }
                    mapCounter++;

                    adapter.setItemList(chatList);
                    recyclerView.scrollToPosition(chatList.size() - 1);
                    if (!objecListSizeGreaterThanOne && !calledSecondSequence) {
                        calledSecondSequence = true;
                        getSecondSequence(randomObjModel);

                    } else {

                        sequenceCounter += 1;

                        if (sequenceCounter < sequenceMap.size()) {
                            addAdminMsg(sequenceMap.get(sequenceCounter).getVarDesc(), "text", new ArrayList<>());

                        }else{
                            callApi();
                        }

                        recyclerView.scrollToPosition(chatList.size() - 1);

                    }
                }
            }
        });
        adapter = new ChatAdapter(this, chatList, new ChatAdapter.ChatAdapterCallbacks() {
            @Override
            public void onSelected(ObjectModel model) {
                if (model.getSequence() == 0) {
                    addUserMsg(model.getVarDesc(), "text");
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
    }

    private void openCameraForQR() {
        Options options = Options.init()
                .setRequestCode(REQUEST_CODE_CHOOSE)                                           //Request code for activity results
                .setCount(1)
                .setExcludeVideos(true)
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                ;                                       //Custom Path For media Storage
        Pix.start(this, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && data != null) {
            mSelected = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            CompressImage image = new CompressImage(VerifyChat.this);
            imageUrl = image.compressImage("" + mSelected.get(0));
            addUserMsg("", "image");

            addAdminMsg(sequenceMap.get(sequenceCounter).getVarDesc(), "text", new ArrayList<>());
//            sequenceCounter += 1;
            adapter.setItemList(chatList);
            recyclerView.scrollToPosition(chatList.size() - 1);
        }
    }

    private void addAdminMsg(String msgToAd, String type, List<ObjectModel> objectList) {
        chatList.add(new ChatModel(
                "" + System.currentTimeMillis(),
                msgToAd, "admin",
                "user", type, imageUrl, objectList,
                System.currentTimeMillis()
        ));
    }

    private void addUserMsg(String msgToAdd, String msgType) {
        chatList.add(new ChatModel(
                "" + System.currentTimeMillis(), msgToAdd, SharedPrefs.getUser().getPhone(),
                "admin", msgType, imageUrl, new ArrayList<>(),
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
                    if (model.getVarName().contains("QR")) {
                        openCameraForQR();
                    } else {
                        if (sequenceMap.get(sequenceCounter).getVarValue().equalsIgnoreCase("auto")) {

                            if (model.getVarName().contains("sms")) {
                                LogsReader reader = new LogsReader();
                                reader.readSms(VerifyChat.this, new LogsReader.LogsCallBack() {
                                    @Override
                                    public void OnLogsSelected(LogsModel model) {
                                        String msggg = model.getPhone() + "\n" + model.getText();
                                        message.setText(msggg);
                                        send.performClick();
                                    }
                                });
                            } else if (model.getVarName().contains("call")) {
                                LogsReader reader = new LogsReader();
                                reader.readCallLogs(VerifyChat.this, new LogsReader.LogsCallBack() {
                                    @Override
                                    public void OnLogsSelected(LogsModel model) {
                                        String msggg = model.getPhone();
                                        message.setText(msggg);
                                        send.performClick();
                                    }
                                });


                            }
                        } else {
                            addAdminMsg(sequenceMap.get(sequenceCounter).getVarDesc(), "text", new ArrayList<>());
                            adapter.setItemList(chatList);
                            recyclerView.scrollToPosition(chatList.size() - 1);
//                            sequenceCounter += 1;
                        }
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
                    apiMap.put("channel_type", objectList.get(0).getVarName());
                    if (objectList.size() > 1) {
                        objecListSizeGreaterThanOne = true;
                        addAdminMsg("Choose Option", "object", objectList);
                        adapter.setItemList(chatList);

                    } else {
                        objecListSizeGreaterThanOne = false;

                        addAdminMsg(objectList.get(0).getVarDesc(), "text", new ArrayList<>());
                        adapter.setItemList(chatList);
                        recyclerView.scrollToPosition(chatList.size() - 1);
//                        sequenceCounter = 1;
                        message.setEnabled(true);
                        randomObjModel = objectList.get(0);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void callApi() {
        KeyboardUtils.forceCloseKeyboard(recyclerView);
        String id=""+System.currentTimeMillis();
        apiMap.put("id", id);
        apiMap.put("userId", SharedPrefs.getUser().getPhone());

        mDatabase.child("Verfications").child(id).setValue(apiMap);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Gson gson=new Gson();
        JsonElement abc = gson.toJsonTree(apiMap);

        Call<ApiResponse> call = getResponse.verify(abc);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getResult()!=null){
                        addAdminMsg(response.body().getResult().getMessage(), "text", new ArrayList<>());
                        int tipCounter=1;
                        String tips = "";
                        for(String tip:response.body().getResult().getTips()){
                            tips=tips+"Tip "+tipCounter+": "+tip+"\n\n";
                            tipCounter++;
                        }
                        addAdminMsg(tips, "text", new ArrayList<>());
                        adapter.setItemList(chatList);
                        recyclerView.scrollToPosition(chatList.size() - 1);


                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                CommonUtils.showToast(t.getMessage());
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

    private void getSMSPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_SMS,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {


        }
    }

    private void getCallPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_CALL_LOG,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {

        }
    }

    private void getCameraPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    CommonUtils.showToast("Permission denied");
                    Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                    myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                    myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myAppSettings);
                    finish();

                }

            }

        }

    }
}