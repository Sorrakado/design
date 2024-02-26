package com.kyou.net.design.util;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.http.HttpClient;

@Slf4j
public class HttpUtils {
    public static JSONObject execute(String url, HttpMethod httpMethod) throws IOException {
        HttpRequestBase http = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            if (httpMethod == HttpMethod.GET) {
                http = new HttpGet(url);
                http.setHeader("Content-Type", "x-www-form-urlencoded;charset=UTF-8");
            } else {
                http = new HttpPost(url);
                http.setHeader("Content-Type", "application/json;charset=UTF-8");
            }
            HttpEntity entity = client.execute(http).getEntity();
            return JSONObject.parseObject(entity.getContent().toString());
        }
    }

    public static JSONObject change(String url, HttpMethod httpMethod) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        if (httpMethod == HttpMethod.GET) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        } else if (httpMethod == HttpMethod.POST) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        org.springframework.http.HttpEntity<?> requestEntity;
        // 如果是GET请求，HttpEntity可以直接创建，无需请求体
        if (httpMethod == HttpMethod.GET) {
            requestEntity = new org.springframework.http.HttpEntity<>(headers);
        } else {
            // 对于POST请求，假设我们有一个名为requestBody的对象作为JSON格式的请求体
            Object requestBody = null; // 实际请求体内容，请替换...
            requestEntity = new org.springframework.http.HttpEntity<>(requestBody, headers);
        }

        ResponseEntity<String> response = restTemplate.exchange(url, httpMethod, requestEntity, String.class);
        log.info(response.toString());
        return JSONObject.parseObject(response.getBody());
    }

    public static JSONObject sendHttpRequest(String url, HttpMethod httpMethod, JSONObject requestBody) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpResponse response;
        if (httpMethod == HttpMethod.GET) {
            HttpGet httpGet = new HttpGet(url);
            response = httpClient.execute(httpGet);
        } else {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            // Assuming you have a requestBody object for POST requests
            String requestBodyJson = JSONObject.toJSONString(requestBody);
            StringEntity stringEntity = new StringEntity(requestBodyJson);
            httpPost.setEntity(stringEntity);

            response = httpClient.execute(httpPost);
        }

        String responseBody = EntityUtils.toString(response.getEntity());
        return JSONObject.parseObject(responseBody);
    }
}
