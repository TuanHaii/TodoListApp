package com.example.todolist.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID định danh duy nhất cho mỗi User

    @Column(unique = true, nullable = false)
    private String username; // Tên đăng nhập (phải unique)

    @Column(nullable = false)
    private String password; // Mật khẩu

    @Column(unique = true, nullable = false)
    private String email; // Email (phải unique)

    private String fullName; // Họ tên đầy đủ

    @CreationTimestamp // Tự động set thời gian tạo khi insert record mới
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // Thời gian tạo user

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TodoItem> todoItems; // Danh sách todo items thuộc user này
}
