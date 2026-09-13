package com.nadson.legacydesk.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Domain Entity Tests")
public class UserTest {

    @Nested
    @DisplayName("Context: User Creation")
    class CreationTests {

        @Test
        @DisplayName("Should create an active user successfully with valid data")
        void shouldCreateValidUser() {
            // Arrange & Act
            User result = User.createNew("John Doe", "johndoe@example.com", "password123", Role.ATTENDANT);

            // Assert
            assertNotNull(result);
            assertTrue(result.isActive());
            assertNotNull(result.getId());
            assertEquals("John Doe", result.getName());
            assertEquals("johndoe@example.com", result.getEmail());
            assertEquals(Role.ATTENDANT, result.getRole());
            assertEquals("password123", result.getPassword());
            assertNotNull(result.getCreatedAt());
            assertNotNull(result.getUpdatedAt());
        }

        @Test
        @DisplayName("Should throw exception when email format is invalid")
        void shouldThrowExceptionForInvalidEmail() {
            assertThrows(IllegalArgumentException.class,
                    () -> User.createNew("John Doe", "johndoeexample.com", "password123", Role.ATTENDANT));
        }

        @Test
        @DisplayName("Should throw exception when password is shorter than 8 characters")
        void shouldThrowExceptionForShortPassword() {
            assertThrows(IllegalArgumentException.class,
                    () -> User.createNew("John Doe", "johndoe@example.com", "pass", Role.ATTENDANT));
        }
    }

    @Nested
    @DisplayName("Context: State Change Operations")
    class StateChangeTests {

        @Test
        @DisplayName("Should deactivate an active user")
        void shouldDeactivateUser() {
            User user = User.createNew("John Doe", "johndoe@example.com", "password123", Role.ATTENDANT);

            User deactivatedUser = user.deactivate();

            assertFalse(deactivatedUser.isActive());
            assertNotNull(deactivatedUser.getUpdatedAt());
        }

        @Test
        @DisplayName("Should activate a deactivated user")
        void shouldActivateUser() {
            User user = User.createNew("John Doe", "johndoe@example.com", "password123", Role.ATTENDANT);
            User deactivatedUser = user.deactivate();

            User activatedUser = deactivatedUser.activate();

            assertTrue(activatedUser.isActive());
            assertNotNull(activatedUser.getUpdatedAt());
        }

        @Test
        @DisplayName("Should change user role")
        void shouldChangeUserRole() {
            User user = User.createNew("John Doe", "johndoe@example.com", "password123", Role.ATTENDANT);

            User changedUser = user.changeRole(Role.TECHNICIAN);

            assertEquals(Role.TECHNICIAN, changedUser.getRole());
            assertNotNull(changedUser.getUpdatedAt());
        }
    }
}