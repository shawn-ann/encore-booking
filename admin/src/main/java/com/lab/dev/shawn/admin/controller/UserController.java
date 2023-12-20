package com.lab.dev.shawn.admin.controller;

import com.lab.dev.shawn.admin.aspect.AuthInterceptor;
import com.lab.dev.shawn.admin.base.dto.ApiResponseBody;
import com.lab.dev.shawn.admin.entity.User;
import com.lab.dev.shawn.admin.service.UserService;
import com.lab.dev.shawn.admin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity userInfo(HttpServletRequest request) {
        String token = request.getHeader("X-Token");

        Long userId = JwtUtil.getTokenClaims(token).get("id", Long.class);
        User user = userService.findById(userId);

        ApiResponseBody body = new ApiResponseBody(Map.of("name", user.getName()));
        return ResponseEntity.ok(body);
    }
    record UserVO(String username, String password) {

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseBody> login(@RequestBody UserVO userVO, HttpServletRequest request) throws Exception {
        User user = userService.login(userVO.username, userVO.password);
        String token = JwtUtil.generateToken(user);
        request.getSession().setAttribute(AuthInterceptor.TOKEN_KEY, token);

        ApiResponseBody body = new ApiResponseBody(token);
        return ResponseEntity.ok(body);

    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseBody> logout(HttpServletRequest request) throws Exception {
        request.getSession().setAttribute(AuthInterceptor.TOKEN_KEY, null);

        ApiResponseBody body = new ApiResponseBody("Logout success!");
        return ResponseEntity.ok(body);

    }
}
