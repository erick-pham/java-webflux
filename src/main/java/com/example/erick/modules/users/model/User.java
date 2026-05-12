package com.example.erick.modules.users.model;

import com.example.erick.modules.orders.model.Order;
import com.example.erick.modules.payments.model.Payment;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity // Đổi từ @Table (R2DBC) thành @Entity (JPA)
@NoArgsConstructor // Bắt buộc phải có cái này cho JPA
@AllArgsConstructor
@Table(name = "users") // Chỉ định tên bảng trong Database
public class User {

    @Id // Vẫn dùng @Id nhưng phải import từ jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng ID (Auto-increment)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    private String fullName;

    // Bạn có thể thêm trường này để lưu vết RequestId nếu cần
    private String lastRequestId;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();
}
