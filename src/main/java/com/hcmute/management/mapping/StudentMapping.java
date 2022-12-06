package com.hcmute.management.mapping;

import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.payload.request.Student.AddNewStudentRequest;

public class StudentMapping {
    public static StudentEntity addStudentToEntity(AddNewStudentRequest addNewStudentRequest) {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setSchoolYear(addNewStudentRequest.getSchoolYear());
        studentEntity.setEducationProgram(addNewStudentRequest.getEducationProgram());
        studentEntity.setMajor(addNewStudentRequest.getMajor());
        studentEntity.setId(addNewStudentRequest.getStudentId());
        return studentEntity;
    }
}
