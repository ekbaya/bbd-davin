package com.example.hbddavin.apis;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.hbddavin.services.AccountListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AccountAPI {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Context context;
    private AccountListener.LoginListener loginListener;
    private AccountListener.RegistrationListener registrationListener;
    private AccountListener.AccountRecoveryListener accountRecoveryListener;

    public AccountAPI(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
    }

    public void loginUser(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginListener.onSuccessLogin();
                        } else {
                            loginListener.onLoginFailure();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loginListener.onFailureResponse(e);
            }
        });
    }

    public void registerUser(final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();

                            //get user email and user id from auth
                            String email = user.getEmail();
                            String uid = user.getUid();
                            // when user is registered store user info in firebase realtime database too
                            //using HashMap
                            HashMap<Object, String> hashMap = new HashMap<>();
                            //put info in hashmap
                            hashMap.put("uid",uid);
                            hashMap.put("email",email);

                            //put data within hashMap in database
                            reference.child(uid).setValue(hashMap);
                            registrationListener.onAccountCreated();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        registrationListener.onFailureResponse(e);
                    }
                });

    }

    public void recoverAccount(String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            accountRecoveryListener.onEmailSent();
                        }
                        else {
                            accountRecoveryListener.onFailureSendingEmail();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                accountRecoveryListener.onFailureResponse(e);
            }
        });
    }

    public AccountListener.LoginListener getLoginListener() {
        return loginListener;
    }

    public void setLoginListener(AccountListener.LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public AccountListener.RegistrationListener getRegistrationListener() {
        return registrationListener;
    }

    public void setRegistrationListener(AccountListener.RegistrationListener registrationListener) {
        this.registrationListener = registrationListener;
    }

    public AccountListener.AccountRecoveryListener getAccountRecoveryListener() {
        return accountRecoveryListener;
    }

    public void setAccountRecoveryListener(AccountListener.AccountRecoveryListener accountRecoveryListener) {
        this.accountRecoveryListener = accountRecoveryListener;
    }
}
