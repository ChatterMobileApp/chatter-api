package com.chatter.chatter_app.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class VerifySmsRequest {
  @NotBlank(message = "Phone number is required")
  private String phoneNumber;

  @NotBlank(message = "Verification code is required")
  @Size(min = 6, max = 6, message = "Verification code must be 6 digits")
  @Pattern(regexp = "\\d{6}", message = "Verification code must contain only digits")
  private String code;

  // Getters and setters
  public String getPhoneNumber() { return phoneNumber; }
  public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }
}
