package com.example.todolist.repository;

// JPA repository - Interface để thao tác với User entity trong database
import com.example.todolist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import cho @Query annotation
import org.springframework.data.repository.query.Param; // Import cho @Param annotation
import java.util.Optional; // Import cho Optional type

public interface TodoUserRepository extends JpaRepository<User, Long>{
    // Tìm user theo username - Spring Data JPA tự động implement
    Optional<User> findByUsername(String username);

    // Tìm user theo email - Spring Data JPA tự động implement
    Optional<User> findByEmail(String email);

    // Kiểm tra user có tồn tại theo username không
    boolean existsByUsername(String username);

    // Kiểm tra user có tồn tại theo email không
    boolean existsByEmail(String email);

    // Custom query: Tìm theo username HOẶC email
    @Query("SELECT u FROM User u WHERE u.username = :login OR u.email = :login")
    Optional<User> findByUsernameOrEmail(@Param("login") String login);
}
