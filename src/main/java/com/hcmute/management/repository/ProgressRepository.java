package com.hcmute.management.repository;

import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface ProgressRepository extends JpaRepository<ProgressEntity, String> {

    Optional<ProgressEntity> findBySubjectAndWeek(SubjectEntity subject, int week);
}
