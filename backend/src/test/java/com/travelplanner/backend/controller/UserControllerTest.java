package com.travelplanner.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelplanner.backend.dto.UserRequest;
import com.travelplanner.backend.dto.UserResponse;
import com.travelplanner.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

  private final Long userId = 1L;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockitoBean
  private UserService userService;
  private UserRequest validRequest;
  private UserResponse expectedResponse;

  @BeforeEach
  void setUp() {
    validRequest = new UserRequest("User Test", "test@email.com", "123456");
    expectedResponse = new UserResponse(userId, "User Test", "test@email.com");
  }

  @Test
  void create_WhenRequestIsValid_ShouldReturn201() throws Exception {

    when(userService.create(any(UserRequest.class))).thenReturn(expectedResponse);

    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRequest)))

        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("User Test"))
        .andExpect(jsonPath("$.email").value("test@email.com"));
  }

  @Test
  void create_WhenRequestIsInvalid_ShouldReturn400() throws Exception {
    UserRequest invalidRequest = createInvalidRequest();

    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))

        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Validation Failure"))
        .andExpect(jsonPath("$.invalid_fields.name").exists())
        .andExpect(jsonPath("$.invalid_fields.email").exists())
        .andExpect(jsonPath("$.invalid_fields.password").exists());
  }

  @Test
  void findMe_ShouldReturnUserSuccessfully() throws Exception {

    when(userService.findMe()).thenReturn(expectedResponse);

    mockMvc.perform(get("/users/me")
            .contentType(MediaType.APPLICATION_JSON))

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("User Test"))
        .andExpect(jsonPath("$.email").value("test@email.com"));

    verify(userService).findMe();
  }

  @Test
  void updateMe_WhenRequestIsValid_ShouldReturn200() throws Exception {

    when(userService.updateMe(any(UserRequest.class))).thenReturn(expectedResponse);

    mockMvc.perform(put("/users/me")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("User Test"));
  }

  @Test
  void updateMe_WhenRequestIsInvalid_ShouldReturn400() throws Exception {
    UserRequest invalidRequest = createInvalidRequest();

    mockMvc.perform(put("/users/me")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Validation Failure"))
        .andExpect(jsonPath("$.invalid_fields.name").exists())
        .andExpect(jsonPath("$.invalid_fields.password").exists());
  }

  @Test
  void deleteMe_ShouldReturn204() throws Exception {

    mockMvc.perform(delete("/users/me")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  private UserRequest createInvalidRequest() {
    return new UserRequest("", "invalid email", "");
  }

}
