package com.example.todolist.service;

import com.example.todolist.entity.User;
import com.example.todolist.model.TodoUserDTO;
import com.example.todolist.repository.TodoUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoUserService {
    private final TodoUserRepository todoUserRepository;

    public TodoUserService(TodoUserRepository todoUserRepository) {
        this.todoUserRepository = todoUserRepository;
    }

    // Tim kiem user theo username
    public TodoUserDTO findByUsername(String username) {
        return todoUserRepository.findByUsername(username)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    // Liet ke tat ca user
    public List<TodoUserDTO> getAllUsers() {
        return todoUserRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Method chuyển đổi từ User entity sang TodoUserDTO
    private TodoUserDTO mapToDTO(User user) {
        // Kiểm tra null để tránh NullPointerException
        if (user == null) {
            return null;
        }

        TodoUserDTO dto = new TodoUserDTO();
        dto.setId(user.getId());

        // Kiểm tra null cho từng thuộc tính
        if (user.getUsername() != null) {
            dto.setUsername(user.getUsername());
        }

        if (user.getEmail() != null) {
            dto.setEmail(user.getEmail());
        }

        if (user.getCreatedAt() != null) {
            dto.setCreatedAt(user.getCreatedAt());
        }

        if (user.getFullName() != null) {
            dto.setFullName(user.getFullName());
        }

        return dto;
    }
}
