package com.hcmute.management.repository;

import com.hcmute.management.model.entity.AttachmentEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity,String> {

    List<AttachmentEntity> findAllByProgress_Subject(SubjectEntity subject);
}
