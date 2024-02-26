package com.kyou.net.design.controller;

import com.kyou.net.design.adapter.Login3rdAdapter;
import com.kyou.net.design.pojo.UserInfo;
import com.kyou.net.design.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private Login3rdAdapter login3rdAdapter;

    @PostMapping("/login")
    public String login(String account, String password){
        return userService.login(account, password);
    }

    @PostMapping("/register")
    public String register(@RequestBody UserInfo userInfo){
        return userService.register(userInfo);
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping("/gitee")
    public String gitee(String code,String state) throws IOException {
        return login3rdAdapter.loginByGitee(code,state);
    }
}
