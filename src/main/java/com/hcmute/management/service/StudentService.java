package com.hcmute.management.service;

import com.hcmute.management.common.OrderByEnum;
import com.hcmute.management.common.StudentSort;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.request.Student.AddNewStudentRequest;
import com.hcmute.management.model.payload.request.Student.ChangeInfoStudentRequest;
import com.hcmute.management.model.payload.response.PagingResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@Service
public interface StudentService {
    List<StudentEntity> findAllStudent();
    StudentEntity findById(String id);

    StudentEntity findByUserId(UserEntity user);
    StudentEntity saveStudent(AddNewStudentRequest addNewStudentRequest, UserEntity user);
    void deleteStudent(String id);
    StudentEntity changeInf(ChangeInfoStudentRequest changeInfoStudentRequest, String user);
    StudentEntity updateStudent(ChangeInfoStudentRequest changeInfoStudentRequest, UserEntity user);

    StudentEntity findStudentbyUserId(String userid);
    PagingResponse search(String keyword, OrderByEnum orderBy, StudentSort order, int pageindex, int pagesize);
}
