package com.airtribe.task_master.dto;

import com.airtribe.task_master.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public class TaskStatusRequest {

    @NotNull
    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
