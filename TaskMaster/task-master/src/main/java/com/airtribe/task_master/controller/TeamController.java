package com.airtribe.task_master.controller;

import com.airtribe.task_master.dto.TeamInviteRequest;
import com.airtribe.task_master.dto.TeamMemberResponse;
import com.airtribe.task_master.dto.TeamRequest;
import com.airtribe.task_master.dto.TeamResponse;
import com.airtribe.task_master.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public TeamResponse createTeam(@Valid @RequestBody TeamRequest request, Authentication authentication) {
        return teamService.createTeam(authentication.getName(), request);
    }

    @GetMapping
    public List<TeamResponse> getMyTeams(Authentication authentication) {
        return teamService.getMyTeams(authentication.getName());
    }

    @GetMapping("/{id}")
    public TeamResponse getTeam(@PathVariable Long id, Authentication authentication) {
        return teamService.getTeam(id, authentication.getName());
    }

    @GetMapping("/{id}/members")
    public List<TeamMemberResponse> getMembers(@PathVariable Long id, Authentication authentication) {
        return teamService.getMembers(id, authentication.getName());
    }

    @PostMapping("/{id}/invite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inviteUser(Authentication authentication, @PathVariable Long id,
            @Valid @RequestBody TeamInviteRequest request) {
        teamService.inviteUser(id,authentication.getName(),request.getUserId());
    }

    @PostMapping("/invitations/{invitationId}/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptInvitation(@PathVariable Long invitationId, Authentication authentication) {
        teamService.acceptInvitation(invitationId,authentication.getName());
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(@PathVariable Long teamId, @PathVariable Long userId, Authentication authentication) {
        teamService.removeMember(teamId, userId, authentication.getName());
    }
}
