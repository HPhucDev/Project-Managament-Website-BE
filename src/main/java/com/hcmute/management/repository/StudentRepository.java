package com.hcmute.management.repository;

import com.hcmute.management.common.OrderByEnum;
import com.hcmute.management.common.StudentSort;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.response.PagingResponse;
import com.hcmute.management.repository.custom.StudentRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@EnableJpaRepositories
public interface StudentRepository extends JpaRepository<StudentEntity, String>, StudentRepositoryCustom {
    Optional<StudentEntity> findByUser(UserEntity user);
    @Modifying
    @Query(value =  "Delete from students where id = ?", nativeQuery = true)
    void deleteById(String id);

    StudentEntity findByUserId(String userid);

    @Query(value = "select * from students",
            countQuery = "select count(*) from students",
            nativeQuery = true)
    Page<StudentEntity> findAllStudentPaging(Pageable pageable);

    PagingResponse search(String searchText, OrderByEnum orderBy, StudentSort order, int pageindex, int pagesize);
}
