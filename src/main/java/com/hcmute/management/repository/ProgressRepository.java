package com.hcmute.management.repository;

import com.hcmute.management.model.entity.ProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ProgressRepository extends JpaRepository<ProgressEntity, String> {

}
