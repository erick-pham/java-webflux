package com.example.erick.modules.users.service;

import com.example.erick.modules.users.dto.request.UserCreateDTO;
import com.example.erick.modules.users.dto.request.UserUpdateDTO;
import com.example.erick.modules.users.dto.response.UserDTO;
import com.example.erick.modules.users.model.User;
import com.example.erick.modules.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserCreateDTO userCreateDTO;
    private UserUpdateDTO userUpdateDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "username1", "abc@gmail.com", "John Doe", null);
        userCreateDTO = UserCreateDTO.builder().fullName("John Doe").build();
        userUpdateDTO = UserUpdateDTO.builder().fullName("John Updated").build();
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        List<User> users = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFullName()).isEqualTo("John Doe");
    }

    @Test
    void getUserById_shouldReturnUserWhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);

        assertThat(result.getEmail()).isEqualTo("abc@gmail.com");
    }

    @Test
    void getUserById_shouldThrowExceptionWhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            userService.getUserById(1L);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("User not found with id: 1");
        }
    }

    @Test
    void createUser_shouldCreateAndReturnUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.createUser(userCreateDTO);

        assertThat(result.getFullName()).isEqualTo("John Doe");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_shouldUpdateAndReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.updateUser(1L, userUpdateDTO);

        assertThat(result.getFullName()).isEqualTo("John Updated");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_shouldThrowExceptionWhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            userService.updateUser(1L, userUpdateDTO);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("User not found with id: 1");
        }
    }

    @Test
    void deleteUser_shouldDeleteUserWhenFound() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser(1L));

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_shouldThrowExceptionWhenNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        try {
            userService.deleteUser(1L);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("User not found with id: 1");
        }
    }
}
