package com.englishcenter.backend.modules.teacher.service.Impl;

import com.englishcenter.backend.core.entity.Attendances;
import com.englishcenter.backend.modules.teacher.service.AttendancesService;
import com.englishcenter.backend.core.repository.AttendancesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendancesServiceImpl implements AttendancesService {

    @Autowired
    private AttendancesRepository attendancesRepository;

    @Override
    public List<Attendances> getAttendancesBySession(Long sessionId) {
        return attendancesRepository.findBySessionId(sessionId);
    }
    @Override
    public Attendances markAttendance(Attendances attendance) { return attendancesRepository.save(attendance); }
    
    @Override
    @org.springframework.transaction.annotation.Transactional
    public List<Attendances> markBatchAttendance(List<Attendances> attendances) {
        return attendancesRepository.saveAll(attendances);
    }
}