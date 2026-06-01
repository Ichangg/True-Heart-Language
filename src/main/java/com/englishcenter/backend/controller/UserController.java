package com.englishcenter.backend.controller;

import com.englishcenter.backend.entity.User;
import com.englishcenter.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController: Khai báo class này là một API Controller. Các kết quả trả về sẽ tự động chuyển thành JSON.
@RestController 
// @RequestMapping: Thiết lập đường dẫn gốc cho toàn bộ các API trong file này.
@RequestMapping("/api/users") 
public class UserController {

    // @Autowired: Tự động nhúng (inject) UserRepository vào để tương tác với Database.
    @Autowired
    private UserService userService;

    // Lấy toàn bộ danh sách người dùng. Dùng phương thức GET.
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Lấy thông tin 1 người dùng theo ID.
    // @PathVariable: Lấy giá trị {id} từ trên URL (VD: /api/users/5 thì id = 5).
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok) // Trả về mã 200 OK kèm dữ liệu nếu tìm thấy
                .orElse(ResponseEntity.notFound().build()); // Trả về mã 404 Not Found nếu không có
    }

    // Lấy danh sách người dùng theo vai trò (TEACHER, STUDENT...).
    @GetMapping("/role/{role}")
    public List<User> getUsersByRole(@PathVariable String role) {
        return userService.getUsersByRole(role);
    }

    // Tạo mới một tài khoản. Dùng phương thức POST.
    // @RequestBody: Chuyển đổi cục dữ liệu JSON gửi từ Frontend thành Object User trong Java.
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Xóa một tài khoản theo ID. Dùng phương thức DELETE.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Trả về mã 204 báo hiệu đã xóa thành công
    }
}