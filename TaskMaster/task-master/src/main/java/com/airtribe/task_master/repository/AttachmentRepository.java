package com.airtribe.task_master.repository;

import com.airtribe.task_master.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByTaskIdOrderByUploadedAtAsc(
        Long taskId
    );
}