package com.englishcenter.backend.modules.teacher.service;

import com.englishcenter.backend.core.entity.Sessions;

import java.util.List;

public interface SessionsService {
    List<Sessions> getSessionsByClass(Long classId);
    List<Sessions> getSessionsByTeacher(Long teacherId);
    Sessions createSession(Sessions session);
}