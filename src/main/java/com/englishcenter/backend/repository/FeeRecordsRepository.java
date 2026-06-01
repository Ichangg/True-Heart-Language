package com.englishcenter.backend.repository;

import com.englishcenter.backend.entity.FeeRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeeRecordsRepository extends JpaRepository<FeeRecords, Long> {
    // Tìm hóa đơn học phí của một học sinh
    List<FeeRecords> findByStudentId(Long studentId);
    
    // Tìm các hóa đơn theo trạng thái (PAID - Đã nộp, UNPAID - Chưa nộp)
    List<FeeRecords> findByStatus(String status);
    
    // Tìm hóa đơn chưa thanh toán của một học sinh cụ thể
    List<FeeRecords> findByStudentIdAndStatus(Long studentId, String status);

    @Query("SELECT SUM(f.feeFinal) FROM FeeRecords f WHERE MONTH(f.createdAt) = :month AND YEAR(f.createdAt) = :year")
    Double sumExpectedRevenueByMonth(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT SUM(f.paidAmount) FROM FeeRecords f WHERE MONTH(f.createdAt) = :month AND YEAR(f.createdAt) = :year")
    Double sumActualRevenueByMonth(@Param("month") Integer month, @Param("year") Integer year);
}