package com.chatter.chatter_app.modules.auth.model;

import java.time.LocalDateTime;

import com.chatter.chatter_app.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "sms_verification")
public class SmsVerification extends BaseEntity {
    
  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;
  
  @Column(name = "code", nullable = false)
  private String code;
  
  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;
  
  @Column(name = "is_used", nullable = false)
  private boolean isUsed = false;
  
  @Column(name = "attempts", nullable = false)
  private int attempts = 0;
  
  // Constructors
  public SmsVerification() {}
  
  public SmsVerification(String phoneNumber, String code) {
    this.phoneNumber = phoneNumber;
    this.code = code;
    this.expiresAt = LocalDateTime.now().plusMinutes(5); // 5 minutes expiry
  }
  
  // Getters and setters
  public String getPhoneNumber() { return phoneNumber; }
  public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
  
  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }
  
  public LocalDateTime getExpiresAt() { return expiresAt; }
  public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
  
  public boolean isUsed() { return isUsed; }
  public void setUsed(boolean used) { isUsed = used; }
  
  public int getAttempts() { return attempts; }
  public void setAttempts(int attempts) { this.attempts = attempts; }
  
  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiresAt);
  }
}
