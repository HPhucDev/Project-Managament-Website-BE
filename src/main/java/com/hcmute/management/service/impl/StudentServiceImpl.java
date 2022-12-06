package com.hcmute.management.service.impl;

import com.hcmute.management.common.AppUserRole;
import com.hcmute.management.common.OrderByEnum;
import com.hcmute.management.common.StudentSort;
import com.hcmute.management.handler.ValueDuplicateException;
import com.hcmute.management.model.entity.*;
import com.hcmute.management.model.payload.request.Student.AddNewStudentRequest;
import com.hcmute.management.model.payload.request.Student.ChangeInfoStudentRequest;
import com.hcmute.management.model.payload.response.PagingResponse;
import com.hcmute.management.repository.ClassRepository;
import com.hcmute.management.repository.RoleRepository;
import com.hcmute.management.repository.StudentRepository;
import com.hcmute.management.repository.UserRepository;
import com.hcmute.management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    final StudentRepository studentRepository;
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final ClassRepository classRepository;
    @Override
    public List<StudentEntity> findAllStudent() {
        List<StudentEntity> studentEntityList = studentRepository.findAll();
        return studentEntityList;
    }

    @Override
    public StudentEntity findById(String id) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(id);
        if(studentEntity.isEmpty())
            return null;
        return studentEntity.get();
    }

    @Override
    public StudentEntity findByUserId(UserEntity user) {
        Optional<StudentEntity> student = studentRepository.findByUser(user);
        if (student.isEmpty())
            return null;
        return student.get();
    }



    @Override
    public StudentEntity saveStudent(AddNewStudentRequest addNewStudentRequest, UserEntity user) {
        StudentEntity student = new StudentEntity();
        user.setFullName(addNewStudentRequest.getFullName());
        user.setGender(addNewStudentRequest.getGender());
        Optional<UserEntity> foundUser = userRepository.findByEmail(addNewStudentRequest.getEmail());
        if (!userRepository.findByEmail(addNewStudentRequest.getEmail()).isEmpty())
            throw new ValueDuplicateException("This email has already existed");
        user.setBirthDay(addNewStudentRequest.getBirthday());
        user.setEmail(addNewStudentRequest.getEmail());
        student.setId(addNewStudentRequest.getStudentId());
        student.setUser(userRepository.save(user));
        student.setMajor(addNewStudentRequest.getMajor().getName());
        student.setEducationProgram(addNewStudentRequest.getEducationProgram());
        student.setSchoolYear(addNewStudentRequest.getSchoolYear());
        ClassEntity classEntity = classRepository.findById(addNewStudentRequest.getClassId()).get();
        student.setClasses(classEntity);
         return studentRepository.save(student);
   }

    @Override
    public void deleteStudent(String id) {
        StudentEntity student = studentRepository.findById(id).get();
        UserEntity user = userRepository.findById(student.getUser().getId()).get();
        studentRepository.deleteById(id);
    }

    @Override
    public StudentEntity changeInf(ChangeInfoStudentRequest changeInfoStudentRequest, String userId) {
        StudentEntity student = studentRepository.findByUserId(userId);
        UserEntity user = userRepository.findById(userId).get();
        if(student == null)
        {
           return null;
        }
        user.setFullName(changeInfoStudentRequest.getFullName());
        user.setGender(changeInfoStudentRequest.getGender());
        student.setUser(userRepository.save(user));
        student.setMajor(changeInfoStudentRequest.getMajor().getName());
        student.setEducationProgram(changeInfoStudentRequest.getEducationProgram());
        student.setSchoolYear(changeInfoStudentRequest.getSchoolYear());
        Optional<ClassEntity> classEntity = classRepository.findById(changeInfoStudentRequest.getClassId());
        if(classEntity.isEmpty())
        {
            throw new RuntimeException("Error: Lớp học không tồn tại");
        }
        student.setClasses(classEntity.get());
        return studentRepository.save(student);
    }

    @Override
    public StudentEntity updateStudent(ChangeInfoStudentRequest changeInfoStudentRequest, UserEntity user) {
        StudentEntity student = findByUserId(user);
        if(student == null)
        {
            return null;
        }
        user.setFullName(changeInfoStudentRequest.getFullName());
        user.setGender(changeInfoStudentRequest.getGender());
        if (!userRepository.findByEmail(changeInfoStudentRequest.getEmail()).isEmpty() && user.getEmail()!= changeInfoStudentRequest.getEmail())
            throw new ValueDuplicateException("This email has already existed");
        user.setEmail(changeInfoStudentRequest.getEmail());
        user.setBirthDay(changeInfoStudentRequest.getBirthday());
        student.setUser(userRepository.save(user));
        student.setMajor(changeInfoStudentRequest.getMajor().getName());
        student.setEducationProgram(changeInfoStudentRequest.getEducationProgram());
        student.setSchoolYear(changeInfoStudentRequest.getSchoolYear());
        ClassEntity classEntity = classRepository.findById(changeInfoStudentRequest.getClassId()).get();
        if(classEntity == null)
        {
            throw new RuntimeException("Error: Lớp học không tồn tại");
        }
        student.setClasses(classEntity);
        return studentRepository.save(student);
    }

    @Override
    public StudentEntity findStudentbyUserId(String userid) {
        StudentEntity student = studentRepository.findByUserId(userid);
        if(student != null)
            return student;
        else return null;
    }

    @Override
    public PagingResponse search(String keyword, OrderByEnum orderBy, StudentSort order, int pageindex, int pagesize) {
        PagingResponse list = studentRepository.search(keyword,orderBy,order,pageindex,pagesize);
       return list;
    }

    @Override
    public void deleteByListId(List<String> listStudentId) {

    }

}
