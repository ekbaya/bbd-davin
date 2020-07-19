package com.example.hbddavin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hbddavin.R;
import com.example.hbddavin.apis.AccountAPI;
import com.example.hbddavin.services.AccountListener;
import com.example.hbddavin.utils.Loader;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, AccountListener.RegistrationListener {
    private EditText userEmailET, userPasswordET;
    private Button registerBtn,alreadyHaveAccountBtn;
    private Loader loader;
    private AccountAPI accountAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        userEmailET = findViewById(R.id.userEmailET);
        userPasswordET = findViewById(R.id.userPasswordET);
        registerBtn = findViewById(R.id.registerBtn);
        alreadyHaveAccountBtn = findViewById(R.id.alreadyHaveAccountBtn);

        registerBtn.setOnClickListener(this);
        alreadyHaveAccountBtn.setOnClickListener(this);
        accountAPI = new AccountAPI(this);
        accountAPI.setRegistrationListener(this);
        loader = new Loader(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(registerBtn)){
            if (validated()){
                accountAPI.registerUser(userEmailET.getText().toString(), userPasswordET.getText().toString());
                loader.showDialogue();
            }

        }
        if (view.equals(alreadyHaveAccountBtn)){
            goToLoginActivity();
        }

    }

    private boolean validated() {
        if (TextUtils.isEmpty(userEmailET.getText().toString())){
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            userEmailET.setFocusable(true);
            return false;
        }
        else if (TextUtils.isEmpty(userPasswordET.getText().toString())){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            userPasswordET.setFocusable(true);
            return false;
        }else return true;
    }

    private void goToLoginActivity() {
        startActivity(new Intent(new Intent(RegistrationActivity.this, LoginActivity.class)));
    }

    @Override
    public void onAccountCreated() {
        loader.hideDialogue();
        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(new Intent(RegistrationActivity.this, MainActivity.class)));
        finish();

    }

    @Override
    public void onFailureResponse(Exception e) {
        loader.hideDialogue();
        Toast.makeText(this, "Registration failed, try again", Toast.LENGTH_SHORT).show();

    }
}