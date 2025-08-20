// Logic
package com.example.todolist.service;
import com.example.todolist.entity.TodoItem;
import com.example.todolist.entity.User;
import com.example.todolist.entity.Category;
import com.example.todolist.model.TodoItemDTO;
import com.example.todolist.repository.TodoItemRepository;
import com.example.todolist.repository.TodoUserRepository;
import com.example.todolist.repository.TodoCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoItemService {
    private final TodoItemRepository todoItemRepository;
    private final TodoUserRepository todoUserRepository;
    private final TodoCategoryRepository todoCategoryRepository;

    public TodoItemService(TodoItemRepository todoItemRepository,
                          TodoUserRepository todoUserRepository,
                          TodoCategoryRepository todoCategoryRepository) {
        this.todoItemRepository = todoItemRepository;
        this.todoUserRepository = todoUserRepository;
        this.todoCategoryRepository = todoCategoryRepository;
    }

    public List<TodoItemDTO> getAllTodoItems() {
        return todoItemRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    public TodoItemDTO getTodoItemById(Long id) {
        return todoItemRepository.findById(id)
                .map(this::mapToDto)
                .orElse(null); // Hoặc throw exception nếu không tìm thấy
    }
    // Method chuyển đổi từ TodoItem entity sang TodoItemDTO
    private TodoItemDTO mapToDto(TodoItem todoItem) {
        TodoItemDTO dto = new TodoItemDTO();
        dto.setId(todoItem.getId());
        dto.setTitle(todoItem.getTitle());
        dto.setDescription(todoItem.getDescription());
        dto.setCompleted(todoItem.isCompleted());

        // Kiểm tra null để tránh NullPointerException
        if (todoItem.getUser() != null) {
            dto.setUsername(todoItem.getUser().getUsername());
        }

        if (todoItem.getCategory() != null) {
            dto.setCategory(todoItem.getCategory().getName());
        }

        return dto;
    }

    // Tạo mới một todo item
    public TodoItemDTO createTodoItem(TodoItemDTO todoItemDTO, String username) {
        // Tìm user theo username
        User user = todoUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // Tạo TodoItem entity mới
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(todoItemDTO.getTitle());
        todoItem.setDescription(todoItemDTO.getDescription());
        todoItem.setCompleted(false); // Mặc định là chưa hoàn thành
        todoItem.setUser(user);

        // Nếu có category được chỉ định
        if (todoItemDTO.getCategory() != null && !todoItemDTO.getCategory().isEmpty()) {
            Category category = todoCategoryRepository.findByNameIgnoreCase(todoItemDTO.getCategory())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + todoItemDTO.getCategory()));
            todoItem.setCategory(category);
        }

        // Lưu vào database
        TodoItem savedTodoItem = todoItemRepository.save(todoItem);

        // Chuyển đổi thành DTO và trả về
        return mapToDto(savedTodoItem);
    }

    // Tạo todo item đơn giản (chỉ cần title)
    public TodoItemDTO createSimpleTodoItem(String title, String username) {
        TodoItemDTO dto = new TodoItemDTO();
        dto.setTitle(title);
        dto.setDescription("");
        dto.setCompleted(false);
        return createTodoItem(dto, username);
    }
}