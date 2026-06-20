package com.englishcenter.backend.core.repository;

import com.englishcenter.backend.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Chỉ giữ lại tìm theo Email (Vì email thay cho username)
    Optional<User> findByEmail(String email);
    
    // Tìm danh sách theo chức vụ
    List<User> findByRole(String role); 
}