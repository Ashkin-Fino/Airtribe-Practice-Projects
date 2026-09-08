package com.airtribe.task_master.service;

import com.airtribe.task_master.dto.AttachmentResponse;
import com.airtribe.task_master.entity.Attachment;
import com.airtribe.task_master.entity.Task;
import com.airtribe.task_master.entity.User;
import com.airtribe.task_master.exception.BadRequestException;
import com.airtribe.task_master.exception.ResourceNotFoundException;
import com.airtribe.task_master.exception.UnauthorizedException;
import com.airtribe.task_master.repository.AttachmentRepository;
import com.airtribe.task_master.repository.TaskRepository;
import com.airtribe.task_master.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskAccessService taskAccessService;

    private final Path uploadDirectory;

    public AttachmentService(
            AttachmentRepository attachmentRepository,
            TaskRepository taskRepository,
            UserRepository userRepository,
            TaskAccessService taskAccessService,
            @Value("${file.upload-dir}") String uploadDir) {

        this.attachmentRepository = attachmentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskAccessService = taskAccessService;

        this.uploadDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.uploadDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @Transactional
    public AttachmentResponse uploadAttachment(Long taskId, String username,
        MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File cannot be empty");
        }

        User user = getUser(username);

        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

            taskAccessService.requireTaskAccess(task, user.getId());

        String storedFileName = UUID.randomUUID() + getExtension(
            file.getOriginalFilename());

        Path targetPath = uploadDirectory.resolve(storedFileName).normalize();
        if (!targetPath.startsWith(uploadDirectory)) {
            throw new BadRequestException("Invalid file path");
        }

        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file",e);
        }

        Attachment attachment = new Attachment();

        attachment.setTask(task);
        attachment.setUploadedBy(user);
        attachment.setOriginalFileName(file.getOriginalFilename());
        attachment.setStoredFileName(storedFileName);
        attachment.setFilePath(targetPath.toString());
        attachment.setContentType(file.getContentType());
        attachment.setFileSize(file.getSize());
        Attachment saved = attachmentRepository.save(attachment);

        return new AttachmentResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AttachmentResponse> getAttachments(Long taskId, String username) {
        User user = getUser(username);
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskAccessService.requireTaskAccess(task, user.getId());
        return attachmentRepository
            .findByTaskIdOrderByUploadedAtAsc(taskId)
            .stream()
            .map(AttachmentResponse::new)
            .toList();
    }

    @Transactional
    public void deleteAttachment(Long attachmentId, String username) {

        User user = getUser(username);

        Attachment attachment = attachmentRepository.findById(attachmentId).orElseThrow(() ->
            new ResourceNotFoundException("Attachment not found"));

        if (!attachment.getUploadedBy().getId().equals(user.getId())) {
            throw new UnauthorizedException("You can only delete your own attachments");
        }

        try {
            Files.deleteIfExists(Paths.get(attachment.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file",e);
        }

        attachmentRepository.delete(attachment);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private String getExtension(String fileName) {
        if (fileName == null) return "";
        int index = fileName.lastIndexOf('.');
        if (index == -1) return "";
        return fileName.substring(index);
    }
}
