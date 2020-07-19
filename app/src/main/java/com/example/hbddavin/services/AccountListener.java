package com.example.hbddavin.services;

public interface AccountListener {
    interface LoginListener{
        void onSuccessLogin();
        void onLoginFailure();
        void onFailureResponse(Exception e);
    }

    interface RegistrationListener{
        void onAccountCreated();
        void onFailureResponse(Exception e);
    }

    interface AccountRecoveryListener{
        void onEmailSent();
        void onFailureSendingEmail();
        void onFailureResponse(Exception e);
    }

}
