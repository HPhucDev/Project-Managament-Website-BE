package com.hcmute.management.service;

import com.hcmute.management.model.entity.ClassEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface ClassService {
    ClassEntity saveClass(ClassEntity classEntity);
    ClassEntity findById(String id);
    void deleteClass(ClassEntity classEntity);
    List<ClassEntity> getAllClass();
    ClassEntity findByName(String name);
}
