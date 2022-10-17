package com.hcmute.management.repository;

import com.hcmute.management.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<UserEntity, String> {
Optional<UserEntity> findByPhone(String phone);
}
