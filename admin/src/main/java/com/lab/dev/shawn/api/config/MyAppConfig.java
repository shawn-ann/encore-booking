package com.lab.dev.shawn.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myapp")
@Data
public class MyAppConfig {
    private boolean isMockSendSms;
    private String appId;
    private String appSecret;
    private String mchntCd;
}
