package com.englishcenter.service;

import com.englishcenter.entity.ParentStudent;
import com.englishcenter.entity.User;
import com.englishcenter.enums.Role;
import com.englishcenter.enums.UserStatus;
import com.englishcenter.exception.DuplicateResourceException;
import com.englishcenter.exception.ResourceNotFoundException;
import com.englishcenter.repository.ParentStudentRepository;
import com.englishcenter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ParentStudentRepository parentStudentRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> getAll(Role role, UserStatus status, String search, int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return userRepository.findWithFilters(role, status, search, pageRequest);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại."));
    }

    public User create(Map<String, Object> data) {
        String username = (String) data.get("username");
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Tên đăng nhập đã tồn tại.");
        }

        String password = data.containsKey("password") ? (String) data.get("password") : "123456";
        Role role = Role.student;
        if (data.containsKey("role")) {
            try { role = Role.valueOf((String) data.get("role")); } catch (Exception ignored) {}
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .fullName((String) data.get("full_name"))
                .email((String) data.get("email"))
                .phone((String) data.get("phone"))
                .role(role)
                .address((String) data.get("address"))
                .status(UserStatus.active)
                .build();

        if (data.containsKey("date_of_birth") && data.get("date_of_birth") != null) {
            user.setDateOfBirth(java.time.LocalDate.parse((String) data.get("date_of_birth")));
        }

        return userRepository.save(user);
    }

    public User update(Long id, Map<String, Object> data) {
        User user = getById(id);

        if (data.containsKey("full_name")) user.setFullName((String) data.get("full_name"));
        if (data.containsKey("email")) user.setEmail((String) data.get("email"));
        if (data.containsKey("phone")) user.setPhone((String) data.get("phone"));
        if (data.containsKey("address")) user.setAddress((String) data.get("address"));
        if (data.containsKey("avatar")) user.setAvatar((String) data.get("avatar"));
        if (data.containsKey("zalo_id")) user.setZaloId((String) data.get("zalo_id"));
        if (data.containsKey("facebook_id")) user.setFacebookId((String) data.get("facebook_id"));
        if (data.containsKey("date_of_birth") && data.get("date_of_birth") != null) {
            user.setDateOfBirth(java.time.LocalDate.parse((String) data.get("date_of_birth")));
        }
        if (data.containsKey("password") && data.get("password") != null) {
            user.setPassword(passwordEncoder.encode((String) data.get("password")));
        }

        return userRepository.save(user);
    }

    public User toggleStatus(Long id) {
        User user = getById(id);
        user.setStatus(user.getStatus() == UserStatus.active ? UserStatus.inactive : UserStatus.active);
        return userRepository.save(user);
    }

    public ParentStudent linkParentStudent(Long parentId, Long studentId, String relationship) {
        User parent = userRepository.findById(parentId)
                .filter(u -> u.getRole() == Role.parent)
                .orElseThrow(() -> new ResourceNotFoundException("Phụ huynh không tồn tại."));

        User student = userRepository.findById(studentId)
                .filter(u -> u.getRole() == Role.student)
                .orElseThrow(() -> new ResourceNotFoundException("Học sinh không tồn tại."));

        if (parentStudentRepository.findByParentIdAndStudentId(parentId, studentId).isPresent()) {
            throw new DuplicateResourceException("Liên kết đã tồn tại.");
        }

        ParentStudent link = ParentStudent.builder()
                .parentId(parentId)
                .studentId(studentId)
                .relationship(relationship)
                .build();

        return parentStudentRepository.save(link);
    }

    public List<ParentStudent> getParentsByStudent(Long studentId) {
        return parentStudentRepository.findByStudentId(studentId);
    }

    public List<ParentStudent> getChildrenByParent(Long parentId) {
        return parentStudentRepository.findByParentId(parentId);
    }
}
