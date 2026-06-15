package com.englishcenter.service;

import com.englishcenter.entity.*;
import com.englishcenter.enums.AttendanceStatus;
import com.englishcenter.enums.SessionStatus;
import com.englishcenter.exception.ResourceNotFoundException;
import com.englishcenter.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final SessionRepository sessionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ClassRepository classRepository;

    public Session createSession(Long classId, LocalDate sessionDate, String topic, String note) {
        classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Lớp không tồn tại."));

        int sessionNumber = sessionRepository.findTopByClassIdOrderBySessionNumberDesc(classId)
                .map(s -> s.getSessionNumber() + 1)
                .orElse(1);

        Session session = Session.builder()
                .classId(classId)
                .sessionDate(sessionDate)
                .sessionNumber(sessionNumber)
                .topic(topic)
                .note(note)
                .status(SessionStatus.scheduled)
                .build();

        return sessionRepository.save(session);
    }

    public List<Attendance> markAttendance(Long sessionId, List<Map<String, Object>> attendanceData, Long notedBy) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Buổi học không tồn tại."));

        List<Attendance> results = new ArrayList<>();
        for (Map<String, Object> item : attendanceData) {
            Long enrollmentId = Long.valueOf(item.get("enrollment_id").toString());
            AttendanceStatus status = AttendanceStatus.present;
            if (item.containsKey("status")) {
                try { status = AttendanceStatus.valueOf(item.get("status").toString()); } catch (Exception ignored) {}
            }
            String noteText = item.containsKey("note") ? (String) item.get("note") : null;

            Optional<Attendance> existing = attendanceRepository.findByEnrollmentIdAndSessionId(enrollmentId, sessionId);
            Attendance attendance;
            if (existing.isPresent()) {
                attendance = existing.get();
                attendance.setStatus(status);
                attendance.setNotedBy(notedBy);
                attendance.setNote(noteText);
            } else {
                attendance = Attendance.builder()
                        .enrollmentId(enrollmentId)
                        .sessionId(sessionId)
                        .status(status)
                        .notedBy(notedBy)
                        .note(noteText)
                        .build();
            }
            results.add(attendanceRepository.save(attendance));
        }

        session.setStatus(SessionStatus.completed);
        sessionRepository.save(session);

        return results;
    }

    public List<Session> getSessionsByClass(Long classId) {
        return sessionRepository.findByClassIdOrderBySessionNumberAsc(classId);
    }

    public List<Session> getAttendanceByClass(Long classId) {
        return sessionRepository.findByClassIdOrderBySessionDateAsc(classId);
    }

    public List<Map<String, Object>> getAttendanceByStudent(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentIdAndStatus(studentId, com.englishcenter.enums.EnrollmentStatus.active);

        return enrollments.stream().map(e -> {
            List<Attendance> records = attendanceRepository.findByEnrollmentId(e.getId());

            Map<String, Object> data = new LinkedHashMap<>();
            ClassEntity classEntity = classRepository.findById(e.getClassId()).orElse(null);
            if (classEntity != null) {
                Map<String, Object> classInfo = new LinkedHashMap<>();
                classInfo.put("id", classEntity.getId());
                classInfo.put("name", classEntity.getName());
                classInfo.put("year", classEntity.getYear());
                data.put("class", classInfo);
            }
            data.put("total_sessions", records.size());
            data.put("present", records.stream().filter(r -> r.getStatus() == AttendanceStatus.present).count());
            data.put("absent", records.stream().filter(r -> r.getStatus() == AttendanceStatus.absent).count());
            data.put("late", records.stream().filter(r -> r.getStatus() == AttendanceStatus.late).count());
            data.put("excused", records.stream().filter(r -> r.getStatus() == AttendanceStatus.excused).count());
            data.put("records", records);

            return data;
        }).collect(Collectors.toList());
    }
}
