package com.hcmute.management.service;

import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.request.Progress.AddNewProgressRequest;
import com.hcmute.management.model.payload.request.Progress.UpdateProgressRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface ProgressService {
    ProgressEntity saveProgress(UserEntity user,AddNewProgressRequest progressRequest);
    List<ProgressEntity> findAllProgress();

    ProgressEntity findById(String id);
    ProgressEntity updateProgress(UpdateProgressRequest updateProgressRequest, String id);
    ProgressEntity findBySubjectAndWeek(SubjectEntity subject,int week);
    void deleteById(List<String> listid);
}
