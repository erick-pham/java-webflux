package com.example.erick.modules.users.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.example.erick.modules.users.model.User;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
