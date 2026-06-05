package com.travelplanner.backend.mapper;

import com.travelplanner.backend.dto.UserRequest;
import com.travelplanner.backend.dto.UserResponse;
import com.travelplanner.backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
  private final PasswordEncoder passwordEncoder;

  public User toEntity(UserRequest request) {
    return new User(
        request.name(),
        request.email(),
        passwordEncoder.encode(request.password())
    );
  }

  public UserResponse toResponse(User user) {
    return new UserResponse(
        user.getId(),
        user.getName(),
        user.getEmail()
    );
  }

  public void updateEntityFromRequest(User user, UserRequest request) {
    user.setName(request.name());
    user.setEmail(request.email());
    user.setPassword(passwordEncoder.encode(request.password()));
  }

}
