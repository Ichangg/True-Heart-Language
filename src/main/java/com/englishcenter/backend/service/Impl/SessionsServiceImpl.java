package com.englishcenter.backend.service.Impl;

import com.englishcenter.backend.entity.Sessions;
import com.englishcenter.backend.repository.SessionsRepository;
import com.englishcenter.backend.service.SessionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionsServiceImpl implements SessionsService {

    @Autowired
    private SessionsRepository sessionsRepository;

    @Override
    public List<Sessions> getSessionsByClass(Long classId) { return sessionsRepository.findByClassId(classId); }

    @Override
    public List<Sessions> getSessionsByTeacher(Long teacherId) { return sessionsRepository.findByTeacherId(teacherId); }

    @Override
    public Sessions createSession(Sessions session) { return sessionsRepository.save(session); }
}