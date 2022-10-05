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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appsinventiv.verifype.Models.User;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.Constants;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CompleteProfile extends AppCompatActivity {

    EditText name, email,phone, city, country;
    RadioButton maleRadio, femaleRadio;
    private String gender;
    Button save;
    private DatabaseReference mDatabase;
    RelativeLayout wholeLayout;
    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        getSupportActionBar().setElevation(0);
        this.setTitle("Complete profile");
        skip = findViewById(R.id.skip);
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
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(CompleteProfile.this,HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
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
        map.put("name",name.getText().toString());
        map.put("email",email.getText().toString());
        map.put("city",city.getText().toString());
        map.put("country",country.getText().toString());
        map.put("gender",gender);
        mDatabase = Constants.M_DATABASE;

        mDatabase.child("Users").child(SharedPrefs.getUser().getPhone()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mDatabase.child("Users").child(SharedPrefs.getUser().getPhone()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);
                        SharedPrefs.setUser(user);
                        wholeLayout.setVisibility(View.GONE);
                        startActivity(new Intent(CompleteProfile.this,HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


    }


}