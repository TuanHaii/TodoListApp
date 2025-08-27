// rest api cho controller
package com.example.todolist.controller;

import com.example.todolist.entity.TodoItem;
import com.example.todolist.model.TodoItemDTO;
import com.example.todolist.repository.TodoItemRepository;
import com.example.todolist.service.TodoItemService;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*") // Cho phép CORS để test từ Postman
public class TodoController {
    private final TodoItemService todoItemService;
    private final TodoItemRepository todoItemRepository;

    public TodoController(TodoItemService todoItemService,
                          TodoItemRepository todoItemRepository) {
        this.todoItemService = todoItemService;
        this.todoItemRepository = todoItemRepository;
    }

    // Lấy todos của user hiện tại - Tạm thời bỏ @PreAuthorize để test
    @GetMapping
    // @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<TodoItemDTO>> getTodos(@RequestParam(required = false) String username){
        if (username != null) {
            // Nếu có username thì lấy theo username đó
            List<TodoItemDTO> result = todoItemService.findByUsername(username);
            return ResponseEntity.ok(result);
        } else {
            // Nếu không có username thì lấy tất cả
            List<TodoItemDTO> result = todoItemService.getAllTodoItems();
            return ResponseEntity.ok(result);
        }
    }

    // GET /api/todos/all - Lấy tất cả todo items - Tạm thời bỏ @PreAuthorize để test
    @GetMapping("/all")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TodoItemDTO>> getAllTodos() {
        List<TodoItemDTO> todos = todoItemService.getAllTodoItems();
        return ResponseEntity.ok(todos);
    }

    // GET /api/todos/{id} - Lấy todo theo ID
    @GetMapping("/{id}")
    public ResponseEntity<TodoItemDTO> getTodoById(@PathVariable Long id) {
        TodoItemDTO todo = todoItemService.getTodoItemById(id);
        if (todo != null) {
            return ResponseEntity.ok(todo);
        }
        return ResponseEntity.notFound().build();
    }

    // POST /api/todos - Tạo todo mới
    @PostMapping
    public ResponseEntity<TodoItemDTO> createTodo(@RequestBody TodoItemDTO todoItemDTO,
                                                 @RequestParam String username) {
        try {
            TodoItemDTO createdTodo = todoItemService.createTodoItem(todoItemDTO, username);
            return ResponseEntity.ok(createdTodo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // POST /api/todos/simple - Tạo todo đơn giản (chỉ cần title)
    @PostMapping("/simple")
    public ResponseEntity<TodoItemDTO> createSimpleTodo(@RequestParam String title,
                                                       @RequestParam String username) {
        try {
            TodoItemDTO createdTodo = todoItemService.createSimpleTodoItem(title, username);
            return ResponseEntity.ok(createdTodo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/todos/health - Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Todo API is running!");
    }
}
