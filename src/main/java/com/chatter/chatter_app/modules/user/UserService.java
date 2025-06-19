package com.chatter.chatter_app.modules.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatter.chatter_app.modules.user.dto.UpdateUserRequest;
import com.chatter.chatter_app.modules.user.dto.UserDTO;
import com.chatter.chatter_app.modules.user.model.UserEntity;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public UserEntity createUser(String phoneNumber) {
      if (userRepository.existsByPhoneNumber(phoneNumber)) {
        throw new RuntimeException("Usuário já existe com este número de telefone");
      }
      
      UserEntity user = new UserEntity(phoneNumber);
      user.setVerified(true);
      return userRepository.save(user);
    }
    
    public UserEntity updateUser(UUID userId, UpdateUserRequest request) {
      UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
      
      if (request.getName() != null && !request.getName().trim().isEmpty()) {
        user.setName(request.getName().trim());
      }
      if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
        user.setEmail(request.getEmail().trim());
      }
      
      return userRepository.save(user);
    }
    
    public Optional<UserEntity> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumberAndIsActiveTrue(phoneNumber);
    }
    
    public UserDTO getUserById(UUID userId) {
      UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
      return new UserDTO(user);
    }
    
    public void deactivateUser(UUID userId) {
      UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
      user.setActive(false);
      userRepository.save(user);
    }
}
