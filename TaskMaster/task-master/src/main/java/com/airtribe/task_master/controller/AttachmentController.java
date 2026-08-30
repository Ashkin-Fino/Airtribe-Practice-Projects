package com.airtribe.task_master.controller;

import com.airtribe.task_master.dto.AttachmentResponse;
import com.airtribe.task_master.service.AttachmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/api/tasks/{taskId}/attachments")
    @ResponseStatus(HttpStatus.CREATED)
    public AttachmentResponse uploadAttachment(
        @PathVariable Long taskId,
        @RequestParam("file") MultipartFile file,
        Authentication authentication) {

        return attachmentService.uploadAttachment(taskId, authentication.getName(), file);
    }

    @GetMapping("/api/tasks/{taskId}/attachments")
    public List<AttachmentResponse> getAttachments(@PathVariable Long taskId, Authentication authentication) {
        return attachmentService.getAttachments(taskId, authentication.getName());
    }

    @DeleteMapping("/api/attachments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAttachment(@PathVariable Long id, Authentication authentication) {
        attachmentService.deleteAttachment(id, authentication.getName());
    }
}