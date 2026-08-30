package com.airtribe.task_master.controller;

import com.airtribe.task_master.dto.CommentRequest;
import com.airtribe.task_master.dto.CommentResponse;
import com.airtribe.task_master.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(
        @PathVariable Long taskId,
        @Valid @RequestBody CommentRequest request,
        Authentication authentication) {
        
        return commentService.addComment(taskId, authentication.getName(), request);
    }

    @GetMapping
    public List<CommentResponse> getComments(@PathVariable Long taskId, Authentication authentication) {
        return commentService.getComments(taskId, authentication.getName());
    }
}
