package com.airtribe.task_master.dto;

import com.airtribe.task_master.entity.User;

public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
