package com.hcmute.management.repository;

import com.hcmute.management.model.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity,String> {
    Optional<RoleEntity> findByName(String roleName);
    Boolean existsByName(String roleName);
}
