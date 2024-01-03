package com.lab.dev.shawn.api.wx.user.controller;

import com.lab.dev.shawn.api.base.dto.ApiResponseBody;
import com.lab.dev.shawn.api.entity.Agent;
import com.lab.dev.shawn.api.util.JwtUtil;
import com.lab.dev.shawn.api.wx.user.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/wx/user")
public class WxUserController {
    @Autowired
    private WxUserService wxUserService;

    record UserVO(String mobile, String smsCode) {
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseBody> login(@RequestBody UserVO userVO) throws Exception {
        Agent agent = wxUserService.login(userVO.mobile, userVO.smsCode);
        String token = JwtUtil.generateToken(agent);
        ApiResponseBody body = new ApiResponseBody(Map.of("token", token));
        return ResponseEntity.ok(body);
    }

    @PostMapping("/send_sms_code/{mobile}")
    public ResponseEntity<ApiResponseBody> sendSmsCode(@PathVariable String mobile) throws Exception {
        String smsCode = wxUserService.sendSmsCode(mobile);
        ApiResponseBody body = new ApiResponseBody(smsCode);
        return ResponseEntity.ok(body);
    }
}
