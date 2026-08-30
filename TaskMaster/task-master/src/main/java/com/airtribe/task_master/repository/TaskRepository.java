package com.airtribe.task_master.repository;

import com.airtribe.task_master.entity.Task;
import com.airtribe.task_master.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
        SELECT DISTINCT t
        FROM Task t
        LEFT JOIN t.team team
        LEFT JOIN TeamMember tm ON tm.team.id = team.id
        WHERE t.createdBy.id = :userId
            OR t.assignedTo.id = :userId
            OR tm.user.id = :userId
    """)
    Page<Task> findRelevantTasks(@Param("userId") Long userId,
        Pageable pageable);

    @Query("""
        SELECT t
        FROM Task t
        WHERE (t.createdBy.id = :userId OR t.assignedTo.id = :userId)
        AND (:query IS NULL
            OR LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%'))) 
        AND (:status IS NULL OR t.status = :status)
        AND (:assigneeId IS NULL OR t.assignedTo.id = :assigneeId)
        AND (:fromDate IS NULL OR t.dueDate >= :fromDate)
        AND (:toDate IS NULL OR t.dueDate <= :toDate)
        """)
    Page<Task> searchTasks(
        @Param("userId") Long userId,
        @Param("query") String query,
        @Param("status") TaskStatus status,
        @Param("assigneeId") Long assigneeId,
        @Param("fromDate") java.time.LocalDate fromDate,
        @Param("toDate") java.time.LocalDate toDate,
        Pageable pageable
    );

    Page<Task> findByCreatedById(Long userId, Pageable pageable);

    Page<Task> findByAssignedToId(Long userId, Pageable pageable);

    Optional<Task> findByIdAndCreatedById(Long taskId, Long userId);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByAssignedToIdAndStatus(Long userId, 
        TaskStatus status, Pageable pageable);

    Page<Task> findByDueDateBetween(LocalDate fromDate, 
        LocalDate toDate, Pageable pageable);
}