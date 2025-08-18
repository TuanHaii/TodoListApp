package com.example.todolist.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import java.util.List;

@Entity
@Table(name = "categories") // Giữ nguyên tên "categories"
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID định danh duy nhất cho mỗi category

    @Column(nullable = false, unique = true)
    private String name; // Tên category (Work, Personal, Shopping...)

    private String description; // Mô tả về category

    private String color; // Màu sắc đại diện (hex code: #FF5733)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // Thời gian tạo category

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TodoItem> todoItems; // Danh sách todo items thuộc category này
}