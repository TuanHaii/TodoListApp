package com.example.todolist.model;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TodoUserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
    private String fullName;
}
