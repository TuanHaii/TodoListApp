package com.example.todolist.service;

import com.example.todolist.entity.Category;
import com.example.todolist.model.TodoCategoryDTO;
import com.example.todolist.repository.TodoCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoCategaryService {
    private final TodoCategoryRepository todoCategoryRepository;

    public TodoCategaryService(TodoCategoryRepository todoCategoryRepository) {
        this.todoCategoryRepository = todoCategoryRepository;
    }

    // Lấy tất cả categories
    public List<TodoCategoryDTO> getAllCategories() {
        return todoCategoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Lấy category theo ID
    public TodoCategoryDTO getCategoryById(Long id) {
        return todoCategoryRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    // Tìm category theo tên
    public TodoCategoryDTO getCategoryByName(String name) {
        return todoCategoryRepository.findByNameIgnoreCase(name)
                .map(this::mapToDTO)
                .orElse(null);
    }

    // Tạo mới category
    public TodoCategoryDTO createCategory(TodoCategoryDTO categoryDTO) {
        // Kiểm tra xem category đã tồn tại chưa
        if (todoCategoryRepository.existsByNameIgnoreCase(categoryDTO.getName())) {
            throw new RuntimeException("Category với tên '" + categoryDTO.getName() + "' đã tồn tại");
        }

        Category category = mapToEntity(categoryDTO);
        Category savedCategory = todoCategoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    // Cập nhật category
    public TodoCategoryDTO updateCategory(Long id, TodoCategoryDTO categoryDTO) {
        Category existingCategory = todoCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category không tồn tại với ID: " + id));

        // Kiểm tra tên trùng lặp (trừ category hiện tại)
        Optional<Category> duplicateCategory = todoCategoryRepository.findByNameIgnoreCase(categoryDTO.getName());
        if (duplicateCategory.isPresent() && !duplicateCategory.get().getId().equals(id)) {
            throw new RuntimeException("Category với tên '" + categoryDTO.getName() + "' đã tồn tại");
        }

        existingCategory.setName(categoryDTO.getName());
        existingCategory.setDescription(categoryDTO.getDescription());
        existingCategory.setColor(categoryDTO.getColor());

        Category updatedCategory = todoCategoryRepository.save(existingCategory);
        return mapToDTO(updatedCategory);
    }

    // Xóa category
    public void deleteCategory(Long id) {
        // Kiểm tra xem category có todo items không
        Long todoCount = todoCategoryRepository.countTodoItemsByCategoryId(id);
        if (todoCount > 0) {
            throw new RuntimeException("Không thể xóa category vì còn " + todoCount + " todo items");
        }

        if (!todoCategoryRepository.existsById(id)) {
            throw new RuntimeException("Category không tồn tại với ID: " + id);
        }

        todoCategoryRepository.deleteById(id);
    }

    // Lấy categories được sử dụng bởi user
    public List<TodoCategoryDTO> getCategoriesByUserId(Long userId) {
        return todoCategoryRepository.findCategoriesByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Tìm kiếm categories theo keyword
    public List<TodoCategoryDTO> searchCategories(String keyword) {
        return todoCategoryRepository.findByKeyword(keyword).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Lấy categories trống (không có todo items)
    public List<TodoCategoryDTO> getEmptyCategories() {
        return todoCategoryRepository.findEmptyCategories().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Kiểm tra category có tồn tại không
    public boolean categoryExists(String name) {
        return todoCategoryRepository.existsByNameIgnoreCase(name);
    }

    // Method chuyển đổi từ Category entity sang TodoCategoryDTO
    private TodoCategoryDTO mapToDTO(Category category) {
        if (category == null) {
            return null;
        }

        TodoCategoryDTO dto = new TodoCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setColor(category.getColor());
        dto.setCreatedAt(category.getCreatedAt());

        return dto;
    }

    // Method chuyển đổi từ TodoCategoryDTO sang Category entity
    private Category mapToEntity(TodoCategoryDTO dto) {
        if (dto == null) {
            return null;
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setColor(dto.getColor());
        // createdAt sẽ được tự động set bởi @CreationTimestamp

        return category;
    }
}
