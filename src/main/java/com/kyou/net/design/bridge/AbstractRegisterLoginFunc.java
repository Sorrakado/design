package com.kyou.net.design.bridge;

import com.kyou.net.design.pojo.UserInfo;
import com.kyou.net.design.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Date;

public abstract class AbstractRegisterLoginFunc implements RegisterLoginFuncInterface{
    public String login(String account, String password){
        throw new UnsupportedOperationException();
    }

    public String register(UserInfo userInfo){
        throw new UnsupportedOperationException();
    }

    public boolean checkUserExists(String userName){
        throw new UnsupportedOperationException();
    }

    public String login3rd(HttpServletRequest request) throws IOException {
        throw new UnsupportedOperationException();
    }
    protected String componentLogin(String account, String password, UserRepository userRepository){
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account, password);

        if(userInfo == null){
            return "account / password ERROR!";
        }
        return "login Success!";
    }

    protected String componentRegister(UserInfo userInfo, UserRepository userRepository){
        if(componentCheckUserExists(userInfo.getUserName(),userRepository)){
            throw new RuntimeException("User already registered!");
        }
        userInfo.setCreateDate(new Date());
        userRepository.save(userInfo);
        return "register Success!";
    }



    protected boolean componentCheckUserExists(String userName, UserRepository userRepository){
        return userRepository.findByUserName(userName)!= null;
    }
}
