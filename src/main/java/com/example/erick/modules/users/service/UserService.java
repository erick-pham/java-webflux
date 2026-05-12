package com.example.erick.modules.users.service;

import com.example.erick.integration.portcontrol.PortControlClient;
import com.example.erick.modules.users.dto.request.UserCreateDTO;
import com.example.erick.modules.users.dto.request.UserUpdateDTO;
import com.example.erick.modules.users.dto.response.UserDTO;
import com.example.erick.modules.users.model.User;
import com.example.erick.modules.users.repository.UserRepository;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PortControlClient portControlClient;

    public List<UserDTO> getAllUsers() {
        // 1. findAll() bây giờ trả về List<User>`
        List<User> users = userRepository.findAll();
        String traceId = MDC.get("traceId");
        log.info("Bắt đầu xử lý với ID: {}", traceId);

        // this.portControlClient.createQuote();
        // 2. Sử dụng Java Stream để chuyển đổi từng phần tử
        return users.stream()
                .map(this::mapToDTO)
                .toList(); // Hoặc .collect(Collectors.toList()) tùy phiên bản Java
    }

    public UserDTO getUserById(@NonNull Long id) {
        User existingUser = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return this.mapToDTO(existingUser);
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        User user = User.builder()
                .username(userCreateDTO.getUsername())
                .email(userCreateDTO.getEmail())
                .fullName(userCreateDTO.getFullName())
                .build();
        User savedUser = userRepository.save(user);

        // 3. Map ngược lại sang DTO để trả về
        return this.mapToDTO(savedUser);
    }

    public UserDTO updateUser(@NonNull Long id, UserUpdateDTO userDTO) {
        // 1. Tìm user (findById trả về Optional thay vì Mono)
        User existingUser = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // 2. Cập nhật thông tin
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFullName(userDTO.getFullName());

        // 3. Lưu vào Database (Virtual Thread tự động unmount khi đợi I/O)
        User updatedUser = userRepository.save(existingUser);

        // 4. Chuyển đổi sang DTO và trả về trực tiếp
        return this.mapToDTO(updatedUser);
    }

    public void deleteUser(@NonNull Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
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
