package com.airtribe.task_master.service;

import com.airtribe.task_master.dto.TaskFilter;
import com.airtribe.task_master.dto.TaskRequest;
import com.airtribe.task_master.dto.TaskResponse;
import com.airtribe.task_master.dto.TaskStatusRequest;
import com.airtribe.task_master.entity.Task;
import com.airtribe.task_master.entity.Team;
import com.airtribe.task_master.entity.User;
import com.airtribe.task_master.enums.TaskStatus;
import com.airtribe.task_master.repository.TaskRepository;
import com.airtribe.task_master.repository.TeamMemberRepository;
import com.airtribe.task_master.repository.TeamRepository;
import com.airtribe.task_master.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    public TaskService(
        TaskRepository taskRepository, 
        UserRepository userRepository,
        TeamRepository teamRepository,
        TeamMemberRepository teamMemberRepository) {

        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    @Transactional
    public TaskResponse createTask(String username, TaskRequest request) {

        User creator = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException(
                "User not found"
            ));

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());

        task.setCreatedBy(creator);
        task.setStatus(TaskStatus.OPEN);

        if (request.getAssignedToId() != null) {
            User assignee = userRepository.findById(request
                .getAssignedToId()).orElseThrow(() ->
                    new IllegalArgumentException("Assignee not found")
                );
            task.setAssignedTo(assignee);
        }

        if (request.getTeamId() != null) {
            Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
            if (!teamMemberRepository.existsByTeamIdAndUserId(team.getId(), creator.getId())) {
                throw new IllegalArgumentException("You are not a member of this team");
            }
            task.setTeam(team);
        }

        Task savedTask =taskRepository.save(task);
        return new TaskResponse(savedTask);
    }

    @Transactional(readOnly = true)
    public TaskResponse getTask(Long taskId,String username) {
        Task task = getTaskEntity(taskId);
        validateAccess(task, username);
        return new TaskResponse(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> getAllTasks(String username,
        Pageable pageable) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException(
                "User not found"
            ));
        return taskRepository.findRelevantTasks(user.getId(), pageable).map(TaskResponse::new);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> getMyTasks(String username, 
        Pageable pageable) {

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException(
                "User not found"
            ));

        Page<Task> tasks = taskRepository.findByAssignedToId(
            user.getId(), pageable
        );

        return tasks.map(TaskResponse::new);
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, String username,
        TaskRequest request) {

        Task task = getTaskEntity(taskId);
        validateOwnership(task, username);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());

        if (request.getAssignedToId() != null) {
            User assignee = userRepository.findById(request
                .getAssignedToId()).orElseThrow(() ->
                    new IllegalArgumentException("Assignee not found")
                );
            task.setAssignedTo(assignee);
        } else {
            task.setAssignedTo(null);
        }

        return new TaskResponse(taskRepository.save(task));
    }

    @Transactional
    public TaskResponse updateStatus(Long taskId, String username,
        TaskStatusRequest request) {

        Task task = getTaskEntity(taskId);
        validateAccess(task, username);
        task.setStatus(request.getStatus());
        return new TaskResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long taskId,String username) {
        Task task = getTaskEntity(taskId);
        validateOwnership(task, username);
        taskRepository.delete(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> searchTasks(String username, TaskFilter filter,
            Pageable pageable) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> 
            new IllegalArgumentException("User not found")
        );

        if (filter.getFromDate() != null && filter.getToDate() != null
            && filter.getFromDate().isAfter(filter.getToDate())) {
            throw new IllegalArgumentException("fromDate cannot be after toDate");
        }

        Page<Task> tasks = taskRepository.searchTasks(
            user.getId(),
            filter.getQuery(),
            filter.getStatus(),
            filter.getAssigneeId(),
            filter.getFromDate(),
            filter.getToDate(),
            pageable
        );
        return tasks.map(TaskResponse::new);
    }

    private Task getTaskEntity(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() ->
            new IllegalArgumentException("Task not found")
        );
    }

    private void validateOwnership(Task task, String username) {
        if (!task.getCreatedBy().getUsername().equals(username)) {
            throw new IllegalArgumentException(
                "You are not authorized to modify this task"
            );
        }
    }

    private void validateAccess(Task task, String username) {
        boolean creator = task.getCreatedBy().getUsername()
            .equals(username);

        boolean assignee = task.getAssignedTo() != null 
            && task.getAssignedTo().getUsername()
            .equals(username);

        if (!creator && !assignee) {
            throw new IllegalArgumentException(
                "You are not authorized to access this task"
            );
        }
    }
}
