package com.example.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorDetails errorDetails = new ErrorDetails(errorCode.getCode(), errorCode.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errorDetails);

        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        ErrorDetails errorDetails = new ErrorDetails(errorCode.getCode(), errorCode.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errorDetails);

        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        ErrorDetails errorDetails = new ErrorDetails(
                ErrorCode.INVALID_CREDENTIALS.getCode(),
                ErrorCode.INVALID_CREDENTIALS.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(errorDetails);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        ErrorDetails errorDetails = new ErrorDetails(
                ErrorCode.INVALID_TOKEN.getCode(),
                ErrorCode.INVALID_TOKEN.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(errorDetails);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({io.jsonwebtoken.JwtException.class, io.jsonwebtoken.ExpiredJwtException.class})
    public ResponseEntity<ErrorResponse> handleJwtException(Exception e) {
        ErrorDetails errorDetails = new ErrorDetails(
                ErrorCode.INVALID_TOKEN.getCode(),
                ErrorCode.INVALID_TOKEN.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(errorDetails);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
