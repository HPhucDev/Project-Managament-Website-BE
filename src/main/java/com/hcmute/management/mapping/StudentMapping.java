package com.hcmute.management.mapping;

import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.payload.request.Student.AddNewStudentRequest;

public class StudentMapping {
    public static StudentEntity addStudentToEntity(AddNewStudentRequest addNewStudentRequest) {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setSchool_year(addNewStudentRequest.getSchoolyear());
        studentEntity.setEducation_program(addNewStudentRequest.getEducationprogram());
        studentEntity.setMajor(addNewStudentRequest.getMajor());
        return studentEntity;
    }
}
