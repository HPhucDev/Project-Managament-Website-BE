package com.hcmute.management.service.impl;

import com.hcmute.management.handler.FileNotImageException;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.repository.SubjectRepository;
import com.hcmute.management.service.ImageStorageService;
import com.hcmute.management.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final ImageStorageService imageStorageService;
    private final SubjectRepository subjectRepository;
    @Override
    public SubjectEntity saveSubject(SubjectEntity entity) {
        return subjectRepository.save(entity);
    }

    @Override
    public List<SubjectEntity> getAllSubject() {
        List<SubjectEntity> listSubject= subjectRepository.findAll();
        return listSubject;
    }

    @Override
    public SubjectEntity getSubjectById(String id) {
        Optional<SubjectEntity> subject = subjectRepository.findById(id);
        if(subject.isEmpty())
            return null;
        return subject.get();
    }

    @Override
    public void deleteById(List<String> listId) {
        for (String id:listId)
        {
            subjectRepository.deleteById(id);
        }
    }

    @Override
    public Page<SubjectEntity> findAllSubjectPaging(int pageNo, int pageSize) {
        Pageable paging =null;
        paging= PageRequest.of(pageNo,pageSize);
        Page<SubjectEntity> pageResult=subjectRepository.findAllSubject(paging);
        return pageResult;
    }

    @Override
    public Page<SubjectEntity> searchByCriteria(String keyWord, String status, int pageNo, int pageSize, String sort, String order) {
        Pageable paging = PageRequest.of(pageNo,pageSize, order=="asc"? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<SubjectEntity> pageResult = subjectRepository.searchByCriteria(keyWord,status,paging);
        return pageResult;
    }

    @Override
    public String uploadSubjectFile(MultipartFile file, SubjectEntity subject) {
        if (!isUploadFile(file))
        {
            throw new FileNotImageException("This file is not Image type");
        }
        else {
            String url = imageStorageService.uploadFile(file, "SubjectAttachment:" + subject.getId());
            return url;
        }
    }
    public boolean isUploadFile(MultipartFile file) {
        return Arrays.asList(new String[] {"application/pdf","application/x-zip-compressed","application/octet-stream"})
                .contains(file.getContentType().trim().toLowerCase());
    }
}
