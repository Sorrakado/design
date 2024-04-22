package com.kyou.net.design.bridge;

import com.kyou.net.design.factory.RegisterLoginComponentFactory;
import com.kyou.net.design.pojo.UserInfo;
import com.kyou.net.design.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class RegisterLoginByDefault extends AbstractRegisterLoginFunc implements RegisterLoginFuncInterface{
    @Resource
    private UserRepository userRepository;


    @PostConstruct
    private void initFuncMap(){
        RegisterLoginComponentFactory.funcMap.put("Default",this);
    }

    @Override
    public String login(String account, String password) {
        return super.componentLogin(account, password, userRepository);
    }

    @Override
    public String register(UserInfo userInfo) {
        return super.componentRegister(userInfo, userRepository);
    }

    @Override
    public boolean checkUserExists(String userName) {
       return super.componentCheckUserExists(userName, userRepository);
    }

}
