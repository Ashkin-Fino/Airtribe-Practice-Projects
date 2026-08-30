package com.airtribe.task_master.repository;

import com.airtribe.task_master.entity.Team;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {    
}