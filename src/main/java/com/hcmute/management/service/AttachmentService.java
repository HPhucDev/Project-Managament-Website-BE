package com.hcmute.management.service;

import com.hcmute.management.handler.FileNotImageException;
import com.hcmute.management.model.entity.AttachmentEntity;
import com.hcmute.management.model.entity.ProgressEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@Service
public interface AttachmentService {
    void uploadFile(MultipartFile[] file, ProgressEntity progress) throws FileNotImageException;
}
