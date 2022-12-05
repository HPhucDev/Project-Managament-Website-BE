package com.hcmute.management.repository;

import com.hcmute.management.common.LecturerSort;
import com.hcmute.management.common.OrderByEnum;
import com.hcmute.management.common.StudentSort;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.response.PagingResponse;
import com.hcmute.management.repository.custom.LecturerRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface LecturerRepository extends JpaRepository<LecturerEntity,String>, LecturerRepositoryCustom {
    LecturerEntity findByUser(UserEntity user);
    @Query(value = "select * from lecturer",
            countQuery = "select count(*) from lecturer",
            nativeQuery = true)
    Page<LecturerEntity>findAllLecturer(Pageable pageable);
    @Query(value = "Select distinct id,position,qualification,user from lecturer JOIN users where (concat(qualification,position,full_name,gender,email) like concat('%',?1,'%') or ?1 is null)",nativeQuery = true)
    Page<Object> searchByCriteria(String keyWord, Pageable pageable);
    PagingResponse search(String searchText, OrderByEnum orderBy, LecturerSort order, int pageindex, int pagesize);
}
