package com.hcmute.management.service;

import com.hcmute.management.model.entity.SubjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@Service
public interface SubjectService {
    SubjectEntity saveSubject(SubjectEntity entity);
    List<SubjectEntity> getAllSubject();
    SubjectEntity getSubjectById(String id);

    void deleteById(List<String> listId);
    Page<SubjectEntity> findAllSubjectPaging(int pageNo, int pageSize);

    Page<SubjectEntity> searchByCriteria(String keyWord,String status,int pageNo,int pageSize,String sort,String order);

    String uploadSubjectFile(MultipartFile file,SubjectEntity subject);
}
