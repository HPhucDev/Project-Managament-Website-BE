package com.hcmute.management.service.impl;

import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.repository.SubjectRepository;
import com.hcmute.management.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
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
    public List<SubjectEntity> findAllSubjectPaging(int pageNo, int pageSize) {
        Pageable paging =null;
        paging= PageRequest.of(pageNo,pageSize);
        Page<SubjectEntity> pageResult=subjectRepository.findAllSubject(paging);
        return pageResult.toList();
    }
}
