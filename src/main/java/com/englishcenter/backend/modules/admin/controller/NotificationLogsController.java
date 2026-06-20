package com.englishcenter.backend.modules.admin.controller;

import com.englishcenter.backend.core.entity.NotificationLogs;
import com.englishcenter.backend.modules.admin.service.NotificationLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification-logs")
public class NotificationLogsController {

    @Autowired
    private NotificationLogsService notificationLogsService;

    // Lấy danh sách thông báo đã gửi tới một người dùng cụ thể (Hộp thư đến)
    @GetMapping("/user/{recipientId}")
    public List<NotificationLogs> getLogsByUser(@PathVariable Long recipientId) {
        return notificationLogsService.getLogsByUser(recipientId);
    }

    // Hệ thống ghi lại lịch sử mỗi khi gửi thành công 1 thông báo/Email/SMS
    @PostMapping
    public NotificationLogs createLog(@RequestBody NotificationLogs log) {
        return notificationLogsService.createLog(log);
    }

    @PostMapping("/broadcast/class/{classId}")
    public Map<String, Object> broadcastNotification(@PathVariable Long classId, @RequestBody Map<String, String> body) {
        return notificationLogsService.broadcastNotification(classId, body);
    }
}