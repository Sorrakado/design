package com.kyou.net.design.adapter;

import com.alibaba.fastjson.JSONObject;
import com.kyou.net.design.pojo.UserInfo;
import com.kyou.net.design.service.UserService;


import com.kyou.net.design.util.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class Login3rdAdapter extends UserService implements Login3rdTarget{

    @Value("${gitee.state}")
    private String giteeState;

    @Value("${gitee.token.url}")
    private String tokenUrl;

    @Value("${gitee.user.url}")
    private String userUrl;

    @Value("${gitee.user.prefix}")
    private String userPrefix;

    @Override
    public String loginByGitee(String code, String state) throws IOException {

        if(!giteeState.equals(state)){
            throw new UnsupportedOperationException("Invalid state");
        }
        String url = tokenUrl.concat(code).concat("&state=GITEE");

        JSONObject tokenResponse = HttpUtils.execute(url, HttpMethod.POST);
        String token = tokenResponse.getString("access_token");
        String userUri = userUrl.concat(token);
        JSONObject response = HttpUtils.execute(userUri, HttpMethod.GET);

        String userName = userPrefix.concat(String.valueOf(response.get("name")));
        return this.autoRegister3rdAndLogin(userName, userName);

    }
    private String autoRegister3rdAndLogin(String userName, String password){
        if(super.checkUserExists(userName)){
            return super.login(userName, password);
        }
        UserInfo build = UserInfo.builder().userName(userName).userPassword(password).createDate(new Date()).userEmail("envkt@example.com").build();
        super.register(build);
        return super.login(userName, password);
    }
}
