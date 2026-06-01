package com.englishcenter.backend.service;

import com.englishcenter.backend.entity.Attendances;
import java.util.List;

public interface AttendancesService {
    List<Attendances> getAttendancesBySession(Long sessionId);

    Attendances markAttendance(Attendances attendance);
    List<Attendances> markBatchAttendance(List<Attendances> attendances);
}