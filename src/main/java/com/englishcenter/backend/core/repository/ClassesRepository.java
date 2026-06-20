package com.englishcenter.backend.core.repository;

import com.englishcenter.backend.core.entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, Long> {
   
   //Tìm tất cả lớp học theo trạng thái
    List<Classes> findByStatus(String status);

    //Tìm tất cả lớp học theo nhóm lớp
    List<Classes> findByClassGroupId(Long classGroupId);

    //Tìm tất cả lớp học theo năm học
    List<Classes> findByYear(Integer year);
}
