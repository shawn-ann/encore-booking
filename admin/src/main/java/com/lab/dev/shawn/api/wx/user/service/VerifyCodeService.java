package com.lab.dev.shawn.api.wx.user.service;

import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.entity.Agent;
import com.lab.dev.shawn.api.repository.AgentRepository;
import com.lab.dev.shawn.api.util.SendSmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class VerifyCodeService {
    @Autowired
    private VerifyCodeRepository verifyCodeRepository;

    private static String generateVerifyCode(){
        return "1111";
    }

    public String sendSmsCode(String mobile,boolean isLogin) throws BaseException {
        //查询该一分钟内是否发过验证码

        //生成验证码
        String code = generateVerifyCode();

        VerifyCode verifyCode = new VerifyCode();
        code.setMobile(mobile);
        code.setCode(code);
        code.setIsLogin(isLogin);
        code.setExpiredDate(LocalDateTime.now().plusMinutes(5));
        verifyCodeRepository.save(code);

        SendSmsUtil.send(mobile, code);
        return code;
    }
}
