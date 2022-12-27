package com.hcmute.management.service.impl;

import com.hcmute.management.handler.FileNotImageException;
import com.hcmute.management.model.entity.AttachmentEntity;
import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.repository.AttachmentRepository;
import com.hcmute.management.repository.ProgressRepository;
import com.hcmute.management.service.AttachmentService;
import com.hcmute.management.service.ImageStorageService;
import com.hcmute.management.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final ImageStorageService imageStorageService;
    private final AttachmentRepository attachmentRepository;

    private final ProgressRepository progressRepository;
    public boolean isUploadFile(MultipartFile file) {
        return Arrays.asList(new String[] {"application/pdf","application/x-zip-compressed","application/octet-stream"})
                .contains(file.getContentType().trim().toLowerCase());
    }
    @Override
    public void uploadFile(MultipartFile[] files, ProgressEntity progress) throws FileNotImageException {

        for (MultipartFile file : files)
        {
            if (!isUploadFile(file))
                throw new FileNotImageException("This file is not Image type");
            else {
                AttachmentEntity attachment = new AttachmentEntity();
                String url = imageStorageService.uploadFile(file, "Subject id:" + progress.getSubject().getId() + "/week" + progress.getWeek() + "/" + attachment.getId() + progress.getId());
                attachment.setProgress(progress);
                attachment.setLink(url);
                attachment=attachmentRepository.save(attachment);
                progress.getAttachments().add(attachment);
            }
        }
        progressRepository.save(progress);
    }

    @Override
    public List<AttachmentEntity> findAllBySubject(SubjectEntity subject) {
        List<AttachmentEntity> list = attachmentRepository.findAllByProgress_Subject(subject);
        return list;
    }
}
