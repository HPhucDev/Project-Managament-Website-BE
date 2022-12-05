package com.hcmute.management.service.impl;

import com.hcmute.management.model.entity.ClassEntity;
import com.hcmute.management.repository.ClassRepository;
import com.hcmute.management.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {
    private final ClassRepository classRepository;
    @Override
    public ClassEntity saveClass(ClassEntity classEntity) {
        return classRepository.save(classEntity);
    }

    @Override
    public ClassEntity findById(String id) {
        Optional<ClassEntity> classEntity = classRepository.findById(id);
        if (classEntity.isEmpty())
            return null;
        return classEntity.get();
    }

    @Override
    public void deleteClass(ClassEntity classEntity) {
         classRepository.delete(classEntity);
    }

    @Override
    public List<ClassEntity> getAllClass() {
        return classRepository.findAll();
    }
}
