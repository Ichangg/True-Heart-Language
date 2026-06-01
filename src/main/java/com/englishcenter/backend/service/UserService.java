package com.englishcenter.backend.service;

import com.englishcenter.backend.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    List<User> getUsersByRole(String role);
    User createUser(User user);
    void deleteUser(Long id);
}
