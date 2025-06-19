package com.chatter.chatter_app.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class SendSmsRequest {
  @NotBlank(message = "Número de telefone é obrigatório")
  private String phoneNumber;
  
  // Getters and setters
  public String getPhoneNumber() { return phoneNumber; }
  public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
