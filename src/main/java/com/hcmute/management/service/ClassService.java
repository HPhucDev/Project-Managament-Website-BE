package com.hcmute.management.service;

import com.hcmute.management.model.entity.ClassEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public interface ClassService {
    ClassEntity saveClass(ClassEntity classEntity);
}
