package com.example.erick.modules.users.service;

import com.example.erick.modules.orders.model.Order;
import com.example.erick.modules.orders.service.OrderService;
import com.example.erick.modules.users.dto.request.UserCreateDTO;
import com.example.erick.modules.users.dto.request.UserUpdateDTO;
import com.example.erick.modules.users.dto.response.UserDTO;
import com.example.erick.modules.users.dto.response.UserDashboardDTO;
import com.example.erick.modules.users.exception.UserNotFoundException;
import com.example.erick.modules.users.model.User;
import com.example.erick.modules.users.repository.UserRepository;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    // private final PortControlClient portControlClient;
    private final OrderService orderService;

    private User getRequiredUser(@NonNull Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        String traceId = MDC.get("traceId");
        log.info("Bắt đầu xử lý với ID: {}", traceId);

        // this.portControlClient.createQuote();
        return users.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public UserDTO getUserById(@NonNull Long id) {
        User existingUser = this.getRequiredUser(id);
        return this.mapToDTO(existingUser);
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        User user = User.builder()
                .username(userCreateDTO.getUsername())
                .email(userCreateDTO.getEmail())
                .fullName(userCreateDTO.getFullName())
                .build();
        User savedUser = userRepository.save(user);

        return this.mapToDTO(savedUser);
    }

    public UserDTO updateUser(@NonNull Long id, UserUpdateDTO userDTO) {

        User existingUser = this.getRequiredUser(id);

        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFullName(userDTO.getFullName());

        User updatedUser = userRepository.save(existingUser);

        return this.mapToDTO(updatedUser);
    }

    public void deleteUser(@NonNull Long id) {
        this.getRequiredUser(id);
        userRepository.deleteById(id);
    }

    public UserDashboardDTO getUserDashboard(@NonNull Long userId) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            Future<User> userFuture = executor.submit(() -> this.getRequiredUser(userId));

            Future<List<Order>> ordersFuture = executor.submit(() -> orderService.getOrderByUserId(userId));

            User user = userFuture.get();
            List<Order> orders = ordersFuture.get();

            return UserDashboardDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .orders(orders.stream()
                            .map(order -> UserDashboardDTO.OrderDashboardDTO.builder()
                                    .id(order.getId())
                                    .totalAmount(order.getTotalAmount())
                                    .status(order.getStatus())
                                    .description(order.getDescription())
                                    .build())
                            .toList())
                    .build();
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException("Dashboard request interrupted", e);

        } catch (ExecutionException e) {

            throw new RuntimeException("Failed to load dashboard", e.getCause());
        }

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
