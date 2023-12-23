package com.lab.dev.shawn.admin.base.handler;

import com.lab.dev.shawn.admin.base.dto.ApiResponseBody;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import org.hibernate.annotations.processing.SQL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponseBody> handleInvalidCredentialsException(BaseException ex) {
        ApiResponseBody response = new ApiResponseBody(ex.getCode(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseBody> handleException(Exception ex) {
        ApiResponseBody response = new ApiResponseBody(500, "系统异常，请联系管理员", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseBody> handleRollbackException(Exception ex) {
        ApiResponseBody response = new ApiResponseBody(4000, "已存在重复记录！", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
