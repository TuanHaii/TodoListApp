//Entity Todo ánh xạ với bảng todo trong cơ sở dữ liệu
package com.example.todolist.model;
import lombok.*;
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TodoItemDTO {
    // DTO này sẽ ẩn chi tiết entity chỉ trả về thông tin cần thiết cho client
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private String username;
    private String category;
}