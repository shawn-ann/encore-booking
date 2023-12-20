package com.lab.dev.shawn.admin.service;

import com.lab.dev.shawn.admin.entity.User;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {

    private static final MessageDigest MESSAGE_DIGEST_INSTANCE;

    static {
        try {
            MESSAGE_DIGEST_INSTANCE = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static String hashPassword(String password) {
        byte[] hashedBytes = MESSAGE_DIGEST_INSTANCE.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public User login(String accountId, String password) throws Exception {
        User user = userRepository.findByAccountId(accountId);

        if (user == null) {
            throw new BaseException(BaseExceptionEnum.INCORRECT_USER_CREDENTIAL);
        }
        if (!hashPassword(password).equals(user.getPassword())) {
            throw new BaseException(BaseExceptionEnum.INCORRECT_USER_CREDENTIAL);
        }
        return user;

    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}