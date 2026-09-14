package com.nadson.legacydesk.infrastructure.web.dto;

import com.nadson.legacydesk.domain.model.Role;
import com.nadson.legacydesk.domain.model.User;

import java.time.Instant;
import java.util.UUID;

public class UserResponse {

    private final UUID id;
    private final String name;
    private final String email;
    private final Role role;
    private final boolean active;
    private final Instant createdAt;

    public UserResponse(UUID id, String name, String email, Role role, boolean active, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
    }

    public static UserResponse fromDomain(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt()
        );
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
}