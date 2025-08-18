// JPA repository
package com.example.todolist.repository;
import com.example.todolist.entity.TodoItem;
import com.example.todolist.entity.User;
import com.example.todolist.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TodoItemRepository extends JpaRepository <TodoItem, Long> {
    // Tương tác với database cho TodoItem entity

    // Tìm tất cả todo items của một user
    List<TodoItem> findByUser(User user);

    // Tìm tất cả todo items của một user theo user ID
    List<TodoItem> findByUserId(Long userId);

    // Tìm todo items theo category
    List<TodoItem> findByCategory(Category category);

    // Tìm todo items theo category ID
    List<TodoItem> findByCategoryId(Long categoryId);

    // Tìm todo items đã hoàn thành của một user
    List<TodoItem> findByUserAndCompleted(User user, boolean completed);

    // Tìm todo items đã hoàn thành theo user ID
    List<TodoItem> findByUserIdAndCompleted(Long userId, boolean completed);

    // Tìm todo items theo title chứa keyword
    List<TodoItem> findByTitleContainingIgnoreCase(String keyword);

    // Tìm todo items của user theo title chứa keyword
    List<TodoItem> findByUserAndTitleContainingIgnoreCase(User user, String keyword);

    // Đếm số todo items của một user
    long countByUser(User user);

    // Đếm số todo items đã hoàn thành của một user
    long countByUserAndCompleted(User user, boolean completed);

    // Custom query: Tìm todo items theo user và category
    @Query("SELECT t FROM TodoItem t WHERE t.user.id = :userId AND t.category.id = :categoryId")
    List<TodoItem> findByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);

    // Custom query: Tìm todo items chưa hoàn thành và sắp xếp theo ngày tạo
    @Query("SELECT t FROM TodoItem t WHERE t.user.id = :userId AND t.completed = false ORDER BY t.id DESC")
    List<TodoItem> findIncompleteByUserOrderByCreatedDesc(@Param("userId") Long userId);

    // Custom query: Tìm todo items theo multiple keywords trong title hoặc description
    @Query("SELECT t FROM TodoItem t WHERE t.user.id = :userId AND " +
           "(LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<TodoItem> searchByKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);
}
