package com.airtribe.task_master.service;

import com.airtribe.task_master.entity.Task;
import com.airtribe.task_master.repository.TeamMemberRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskAccessService {

    private final TeamMemberRepository teamMemberRepository;

    public TaskAccessService(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    public boolean canAccessTask(Task task, Long userId) {
        // Task creator
        if (task.getCreatedBy() != null && task.getCreatedBy().getId().equals(userId)) {
            return true;
        }

        // Assigned user
        if (task.getAssignedTo() != null && task.getAssignedTo().getId().equals(userId)) {
            return true;
        }

        // Team member
        if (task.getTeam() != null && teamMemberRepository.existsByTeamIdAndUserId(
            task.getTeam().getId(), userId)) {
            return true;
        }
        return false;
    }

    public void requireTaskAccess(Task task, Long userId) {
        if (!canAccessTask(task, userId)) {
            throw new IllegalStateException("You do not have access to this task");
        }
    }
}