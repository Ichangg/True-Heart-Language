package com.englishcenter.service;

import com.englishcenter.entity.*;
import com.englishcenter.enums.*;
import com.englishcenter.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ParentStudentRepository parentStudentRepository;
    private final ClassRepository classRepository;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public Message sendMessage(Long senderId, String recipientType, List<Long> recipientIds, Long classId, MessageChannel channel, String subject, String content) {
        List<Long> finalRecipientIds = recipientIds != null ? recipientIds : new ArrayList<>();

        // Nếu gửi cho cả lớp, tìm phụ huynh
        if ("class".equals(recipientType) && classId != null) {
            List<Enrollment> enrollments = enrollmentRepository.findByClassId(classId).stream()
                    .filter(e -> e.getStatus() == EnrollmentStatus.active)
                    .collect(Collectors.toList());

            List<Long> studentIds = enrollments.stream().map(Enrollment::getStudentId).collect(Collectors.toList());

            Set<Long> parentIds = new HashSet<>();
            for (Long sid : studentIds) {
                parentStudentRepository.findByStudentId(sid).forEach(ps -> parentIds.add(ps.getParentId()));
            }
            finalRecipientIds = new ArrayList<>(parentIds);
        }

        String recipientIdsJson;
        try {
            recipientIdsJson = objectMapper.writeValueAsString(finalRecipientIds);
        } catch (JsonProcessingException e) {
            recipientIdsJson = "[]";
        }

        Message message = Message.builder()
                .senderId(senderId)
                .recipientType(recipientType)
                .recipientIds(recipientIdsJson)
                .classId(classId)
                .channel(channel != null ? channel : MessageChannel.system)
                .subject(subject)
                .content(content)
                .status(MessageStatus.pending)
                .build();

        return messageRepository.save(message);
    }

    public String generateAbsencePaymentMessage(Long parentId) {
        List<ParentStudent> parentStudents = parentStudentRepository.findByParentId(parentId);
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        StringBuilder sb = new StringBuilder("Kính gửi Quý Phụ Huynh,\n\n");

        for (ParentStudent ps : parentStudents) {
            User student = userRepository.findById(ps.getStudentId()).orElse(null);
            if (student == null) continue;

            List<Enrollment> enrollments = enrollmentRepository.findByStudentIdAndStatus(student.getId(), EnrollmentStatus.active);
            sb.append("📚 Học sinh: ").append(student.getFullName()).append("\n");

            for (Enrollment enrollment : enrollments) {
                ClassEntity classEntity = classRepository.findById(enrollment.getClassId()).orElse(null);
                if (classEntity == null) continue;

                List<Attendance> absentRecords = attendanceRepository.findByEnrollmentIdAndStatus(enrollment.getId(), AttendanceStatus.absent);
                List<Payment> payments = paymentRepository.findByEnrollmentIdOrderByYearDescMonthDesc(enrollment.getId());
                BigDecimal totalPaid = payments.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal baseFee = enrollment.getCustomFee() != null ? enrollment.getCustomFee()
                        : classEntity.getFeePerSession().multiply(BigDecimal.valueOf(classEntity.getSessionsPerMonth()));
                BigDecimal discountedFee = baseFee.subtract(baseFee.multiply(enrollment.getDiscountPercent()).divide(BigDecimal.valueOf(100)));

                sb.append("  - Lớp: ").append(classEntity.getName()).append(" (").append(classEntity.getYear()).append(")\n");
                sb.append("  - Số buổi vắng: ").append(absentRecords.size()).append("\n");
                sb.append("  - Học phí/tháng: ").append(formatter.format(discountedFee)).append(" ₫\n");
                if (enrollment.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0) {
                    sb.append("  - Giảm giá: ").append(enrollment.getDiscountPercent()).append("%\n");
                }
                sb.append("  - Đã đóng: ").append(formatter.format(totalPaid)).append(" ₫\n\n");
            }
        }

        sb.append("Trân trọng,\nTrung Tâm Tiếng Anh");
        return sb.toString();
    }

    public Map<String, Object> getMessageHistory(MessageChannel channel, MessageStatus status, int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Message> result = messageRepository.findWithFilters(channel, status, pageRequest);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("messages", result.getContent());
        response.put("total", result.getTotalElements());
        return response;
    }

    public Message updateMessageStatus(Long messageId, MessageStatus status, String errorLog) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message == null) return null;

        message.setStatus(status);
        if (status == MessageStatus.sent) message.setSentAt(LocalDateTime.now());
        if (errorLog != null) message.setErrorLog(errorLog);

        return messageRepository.save(message);
    }

    public List<Long> parseRecipientIds(String json) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Long.class));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
