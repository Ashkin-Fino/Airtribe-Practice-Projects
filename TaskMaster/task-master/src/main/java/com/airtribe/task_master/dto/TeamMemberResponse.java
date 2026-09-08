package com.airtribe.task_master.dto;

import com.airtribe.task_master.entity.TeamMember;

import java.time.LocalDateTime;

public class TeamMemberResponse {

    private Long userId;
    private String username;
    private String email;
    private LocalDateTime joinedAt;

    public TeamMemberResponse(TeamMember member) {
        this.userId = member.getUser().getId();
        this.username = member.getUser().getUsername();
        this.email = member.getUser().getEmail();
        this.joinedAt = member.getJoinedAt();
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}
