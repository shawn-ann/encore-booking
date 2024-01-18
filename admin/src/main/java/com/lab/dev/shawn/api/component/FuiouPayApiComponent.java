package com.lab.dev.shawn.api.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.component.entity.*;
import com.lab.dev.shawn.api.config.MyAppConfig;
import com.lab.dev.shawn.api.util.RsaUtil;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class FuiouPayApiComponent {

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

    private static final String API_PREFIX = "https://aggpc-test.fuioupay.com/aggpos";

    private String sendRequest(String apiUrl, String message) throws Exception {
        // 构建请求 URL
        String url = API_PREFIX + apiUrl;
        Map<String, String> req = Map.of("mchnt_cd", myAppConfig.getFuiouMchntCd(), "message", encrypt(message));
        ObjectMapper objectMapper = new ObjectMapper();
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
        FuiouApiResponse responseBody = objectMapper.readValue(body, FuiouApiResponse.class);
        if (!"0000".equals(responseBody.getResp_code()) || StringUtils.isBlank(responseBody.getMessage())) {
            System.out.println("调用接口异常，返回内容为：" + responseBody.toString());
            throw new BaseException(BaseExceptionEnum.CALL_THIRD_PARTY_API_ERROR);
        }
        return decrypt(responseBody.getMessage());
    }

    private String decrypt(String message) {
        //商户私钥解密
        return RsaUtil.decryptByRsaPri(message, myAppConfig.getFuiouPriKey(), "GBK");
    }

    private String encrypt(String message) {
        //富友公钥加密
        return RsaUtil.encryptByRsaPub(myAppConfig.getFuiouPubKey(), myAppConfig.getFuiouPubKey(), "GBK");
    }

    public FuiouPayResponseBody pay(FuiouPayRequestBody requestBody) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("mchnt_cd", myAppConfig.getFuiouMchntCd());
        param.put("order_date", requestBody.getOrder_date());
        param.put("order_id", requestBody.getOrder_id());
        param.put("order_amt", requestBody.getOrder_amt());
        param.put("order_pay_type", "LETPAY");
        param.put("back_notify_url", myAppConfig.getBackendPayNotifyUrl());
        param.put("goods_name", requestBody.getGoods_name());
        param.put("goods_detail", requestBody.getGoods_detail());
        param.put("order_timeout", myAppConfig.getOrderPayTimeout());
        param.put("appid", myAppConfig.getAppid());
        param.put("openid", requestBody.getOpenid());
        param.put("ver", "1.0.0");
        ObjectMapper objectMapper = new ObjectMapper();
        String reqMessage = objectMapper.writer().writeValueAsString(param);
        String apiUrl = "/order.fuiou";
        String response = sendRequest(apiUrl, reqMessage);
        FuiouPayResponseBody fuiouPayResponseBody = objectMapper.readValue(response, FuiouPayResponseBody.class);
        return fuiouPayResponseBody;
    }

    public FuiouRefundResponseBody refund(FuiouRefundRequestBody requestBody) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("mchnt_cd", myAppConfig.getFuiouMchntCd());
        param.put("refund_order_date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        param.put("refund_order_id", requestBody.getRefund_order_id());
        param.put("pay_order_date", requestBody.getPay_order_date());
        param.put("pay_order_id", requestBody.getPay_order_id());
        param.put("refund_amt", requestBody.getRefund_amt());
        param.put("ver", "1.0.0");
        ObjectMapper objectMapper = new ObjectMapper();
        String reqMessage = objectMapper.writer().writeValueAsString(param);
        String apiUrl = "/order.fuiou";
        String response = sendRequest(apiUrl, reqMessage);
        FuiouRefundResponseBody fuiouPayResponseBody = objectMapper.readValue(response, FuiouRefundResponseBody.class);
        return fuiouPayResponseBody;
    }

    public FuiouPayNotifyRequestBody parseNotifyMessage(String requestJson) throws JsonProcessingException {
        System.out.println("接收到支付通知，消息体为【" + requestJson + "】");
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(requestJson, Map.class);
        String json = decrypt(map.get("message").toString());
        System.out.println("解析后的消息体为【" + json + "】");
        return objectMapper.readValue(json, FuiouPayNotifyRequestBody.class);
    }

    public static void main(String[] args) throws Exception {
        FuiouPayRequestBody requestBody = new FuiouPayRequestBody();

    }
}
