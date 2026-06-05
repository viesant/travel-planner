package com.travelplanner.backend.service;

import com.travelplanner.backend.dto.UserRequest;
import com.travelplanner.backend.dto.UserResponse;
import com.travelplanner.backend.entity.User;
import com.travelplanner.backend.exception.EmailAlreadyExistsException;
import com.travelplanner.backend.mapper.UserMapper;
import com.travelplanner.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final AuthService authService;
  private final PasswordEncoder passwordEncoder;

  public UserResponse create(UserRequest request) {
    validateEmailUnique(request.email());

    User newUser = userMapper.toEntity(request);
    User savedUser = userRepository.save(newUser);

    return userMapper.toResponse(savedUser);
  }

  public UserResponse findMe() {

    User user = authService.getAuthenticatedUser();
    return userMapper.toResponse(user);
  }

  public UserResponse updateMe(UserRequest request) {

    User user = authService.getAuthenticatedUser();

    if (!user.getEmail().equals(request.email())) {
      validateEmailUnique(request.email());
    }

    userMapper.updateEntityFromRequest(user, request);

    User savedUser = userRepository.save(user);
    return userMapper.toResponse(savedUser);
  }

  public void deleteMe() {
    User user = authService.getAuthenticatedUser();
    userRepository.delete(user);
  }

  private void validateEmailUnique(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new EmailAlreadyExistsException(email);
    }
  }

}
