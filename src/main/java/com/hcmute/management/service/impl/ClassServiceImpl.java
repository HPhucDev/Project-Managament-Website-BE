package com.hcmute.management.service.impl;

import com.hcmute.management.model.entity.ClassEntity;
import com.hcmute.management.repository.ClassRepository;
import com.hcmute.management.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {
    private final ClassRepository classRepository;
    @Override
    public ClassEntity saveClass(ClassEntity classEntity) {
        return classRepository.save(classEntity);
    }
}
