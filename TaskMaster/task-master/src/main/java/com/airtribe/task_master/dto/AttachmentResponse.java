package com.airtribe.task_master.dto;

import com.airtribe.task_master.entity.Attachment;

import java.time.LocalDateTime;

public class AttachmentResponse {

    private Long id;
    private Long taskId;

    private Long uploadedById;
    private String uploadedByUsername;

    private String originalFileName;
    private String contentType;
    private long fileSize;

    private LocalDateTime uploadedAt;

    public AttachmentResponse(Attachment attachment) {
        this.id = attachment.getId();
        this.taskId = attachment.getTask().getId();
        this.uploadedById = attachment.getUploadedBy().getId();
        this.uploadedByUsername = attachment.getUploadedBy().getUsername();
        this.originalFileName = attachment.getOriginalFileName();
        this.contentType = attachment.getContentType();
        this.fileSize = attachment.getFileSize();
        this.uploadedAt = attachment.getUploadedAt();
    }

    public Long getId() {
        return id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public Long getUploadedById() {
        return uploadedById;
    }

    public String getUploadedByUsername() {
        return uploadedByUsername;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}
