package com.englishcenter.repository;

import com.englishcenter.entity.User;
import com.englishcenter.enums.Role;
import com.englishcenter.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    long countByRoleAndStatus(Role role, UserStatus status);

    List<User> findByRoleAndStatus(Role role, UserStatus status);

    @Query("SELECT u FROM User u WHERE " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:status IS NULL OR u.status = :status) AND " +
            "(:search IS NULL OR u.fullName LIKE %:search% OR u.username LIKE %:search% " +
            "OR u.email LIKE %:search% OR u.phone LIKE %:search%)")
    Page<User> findWithFilters(
            @Param("role") Role role,
            @Param("status") UserStatus status,
            @Param("search") String search,
            Pageable pageable);
}
