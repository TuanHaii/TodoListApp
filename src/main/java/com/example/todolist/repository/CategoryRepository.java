package com.example.todolist.repository;

import com.example.todolist.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Annotation đánh dấu đây là repository component
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Tìm category theo tên (case insensitive)
    Optional<Category> findByNameIgnoreCase(String name);

    // Tìm tất cả categories được sử dụng bởi một user cụ thể
    // Thông qua mối quan hệ User -> TodoItem -> Category
    @Query("SELECT DISTINCT c FROM Category c " +
           "JOIN c.todoItems ti " +
           "WHERE ti.user.id = :userId")
    List<Category> findCategoriesByUserId(@Param("userId") Long userId);

    // Tìm categories có chứa từ khóa trong tên hoặc mô tả
    @Query("SELECT c FROM Category c " +
           "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Category> findByKeyword(@Param("keyword") String keyword);

    // Tìm category theo màu sắc
    List<Category> findByColor(String color);

    // Kiểm tra xem category có tồn tại với tên cụ thể không
    boolean existsByNameIgnoreCase(String name);

    // Đếm số lượng todo items trong một category
    @Query("SELECT COUNT(ti) FROM TodoItem ti WHERE ti.category.id = :categoryId")
    Long countTodoItemsByCategoryId(@Param("categoryId") Long categoryId);

    // Tìm categories không có todo items nào
    @Query("SELECT c FROM Category c " +
           "WHERE c.id NOT IN (SELECT DISTINCT ti.category.id FROM TodoItem ti WHERE ti.category IS NOT NULL)")
    List<Category> findEmptyCategories();
}
