package com.airtribe.task_master.repository;

import com.airtribe.task_master.entity.TeamInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long> {

    Optional<TeamInvitation>
    findByIdAndInvitedUserId(Long id, Long userId);

    boolean existsByTeamIdAndInvitedUserIdAndAcceptedFalseAndRevokedFalse(
        Long teamId,
        Long userId
    );
}