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
    @Query(value = "Select * from subject where (concat(name,major,subject_type) like concat('%',?1,'%') or ?1 is null) and (status=?2 or ?2='null'  )",nativeQuery = true)
    Page<SubjectEntity> searchByCriteria(String keyWord,String status,Pageable pageable);
}
