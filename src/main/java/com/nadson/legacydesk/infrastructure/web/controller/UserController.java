package com.nadson.legacydesk.infrastructure.web.controller;

import com.nadson.legacydesk.application.usecase.RegisterUserUseCase;
import com.nadson.legacydesk.domain.model.User;
import com.nadson.legacydesk.infrastructure.web.dto.RegisterUserRequest;
import com.nadson.legacydesk.infrastructure.web.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        User registered = registerUserUseCase.execute(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromDomain(registered));
    }
}