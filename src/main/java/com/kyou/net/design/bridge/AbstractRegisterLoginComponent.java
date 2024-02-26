package com.kyou.net.design.bridge;

import com.kyou.net.design.pojo.UserInfo;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public abstract class AbstractRegisterLoginComponent {

    protected  RegisterLoginFuncInterface funcInterface;

    public AbstractRegisterLoginComponent(RegisterLoginFuncInterface funcInterface){
        validate(funcInterface);
        this.funcInterface = funcInterface;
    }

    protected final void validate(RegisterLoginFuncInterface func){
        if(func != null && !(func instanceof RegisterLoginFuncInterface)){
            throw new UnsupportedOperationException("Unsupported login");
        }

    }

    public abstract String login(String username, String password);
    public abstract String register(UserInfo userInfo);

    protected abstract boolean checkUserExists(String username);
    public abstract String login3rd(HttpServletRequest request) throws IOException;

}
