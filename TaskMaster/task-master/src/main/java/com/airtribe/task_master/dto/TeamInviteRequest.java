package com.airtribe.task_master.dto;

import jakarta.validation.constraints.NotNull;

public class TeamInviteRequest {

    @NotNull
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
