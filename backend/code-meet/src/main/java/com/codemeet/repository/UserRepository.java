package com.codemeet.repository;


import java.util.Optional;

import com.codemeet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
