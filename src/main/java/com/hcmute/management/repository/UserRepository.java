package com.hcmute.management.repository;

import com.hcmute.management.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    @Query(value = "SELECT * From users where user_name=?1 limit 1",nativeQuery = true)
Optional<UserEntity> findByUserName(String username);

Optional<UserEntity> findByEmail(String email);
}
