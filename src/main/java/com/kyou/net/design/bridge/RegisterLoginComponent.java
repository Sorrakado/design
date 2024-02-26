package com.kyou.net.design.bridge;

import com.kyou.net.design.pojo.UserInfo;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class RegisterLoginComponent extends AbstractRegisterLoginComponent{
    public RegisterLoginComponent(RegisterLoginFuncInterface funcInterface) {
        super(funcInterface);
    }

    @Override
    public String login(String username, String password) {
        return funcInterface.login(username, password);
    }

    @Override
    public String register(UserInfo userInfo) {
        return funcInterface.register(userInfo);
    }

    @Override
    protected boolean checkUserExists(String username) {
        return funcInterface.checkUserExists(username);
    }

    @Override
    public String login3rd(HttpServletRequest request) throws IOException {
        return funcInterface.login3rd(request);
    }
}
