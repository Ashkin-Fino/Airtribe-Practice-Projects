package com.airtribe.task_master.service;

import com.airtribe.task_master.dto.TeamMemberResponse;
import com.airtribe.task_master.dto.TeamRequest;
import com.airtribe.task_master.dto.TeamResponse;
import com.airtribe.task_master.entity.Team;
import com.airtribe.task_master.entity.TeamInvitation;
import com.airtribe.task_master.entity.TeamMember;
import com.airtribe.task_master.entity.User;
import com.airtribe.task_master.exception.BadRequestException;
import com.airtribe.task_master.exception.ResourceNotFoundException;
import com.airtribe.task_master.exception.UnauthorizedException;
import com.airtribe.task_master.repository.TeamInvitationRepository;
import com.airtribe.task_master.repository.TeamMemberRepository;
import com.airtribe.task_master.repository.TeamRepository;
import com.airtribe.task_master.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamInvitationRepository invitationRepository;
    private final UserRepository userRepository;

    public TeamService(
            TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository,
            TeamInvitationRepository invitationRepository,
            UserRepository userRepository) {

        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TeamResponse createTeam(String username, TeamRequest request) {
        User creator = getUser(username);

        Team team = new Team();
        team.setName(request.getName());
        team.setDescription(request.getDescription());
        team.setCreatedBy(creator);
        Team savedTeam = teamRepository.save(team);

        TeamMember member = new TeamMember();
        member.setTeam(savedTeam);
        member.setUser(creator);
        teamMemberRepository.save(member);

        return new TeamResponse(savedTeam);
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> getMyTeams(String username) {
        User user = getUser(username);
        return teamMemberRepository
            .findByUserId(user.getId())
            .stream()
            .map(member -> new TeamResponse(member.getTeam()))
            .toList();
    }

    @Transactional(readOnly = true)
    public TeamResponse getTeam(Long teamId, String username) {
        Team team = getTeamEntity(teamId);
        User user = getUser(username);
        validateMembership(teamId, user.getId());
        return new TeamResponse(team);
    }

    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getMembers(Long teamId, String username) {
        User user = getUser(username);
        validateMembership(teamId, user.getId());
        return teamMemberRepository
            .findByTeamId(teamId)
            .stream()
            .map(TeamMemberResponse::new)
            .toList();
    }

    @Transactional
    public void inviteUser(Long teamId, String username, Long invitedUserId) {
        Team team = getTeamEntity(teamId);
        User inviter = getUser(username);

        validateMembership(teamId, inviter.getId());

        User invitedUser = userRepository.findById(invitedUserId).orElseThrow(() ->
            new ResourceNotFoundException("Invited user not found")
        );

        if (teamMemberRepository.existsByTeamIdAndUserId(teamId, invitedUserId)) {
            throw new BadRequestException("User is already a team member");
        }

        if (invitationRepository.existsByTeamIdAndInvitedUserIdAndAcceptedFalseAndRevokedFalse(
            teamId, invitedUserId)) {
            throw new BadRequestException("Active invitation already exists");
        }

        TeamInvitation invitation = new TeamInvitation();

        invitation.setTeam(team);
        invitation.setInvitedUser(invitedUser);
        invitation.setInvitedBy(inviter);
        invitation.setAccepted(false);
        invitation.setRevoked(false);

        invitationRepository.save(invitation);
    }

    @Transactional
    public void acceptInvitation(Long invitationId, String username) {
        User user = getUser(username);

        TeamInvitation invitation =invitationRepository
            .findByIdAndInvitedUserId(invitationId, user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Invitation not found")
            );

        if (invitation.isAccepted()) {
            throw new BadRequestException("Invitation already accepted");
        }

        if (invitation.isRevoked()) {
            throw new BadRequestException("Invitation has been revoked");
        }

        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Invitation has expired");
        }

        if (!teamMemberRepository.existsByTeamIdAndUserId(invitation.getTeam().getId(), user.getId())) {
            TeamMember member = new TeamMember();
            member.setTeam(invitation.getTeam());
            member.setUser(user);
            teamMemberRepository.save(member);
        }
        invitation.setAccepted(true);
        invitationRepository.save(invitation);
    }

    @Transactional
    public void removeMember(Long teamId, Long userId, String username) {
        Team team = getTeamEntity(teamId);
        User requester = getUser(username);

        if (!team.getCreatedBy().getId().equals(requester.getId())) {
            throw new UnauthorizedException("Only the team creator can remove members");
        }

        if (team.getCreatedBy().getId().equals(userId)) {
            throw new BadRequestException("Team creator cannot be removed");
        }

        teamMemberRepository.deleteByTeamIdAndUserId(teamId, userId);
    }

    private Team getTeamEntity(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void validateMembership(Long teamId, Long userId) {
        if (!teamMemberRepository.existsByTeamIdAndUserId(teamId, userId)) {
            throw new UnauthorizedException("You are not a member of this team");
        }
    }
}
