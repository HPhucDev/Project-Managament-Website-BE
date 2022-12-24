package com.hcmute.management.service;

import com.hcmute.management.model.entity.CriteriaEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CriteriaService {
    CriteriaEntity save(CriteriaEntity entity);
    List<CriteriaEntity> findAllCriteria();
}
