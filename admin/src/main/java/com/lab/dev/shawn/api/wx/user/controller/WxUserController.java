package com.lab.dev.shawn.api.wx.user.controller;

import com.lab.dev.shawn.api.base.dto.ApiResponseBody;
import com.lab.dev.shawn.api.entity.Agent;
import com.lab.dev.shawn.api.util.JwtUtil;
import com.lab.dev.shawn.api.wx.user.service.WxUserService;
import com.lab.dev.shawn.api.wx.verify_code.service.VerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/wx/user")
public class WxUserController {
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private VerifyCodeService verifyCodeService;

    record UserVO(String mobile, String smsCode, String wxCode) {
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponseBody> login(@RequestBody UserVO userVO) throws Exception {

        verifyCodeService.verifySmsCode(userVO.mobile, userVO.smsCode, true);

        Agent agent = wxUserService.login(userVO.mobile, userVO.wxCode);
        String token = JwtUtil.generateToken(agent);
        ApiResponseBody body = new ApiResponseBody(Map.of("token", token, "name", agent.getName(), "mobile", agent.getMobile()));
        return ResponseEntity.ok(body);
    }

}
