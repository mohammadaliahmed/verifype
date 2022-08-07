package com.appsinventiv.verifype.Activites;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Adapters.ChatAdapter;
import com.appsinventiv.verifype.Adapters.SupportChatAdapter;
import com.appsinventiv.verifype.Models.ChatModel;
import com.appsinventiv.verifype.Models.SupportChatModel;
import com.appsinventiv.verifype.Models.User;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.KeyboardUtils;
import com.appsinventiv.verifype.Utils.NotificationAsync;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatScreen extends AppCompatActivity {
    EditText message;
    ImageView send;
    private DatabaseReference mDatabase;
    String msg = "";
    TextView name;
    private List<SupportChatModel> itemList = new ArrayList<>();
    SupportChatAdapter adapter;
    RecyclerView recyclerView;
    private String fcmKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);

        }
        this.setTitle("Support Chat");
        mDatabase = FirebaseDatabase.getInstance("https://verifipe-default-rtdb.firebaseio.com/").getReference();
        adapter = new SupportChatAdapter(this, itemList);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        name = findViewById(R.id.name);
        send = findViewById(R.id.send);
        message = findViewById(R.id.message);
        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                recyclerView.scrollToPosition(itemList.size() - 1);


            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().length() == 0) {
                    message.setError("Cant send empty message");
                } else {
                    sendMessageToDb();
                }
            }
        });
        getOtherUserFromDb();
        getDataFromDb();

    }

    private void getOtherUserFromDb() {
        mDatabase.child("Admin").child("fcmKey").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    fcmKey = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDataFromDb() {
        mDatabase.child("SupportChat").child(SharedPrefs.getUser().getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            itemList.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                SupportChatModel model = snapshot.getValue(SupportChatModel.class);
                                if (model != null && model.getMessage() != null) {
                                    itemList.add(model);
                                }
                            }
                            adapter.setItemList(itemList);
                            recyclerView.scrollToPosition(itemList.size() - 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void sendMessageToDb() {
        msg = message.getText().toString();
        message.setText("");
        String key = "" + System.currentTimeMillis();
        SupportChatModel myModel = new SupportChatModel(key,
                msg, SharedPrefs.getUser().getPhone(),
                "admin",
                SharedPrefs.getUser().getName(),
                System.currentTimeMillis());
        mDatabase.child("SupportChat").child(SharedPrefs.getUser().getPhone()).child(key).setValue(
                myModel
        );

        mDatabase.child("AdminSupportChat").child(SharedPrefs.getUser().getPhone())
                .child(key).setValue(myModel);
        sendNotification();
    }

    private void sendNotification() {
        NotificationAsync notificationAsync = new NotificationAsync(this);
        String NotificationTitle = "New message from: " + SharedPrefs.getUser().getName();
        String NotificationMessage = msg;
        notificationAsync.execute(
                "ali",
                fcmKey,
                NotificationTitle,
                NotificationMessage,
                SharedPrefs.getUser().getPhone(),
                "msg");
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


}