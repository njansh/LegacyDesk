package com.nadson.legacydesk.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class User {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    private final UUID id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;
    private final boolean active;
    private final Instant createdAt;
    private final Instant updatedAt;

    public User(UUID id, String name, String email, String password, Role role, boolean active, Instant createdAt, Instant updatedAt) {
        this.name = validateName(name);
        this.email = validateEmail(email);
        this.password = validatePassword(password);
        this.role = Objects.requireNonNull(role, "role cannot be null");
        this.id = id;
        this.active = active;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
    }

    public static User createNew(String name, String email, String password, Role role) {
        Instant now = Instant.now();
        return new User(UUID.randomUUID(), name, email, password, role, true, now, now);
    }

    public User deactivate() {
        return new User(this.id, this.name, this.email, this.password, this.role, false, this.createdAt, Instant.now());
    }

    public User activate() {
        return new User(this.id, this.name, this.email, this.password, this.role, true, this.createdAt, Instant.now());
    }

    public User changeRole(Role newRole) {
        Objects.requireNonNull(newRole, "new role cannot be null");
        return new User(this.id, this.name, this.email, this.password, newRole, this.active, this.createdAt, Instant.now());
    }

    private static String validateName(String name) {
        if (name == null || name.trim().isEmpty() ) {
            throw new IllegalArgumentException("name can't be null or blank");
        }
        if (name.trim().length() > 100) {
            throw new IllegalArgumentException("name must have at most 100 characters");
        }
        return name.trim();
    }

    private static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email can't be null or blank");
        }
        String normalized = email.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("email must be a valid format");
        }
        return normalized;
    }

    private static String validatePassword(String password) {
        if (password == null || password.trim().isEmpty() || password.length() < 8) {
            throw new IllegalArgumentException("password can't be null or blank and must have at least 8 characters");
        }
        return password;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}