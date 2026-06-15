package com.englishcenter.integration;

import com.englishcenter.entity.Message;
import com.englishcenter.entity.User;
import com.englishcenter.enums.MessageStatus;
import com.englishcenter.repository.UserRepository;
import com.englishcenter.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationManager {

    private final ZaloService zaloService;
    private final FacebookService facebookService;
    private final SmsService smsService;
    private final UserRepository userRepository;
    private final MessageService messageService;

    @Async
    public void sendAsync(Message message) {
        try {
            send(message);
        } catch (Exception e) {
            log.error("Notification send error: {}", e.getMessage());
        }
    }

    public void send(Message message) {
        List<Long> recipientIds = messageService.parseRecipientIds(message.getRecipientIds());
        boolean success = true;
        StringBuilder errorLog = new StringBuilder();

        for (Long userId : recipientIds) {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) continue;

            Map<String, Object> result;
            try {
                result = switch (message.getChannel()) {
                    case zalo -> {
                        if (user.getZaloId() != null && !user.getZaloId().isBlank()) {
                            yield zaloService.sendOAMessage(user.getZaloId(), message.getContent());
                        } else if (user.getPhone() != null) {
                            yield zaloService.sendMessage(user.getPhone(), message.getContent());
                        } else {
                            yield Map.<String, Object>of("success", false, "message", "Không có Zalo ID hoặc số điện thoại");
                        }
                    }
                    case facebook -> {
                        if (user.getFacebookId() != null && !user.getFacebookId().isBlank()) {
                            yield facebookService.sendMessage(user.getFacebookId(), message.getContent());
                        } else {
                            yield Map.<String, Object>of("success", false, "message", "Không có Facebook ID");
                        }
                    }
                    case sms -> {
                        if (user.getPhone() != null) {
                            yield smsService.sendSms(user.getPhone(), message.getContent());
                        } else {
                            yield Map.<String, Object>of("success", false, "message", "Không có số điện thoại");
                        }
                    }
                    default -> Map.<String, Object>of("success", true, "message", "Đã lưu trong hệ thống");
                };

                if (!Boolean.TRUE.equals(result.get("success"))) {
                    success = false;
                    errorLog.append("User ").append(userId).append(": ").append(result.get("message")).append("\n");
                }
            } catch (Exception e) {
                success = false;
                errorLog.append("User ").append(userId).append(": ").append(e.getMessage()).append("\n");
            }
        }

        messageService.updateMessageStatus(
                message.getId(),
                success ? MessageStatus.sent : MessageStatus.failed,
                errorLog.length() > 0 ? errorLog.toString() : null
        );
    }
}
