package com.englishcenter.backend.core.repository;

import com.englishcenter.backend.core.entity.ClassStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ClassStudentsRepository extends JpaRepository<ClassStudents, Long> {
    //Tìm tất cả học sinh thuộc một lớp cụ thể
    List<ClassStudents> findByClassId(Long classId);

    //Tìm tất cả lớp mà một học sinh cụ thể đang tham gia
    List<ClassStudents> findByStudentId(Long studentId);
}
