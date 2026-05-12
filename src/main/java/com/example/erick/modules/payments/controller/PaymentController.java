package com.example.erick.modules.payments.controller;

import com.example.erick.modules.payments.dto.request.PaymentCreateDTO;
import com.example.erick.modules.payments.dto.request.PaymentUpdateDTO;
import com.example.erick.modules.payments.dto.response.PaymentDTO;
import com.example.erick.modules.payments.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public PaymentDTO getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDTO createPayment(@Valid @RequestBody PaymentCreateDTO paymentCreateDTO) {
        return paymentService.createPayment(paymentCreateDTO);
    }

    @PutMapping("/{id}")
    public PaymentDTO updatePayment(@PathVariable Long id, @RequestBody PaymentUpdateDTO paymentUpdateDTO) {
        return paymentService.updatePayment(id, paymentUpdateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }
}
