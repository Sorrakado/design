package com.kyou.net.design.bridge;

import com.alibaba.fastjson.JSONObject;
import com.kyou.net.design.config.RegisterLoginComponentFactory;
import com.kyou.net.design.pojo.UserInfo;
import com.kyou.net.design.repo.UserRepository;
import com.kyou.net.design.util.HttpUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class RegisterLoginByGitee extends AbstractRegisterLoginFunc implements RegisterLoginFuncInterface{
    @Value("${gitee.state}")
    private String giteeState;

    @Value("${gitee.token.url}")
    private String tokenUrl;

    @Value("${gitee.user.url}")
    private String userUrl;

    @Value("${gitee.user.prefix}")
    private String userPrefix;

    @Resource
    private UserRepository userRepository;

    @PostConstruct
    private void initFuncMap(){
        RegisterLoginComponentFactory.funcMap.put("GITEE",this);
    }

    @Override
    public String login3rd(HttpServletRequest request) throws IOException {

        String code = request.getParameter("code");
        String state = request.getParameter("state");
        if(!giteeState.equals(state)){
            throw new UnsupportedOperationException("Invalid state");
        }
        String url = tokenUrl.concat(code).concat("&state=GITEE");

        JSONObject tokenResponse = HttpUtils.change(url, HttpMethod.POST);
        String token = tokenResponse.getString("access_token");
        String userUri = userUrl.concat(token);
        JSONObject response = HttpUtils.change(userUri, HttpMethod.GET);

        String userName = userPrefix.concat(String.valueOf(response.get("name")));
        return this.autoRegister3rdAndLogin(userName, userName);
    }
    private String autoRegister3rdAndLogin(String userName, String password){
        if(super.componentCheckUserExists(userName,userRepository)){
            return super.componentLogin(userName, password,userRepository);
        }
        UserInfo build = UserInfo.builder().userName(userName).userPassword(password).createDate(new Date()).userEmail("envkt@example.com").build();
        super.componentRegister(build, userRepository);
        return super.componentLogin(userName, password, userRepository);
    }
}
