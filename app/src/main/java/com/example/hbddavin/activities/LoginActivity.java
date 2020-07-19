package com.example.hbddavin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hbddavin.R;
import com.example.hbddavin.apis.AccountAPI;
import com.example.hbddavin.services.AccountListener;
import com.example.hbddavin.utils.Loader;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AccountListener.LoginListener , AccountListener.AccountRecoveryListener {
    private EditText emailEditText, passwordEditText;
    private TextView forgetPasswordTv;
    private Button loginBtn, createAccountBtn;
    private Loader loader;
    private AccountAPI accountAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        forgetPasswordTv = findViewById(R.id.forgetPasswordTv);
        loginBtn = findViewById(R.id.loginBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);

        loginBtn.setOnClickListener(this);
        createAccountBtn.setOnClickListener(this);

        accountAPI = new AccountAPI(this);
        accountAPI.setLoginListener(this);
        accountAPI.setAccountRecoveryListener(this);
        loader = new Loader(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(loginBtn)){
            if (validated()){
               accountAPI.loginUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
               loader.showDialogue();
            }

        }
        if (view.equals(createAccountBtn)){
            goToRegistrationActivity();

        }
        if (view.equals(forgetPasswordTv)){
            showRecoveryPasswordDialog();
        }

    }

    private void goToRegistrationActivity() {
      startActivity(new Intent(new Intent(LoginActivity.this, RegistrationActivity.class)));
    }

    private void showRecoveryPasswordDialog() {
        //AlertDialog
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");

        //set Layout Linear Layout
        LinearLayout linearLayout = new LinearLayout(this);
        // Views to set in dialog
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        /*sets the main width of EditView to fit a text of n 'M' letters regardless of the actual
        text extension and text size*/
        emailEt.setMinEms(16);
        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        //buttons recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                String email = emailEt.getText().toString().trim();
                accountAPI.recoverAccount(email);
                loader.showDialogue();
            }
        });
        //buttons cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });

        //show dialog
        builder.create().show();
    }

    private boolean validated() {
        if (TextUtils.isEmpty(emailEditText.getText().toString())){
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(passwordEditText.getText().toString())){
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }

    @Override
    public void onSuccessLogin() {
        loader.hideDialogue();
       startActivity(new Intent(new Intent(LoginActivity.this, MainActivity.class)));
       finish();
    }

    @Override
    public void onLoginFailure() {
        loader.hideDialogue();
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmailSent() {
        loader.hideDialogue();
        Toast.makeText(this, "Email sent check your inbox to reset password", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFailureSendingEmail() {
        loader.hideDialogue();
        Toast.makeText(this, "Operation failed. Try again", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFailureResponse(Exception e) {
        loader.hideDialogue();
        Toast.makeText(this, "Failed... "+e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}