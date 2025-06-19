package com.chatter.chatter_app.modules.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatter.chatter_app.common.dto.ApiResponse;
import com.chatter.chatter_app.modules.auth.security.JwtUtil;
import com.chatter.chatter_app.modules.user.dto.UpdateUserRequest;
import com.chatter.chatter_app.modules.user.dto.UserDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        UUID userId = jwtUtil.extractUserId(token);
        
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success("Perfil do usuário recuperado com sucesso", user));
    }
    
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequest request,
            HttpServletRequest httpRequest) {
        
        String token = extractTokenFromRequest(httpRequest);
        UUID tokenUserId = jwtUtil.extractUserId(token);
        
        if (!userId.equals(tokenUserId)) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Você só pode atualizar seu próprio perfil"));
        }
        
        userService.updateUser(userId, request);
        UserDTO updatedUser = userService.getUserById(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Usuário atualizado com sucesso", updatedUser));
    }
    
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("Token válido não encontrado");
    }
}

