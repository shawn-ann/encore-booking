package com.lab.dev.shawn.api.wx.verify_code.service;

import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.entity.Agent;
import com.lab.dev.shawn.api.entity.VerifyCode;
import com.lab.dev.shawn.api.repository.AgentRepository;
import com.lab.dev.shawn.api.repository.VerifyCodeRepository;
import com.lab.dev.shawn.api.util.SendSmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Transactional(rollbackFor = Exception.class)
public class VerifyCodeService {
    @Autowired
    private VerifyCodeRepository verifyCodeRepository;
    @Autowired
    private AgentRepository agentRepository;

    public String sendSmsCode(String mobile, boolean isLogin) throws BaseException {
        // 只有注册过的代理商才能发短信
        Agent agent = agentRepository.findActiveByMobile(mobile);
        if (agent == null) {
            throw new BaseException(BaseExceptionEnum.USER_NOT_EXIST);
        }

        //查询该一分钟内是否发过验证码
        int sendCounts = verifyCodeRepository.findByMobileAndIsLoginAndCreateDateGreaterThan(isLogin, mobile, LocalDateTime.now().minusMinutes(1)).size();
        if (sendCounts > 0) {
            throw new BaseException(BaseExceptionEnum.SMS_CODE_ALREADY_SEND);
        }
        //生成验证码
        String code = generateRandomCode();
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setMobile(mobile);
        verifyCode.setCode(code);
        verifyCode.setLogin(isLogin);
        verifyCodeRepository.save(verifyCode);

        SendSmsUtil.send(mobile, code);
        return code;
    }

    public boolean verifySmsCode(String mobile, String code, boolean isLogin) throws BaseException {
        //查询该一分钟内是否发过验证码
        VerifyCode verifyCode = verifyCodeRepository.findNotExpiredByMobileAndIsLogin(isLogin, mobile);
        if (verifyCode == null || !verifyCode.getCode().equals(code)) {
            throw new BaseException(BaseExceptionEnum.SMS_CODE_INCORRECT);
        }
        verifyCode.setVerified(true);
        verifyCodeRepository.save(verifyCode);
        return true;
    }

    private static String generateRandomCode() {
        Random random = new Random();
        int randomNum = random.nextInt(10000); // 生成一个0到9999的随机数
        // TODO
        randomNum = 1111;
        return String.format("%04d", randomNum);
    }
}
