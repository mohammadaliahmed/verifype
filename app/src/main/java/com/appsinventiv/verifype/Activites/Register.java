package com.appsinventiv.verifype.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appsinventiv.verifype.Models.User;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.CommonUtils;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    EditText phone, password, name;
    Button login, register;
    private HashMap<String, User> usersMap = new HashMap<>();
    private CountryCodePicker ccp;
    private String foneCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);

        name = findViewById(R.id.name);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerPhoneNumberTextView(phone);

        foneCode = "+" + ccp.getDefaultCountryCode();
//        countryName.setText("(" + ccp.getDefaultCountryName() + ")");
//        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
//            @Override
//            public void onCountrySelected(Country selectedCountry) {
//                foneCode = "+" + selectedCountry.getPhoneCode();
//                countryName.setText("(" + selectedCountry.getName() + ")");
//            }
//        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().length() == 0) {
                    phone.setError("Enter Phone");
                } else if (phone.getText().length() < 10 || phone.getText().length() > 12) {
                    phone.setError("Enter valid phone number");
                } else if (name.getText().length() == 0) {
                    name.setError("Enter Name");
                } else if (password.getText().length() == 0) {
                    password.setError("Enter Password");
                } else {
                    requestCode();
                }
            }
        });
    }

    private void requestCode() {
        String ph = phone.getText().toString();
        Intent i = new Intent(Register.this, VerifyPhone.class);
        i.putExtra("number", foneCode + ph);
        i.putExtra("name", name.getText().toString());
        i.putExtra("password", password.getText().toString());
        startActivity(i);


    }


}