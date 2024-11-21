package org.example.core.helpers;

import org.example.core.interfaces.Auth;
import org.example.core.presenter.LoginPresenter;

public class LoginHelper {

    public static LoginHelper loginPresenter = null;
    private Auth auth = null;

    public static  LoginHelper getInstance(){
        if(loginPresenter == null){
            loginPresenter = new LoginHelper();
        }
        return loginPresenter;
    }

    public LoginHelper attachLoginListener(Auth auth){
        this.auth = auth;
        return this;
    }

    public void performLogin(String username, String password){
        if(auth != null){
            LoginPresenter.getInstance().performLogin(username,password, auth);
        }
    }
}
