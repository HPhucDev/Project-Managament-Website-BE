package com.hcmute.management.service.impl;

import com.hcmute.management.common.AppUserRole;
import com.hcmute.management.mapping.StudentMapping;
import com.hcmute.management.model.entity.*;
import com.hcmute.management.model.payload.request.Student.AddNewStudentRequest;
import com.hcmute.management.model.payload.request.Student.ChangeInfoStudentRequest;
import com.hcmute.management.repository.ClassRepository;
import com.hcmute.management.repository.RoleRepository;
import com.hcmute.management.repository.StudentRepository;
import com.hcmute.management.repository.UserRepository;
import com.hcmute.management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    @Autowired
    final StudentRepository studentRepository;
    @Autowired
    final UserRepository userRepository;
    @Autowired
    final RoleRepository roleRepository;
    @Autowired
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
        StudentEntity student = studentRepository.findByUser(user);
        if(student != null)
            return student;
        else return null;
    }

    @Override
    public StudentEntity saveStudent(AddNewStudentRequest addNewStudentRequest) {
        UserEntity user = new UserEntity();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        RoleEntity roleEntity = roleRepository.findByName(AppUserRole.ROLE_STUDENT);
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleEntity);
        user.setRoles(roles);
        user.setFullName(addNewStudentRequest.getFullname());
        user.setGender(addNewStudentRequest.getSex());
        user.setPhone(addNewStudentRequest.getPhone());
        user.setPassword(passwordEncoder.encode(addNewStudentRequest.getMssv()));
        StudentEntity student = StudentMapping.addStudentToEntity(addNewStudentRequest);
        student.setUser(userRepository.save(user));
        ClassEntity classEntity = classRepository.findById(addNewStudentRequest.getClassid()).get();
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
        user.setFullName(changeInfoStudentRequest.getFullname());
        user.setGender(changeInfoStudentRequest.getSex());
        student.setUser(userRepository.save(user));
        student.setMajor(changeInfoStudentRequest.getMajor());
        student.setEducation_program(changeInfoStudentRequest.getEducationprogram());
        student.setSchool_year(changeInfoStudentRequest.getSchoolyear());
        ClassEntity classEntity = classRepository.findById(changeInfoStudentRequest.getClassid()).get();
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
    public Page<StudentEntity> search(int pageNo, int pagSize) {
        Pageable paging = PageRequest.of(pageNo,pagSize);
        Page<StudentEntity> pageResult =studentRepository.findAllStudentPaging(paging);
        return pageResult;
    }
}
