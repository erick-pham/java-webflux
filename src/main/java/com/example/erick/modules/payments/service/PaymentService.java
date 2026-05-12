package com.example.erick.modules.payments.service;

import com.example.erick.modules.payments.dto.request.PaymentCreateDTO;
import com.example.erick.modules.payments.dto.request.PaymentUpdateDTO;
import com.example.erick.modules.payments.dto.response.PaymentDTO;
import com.example.erick.modules.orders.model.Order;
import com.example.erick.modules.orders.repository.OrderRepository;
import com.example.erick.modules.payments.model.Payment;
import com.example.erick.modules.payments.repository.PaymentRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public PaymentDTO getPaymentById(@NonNull Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return mapToDTO(payment);
    }

    public PaymentDTO createPayment(PaymentCreateDTO paymentCreateDTO) {
        Order order = orderRepository.findById(paymentCreateDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + paymentCreateDTO.getOrderId()));

        Payment payment = Payment.builder()
                .order(order)
                .user(order.getUser()) // Set user from order
                .amount(paymentCreateDTO.getAmount())
                .paymentMethod(paymentCreateDTO.getPaymentMethod())
                .status("PENDING")
                .build();
        Payment savedPayment = paymentRepository.save(payment);
        return mapToDTO(savedPayment);
    }

    public PaymentDTO updatePayment(@NonNull Long id, PaymentUpdateDTO paymentUpdateDTO) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        if (paymentUpdateDTO.getStatus() != null) {
            existingPayment.setStatus(paymentUpdateDTO.getStatus());
        }

        Payment updatedPayment = paymentRepository.save(existingPayment);
        return mapToDTO(updatedPayment);
    }

    public void deletePayment(@NonNull Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    private PaymentDTO mapToDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrder() != null ? payment.getOrder().getId() : null)
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .build();
    }
}
