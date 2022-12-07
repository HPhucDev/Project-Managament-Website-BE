package com.hcmute.management.repository;

import com.hcmute.management.model.entity.ClassEntity;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface ClassRepository extends JpaRepository<ClassEntity, String> {
    Optional<ClassEntity> findByClassName(String name);
}
