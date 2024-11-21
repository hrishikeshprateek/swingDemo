package org.example.core.contract;

import org.example.core.interfaces.Auth;

public interface LoginContract {

    void performLogin(String username, String password, Auth auth) throws Exception;
}