package com.chatter.chatter_app.modules.user;

import com.chatter.chatter_app.modules.user.model.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {
  private final UserRepository userRepository;

  public UserController(UserRepository userRepository){
      this.userRepository = userRepository;
  }

  @GetMapping("/me")
    public ResponseEntity<UserEntity> getMyUser(){
      UserEntity user = new UserEntity();
      user.setName("Lucas");
      user.setCountryCode(55);
      user.setCreatedAt(LocalDateTime.now());
      user.setUpdatedAt(LocalDateTime.now());
      user.setEmail("Aasdasd");
      user.setId(UUID.randomUUID());
      return new ResponseEntity<>(user, HttpStatus.OK);
  }
}

