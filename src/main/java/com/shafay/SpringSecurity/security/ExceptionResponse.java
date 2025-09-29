package com.shafay.SpringSecurity.security;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(HttpStatus status, String message) {
}