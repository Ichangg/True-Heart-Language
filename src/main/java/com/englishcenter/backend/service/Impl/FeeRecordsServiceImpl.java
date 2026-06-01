package com.englishcenter.backend.service.Impl;

import com.englishcenter.backend.entity.FeeRecords;
import com.englishcenter.backend.repository.FeeRecordsRepository;
import com.englishcenter.backend.service.FeeRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeeRecordsServiceImpl implements FeeRecordsService {

    @Autowired
    private FeeRecordsRepository feeRecordsRepository;

    @Override
    public List<FeeRecords> getFeesByStudent(Long studentId) { return feeRecordsRepository.findByStudentId(studentId); }

    @Override
    public Map<String, Object> getStudentDebtSummary(Long studentId) {
        List<FeeRecords> records = feeRecordsRepository.findByStudentId(studentId);
        double totalFee = records.stream()
                .filter(r -> r.getFeeFinal() != null)
                .mapToDouble(r -> r.getFeeFinal().doubleValue())
                .sum();
        double totalPaid = records.stream()
                .filter(r -> r.getPaidAmount() != null)
                .mapToDouble(r -> r.getPaidAmount().doubleValue())
                .sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("studentId", studentId);
        summary.put("totalExpected", totalFee);
        summary.put("totalPaid", totalPaid);
        summary.put("currentDebt", totalFee - totalPaid);
        return summary;
    }
    @Override
    public FeeRecords createFeeRecord(FeeRecords feeRecord) { return feeRecordsRepository.save(feeRecord); }
    
    @Autowired
    private com.englishcenter.backend.repository.ClassStudentsRepository classStudentsRepository;
    
    @Autowired
    private com.englishcenter.backend.repository.ClassesRepository classesRepository;
    
    @org.springframework.transaction.annotation.Transactional
    public void generateMonthlyFees(Long classId, java.time.LocalDate month) {
        com.englishcenter.backend.entity.Classes classInfo = classesRepository.findById(classId).orElse(null);
        if (classInfo == null) return;
        
        List<com.englishcenter.backend.entity.ClassStudents> students = classStudentsRepository.findByClassId(classId);
        for (com.englishcenter.backend.entity.ClassStudents cs : students) {
            FeeRecords record = new FeeRecords();
            record.setStudentId(cs.getStudentId());
            record.setClassId(classId);
            record.setMonth(month);
            record.setFeeBase(classInfo.getFeePerSession().multiply(new java.math.BigDecimal("8"))); // Tạm tính 8 buổi/tháng
            
            java.math.BigDecimal discountAmount = record.getFeeBase()
                .multiply(cs.getDiscountPercent())
                .divide(new java.math.BigDecimal("100"));
            
            record.setDiscountAmount(discountAmount);
            record.setFeeFinal(record.getFeeBase().subtract(discountAmount));
            record.setPaidAmount(java.math.BigDecimal.ZERO);
            record.setStatus("unpaid");
            
            feeRecordsRepository.save(record);
        }
    }
}