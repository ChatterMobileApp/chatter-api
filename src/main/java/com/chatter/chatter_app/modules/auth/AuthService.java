package com.chatter.chatter_app.modules.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatter.chatter_app.modules.auth.dto.AuthResponse;
import com.chatter.chatter_app.modules.auth.security.JwtUtil;
import com.chatter.chatter_app.modules.auth.service.SmsVerificationService;
import com.chatter.chatter_app.modules.user.UserService;
import com.chatter.chatter_app.modules.user.dto.UserDTO;
import com.chatter.chatter_app.modules.user.model.UserEntity;

@Service
@Transactional
public class AuthService {
    
    @Autowired
    private SmsVerificationService smsVerificationService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public void sendVerificationCode(String phoneNumber) {
        smsVerificationService.sendVerificationCode(phoneNumber);
    }
    
    public AuthResponse authenticateUser(String phoneNumber, String code) {
        boolean isValid = smsVerificationService.verifyCode(phoneNumber, code);
        
        if (!isValid) {
            throw new RuntimeException("Invalid or expired verification code");
        }
        
        // Get or create user
        Optional<UserEntity> existingUser = userService.findByPhoneNumber(phoneNumber);
        UserEntity user;
        
        if (existingUser.isPresent()) {
            user = existingUser.get();
            user.setVerified(true);
        } else {
            user = userService.createUser(phoneNumber);
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getPhoneNumber(), user.getId());
        
        return new AuthResponse(token, new UserDTO(user));
    }
}
