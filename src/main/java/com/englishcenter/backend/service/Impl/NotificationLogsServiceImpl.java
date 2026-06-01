package com.englishcenter.backend.service.Impl;

import com.englishcenter.backend.entity.NotificationLogs;
import com.englishcenter.backend.repository.NotificationLogsRepository;
import com.englishcenter.backend.service.NotificationLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationLogsServiceImpl implements NotificationLogsService {

    @Autowired
    private NotificationLogsRepository notificationLogsRepository;

    @Override
    public List<NotificationLogs> getLogsByUser(Long recipientId) { return notificationLogsRepository.findByRecipientId(recipientId); }

    @Override
    public NotificationLogs createLog(NotificationLogs log) {
        log.setStatus("sent"); // Giả lập đã gửi
        return notificationLogsRepository.save(log);
    }

    @Override
    public Map<String, Object> broadcastNotification(Long classId, Map<String, String> body) {
        return Map.of("status", "Success", "message", "Đã gửi thông báo đến toàn bộ lớp " + classId);
    }
}