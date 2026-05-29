package com.travelplanner.backend.service;

import com.travelplanner.backend.dto.UserRequest;
import com.travelplanner.backend.dto.UserResponse;
import com.travelplanner.backend.entity.User;
import com.travelplanner.backend.mapper.UserMapper;
import com.travelplanner.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public UserResponse create(UserRequest request) {
    validateEmailUnique(request.email());

    User newUser = userMapper.toEntity(request);
    newUser.setPassword(passwordEncoder.encode(request.password()));
    User savedUser = userRepository.save(newUser);

    return userMapper.toResponse(savedUser);
  }

  public List<UserResponse> findAll() {
    return userRepository.findAll()
        .stream()
        .map(userMapper::toResponse)
        .toList();
  }

  public UserResponse findById(Long id) {
    User user = findByIdOrThrow(id);
    return userMapper.toResponse(user);
  }

  public UserResponse update(Long id, UserRequest request) {
    User user = findByIdOrThrow(id);

    if (!user.getEmail().equals(request.email())) {
      validateEmailUnique(request.email());
    }

    user.setName(request.name());
    user.setEmail(request.email());
    user.setPassword(passwordEncoder.encode(request.password()));

    User savedUser = userRepository.save(user);
    return userMapper.toResponse(savedUser);
  }

  public void delete(Long id) {
    User user = findByIdOrThrow(id);
    userRepository.delete(user);
  }

  private User findByIdOrThrow(Long id) {
    return userRepository.findById(id)
        .orElseThrow(
//            () -> new UserNotFoundException(id)
        )
        ;
  }

  private void validateEmailUnique(String email) {
    if (userRepository.existsByEmail(email)) {
//      throw new EmailAlreadyExistsException(email);
    }
  }

}
