package com.example.todolist.service;

import com.example.todolist.entity.User;
import com.example.todolist.model.UserDTO;
import com.example.todolist.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Tim kiem user theo username
    public UserDTO findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    // Liet ke tat ca user
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Add user moi
    public String addUser(UserDTO userDTO) {
        // Tạo User entity từ DTO
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : "USER"); // Đặt role mặc định

        userRepository.save(user); // Lưu User entity, không phải DTO
        return "User registered successfully";
    }

    // Xác thực user (kiểm tra đăng nhập)
    public boolean validateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword())) // Sử dụng passwordEncoder.matches
                .orElse(false);
    }

    // Method chuyển đổi từ User entity sang UserDTO
    private UserDTO mapToDTO(User user) {
        // Kiểm tra null để tránh NullPointerException
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
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

        // Thêm field role vào DTO
        if (user.getRole() != null) {
            dto.setRole(user.getRole());
        }

        return dto;
    }
}
