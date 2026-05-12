package com.example.erick.modules.orders.service;

import com.example.erick.modules.orders.dto.request.OrderCreateDTO;
import com.example.erick.modules.orders.dto.request.OrderUpdateDTO;
import com.example.erick.modules.orders.dto.response.OrderDTO;
import com.example.erick.modules.orders.model.Order;
import com.example.erick.modules.orders.repository.OrderRepository;
import com.example.erick.modules.users.model.User;
import com.example.erick.modules.users.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public OrderDTO getOrderById(@NonNull Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return mapToDTO(order);
    }

    public List<Order> getOrderByUserId(@NonNull Long id) {
        List<Order> orders = orderRepository.findByUserId(id);
        return orders;
    }

    public OrderDTO createOrder(OrderCreateDTO orderCreateDTO) {
        User user = userRepository.findById(orderCreateDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderCreateDTO.getUserId()));

        Order order = Order.builder()
                .user(user)
                .totalAmount(orderCreateDTO.getTotalAmount())
                .status("PENDING")
                .description(orderCreateDTO.getDescription())
                .build();
        Order savedOrder = orderRepository.save(order);
        return mapToDTO(savedOrder);
    }

    public OrderDTO updateOrder(@NonNull Long id, OrderUpdateDTO orderUpdateDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (orderUpdateDTO.getTotalAmount() != null) {
            existingOrder.setTotalAmount(orderUpdateDTO.getTotalAmount());
        }
        if (orderUpdateDTO.getStatus() != null) {
            existingOrder.setStatus(orderUpdateDTO.getStatus());
        }
        if (orderUpdateDTO.getDescription() != null) {
            existingOrder.setDescription(orderUpdateDTO.getDescription());
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        return mapToDTO(updatedOrder);
    }

    public void deleteOrder(@NonNull Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    private OrderDTO mapToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .description(order.getDescription())
                .build();
    }
}
