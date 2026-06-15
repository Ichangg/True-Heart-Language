package com.englishcenter.job;

import com.englishcenter.entity.Message;
import com.englishcenter.entity.User;
import com.englishcenter.enums.MessageChannel;
import com.englishcenter.enums.Role;
import com.englishcenter.enums.UserStatus;
import com.englishcenter.integration.NotificationManager;
import com.englishcenter.repository.UserRepository;
import com.englishcenter.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReminderJob {

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final NotificationManager notificationManager;

    /**
     * Nhắc nhở phụ huynh mỗi thứ Hai lúc 9:00 sáng
     */
    @Scheduled(cron = "0 0 9 * * MON")
    public void weeklyReminder() {
        log.info("🔔 Running weekly absence & payment reminder job...");

        try {
            List<User> parents = userRepository.findByRoleAndStatus(Role.parent, UserStatus.active);

            for (User parent : parents) {
                try {
                    String content = messageService.generateAbsencePaymentMessage(parent.getId());
                    if (content.contains("Số buổi vắng") || content.contains("chưa đóng")) {
                        Message message = messageService.sendMessage(
                                1L, "individual", List.of(parent.getId()), null,
                                MessageChannel.system, "[Tự động] Nhắc nhở buổi vắng và học phí", content
                        );
                        notificationManager.send(message);
                    }
                } catch (Exception e) {
                    log.error("Error sending reminder to parent {}: {}", parent.getId(), e.getMessage());
                }
            }

            log.info("✅ Reminder job completed");
        } catch (Exception e) {
            log.error("❌ Reminder job failed: {}", e.getMessage());
        }
    }
}
