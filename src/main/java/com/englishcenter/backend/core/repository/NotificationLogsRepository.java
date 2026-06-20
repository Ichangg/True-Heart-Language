package com.englishcenter.backend.core.repository;
import com.englishcenter.backend.core.entity.User;
import com.englishcenter.backend.core.entity.NotificationLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationLogsRepository extends JpaRepository<NotificationLogs, Long> {
    // Tìm lịch sử thông báo được gửi tới một User cụ thể
    List<NotificationLogs> findByRecipientId(Long recipientId);
}