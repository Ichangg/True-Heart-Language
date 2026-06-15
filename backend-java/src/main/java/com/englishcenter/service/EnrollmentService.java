package com.englishcenter.service;

import com.englishcenter.entity.*;
import com.englishcenter.enums.EnrollmentStatus;
import com.englishcenter.enums.Role;
import com.englishcenter.exception.BusinessException;
import com.englishcenter.exception.DuplicateResourceException;
import com.englishcenter.exception.ResourceNotFoundException;
import com.englishcenter.repository.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final ClassRepository classRepository;
    private final UserRepository userRepository;

    public Enrollment enrollStudent(@NonNull Long studentId, @NonNull Long classId, BigDecimal discountPercent, String discountReason, BigDecimal customFee, String note) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Lớp không tồn tại."));

        if (classEntity.getStatus() == com.englishcenter.enums.ClassStatus.closed) {
            throw new BusinessException("Lớp đã đóng, không thể đăng ký.");
        }

        User student = userRepository.findById(studentId)
                .filter(u -> u.getRole() == Role.student)
                .orElseThrow(() -> new ResourceNotFoundException("Học sinh không tồn tại."));

        if (enrollmentRepository.findByStudentIdAndClassId(studentId, classId).isPresent()) {
            throw new DuplicateResourceException("Học sinh đã đăng ký lớp này.");
        }

        long currentCount = enrollmentRepository.countByClassIdAndStatus(classId, EnrollmentStatus.active);
        if (currentCount >= classEntity.getMaxStudents()) {
            throw new BusinessException("Lớp đã đầy sĩ số.");
        }

        Enrollment enrollment = Enrollment.builder()
                .studentId(studentId)
                .classId(classId)
                .discountPercent(discountPercent != null ? discountPercent : BigDecimal.ZERO)
                .discountReason(discountReason)
                .customFee(customFee)
                .note(note)
                .status(EnrollmentStatus.active)
                .build();

        return enrollmentRepository.save(enrollment);
    }

    public Enrollment updateDiscount(@NonNull Long enrollmentId, BigDecimal discountPercent, String discountReason, BigDecimal customFee) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Đăng ký không tồn tại."));

        if (discountPercent != null) enrollment.setDiscountPercent(discountPercent);
        if (discountReason != null) enrollment.setDiscountReason(discountReason);
        if (customFee != null) enrollment.setCustomFee(customFee);

        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getEnrollmentsByClass(@NonNull Long classId) {
        return enrollmentRepository.findByClassId(classId);
    }

    public List<Enrollment> getEnrollmentsByStudent(@NonNull Long studentId) {
        return enrollmentRepository.findByStudentIdAndStatus(studentId, EnrollmentStatus.active);
    }

    public Enrollment withdrawStudent(@NonNull Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Đăng ký không tồn tại."));

        enrollment.setStatus(EnrollmentStatus.withdrawn);
        return enrollmentRepository.save(enrollment);
    }

    public BigDecimal calculateActualFee(Enrollment enrollment, ClassEntity classEntity) {
        BigDecimal baseFee;
        if (enrollment.getCustomFee() != null) {
            baseFee = enrollment.getCustomFee();
        } else {
            baseFee = classEntity.getFeePerSession().multiply(BigDecimal.valueOf(classEntity.getSessionsPerMonth()));
        }
        BigDecimal discount = baseFee.multiply(enrollment.getDiscountPercent()).divide(BigDecimal.valueOf(100));
        return baseFee.subtract(discount);
    }
}
