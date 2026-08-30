package com.airtribe.task_master.controller;

import com.airtribe.task_master.dto.TaskFilter;
import com.airtribe.task_master.dto.TaskRequest;
import com.airtribe.task_master.dto.TaskResponse;
import com.airtribe.task_master.dto.TaskStatusRequest;
import com.airtribe.task_master.service.TaskService;
import com.airtribe.task_master.enums.TaskStatus;

import java.time.LocalDate;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponse createTask(Authentication authentication,
        @Valid @RequestBody TaskRequest request) {
        return taskService.createTask(authentication.getName(), request);
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(Authentication authentication, @PathVariable Long id) {
        return taskService.getTask(id, authentication.getName());
    }

    @GetMapping
    public Page<TaskResponse> getTasks(
        Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String direction) {

        Pageable pageable = createPageable(page, size, sortBy, direction);
        return taskService.getAllTasks(authentication.getName(), pageable);
    }

    @GetMapping("/my-tasks")
    public Page<TaskResponse> getMyTasks(
        Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String direction) {

        Pageable pageable = createPageable(page, size, sortBy,direction);
        return taskService.getMyTasks(authentication.getName(), pageable);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(
        Authentication authentication,
        @PathVariable Long id,
        @Valid @RequestBody TaskRequest request) {

        return taskService.updateTask(id, authentication.getName(), request);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(
        Authentication authentication,
        @PathVariable Long id,
        @Valid @RequestBody TaskStatusRequest request) {

        return taskService.updateStatus(id, authentication.getName(), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(Authentication authentication, @PathVariable Long id) {
        taskService.deleteTask(id, authentication.getName());
    }

    @GetMapping("/search")
    public Page<TaskResponse> searchTasks(
        Authentication authentication,
        @RequestParam(required = false) String query,
        @RequestParam(required = false) TaskStatus status,
        @RequestParam(required = false) Long assigneeId,
        @RequestParam(required = false) LocalDate fromDate,
        @RequestParam(required = false) LocalDate toDate,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String direction) {

        TaskFilter filter = new TaskFilter();
        filter.setQuery(query);
        filter.setStatus(status);
        filter.setAssigneeId(assigneeId);
        filter.setFromDate(fromDate);
        filter.setToDate(toDate);

        Pageable pageable = createPageable(page, size, sortBy, direction);
        return taskService.searchTasks(authentication.getName(), filter, pageable);
    }

    private Pageable createPageable(int page, int size, String sortBy, String direction) {

        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }

        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") 
            ? Sort.Direction.ASC : Sort.Direction.DESC;

        return PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    }
}
