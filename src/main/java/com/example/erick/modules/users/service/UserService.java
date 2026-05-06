package com.example.erick.modules.users.service;

import com.example.erick.integration.portcontrol.PortControlClient;
import com.example.erick.modules.users.dto.request.UserCreateDTO;
import com.example.erick.modules.users.dto.request.UserUpdateDTO;
import com.example.erick.modules.users.dto.response.UserDTO;
import com.example.erick.modules.users.model.User;
import com.example.erick.modules.users.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PortControlClient portControlClient;

  public Flux<UserDTO> getAllUsers() {
    return this.portControlClient.createQuote()
        .flatMap(apiResponse -> {
          System.out.println("Response: " + apiResponse);
          return userRepository.findAll()
              .map(this::mapToDTO);
        });
  }

  public Mono<UserDTO> getUserById(@NonNull Long id) {
    return userRepository.findById(id)
        .map(this::mapToDTO);
  }

  public Mono<UserDTO> createUser(UserCreateDTO userCreateDTO) {
    User user = User.builder()
        .username(userCreateDTO.getUsername())
        .email(userCreateDTO.getEmail())
        .fullName(userCreateDTO.getFullName())
        .build();
    return userRepository.save(Objects.requireNonNull(user))
        .map(this::mapToDTO);
  }

  public Mono<UserDTO> updateUser(@NonNull Long id, UserUpdateDTO userDTO) {
    return userRepository.findById(id)
        .flatMap(existingUser -> {
          existingUser.setEmail(userDTO.getEmail());
          existingUser.setFullName(userDTO.getFullName());
          return userRepository.save(existingUser);
        })
        .map(this::mapToDTO);
  }

  public Mono<Void> deleteUser(@NonNull Long id) {
    return userRepository.deleteById(id);
  }

  private UserDTO mapToDTO(User user) {
    return UserDTO.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .fullName(user.getFullName())
        .build();
  }
}
