package com.airtribe.task_master.dto;

import com.airtribe.task_master.entity.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private String status;
    private LocalDate dueDate;

    private Long createdById;
    private String createdByUsername;

    private Long assignedToId;
    private String assignedToUsername;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long teamId;
    private String teamName;

    public TaskResponse(Task task) {

        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus().name();
        this.dueDate = task.getDueDate();
        
        this.createdById = task.getCreatedBy().getId();
        this.createdByUsername = task.getCreatedBy().getUsername();

        if (task.getAssignedTo() != null) {
            this.assignedToId = task.getAssignedTo().getId();
            this.assignedToUsername = task.getAssignedTo().getUsername();
        }

        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();

        if (task.getTeam() != null) {
            this.teamId = task.getTeam().getId();
            this.teamName = task.getTeam().getName();
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public Long getAssignedToId() {
        return assignedToId;
    }

    public String getAssignedToUsername() {
        return assignedToUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getTeamId() {
        return teamId;
    }
    
    public String getTeamName() {
        return teamName;
    }
}
