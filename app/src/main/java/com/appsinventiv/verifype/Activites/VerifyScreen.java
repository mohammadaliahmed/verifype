package com.appsinventiv.verifype.Activites;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.verifype.Adapters.VerifyRecyclerAdapter;
import com.appsinventiv.verifype.R;

import java.util.ArrayList;
import java.util.List;

public class VerifyScreen extends AppCompatActivity {

    RecyclerView recycler;
    VerifyRecyclerAdapter adapter;
    private List<String> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);

        }
        this.setTitle("Verify Screen");
        recycler = findViewById(R.id.recycler);

        itemList.add("SMS");
        itemList.add("Call");
        itemList.add("Email Id");
        itemList.add("QR Code");
        itemList.add("UPI ID");
        itemList.add("Website");
        itemList.add("Mobile App Link");
        itemList.add("Others");

        adapter = new VerifyRecyclerAdapter(this, itemList);
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        recycler.setAdapter(adapter);


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