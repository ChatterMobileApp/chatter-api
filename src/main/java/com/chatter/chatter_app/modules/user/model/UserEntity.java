package com.chatter.chatter_app.modules.user.model;

import java.util.Objects;

import com.chatter.chatter_app.common.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

  @Column(name = "name", unique = true)
  private String name;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "phone_number", nullable = false, unique = true)
  private String phoneNumber;

  @Column(name = "is_verified", nullable = false )
  private boolean isVerified = false;
    
  @Column(name = "is_active", nullable = false)
  private boolean isActive = true;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public boolean isVerified() { return isVerified; }
  public void setVerified(boolean verified) { isVerified = verified; }
    
  public boolean isActive() { return isActive; }
  public void setActive(boolean active) { isActive = active; }

  public UserEntity() {}

  public UserEntity(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    UserEntity that = (UserEntity) o;
    return Objects.equals(this.getId(), that.getId()) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(phoneNumber, that.phoneNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId(), name, email, phoneNumber);
  }
}
