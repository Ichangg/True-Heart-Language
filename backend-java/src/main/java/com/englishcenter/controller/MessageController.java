package com.englishcenter.controller;

import com.englishcenter.dto.ApiResponse;
import com.englishcenter.dto.PaginatedResponse;
import com.englishcenter.entity.Message;
import com.englishcenter.entity.User;
import com.englishcenter.enums.MessageChannel;
import com.englishcenter.enums.MessageStatus;
import com.englishcenter.integration.NotificationManager;
import com.englishcenter.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final NotificationManager notificationManager;

    @PostMapping("/send")
    @PreAuthorize("hasRole('ADMIN')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<ApiResponse<Message>> send(@RequestBody Map<String, Object> body, @AuthenticationPrincipal User user) {
        Long senderId = user.getId();
        String recipientType = (String) body.get("recipient_type");
        List<Long> recipientIds = body.containsKey("recipient_ids") ?
                ((List<Object>) body.get("recipient_ids")).stream().map(o -> Long.valueOf(o.toString())).toList() : null;
        Long classId = body.containsKey("class_id") && body.get("class_id") != null ? Long.valueOf(body.get("class_id").toString()) : null;
        MessageChannel channel = body.containsKey("channel") ? MessageChannel.valueOf((String) body.get("channel")) : MessageChannel.system;
        String subject = (String) body.get("subject");
        String content = (String) body.get("content");

        Message message = messageService.sendMessage(senderId, recipientType, recipientIds, classId, channel, subject, content);

        // Gửi async qua kênh tương ứng
        notificationManager.sendAsync(message);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(message, "Tin nhắn đã được gửi."));
    }

    @PostMapping("/absence-reminder")
    @PreAuthorize("hasRole('ADMIN')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<ApiResponse<List<Message>>> sendAbsenceReminder(@RequestBody Map<String, Object> body, @AuthenticationPrincipal User user) {
        List<Long> parentIds = ((List<Object>) body.get("parent_ids")).stream().map(o -> Long.valueOf(o.toString())).toList();
        String channelStr = body.containsKey("channel") ? (String) body.get("channel") : "system";
        MessageChannel channel = MessageChannel.valueOf(channelStr);

        List<Message> results = new ArrayList<>();
        for (Long parentId : parentIds) {
            String content = messageService.generateAbsencePaymentMessage(parentId);
            Message message = messageService.sendMessage(user.getId(), "individual", List.of(parentId), null, channel, "Thông báo số buổi vắng và học phí", content);
            notificationManager.sendAsync(message);
            results.add(message);
        }

        return ResponseEntity.ok(ApiResponse.success(results, "Đã gửi thông báo cho " + results.size() + " phụ huynh."));
    }

    @PostMapping("/emergency")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Message>> sendEmergencyNotice(@RequestBody Map<String, Object> body, @AuthenticationPrincipal User user) {
        Long classId = Long.valueOf(body.get("class_id").toString());
        String content = (String) body.get("content");
        String channelStr = body.containsKey("channel") ? (String) body.get("channel") : "system";
        MessageChannel channel = MessageChannel.valueOf(channelStr);

        Message message = messageService.sendMessage(user.getId(), "class", null, classId, channel, "⚠️ Thông báo khẩn cấp", content);
        notificationManager.sendAsync(message);

        return ResponseEntity.ok(ApiResponse.success(message, "Đã gửi thông báo khẩn cấp."));
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getHistory(
            @RequestParam(required = false) String channel,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        MessageChannel channelEnum = channel != null ? MessageChannel.valueOf(channel) : null;
        MessageStatus statusEnum = status != null ? MessageStatus.valueOf(status) : null;
        Map<String, Object> result = messageService.getMessageHistory(channelEnum, statusEnum, page, limit);
        return ResponseEntity.ok(PaginatedResponse.of(result.get("messages"), (Long) result.get("total"), page, limit));
    }
}
