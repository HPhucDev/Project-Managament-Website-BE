package com.hcmute.management.repository;

import com.hcmute.management.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, String> {
Optional<UserEntity> findByPhone(String phone);
}
