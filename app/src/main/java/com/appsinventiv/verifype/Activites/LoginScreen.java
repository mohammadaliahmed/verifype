package com.appsinventiv.verifype.Activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appsinventiv.verifype.Models.User;
import com.appsinventiv.verifype.R;
import com.appsinventiv.verifype.Utils.AlertsUtils;
import com.appsinventiv.verifype.Utils.CommonUtils;
import com.appsinventiv.verifype.Utils.Constants;
import com.appsinventiv.verifype.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.CountryCodePicker;

import java.util.HashMap;

public class LoginScreen extends AppCompatActivity {
    EditText phone;
    Button login;
    TextView register;
    DatabaseReference mDatabase;
    ProgressBar progress;
    CheckBox checkit;
    boolean checked = false;
    TextView textt;
    private CountryCodePicker ccp;
    private String foneCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textt = findViewById(R.id.textt);
        login = findViewById(R.id.login);
        progress = findViewById(R.id.progress);
        register = findViewById(R.id.register);
        checkit = findViewById(R.id.checkit);
        phone = findViewById(R.id.phone);
        mDatabase = Constants.M_DATABASE;
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerPhoneNumberTextView(phone);
        foneCode = "+" + ccp.getDefaultCountryCode();

        checkit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    checked = isChecked;
                }
            }
        });
        AlertsUtils.customTextView(LoginScreen.this, textt);

        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "Don't have an account? ");
        spanTxt.append("Register Here");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(LoginScreen.this, Register.class));

            }
        }, spanTxt.length() - "Register Here".length(), spanTxt.length(), 0);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        register.setText(spanTxt, TextView.BufferType.SPANNABLE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().length() == 0) {
                    phone.setError("Enter Phone");
                } else if (!checked) {
                    CommonUtils.showToast("Please accept terms and conditions");
                } else {
                    Intent intent = new Intent(LoginScreen.this, VerifyPhone.class);
                    intent.putExtra("number", foneCode + phone.getText().toString());
                    someActivityResultLauncher.launch(intent);
                }
            }
        });

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 123) {
                        checkLogin();
                    }
                }
            });

    private void checkLogin() {
        SharedPrefs.setPhoneVerified("true");
        progress.setVisibility(View.VISIBLE);

        String ph = phone.getText().toString().substring(phone.getText().toString().length() - 10);
        mDatabase.child("Users").child(ph).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    User user = snapshot.getValue(User.class);
                    SharedPrefs.setUser(user);
                    CommonUtils.showToast("Login successful");
                    startActivity(new Intent(LoginScreen.this, HomeActivity.class));
                    finish();
                } else {
                    User user = new User(ph, foneCode + phone.getText().toString());
                    SharedPrefs.setUser(user);
                    mDatabase.child("Users").child(ph).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            startActivity(new Intent(LoginScreen.this, CompleteProfile.class));
                            finish();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}