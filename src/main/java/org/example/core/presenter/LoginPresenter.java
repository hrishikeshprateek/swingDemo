package org.example.core.presenter;

import org.example.core.contract.LoginContract;
import org.example.core.interfaces.Auth;
import utils.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static utils.MyUtils.hashPassword;

public class LoginPresenter implements LoginContract {

    private static LoginPresenter loginPresenter = null;

    public static LoginPresenter getInstance() {
        if (loginPresenter == null) {
            loginPresenter = new LoginPresenter();
        }
        return loginPresenter;
    }

    @Override
    public void performLogin(String username, String password, Auth auth) {
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            Connection connection = DatabaseHelper.getInstance().getDbConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                auth.onLoginSuccess(rs.toString());
            } else {
                auth.onLoginFailed("Login failed id or password incorrect !!");
            }
        }catch (Exception e){
            e.printStackTrace();
            auth.onLoginFailed(e.getMessage());

        }
    }

}
