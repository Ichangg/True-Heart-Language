package com.englishcenter.backend.modules.admin.service;

import com.englishcenter.backend.core.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    List<User> getUsersByRole(String role);
    User createUser(User user);
    void deleteUser(Long id);

    User createTeacher(com.englishcenter.backend.modules.admin.dto.UserCreationDTO dto);
    User createParent(com.englishcenter.backend.modules.admin.dto.UserCreationDTO dto);
    User createStudent(com.englishcenter.backend.modules.admin.dto.UserCreationDTO dto);
    
    com.englishcenter.backend.modules.admin.dto.TeacherProfileDTO getTeacherProfile(Long teacherId);
    List<com.englishcenter.backend.modules.admin.dto.ParentProfileDTO> getParentsByClass(Long classId);
}
