package com.chatter.chatter_app.modules.user.dto;
import java.time.LocalDateTime;
import java.util.UUID;

import com.chatter.chatter_app.modules.user.model.UserEntity;

public class UserDTO {
    private final UUID id;
    private final String phoneNumber;
    private final String name;
    private final String email;
    private final boolean isVerified;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    
    public UserDTO(UserEntity user) {
      this.id = user.getId();
      this.phoneNumber = user.getPhoneNumber();
      this.name = user.getName();
      this.email = user.getEmail();
      this.isVerified = user.isVerified();
      this.isActive = user.isActive();
      this.createdAt = user.getCreatedAt();
    }
    
    // Getters
    public UUID getId() { return id; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public boolean isVerified() { return isVerified; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}