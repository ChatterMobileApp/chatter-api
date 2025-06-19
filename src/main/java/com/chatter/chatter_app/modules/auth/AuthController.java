package com.chatter.chatter_app.modules.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatter.chatter_app.common.dto.ApiResponse;
import com.chatter.chatter_app.modules.auth.dto.AuthResponse;
import com.chatter.chatter_app.modules.auth.dto.SendSmsRequest;
import com.chatter.chatter_app.modules.auth.dto.VerifySmsRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  @Autowired
  private AuthService authService;
  
  @PostMapping("/send-sms")
  public ResponseEntity<ApiResponse<Void>> sendSms(@Valid @RequestBody SendSmsRequest request) {
      authService.sendVerificationCode(request.getPhoneNumber());
      return ResponseEntity.ok(ApiResponse.success("Código de verificação SMS enviado com sucesso"));
  }
  
  @PostMapping("/verify")
  public ResponseEntity<ApiResponse<AuthResponse>> verifySms(@Valid @RequestBody VerifySmsRequest request) {
      AuthResponse authResponse = authService.authenticateUser(request.getPhoneNumber(), request.getCode());
      return ResponseEntity.ok(ApiResponse.success("Autenticação bem sucedida", authResponse));
  }
}
