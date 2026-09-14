package com.nadson.legacydesk.application.usecase;

import com.nadson.legacydesk.application.dto.RegisterUserCommand;
import com.nadson.legacydesk.domain.exception.EmailAlreadyExistsException;
import com.nadson.legacydesk.domain.model.Role;
import com.nadson.legacydesk.domain.model.User;
import com.nadson.legacydesk.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(RegisterUserCommand command) {
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(command.getPassword());
        User user = User.createNew(command.getName(), command.getEmail(), encodedPassword, Role.ATTENDANT);

        return userRepository.save(user);
    }
}