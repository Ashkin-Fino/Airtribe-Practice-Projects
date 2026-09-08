package com.airtribe.task_master.dto;

import com.airtribe.task_master.entity.Team;

import java.time.LocalDateTime;

public class TeamResponse {

    private Long id;
    private String name;
    private String description;

    private Long createdById;
    private String createdByUsername;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TeamResponse(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.description = team.getDescription();
        this.createdById = team.getCreatedBy().getId();
        this.createdByUsername = team.getCreatedBy().getUsername();
        this.createdAt = team.getCreatedAt();
        this.updatedAt = team.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
