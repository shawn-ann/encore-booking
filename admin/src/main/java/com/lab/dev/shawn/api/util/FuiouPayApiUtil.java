package com.lab.dev.shawn.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.config.MyAppConfig;
import com.lab.dev.shawn.api.util.entity.FuiouPayApiResponse;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class FuiouPayApiUtil {

    //    生产环境：
//    获取openid     			  https://aggpcpay.fuioupay.com/aggpos/getOpenid.fuiou
//    订单支付接口   			  https://aggpcpay.fuioupay.com/aggpos/order.fuiou
//    订单支付被扫接口          https://aggpcpay.fuioupay.com/aggpos/orderScan.fuiou
//    支付查询接口   			  https://aggpcpay.fuioupay.com/aggpos/orderQuery.fuiou
//    订单退款接口   			  https://refund-transfer.fuioupay.com/refund_transfer/aggposRefund.fuiou
//    退款查询接口   			  https://refund-transfer.fuioupay.com/refund_transfer/aggposRefundQuery.fuiou
//    测试环境
//    获取openid     			  https://aggpc-test.fuioupay.com/aggpos/getOpenid.fuiou
//    订单支付接口   			  https://aggpc-test.fuioupay.com/aggpos/order.fuiou
//    订单支付被扫接口          https://aggpc-test.fuioupay.com/aggpos/orderScan.fuiou
//    支付查询接口   			  https://aggpc-test.fuioupay.com/aggpos/orderQuery.fuiou
//    订单退款接口   			  https://refund-transfer-test.fuioupay.com/refund_transfer/aggposRefund.fuiou
//    退款查询接口   			  https://refund-transfer-test.fuioupay.com/refund_transfer/aggposRefundQuery.fuiou
    @Autowired
    private MyAppConfig myAppConfig;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String WECHAT_API_URL = "https://aggpc-test.fuioupay.com/aggpos";

    public static String retrieveOpenId(String apiUrl, String message) throws Exception {
        String mchnt_cd = "0001000F0040992";
        // 构建请求 URL
        String url = WECHAT_API_URL + apiUrl;
        Map<String, String> req = Map.of("mchnt_cd", mchnt_cd, "message", encrypt(message));
        String requestBody = objectMapper.writer().writeValueAsString(req);

        // 创建 HttpClient 实例
        HttpClient client = HttpClient.newHttpClient();

        // 创建 HTTP 请求对象
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        // 发送 HTTP 请求并获取响应
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 处理响应
        if (response.statusCode() != 200) {
            throw new BaseException(BaseExceptionEnum.RETRIEVE_OPENID_FAILED);
        }
        String body = response.body();
        FuiouPayApiResponse responseBody = objectMapper.readValue(body, FuiouPayApiResponse.class);
        if (!"0000".equals(responseBody.getResp_code()) || StringUtils.isBlank(responseBody.getMessage())) {
            System.out.println("调用接口异常，返回内容为：" + responseBody.toString());
            throw new BaseException(BaseExceptionEnum.CALL_THIRD_PARTY_API_ERROR);
        }
        return decrypt(responseBody.getMessage());
    }

    private static String decrypt(String message) {
        //商户私钥解密
        String pri_key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJMr8NnRV7ve7Y5FEBium/TsU0fK5NvzvFpsYxPAQhBXY+EN0Bi2JEg790C1njk9Q3U36u2JBDHAiDIomlgh6wWkJsFn7dghV/fCWSX1VVJ+dRINZy1432fRaJ8GqspvMneBpeLjBe94IwlWKpN+AOR+BNX8QL/uHmfCPlVQXos9AgMBAAECgYAzqbMs434m50UBMmFKKNF6kxNRGnpodBFktLO7FTybu/HF6TFp21a1PMe5IYhfk5AAsBZ6OCUOygWFhhdYZN+5W+dweF3kp1rLE4y5CjwqNlk/g22TAndf9znh/ltHFLvITToqu/eh/34tE1gyNxRbsi1olw/1wv8ZRjM3vtM9QQJBANvNwFq+CJHUyFzkXQB7+ycQFnY8wDq8Uw2Hv9ZMjgIntH7FSlJtdu5mAYPPo6f74slO5tFUMNP7EVppqsjYaNkCQQCraD6iKHo+OIlvvYIKiMXatJGD7N1GNhq5CrhUNPWLHwv/Ih2D3JJdF8IUZOPIJfUxTfM2fZYI+EVdsv6s4RcFAkAGjNYbnighOGcUJZYD6q3sVxVkRqEv3ubWs2HrH/Lna4l8caKqXCq8JfwLkod8/QugFiLYwBqIZqX4vMdjHtfZAkBsAl9dbWZCaPvpxp/4JWGPxDLhz9NLV/KU4bVvkoObq++yUHwKyGYOdVcd5MlIKOsNq5Hzp0Vw14lWVuF2bMxFAkBuNrZksvUULNIaWDKd4rQ6GVzUxXuIZW0ZE6atHYDiXPB4jVAjKRtLxZAV1qH9cr1zNJlcg+RbGYUdF9t4A9n5";
        return RsaUtil.decryptByRsaPri(message, pri_key, "GBK");
    }

    private static String encrypt(String message) {
        //富友公钥加密
        String pub_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKPD4f/3xMjPuHcQSGxPIYZNgF2i0sJhzmKpN3dmzhbKH/1nG+aXUJDhswyitzrI+U0ic/GL/IWB1wQ3noWuLFr8jDSGafenTFiY9c0H9ZAEfqW/oywx95G5JWu5y/ffp4yCmlt8k5kHO/4kE1qnJcGaQlb6/+7t3MPSV5ybmBZwIDAQAB";
        return RsaUtil.encryptByRsaPub(pub_key, pub_key, "GBK");
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        param.put("mchnt_cd", "0001000F0040992");
        param.put("order_date", "20221110");
        param.put("order_id", "2023F1110551545151212");
        param.put("order_amt", "1");
        param.put("order_pay_type", "LETPAY");
        param.put("back_notify_url", "http://www.baidu.com");
        param.put("goods_name", "苹果手机");
        param.put("goods_detail", "苹果手机");
        param.put("appid", "苹果手机");
        param.put("openid", "苹果手机");
        param.put("ver", "1.0.0");
        String reqMessage = objectMapper.writer().writeValueAsString(param);
        String apiUrl = "/order.fuiou";
        String response = retrieveOpenId(apiUrl, reqMessage);
        System.out.println(response);
    }
}
