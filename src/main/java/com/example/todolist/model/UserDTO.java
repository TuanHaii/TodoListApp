package com.example.todolist.model;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
    private String fullName;
    private String role; // Thêm field role để phù hợp với Entity User
}
