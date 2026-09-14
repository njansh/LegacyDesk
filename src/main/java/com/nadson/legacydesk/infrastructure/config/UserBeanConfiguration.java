package com.nadson.legacydesk.infrastructure.config;

import com.nadson.legacydesk.application.usecase.RegisterUserUseCase;
import com.nadson.legacydesk.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserBeanConfiguration {

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new RegisterUserUseCase(userRepository, passwordEncoder);
    }
}