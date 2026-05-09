package com.example.erick.modules.users.model;

import jakarta.persistence.*; // Sử dụng gói jakarta cho JPA
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity // Đổi từ @Table (R2DBC) thành @Entity (JPA)
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
}
