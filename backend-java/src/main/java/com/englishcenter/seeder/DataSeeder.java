package com.englishcenter.seeder;

import com.englishcenter.entity.*;
import com.englishcenter.enums.*;
import com.englishcenter.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ParentStudentRepository parentStudentRepository;
    private final SessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;
    private final PromotionRepository promotionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already has data, skipping seed.");
            return;
        }

        log.info("Seeding database...");
        String hashedPassword = passwordEncoder.encode("123456");

        // ─── Users ─────────────────────────────────
        User admin = userRepository.save(User.builder().username("admin").password(hashedPassword).fullName("Vũ Dương").email("admin@englishcenter.vn").phone("0901234567").role(Role.admin).status(UserStatus.active).build());
        User teacher1 = userRepository.save(User.builder().username("teacher1").password(hashedPassword).fullName("Trần Thị Mai").email("mai.tran@englishcenter.vn").phone("0912345678").role(Role.teacher).status(UserStatus.active).build());
        User teacher2 = userRepository.save(User.builder().username("teacher2").password(hashedPassword).fullName("Lê Minh Tuấn").email("tuan.le@englishcenter.vn").phone("0923456789").role(Role.teacher).status(UserStatus.active).build());

        String[] studentNames = {"Nguyễn Minh Anh", "Phạm Quốc Bảo", "Trần Thùy Dung", "Hoàng Gia Huy", "Lê Thanh Hà", "Vũ Đức Minh", "Đặng Thị Ngọc", "Bùi Anh Quân", "Ngô Thị Phương", "Đinh Văn Thành", "Phan Minh Khôi", "Lý Thị Hồng"};
        List<User> students = new ArrayList<>();
        for (int i = 0; i < studentNames.length; i++) {
            User student = userRepository.save(User.builder().username("student" + (i + 1)).password(hashedPassword).fullName(studentNames[i]).email("student" + (i + 1) + "@example.com").phone("098" + String.format("%07d", i + 1)).role(Role.student).status(UserStatus.active).dateOfBirth(LocalDate.of(2015 - i / 4, (i % 12) + 1, 15)).build());
            students.add(student);
        }

        String[][] parentData = {{"Nguyễn Văn Hùng", "Bố"}, {"Phạm Thị Lan", "Mẹ"}, {"Trần Văn Đức", "Bố"}, {"Hoàng Thị Hương", "Mẹ"}, {"Lê Văn Phong", "Bố"}, {"Vũ Thị Thảo", "Mẹ"}};
        List<User> parents = new ArrayList<>();
        for (int i = 0; i < parentData.length; i++) {
            User parent = userRepository.save(User.builder().username("parent" + (i + 1)).password(hashedPassword).fullName(parentData[i][0]).email("parent" + (i + 1) + "@example.com").phone("097" + String.format("%07d", i + 1)).role(Role.parent).status(UserStatus.active).build());
            parents.add(parent);
        }

        // ─── Parent-Student Links ─────────────────
        for (int i = 0; i < parents.size(); i++) {
            parentStudentRepository.save(ParentStudent.builder().parentId(parents.get(i).getId()).studentId(students.get(i * 2).getId()).relationship(parentData[i][1]).build());
            if (i * 2 + 1 < students.size()) {
                parentStudentRepository.save(ParentStudent.builder().parentId(parents.get(i).getId()).studentId(students.get(i * 2 + 1).getId()).relationship(parentData[i][1]).build());
            }
        }
        log.info("Users created");

        // ─── Classes ──────────────────────────────
        ClassEntity class3_1 = classRepository.save(ClassEntity.builder().name("Lớp 3.1").ageGroup(3).year(2024).classNumber(1).teacherId(teacher1.getId()).feePerSession(new BigDecimal("150000")).sessionsPerMonth(8).scheduleInfo("Thứ 3 & Thứ 5, 17:30 - 19:00").maxStudents(20).status(ClassStatus.active).build());
        ClassEntity class3_2 = classRepository.save(ClassEntity.builder().name("Lớp 3.2").ageGroup(3).year(2024).classNumber(2).teacherId(teacher2.getId()).feePerSession(new BigDecimal("150000")).sessionsPerMonth(8).scheduleInfo("Thứ 4 & Thứ 6, 17:30 - 19:00").maxStudents(20).status(ClassStatus.active).build());
        ClassEntity class5_1 = classRepository.save(ClassEntity.builder().name("Lớp 5.1").ageGroup(5).year(2024).classNumber(1).teacherId(teacher1.getId()).feePerSession(new BigDecimal("180000")).sessionsPerMonth(8).scheduleInfo("Thứ 2 & Thứ 4, 18:00 - 19:30").maxStudents(25).status(ClassStatus.active).build());
        classRepository.save(ClassEntity.builder().name("Lớp 3.1 (2023)").ageGroup(3).year(2023).classNumber(1).teacherId(teacher1.getId()).feePerSession(new BigDecimal("140000")).sessionsPerMonth(8).scheduleInfo("Thứ 3 & Thứ 5, 17:30 - 19:00").maxStudents(20).status(ClassStatus.closed).build());
        log.info("Classes created");

        // ─── Enrollments ──────────────────────────
        List<Enrollment> enrollments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            enrollments.add(enrollmentRepository.save(Enrollment.builder().studentId(students.get(i).getId()).classId(class3_1.getId()).discountPercent(i == 0 ? new BigDecimal("10") : BigDecimal.ZERO).discountReason(i == 0 ? "Gia đình quen biết" : null).status(EnrollmentStatus.active).build()));
        }
        for (int i = 4; i < 8; i++) {
            enrollments.add(enrollmentRepository.save(Enrollment.builder().studentId(students.get(i).getId()).classId(class3_2.getId()).discountPercent(i == 5 ? new BigDecimal("15") : BigDecimal.ZERO).discountReason(i == 5 ? "Học sinh giỏi" : null).status(EnrollmentStatus.active).build()));
        }
        for (int i = 8; i < 12; i++) {
            enrollments.add(enrollmentRepository.save(Enrollment.builder().studentId(students.get(i).getId()).classId(class5_1.getId()).discountPercent(BigDecimal.ZERO).status(EnrollmentStatus.active).build()));
        }
        log.info("Enrollments created");

        // ─── Sessions & Attendance ────────────────
        String[] sessionDates = {"2024-09-03", "2024-09-05", "2024-09-10", "2024-09-12", "2024-09-17", "2024-09-19", "2024-09-24", "2024-09-26"};
        for (int s = 0; s < sessionDates.length; s++) {
            Session session = sessionRepository.save(Session.builder().classId(class3_1.getId()).sessionDate(LocalDate.parse(sessionDates[s])).sessionNumber(s + 1).topic("Unit " + (s + 1) + ": Lesson " + (s + 1)).status(SessionStatus.completed).build());
            for (int e = 0; e < 4; e++) {
                boolean isAbsent = (s == 2 && e == 1) || (s == 5 && e == 0) || (s == 6 && e == 3);
                attendanceRepository.save(Attendance.builder().enrollmentId(enrollments.get(e).getId()).sessionId(session.getId()).status(isAbsent ? AttendanceStatus.absent : AttendanceStatus.present).notedBy(teacher1.getId()).build());
            }
        }
        log.info("Sessions & Attendance created");

        // ─── Payments ────────────────────────────
        paymentRepository.save(Payment.builder().enrollmentId(enrollments.get(0).getId()).amount(new BigDecimal("1080000")).expectedAmount(new BigDecimal("1080000")).month(9).year(2024).receivedBy(admin.getId()).note("Đóng đủ tháng 9").build());
        paymentRepository.save(Payment.builder().enrollmentId(enrollments.get(1).getId()).amount(new BigDecimal("800000")).expectedAmount(new BigDecimal("1200000")).month(9).year(2024).receivedBy(admin.getId()).note("Đóng trước 1 phần").build());
        log.info("Payments created");

        // ─── Promotions ──────────────────────────
        promotionRepository.save(Promotion.builder().title("Khai giảng Lớp Tiếng Anh Mùa Hè 2024").description("Đăng ký ngay để nhận ưu đãi giảm 20% học phí tháng đầu tiên!").type(PromotionType.slider).isActive(true).startDate(LocalDate.of(2024, 6, 1)).endDate(LocalDate.of(2024, 12, 31)).displayOrder(1).createdBy(admin.getId()).build());
        promotionRepository.save(Promotion.builder().title("Mở Lớp Mới - Lớp 4 Năm 2024").description("Trung tâm mở thêm lớp 4 cho các bé 9-10 tuổi.").type(PromotionType.popup).isActive(true).startDate(LocalDate.of(2024, 8, 1)).endDate(LocalDate.of(2024, 12, 31)).displayOrder(2).createdBy(admin.getId()).build());
        promotionRepository.save(Promotion.builder().title("Ưu đãi đặc biệt - Giới thiệu bạn mới").description("Giới thiệu bạn mới, cả hai cùng được giảm 10% trong 1 tháng!").type(PromotionType.slider).isActive(true).displayOrder(3).createdBy(admin.getId()).build());
        log.info("Promotions created");

        log.info("\nSeed data complete!");
        log.info("────────────────────────────────────────");
        log.info("   Tài khoản đăng nhập:");
        log.info("   Admin:    admin / 123456");
        log.info("   Teacher:  teacher1 / 123456, teacher2 / 123456");
        log.info("   Student:  student1-12 / 123456");
        log.info("   Parent:   parent1-6 / 123456");
        log.info("────────────────────────────────────────");
    }
}
