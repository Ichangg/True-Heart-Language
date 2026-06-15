package com.englishcenter.repository;

import com.englishcenter.entity.Message;
import com.englishcenter.enums.MessageChannel;
import com.englishcenter.enums.MessageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
            "(:channel IS NULL OR m.channel = :channel) AND " +
            "(:status IS NULL OR m.status = :status)")
    Page<Message> findWithFilters(
            @Param("channel") MessageChannel channel,
            @Param("status") MessageStatus status,
            Pageable pageable);
}
