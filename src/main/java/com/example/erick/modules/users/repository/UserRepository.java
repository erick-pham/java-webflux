package com.example.erick.modules.users.repository;

// Thay đổi gói import từ reactive sang jpa
import com.example.erick.modules.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
