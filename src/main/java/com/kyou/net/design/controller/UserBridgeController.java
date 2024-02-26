package com.kyou.net.design.controller;

import com.kyou.net.design.pojo.UserInfo;
import com.kyou.net.design.service.UserBridgeService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/bridge")
public class UserBridgeController {


    @Resource
    public UserBridgeService userBridgeService;

    @PostMapping("/login")
    public String login(String userName, String password) {
        return userBridgeService.login(userName, password);
    }

    @PostMapping("/register")
    public String register(UserInfo userInfo) {
        return userBridgeService.register(userInfo);
    }

    @GetMapping("/gitee")
    public String gitee(HttpServletRequest request) throws IOException {
        return userBridgeService.login3rd(request,"GITEE");
    }
}
