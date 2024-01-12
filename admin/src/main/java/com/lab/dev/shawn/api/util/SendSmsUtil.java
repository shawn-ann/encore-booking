package com.lab.dev.shawn.api.util;

public class SendSmsUtil {
    public static boolean send(String mobile, String code) {
        System.out.println("手机号【" + mobile + "】，验证码【" + code + "】");
        return true;
    }
}
