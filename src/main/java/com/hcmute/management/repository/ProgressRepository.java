package com.hcmute.management.repository;

import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface ProgressRepository extends JpaRepository<ProgressEntity, String> {

    @Query(value = "Select count(progress_id) from progress where subject_id = ?",nativeQuery = true)
    int getPercent(String subjectId);

    Optional<ProgressEntity> findBySubjectAndWeek(SubjectEntity subject, int week);
}
