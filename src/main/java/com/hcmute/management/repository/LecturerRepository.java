package com.hcmute.management.repository;

import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface LecturerRepository extends JpaRepository<LecturerEntity,String> {
    LecturerEntity findByUser(UserEntity user);
    @Query(value = "select * from lecturer",
            countQuery = "select count(*) from lecturer",
            nativeQuery = true)
    Page<LecturerEntity>findAllLecturer(Pageable pageable);
}
