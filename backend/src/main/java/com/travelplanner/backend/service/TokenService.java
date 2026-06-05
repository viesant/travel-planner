package com.travelplanner.backend.service;

import com.travelplanner.backend.dto.LoginResponse;
import com.travelplanner.backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final JwtEncoder jwtEncoder;

  public LoginResponse generateToken(
      Authentication authentication) {

    User loggedUser = (User) authentication.getPrincipal();

    Instant now = Instant.now();
    long expiresIn = 7200L;

    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("travel-planner-backend")
        .subject(loggedUser.getId().toString())
        .claim("email", loggedUser.getEmail())
        .issuedAt(now)
        .expiresAt(now.plusSeconds(expiresIn))
        .build();

    String token = jwtEncoder
        .encode(JwtEncoderParameters.from(claims))
        .getTokenValue();

    return new LoginResponse(token, expiresIn);
  }

}
