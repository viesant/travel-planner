package com.travelplanner.backend.controller;

import com.travelplanner.backend.dto.UserRequest;
import com.travelplanner.backend.dto.UserResponse;
import com.travelplanner.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
    UserResponse response = userService.create(request);

    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/me")
        .buildAndExpand(response.id())
        .toUri();
    return ResponseEntity.created(uri).body(response);
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponse> findMe() {
    UserResponse response = userService.findMe();
    return ResponseEntity.ok(response);
  }

  @PutMapping("/me")
  public ResponseEntity<UserResponse> updateMe(@Valid @RequestBody UserRequest request) {
    UserResponse response = userService.updateMe(request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/me")
  public ResponseEntity<Void> deleteMe() {
    userService.deleteMe();
    return ResponseEntity.noContent().build();
  }

//  @GetMapping
//  public ResponseEntity<List<UserResponse>> findAll() {
//    List<UserResponse> response = userService.findAll();
//    return ResponseEntity.ok(response);
//  }
//
//  @GetMapping("/{id}")
//  public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
//    UserResponse response = userService.findById(id);
//    return ResponseEntity.ok(response);
//  }
//
//  @PutMapping("/{id}")
//  public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
//    UserResponse response = userService.update(id, request);
//    return ResponseEntity.ok(response);
//  }
//
//  @DeleteMapping("/{id}")
//  public ResponseEntity<Void> delete(@PathVariable Long id) {
//    userService.delete(id);
//    return ResponseEntity.noContent().build();
//  }
}
