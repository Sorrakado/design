package com.kyou.net.design.bridge;

import com.kyou.net.design.pojo.UserInfo;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface RegisterLoginFuncInterface {

    String login(String account,String password);

    String register(UserInfo userInfo);

    boolean checkUserExists(String userName);

    String login3rd(HttpServletRequest request) throws IOException;

}
