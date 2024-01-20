package com.lab.dev.shawn.api.component;

import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.config.MyAppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SendSmsComponent {

    @Autowired
    private MyAppConfig myAppConfig;

    public String send(String mobile) throws Exception {
        String code = generateRandomCode();
        if (myAppConfig.isMockSendSms()) {
            code = "1111";
            return code;
        }
        System.out.println("手机号【" + mobile + "】，验证码【" + code + "】");
        String accessKeyId = myAppConfig.getAliyunAccessKeyId();
        String accessKeySecret = myAppConfig.getAliyunAccessKeySecret();
        String signName = myAppConfig.getAliyunSmsSignName();
        String smsTemplateCode = myAppConfig.getAliyunSmsTemplateCode();

        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        // 特化请求客户端
        com.aliyun.dysmsapi20170525.Client dysmsapiClient = new com.aliyun.dysmsapi20170525.Client(config);


        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setPhoneNumbers(mobile)
                .setSignName(signName)
                .setTemplateCode(smsTemplateCode).setTemplateParam("{\"code\":\"" + code + "\"}");
        SendSmsResponse sendSmsResponse = dysmsapiClient.sendSms(sendSmsRequest);
        if (sendSmsResponse.getStatusCode() != 200 || !"OK".equals(sendSmsResponse.getBody().getCode())) {
            System.out.println("调用阿里云发送短信接口失败，响应信息为【" + new Gson().toJson(sendSmsResponse) + "】");
            throw new BaseException(BaseExceptionEnum.SENDSMS_FAILED);
        }
        return code;
    }


    private static String generateRandomCode() {
        Random random = new Random();
        int randomNum = random.nextInt(10000); // 生成一个0到9999的随机数
        return String.format("%04d", randomNum);
    }

    public static void main(String[] args) {

    }
}
