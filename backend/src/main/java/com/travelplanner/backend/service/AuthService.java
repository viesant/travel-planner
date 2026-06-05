package com.travelplanner.backend.service;

import com.travelplanner.backend.dto.LoginRequest;
import com.travelplanner.backend.dto.LoginResponse;
import com.travelplanner.backend.entity.User;
import com.travelplanner.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final UserRepository userRepository;

  public LoginResponse login(LoginRequest request) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
            )
        );

    return tokenService.generateToken(authentication);
  }

  public User getAuthenticatedUser() {
    String strId = SecurityContextHolder.getContext()
        .getAuthentication()
        .getName();

    Long id = Long.parseLong(strId);

    return userRepository.findById(id)
        .orElseThrow(
//            () -> new ResourceNotFoundException("Logged user", id)
            () -> new UsernameNotFoundException("User no longer exists")
        );

//    String email =
//        SecurityContextHolder.getContext()
//            .getAuthentication()
//            .getName();

//    return userRepository.findByEmail(email)
//        .orElseThrow(
//            () -> new AuthEmailNotFoundException(email)
//        );
  }

}
