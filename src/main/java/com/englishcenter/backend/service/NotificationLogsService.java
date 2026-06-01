package com.englishcenter.backend.service;

import com.englishcenter.backend.entity.NotificationLogs;
import java.util.List;
import java.util.Map;

public interface NotificationLogsService {
    List<NotificationLogs> getLogsByUser(Long recipientId);
    NotificationLogs createLog(NotificationLogs log);
    Map<String, Object> broadcastNotification(Long classId, Map<String, String> body);
}