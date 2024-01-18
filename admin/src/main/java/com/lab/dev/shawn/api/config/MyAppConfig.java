package com.lab.dev.shawn.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myapp")
@Data
public class MyAppConfig {
    private boolean isMockSendSms;
    private boolean isMockPay;

    private String appid;
    private String appSecret;
    private String fuiouMchntCd;
    private String fuiouPriKey;
    private String fuiouPubKey;
    private int orderPayTimeout;
    private String backendPayNotifyUrl;
}
