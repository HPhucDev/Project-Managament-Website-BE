package com.hcmute.management.service;

import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.payload.request.Progress.AddNewProgressRequest;
import com.hcmute.management.model.payload.request.Progress.UpdateProgressRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface ProgressService {
    ProgressEntity saveProgress(AddNewProgressRequest progressRequest);
    List<ProgressEntity> findAllProgress();

    ProgressEntity findById(int id);
    ProgressEntity updateProgress(UpdateProgressRequest updateProgressRequest, int id);

    void deleteById(List<Integer> listid);
}
