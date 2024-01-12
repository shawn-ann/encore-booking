package com.lab.dev.shawn.api.wx.verify_code.controller;

import com.lab.dev.shawn.api.base.dto.ApiResponseBody;
import com.lab.dev.shawn.api.wx.verify_code.service.VerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wx/verify/code")
public class WxVerifyCodeController {
    @Autowired
    private VerifyCodeService verifyCodeService;

    record SendRequestVO(String mobile, boolean isLogin) {
    }

    record VerifyRequestVO(String mobile, String inputCode, boolean isLogin) {
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponseBody> sendSmsCode(@RequestBody SendRequestVO requestVO) throws Exception {
        String smsCode = verifyCodeService.sendSmsCode(requestVO.mobile, requestVO.isLogin);
        ApiResponseBody body = new ApiResponseBody(smsCode);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponseBody> verifySmsCode(@RequestBody VerifyRequestVO requestVO) throws Exception {
        boolean result = verifyCodeService.verifySmsCode(requestVO.mobile, requestVO.inputCode, requestVO.isLogin);
        ApiResponseBody body = new ApiResponseBody(result);
        return ResponseEntity.ok(body);
    }

}
