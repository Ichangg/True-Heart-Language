package com.englishcenter.controller;

import com.englishcenter.dto.ApiResponse;
import com.englishcenter.dto.PaginatedResponse;
import com.englishcenter.entity.ParentStudent;
import com.englishcenter.entity.User;
import com.englishcenter.enums.Role;
import com.englishcenter.enums.UserStatus;
import com.englishcenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Role roleEnum = role != null ? Role.valueOf(role) : null;
        UserStatus statusEnum = status != null ? UserStatus.valueOf(status) : null;
        Page<User> result = userService.getAll(roleEnum, statusEnum, search, page, limit);
        return ResponseEntity.ok(PaginatedResponse.of(result.getContent(), result.getTotalElements(), page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> create(@RequestBody Map<String, Object> body) {
        User user = userService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(user, "Tạo người dùng thành công."));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(ApiResponse.success(userService.update(id, body), "Cập nhật thành công."));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> toggleStatus(@PathVariable Long id) {
        User user = userService.toggleStatus(id);
        String msg = user.getStatus() == UserStatus.active ? "Đã kích hoạt tài khoản." : "Đã vô hiệu hóa tài khoản.";
        return ResponseEntity.ok(ApiResponse.success(user, msg));
    }

    @PostMapping("/link-parent-student")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ParentStudent>> linkParentStudent(@RequestBody Map<String, Object> body) {
        Long parentId = Long.valueOf(body.get("parent_id").toString());
        Long studentId = Long.valueOf(body.get("student_id").toString());
        String relationship = (String) body.get("relationship");
        ParentStudent link = userService.linkParentStudent(parentId, studentId, relationship);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(link, "Liên kết phụ huynh - học sinh thành công."));
    }

    @GetMapping("/parents/{studentId}")
    public ResponseEntity<ApiResponse<List<ParentStudent>>> getParentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getParentsByStudent(studentId)));
    }

    @GetMapping("/children/{parentId}")
    public ResponseEntity<ApiResponse<List<ParentStudent>>> getChildrenByParent(@PathVariable Long parentId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getChildrenByParent(parentId)));
    }

    @GetMapping("/my-children")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<ApiResponse<List<ParentStudent>>> getMyChildren(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(userService.getChildrenByParent(user.getId())));
    }
}
