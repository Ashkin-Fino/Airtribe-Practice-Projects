package com.airtribe.task_master.service;

import com.airtribe.task_master.dto.CommentRequest;
import com.airtribe.task_master.dto.CommentResponse;
import com.airtribe.task_master.entity.Comment;
import com.airtribe.task_master.entity.Task;
import com.airtribe.task_master.entity.User;
import com.airtribe.task_master.exception.ResourceNotFoundException;
import com.airtribe.task_master.repository.CommentRepository;
import com.airtribe.task_master.repository.TaskRepository;
import com.airtribe.task_master.repository.TeamMemberRepository;
import com.airtribe.task_master.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskAccessService taskAccessService;

    public CommentService(
        CommentRepository commentRepository,
        TaskRepository taskRepository,
        UserRepository userRepository,
        TaskAccessService taskAccessService) {

        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskAccessService = taskAccessService;
    }

    @Transactional
    public CommentResponse addComment(Long taskId, String username, CommentRequest request) {

        User user = getUser(username);
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskAccessService.requireTaskAccess(task, user.getId());

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setContent(request.getContent());
        Comment saved = commentRepository.save(comment);

        return new CommentResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long taskId, String username) {
        User user = getUser(username);
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskAccessService.requireTaskAccess(task, user.getId());
        return commentRepository
            .findByTaskIdOrderByCreatedAtAsc(taskId)
            .stream()
            .map(CommentResponse::new)
            .toList();
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found")
        );
    }
}