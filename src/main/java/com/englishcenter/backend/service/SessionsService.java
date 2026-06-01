package com.englishcenter.backend.service;

import com.englishcenter.backend.entity.Sessions;

import java.util.List;

public interface SessionsService {
    List<Sessions> getSessionsByClass(Long classId);
    List<Sessions> getSessionsByTeacher(Long teacherId);
    Sessions createSession(Sessions session);
}