package com.kyou.net.design.service;

import com.kyou.net.design.pojo.UserInfo;
import com.kyou.net.design.repo.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    public String login(String account,String password){
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account, password);

        if(userInfo == null){
            return "account / password ERROR!";
        }
        return "login Success!";
    }

    public String register(UserInfo userInfo){
        if(checkUserExists(userInfo.getUserName())){
            throw new RuntimeException("User already registered!");
        }
        userInfo.setCreateDate(new Date());
        userRepository.save(userInfo);
        return "register Success!";
    }

    public boolean checkUserExists(String userName){
        UserInfo user = userRepository.findByUserName(userName);
        return user != null;
    }

}
