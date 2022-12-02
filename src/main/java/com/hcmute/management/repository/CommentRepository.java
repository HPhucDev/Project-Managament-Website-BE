package com.hcmute.management.repository;

import com.hcmute.management.model.entity.CommentEntity;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;


@EnableJpaRepositories
public interface CommentRepository extends JpaRepository<CommentEntity,String> {
    CommentEntity findByUserComment(UserEntity user);
    List<CommentEntity> findByProgressComment(ProgressEntity progress);
    @Query(value = "select * from  order by time asc",
            countQuery = "select count(*) from comment",
            nativeQuery = true)
    Page<CommentEntity> findAllComment(Pageable pageable);

}
