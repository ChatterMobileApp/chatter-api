package com.chatter.chatter_app.modules.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatter.chatter_app.modules.user.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  Optional<UserEntity> findByPhoneNumber(String phoneNumber);
  boolean existsByPhoneNumber(String phoneNumber);
  Optional<UserEntity> findByPhoneNumberAndIsActiveTrue(String phoneNumber);
}
