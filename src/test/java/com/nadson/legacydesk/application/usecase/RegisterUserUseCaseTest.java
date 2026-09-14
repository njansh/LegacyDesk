package com.nadson.legacydesk.application.usecase;

import com.nadson.legacydesk.application.dto.RegisterUserCommand;
import com.nadson.legacydesk.domain.exception.EmailAlreadyExistsException;
import com.nadson.legacydesk.domain.model.Role;
import com.nadson.legacydesk.domain.model.User;
import com.nadson.legacydesk.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterUserUseCase Tests")
public class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterUserUseCase(userRepository, passwordEncoder);
    }

    @Nested
    @DisplayName("Context: Successful Registration")
    class SuccessTests {

        @Test
        @DisplayName("Should hash password, assign ATTENDANT role and persist user")
        void shouldRegisterUserSuccessfully() {
            RegisterUserCommand command = new RegisterUserCommand("John Doe", "john.doe@example.com", "securePassword123");
            String encodedPassword = "encoded_hash_value";

            when(userRepository.existsByEmail(command.getEmail())).thenReturn(false);
            when(passwordEncoder.encode(command.getPassword())).thenReturn(encodedPassword);
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            User result = useCase.execute(command);

            assertNotNull(result);
            assertEquals("John Doe", result.getName());
            assertEquals("john.doe@example.com", result.getEmail());
            assertEquals(encodedPassword, result.getPassword());
            assertEquals(Role.ATTENDANT, result.getRole());
            assertTrue(result.isActive());

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository, times(1)).save(userCaptor.capture());
            assertEquals(encodedPassword, userCaptor.getValue().getPassword());
            assertEquals(Role.ATTENDANT, userCaptor.getValue().getRole());
        }
    }

    @Nested
    @DisplayName("Context: Registration Failures")
    class FailureTests {

        @Test
        @DisplayName("Should throw EmailAlreadyExistsException when email is taken")
        void shouldThrowExceptionWhenEmailExists() {
            RegisterUserCommand command = new RegisterUserCommand("John Doe", "john.doe@example.com", "securePassword123");
            when(userRepository.existsByEmail(command.getEmail())).thenReturn(true);

            assertThrows(EmailAlreadyExistsException.class, () -> useCase.execute(command));

            verify(passwordEncoder, never()).encode(any());
            verify(userRepository, never()).save(any());
        }
    }
}