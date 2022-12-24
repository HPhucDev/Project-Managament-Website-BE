package com.hcmute.management.service.impl;

import com.hcmute.management.model.entity.CriteriaEntity;
import com.hcmute.management.repository.CriteriaRepository;
import com.hcmute.management.service.CriteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CriteriaServiceImpl implements CriteriaService {
    private final CriteriaRepository criteriaRepository;
    @Override
    public CriteriaEntity save(CriteriaEntity entity) {
        return criteriaRepository.save(entity);
    }

    @Override
    public List<CriteriaEntity> findAllCriteria() {
        return criteriaRepository.findAll();
    }
}
