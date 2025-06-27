package com.codemeet.repository;

import java.util.List;
import java.util.Optional;

import com.codemeet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query(
        """
        SELECT u
        FROM User u
        WHERE u.username LIKE CONCAT('%', :query, '%')
        """
    )
    List<User> findByUsernameContaining(String query);
    
    @Query(
        """
        SELECT u
        FROM User u
        WHERE u.username LIKE CONCAT('%', :query, '%')
        OR LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :query, '%'))
        """
    )
    List<User> findByUsernameContainingOrFullNameContainingIgnoreCase(String query);
}
