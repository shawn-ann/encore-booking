package com.lab.dev.shawn.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.config.MyAppConfig;
import com.lab.dev.shawn.api.util.entity.WxOpenIdApiResponse;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class WeChatApiUtil {
    @Autowired
    private MyAppConfig myAppConfig;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String WECHAT_API_URL = "https://api.weixin.qq.com/sns/jscode2session";

    public String retrieveOpenId(String wxCode) throws Exception {
        String appId = myAppConfig.getAppId();
        String appSecret = myAppConfig.getAppSecret();
        // 构建请求 URL
        String url = WECHAT_API_URL + "?appid=" + appId + "&secret=" + appSecret + "&js_code=" + wxCode + "&grant_type=authorization_code";

        // 创建 HttpClient 实例
        HttpClient client = HttpClient.newHttpClient();

        // 创建 HTTP 请求对象
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        // 发送 HTTP 请求并获取响应
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 处理响应
        if (response.statusCode() != 200) {
            throw new BaseException(BaseExceptionEnum.RETRIEVE_OPENID_FAILED);
        }
        String body = response.body();
        WxOpenIdApiResponse responseBody = objectMapper.readValue(body, WxOpenIdApiResponse.class);
        if (responseBody.getErrcode() != 0) {
            System.out.println("获取openId失败，" + responseBody.getErrmsg());
            throw new BaseException(BaseExceptionEnum.RETRIEVE_OPENID_FAILED);
        }
        if (StringUtils.isBlank(responseBody.getOpenid())) {
            System.out.println("获取的openId为空！");
            throw new BaseException(BaseExceptionEnum.RETRIEVE_OPENID_FAILED);
        }
        return responseBody.getOpenid();
    }


}
