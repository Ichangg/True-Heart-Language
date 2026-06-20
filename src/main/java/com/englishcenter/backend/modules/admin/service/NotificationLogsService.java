package com.englishcenter.backend.modules.admin.service;

import com.englishcenter.backend.core.entity.NotificationLogs;
import java.util.List;
import java.util.Map;

public interface NotificationLogsService {
    List<NotificationLogs> getLogsByUser(Long recipientId);
    NotificationLogs createLog(NotificationLogs log);
    Map<String, Object> broadcastNotification(Long classId, Map<String, String> body);
}