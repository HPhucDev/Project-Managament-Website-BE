package com.hcmute.management.repository;


import com.hcmute.management.model.entity.SubjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubjectRepository extends JpaRepository<SubjectEntity,String> {
    @Query(value = "select * from subject",
    countQuery = "select count(*) from subject",
    nativeQuery = true)
    Page<SubjectEntity> findAllSubject(Pageable pageable);
}
