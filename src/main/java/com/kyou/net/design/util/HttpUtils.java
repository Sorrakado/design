package com.kyou.net.design.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.http.HttpClient;

public class HttpUtils {
    public static JSONObject execute(String url, HttpMethod httpMethod) throws IOException {
        HttpRequestBase http = null;
        try(CloseableHttpClient client = HttpClients.createDefault()){

            if(httpMethod == HttpMethod.GET){
                http = new HttpGet(url);
                http.setHeader("Content-Type", "x-www-form-urlencoded;charset=UTF-8");
            }else{
                http = new HttpPost(url);
                http.setHeader("Content-Type", "application/json;charset=UTF-8");
            }
            HttpEntity entity = client.execute(http).getEntity();
            return JSONObject.parseObject(entity.getContent().toString());
        }
    }
}
