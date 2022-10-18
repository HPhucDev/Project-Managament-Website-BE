package com.hcmute.management.repository;

import com.hcmute.management.model.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
@EnableJpaRepositories
public interface RoleRepository extends JpaRepository<RoleEntity,String> {
    Optional<RoleEntity> findByName(String roleName);
    Boolean existsByName(String roleName);
}
