package com.nadson.legacydesk.infrastructure.persistence.adapter;

import com.nadson.legacydesk.domain.model.User;
import com.nadson.legacydesk.domain.repository.UserRepository;
import com.nadson.legacydesk.infrastructure.persistence.entity.UserJpaEntity;
import com.nadson.legacydesk.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepository {
    private final SpringDataUserRepository springDataUserRepository;

    public UserRepositoryAdapter(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }
    @Override
    public User save(User user) {
        UserJpaEntity userJpaEntity = UserJpaEntity.fromDomain(user);
        UserJpaEntity savedUserJpaEntity = springDataUserRepository.save(userJpaEntity);
        return savedUserJpaEntity.toDomain();
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(springDataUserRepository.findByEmail(email).map(UserJpaEntity::toDomain).orElse(null));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(springDataUserRepository.findById(id).map(UserJpaEntity::toDomain).orElse(null));
    }
}