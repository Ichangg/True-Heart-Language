package com.englishcenter.backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<?> handleDateError(DateTimeParseException e) {
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Định dạng ngày tháng không hợp lệ. Vui lòng dùng yyyy-MM hoặc yyyy-MM-dd"
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralError(Exception e) {
        return ResponseEntity.internalServerError().body(Map.of(
            "error", "Đã có lỗi hệ thống xảy ra: " + e.getMessage()
        ));
    }
}