package com.hcmute.management.repository;

import com.hcmute.management.model.entity.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity,String> {
}
