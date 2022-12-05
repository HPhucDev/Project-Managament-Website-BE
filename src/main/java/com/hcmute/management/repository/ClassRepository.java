package com.hcmute.management.repository;

import com.hcmute.management.model.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface ClassRepository extends JpaRepository<ClassEntity, String> {
}
