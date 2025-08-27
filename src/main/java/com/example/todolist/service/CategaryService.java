package com.example.todolist.service;

import com.example.todolist.entity.Category;
import com.example.todolist.model.CategoryDTO;
import com.example.todolist.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategaryService {
    private final CategoryRepository todoCategoryRepository;

    public CategaryService(CategoryRepository todoCategoryRepository) {
        this.todoCategoryRepository = todoCategoryRepository;
    }

    // Lấy tất cả categories
    public List<CategoryDTO> getAllCategories() {
        return todoCategoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Lấy category theo ID
    public CategoryDTO getCategoryById(Long id) {
        return todoCategoryRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    // Tìm category theo tên
    public CategoryDTO getCategoryByName(String name) {
        return todoCategoryRepository.findByNameIgnoreCase(name)
                .map(this::mapToDTO)
                .orElse(null);
    }

    // Tạo mới category
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // Kiểm tra xem category đã tồn tại chưa
        if (todoCategoryRepository.existsByNameIgnoreCase(categoryDTO.getName())) {
            throw new RuntimeException("Category với tên '" + categoryDTO.getName() + "' đã tồn tại");
        }

        Category category = mapToEntity(categoryDTO);
        Category savedCategory = todoCategoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    // Cập nhật category
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
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
    public List<CategoryDTO> getCategoriesByUserId(Long userId) {
        return todoCategoryRepository.findCategoriesByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Tìm kiếm categories theo keyword
    public List<CategoryDTO> searchCategories(String keyword) {
        return todoCategoryRepository.findByKeyword(keyword).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Lấy categories trống (không có todo items)
    public List<CategoryDTO> getEmptyCategories() {
        return todoCategoryRepository.findEmptyCategories().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Kiểm tra category có tồn tại không
    public boolean categoryExists(String name) {
        return todoCategoryRepository.existsByNameIgnoreCase(name);
    }

    // Method chuyển đổi từ Category entity sang TodoCategoryDTO
    private CategoryDTO mapToDTO(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setColor(category.getColor());
        dto.setCreatedAt(category.getCreatedAt());

        return dto;
    }

    // Method chuyển đổi từ TodoCategoryDTO sang Category entity
    private Category mapToEntity(CategoryDTO dto) {
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
