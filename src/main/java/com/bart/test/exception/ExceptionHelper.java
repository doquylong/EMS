package com.bart.test.exception;

import com.bart.test.dto.response.AppResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHelper{

    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<Object> badCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.ok().body(AppResponse.failed(ErrorInfo.USERNAME_OR_PASS_WRONG));
    }

    @ExceptionHandler({ AppException.class })
    public ResponseEntity<Object> handleAppException(AppException ex) {
        return ResponseEntity.ok().body(new AppResponse(ex.getCode(),ex.getMessage()));
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.ok().body(new AppResponse(ErrorInfo.ACCESS_DENIED));
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.ok().body(new AppResponse(ErrorInfo.SYSTEM_ERROR));
    }
}
