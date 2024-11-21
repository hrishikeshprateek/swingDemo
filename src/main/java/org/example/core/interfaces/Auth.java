package org.example.core.interfaces;

public abstract interface Auth {
    void onLoginSuccess(String data);
    void onLoginFailed(String error);
}
