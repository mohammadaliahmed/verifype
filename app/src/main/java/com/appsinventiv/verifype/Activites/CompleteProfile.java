package com.appsinventiv.verifype.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CompleteProfile extends AppCompatActivity {

    EditText name, email,phone, city, country;
    RadioButton maleRadio, femaleRadio;
    private String gender;
    Button save;
    private DatabaseReference mDatabase;
    RelativeLayout wholeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        this.setTitle("Complete profile");
        email = findViewById(R.id.email);
        city = findViewById(R.id.city);
        phone = findViewById(R.id.phone);
        wholeLayout = findViewById(R.id.wholeLayout);
        country = findViewById(R.id.country);
        name = findViewById(R.id.name);
        maleRadio = findViewById(R.id.maleRadio);
        save = findViewById(R.id.save);
        femaleRadio = findViewById(R.id.femaleRadio);

        maleRadio.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                gender = "Male";
            }
        });
        femaleRadio.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                gender = "Female";
            }
        });
        name.setText(SharedPrefs.getUser().getName());
        phone.setText(SharedPrefs.getUser().getFullFone());
        city.setText(SharedPrefs.getUser().getCity());
        country.setText(SharedPrefs.getUser().getCountry());
        email.setText(SharedPrefs.getUser().getEmail());
        name.setText(SharedPrefs.getUser().getName());
        name.setText(SharedPrefs.getUser().getName());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().length()==0){
                    name.setError("Enter name");
                }else  if(email.getText().length()==0) {
                    email.setError("Enter email");
                }else if(city.getText().length()==0){
                    city.setError("Enter city");
                }else if(country.getText().length()==0){
                    country.setError("Enter country");
                }else{
                    saveProfile();
                }
            }
        });


    }

    private void saveProfile() {
        wholeLayout.setVisibility(View.VISIBLE);
        HashMap<String,Object> map=new HashMap<>();
        map.put("email",email.getText().toString());
        map.put("city",city.getText().toString());
        map.put("country",country.getText().toString());
        map.put("gender",gender);
        mDatabase = FirebaseDatabase.getInstance("https://verifipe-default-rtdb.firebaseio.com/").getReference();

        mDatabase.child("Users").child(SharedPrefs.getUser().getPhone()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                wholeLayout.setVisibility(View.GONE);
                startActivity(new Intent(CompleteProfile.this,HomeActivity.class));
                finish();
            }
        });


    }


}