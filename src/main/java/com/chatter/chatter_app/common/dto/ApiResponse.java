package com.chatter.chatter_app.common.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    private final String error;
    private final LocalDateTime timestamp;
    
    private ApiResponse(boolean success, String message, T data, String error) {
      this.success = success;
      this.message = message;
      this.data = data;
      this.error = error;
      this.timestamp = LocalDateTime.now();
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
      return new ApiResponse<>(true, message, data, null);
    }
    
    public static <T> ApiResponse<T> success(String message) {
      return new ApiResponse<>(true, message, null, null);
    }
    
    public static <T> ApiResponse<T> error(String error) {
      return new ApiResponse<>(false, null, null, error);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getError() { return error; }
    public LocalDateTime getTimestamp() { return timestamp; }
}