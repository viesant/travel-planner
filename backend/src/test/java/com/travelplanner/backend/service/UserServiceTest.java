package com.travelplanner.backend.service;

import com.travelplanner.backend.dto.UserRequest;
import com.travelplanner.backend.dto.UserResponse;
import com.travelplanner.backend.entity.User;
import com.travelplanner.backend.exception.EmailAlreadyExistsException;
import com.travelplanner.backend.mapper.UserMapper;
import com.travelplanner.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  private final Long userId = 1L;
  private User user;
  private UserRequest validRequest;
  private UserResponse expectedResponse;

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;
  @Mock
  private AuthService authService;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {

    user = new User();
    user.setId(userId);

    validRequest = new UserRequest(
        "User Test",
        "test@email.com",
        "123456"
    );
    expectedResponse = new UserResponse(
        userId,
        "User Test",
        "test@email.com"
    );
  }

  @Test
  void create_WhenDataIsValid_ShouldReturnUser() {
    when(userRepository.existsByEmail(validRequest.email())).thenReturn(false);
    when(userMapper.toEntity(validRequest)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toResponse(user)).thenReturn(expectedResponse);

    UserResponse result = userService.create(validRequest);

    assertNotNull(result);
    assertEquals(userId, result.id());
    assertEquals(validRequest.name(), result.name());
  }

  @Test
  void create_WhenEmailAlreadyExists_ShouldThrowException() {
    when(userRepository.existsByEmail(validRequest.email())).thenReturn(true);
    EmailAlreadyExistsException exception = assertThrows(
        EmailAlreadyExistsException.class,
        () -> userService.create(validRequest)
    );

    assertEquals("The email '" + validRequest.email() + "' is already registered.", exception.getMessage());

    verify(userMapper, never()).toEntity(any());
    verify(userRepository, never()).save(any());
    verify(userMapper, never()).toResponse(any());
  }

  @Test
  void updateMe_WhenDataIsValidAndEmailDoesNotChange_ShouldReturnUser() {
    user.setEmail("test@email.com");
    when(authService.getAuthenticatedUser()).thenReturn(user);
//    when(userRepository.existsByEmail(validRequest.email())).thenReturn(false);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toResponse(user)).thenReturn(expectedResponse);

    UserResponse result = userService.updateMe(validRequest);

    assertNotNull(result);
    assertEquals(userId, result.id());
    assertEquals(validRequest.name(), result.name());
    verify(userMapper, times(1)).updateEntityFromRequest(user, validRequest);
    verify(userRepository, never()).existsByEmail(anyString());
  }

  @Test
  void updateMe_WhenDataIsValidAndEmailChanges_ShouldReturnUser() {
    user.setEmail("test2@email.com");
    when(authService.getAuthenticatedUser()).thenReturn(user);
    when(userRepository.existsByEmail(validRequest.email())).thenReturn(false);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toResponse(user)).thenReturn(expectedResponse);

    UserResponse result = userService.updateMe(validRequest);

    assertNotNull(result);
    assertEquals(userId, result.id());
    assertEquals(validRequest.name(), result.name());
    verify(userMapper, times(1)).updateEntityFromRequest(user, validRequest);
  }

  @Test
  void updateMe_WhenEmailChangesAndAlreadyExists_ShouldThrowException() {
    user.setEmail("old@email.com");
    when(authService.getAuthenticatedUser()).thenReturn(user);
    when(userRepository.existsByEmail(validRequest.email())).thenReturn(true);

    EmailAlreadyExistsException exception = assertThrows(
        EmailAlreadyExistsException.class,
        () -> userService.updateMe(validRequest)
    );

    assertEquals("The email '" + validRequest.email() + "' is already registered.", exception.getMessage());

    verify(userRepository, never()).save(any());
    verify(userMapper, never()).toResponse(any());
  }

  @Test
  void deleteMe_WhenUserExists_ShouldDeleteUser() {
    when(authService.getAuthenticatedUser()).thenReturn(user);
    userService.deleteMe();
    verify(userRepository, times(1)).delete(user);
  }

}
