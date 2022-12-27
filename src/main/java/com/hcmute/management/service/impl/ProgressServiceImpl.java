package com.hcmute.management.service.impl;

import com.hcmute.management.mapping.ProgressMapping;
import com.hcmute.management.model.entity.AttachmentEntity;
import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.payload.request.Progress.AddNewProgressRequest;
import com.hcmute.management.model.payload.request.Progress.UpdateProgressRequest;
import com.hcmute.management.repository.ProgressRepository;
import com.hcmute.management.repository.StudentRepository;
import com.hcmute.management.repository.SubjectRepository;
import com.hcmute.management.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    final StudentRepository studentRepository;

    final SubjectRepository subjectRepository;

    final ProgressRepository progressRepository;

    @Override
    public ProgressEntity saveProgress(AddNewProgressRequest progressRequest) {
        Optional<StudentEntity> student = studentRepository.findById(progressRequest.getStudentId());
        Optional<SubjectEntity> subject = subjectRepository.findById(progressRequest.getSubjectId());
        if(student.isEmpty()) {
            throw new RuntimeException("STUDENT_NOT_FOUND");
        }
        if(subject.isEmpty()) {
            throw  new RuntimeException("SUBJECT_NOT_FOUND");
        }

        ProgressEntity progress = ProgressMapping.addProgressToEntity(progressRequest);
        progress.setAttachments(new HashSet<>());
        progress.setStudent(student.get());
        progress.setSubject(subject.get());
        return progressRepository.save(progress);

    }

    @Override
    public List<ProgressEntity> findAllProgress() {
        List<ProgressEntity> progressEntities = progressRepository.findAll();
        return progressEntities;
    }

    @Override
    public ProgressEntity findById(String id) {
        Optional<ProgressEntity> progress = progressRepository.findById(id);
        if (progress.isEmpty())
            return null;
        return progress.get();
    }

    @Override
    public ProgressEntity updateProgress(UpdateProgressRequest updateProgressRequest, String id) {
        StudentEntity student = studentRepository.findById(updateProgressRequest.getStudentId()).get();
        SubjectEntity subject = subjectRepository.findById(updateProgressRequest.getSubjectId()).get();
        ProgressEntity progress = ProgressMapping.updateProgressToEntity(updateProgressRequest);
        progress.setStudent(student);
        progress.setSubject(subject);
        progress.setId(id);
        return progressRepository.save(progress);
    }

    @Override
    public void deleteById(List<String> listid) {
        for (String id : listid) {
            progressRepository.deleteById(id);
        }
    }

    @Override
    public int getPercent(String subjectId) {
        int count = progressRepository.getPercent(subjectId);
        return count;
    }

}
