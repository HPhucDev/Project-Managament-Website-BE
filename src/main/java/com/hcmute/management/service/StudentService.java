package com.hcmute.management.service;

import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.request.Student.AddNewStudentRequest;
import com.hcmute.management.model.payload.request.Student.ChangeInfoStudentRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface StudentService {
    List<StudentEntity> findAllStudent();
    StudentEntity findById(String id);
    StudentEntity findByUserId(UserEntity user);
    StudentEntity saveStudent(AddNewStudentRequest addNewStudentRequest);
    void deleteStudent(String id);
    StudentEntity changeInf(ChangeInfoStudentRequest changeInfoStudentRequest, String user);

    StudentEntity findStudentbyUserId(String userid);
}
