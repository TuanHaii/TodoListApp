package com.example.todolist.model;
import lombok.*;

import java.time.LocalDateTime;

@Setter    @Getter
@NoArgsConstructor @AllArgsConstructor
public class TodoCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt; // Thời gian tạo category
    private String color; // Hex color code for the category
}
