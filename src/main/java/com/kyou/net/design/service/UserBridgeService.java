package com.kyou.net.design.service;

import com.kyou.net.design.bridge.AbstractRegisterLoginComponent;
import com.kyou.net.design.factory.RegisterLoginComponentFactory;
import com.kyou.net.design.pojo.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserBridgeService {
    public String login(String userName, String password) {
        AbstractRegisterLoginComponent component = RegisterLoginComponentFactory.getComponent("Default");
        return component.login(userName, password);
    }
    public String register(UserInfo userInfo) {
        AbstractRegisterLoginComponent component = RegisterLoginComponentFactory.getComponent("Default");
        return component.register(userInfo);
    }

    public String login3rd(HttpServletRequest request,String type) throws IOException {
        AbstractRegisterLoginComponent component = RegisterLoginComponentFactory.getComponent(type);
        return component.login3rd(request);
    }
}
