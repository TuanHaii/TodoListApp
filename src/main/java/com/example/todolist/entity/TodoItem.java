// ánh xạ DB với table todo_items
package com.example.todolist.entity; // Khai báo package chứa các entity (thực thể) của ứng dụng todolist

import jakarta.persistence.*; // Import các annotation của JPA để làm việc với database
import lombok.*; // Import tất cả annotation của Lombok để tự động tạo getter, setter, constructor

@Entity // Annotation đánh dấu class này là một entity JPA, ánh xạ với bảng trong database
@Getter // Lombok tự động tạo các phương thức getter cho tất cả thuộc tính
@Setter // Lombok tự động tạo các phương thức setter cho tất cả thuộc tính
@NoArgsConstructor // Lombok tự động tạo constructor không có tham số
@AllArgsConstructor // Lombok tự động tạo constructor với tất cả tham số
@Table(name = "todo_items") // Chỉ định tên bảng trong database là "todo_items"
public class TodoItem { // Khai báo class TodoItem đại diện cho một nhiệm vụ trong danh sách todo

    @Id // Đánh dấu thuộc tính này là khóa chính (primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động sinh giá trị ID, sử dụng auto-increment của database
    private Long id; // Thuộc tính ID kiểu Long, định danh duy nhất cho mỗi todo item

    private String title; // Tiêu đề của todo item
    private String description; // Mô tả chi tiết của todo item
    private boolean completed; // Trạng thái hoàn thành của todo item (true = đã hoàn thành, false = chưa hoàn thành)

    @ManyToOne // Quan hệ nhiều-một: nhiều todo items có thể thuộc về một user
    @JoinColumn(name = "user_id") // Chỉ định cột foreign key trong bảng todo_items để liên kết với bảng users
    private User user; // Thuộc tính user - chủ sở hữu của todo item

    @ManyToOne // Quan hệ nhiều-một: nhiều todo items có thể thuộc về một category
    @JoinColumn(name = "category_id") // Chỉ định cột foreign key trong bảng todo_items để liên kết với bảng categories
    private Category category; // Thuộc tính category - danh mục của todo item
}
