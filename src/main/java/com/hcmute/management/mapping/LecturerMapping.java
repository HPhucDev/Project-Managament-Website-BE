package com.hcmute.management.mapping;

import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.payload.request.Lecturer.AddNewLecturerRequest;
import com.hcmute.management.model.payload.request.Lecturer.UpdateLecturerRequest;

public class LecturerMapping {
    public static LecturerEntity updateRequestToEntity(LecturerEntity lecturer,UpdateLecturerRequest updateLecturerRequest){
        lecturer.setQualification(updateLecturerRequest.getQualification());
        lecturer.setPosition(updateLecturerRequest.getPosition());
        return lecturer;
    }

    public static LecturerEntity addLecturerToEntity(AddNewLecturerRequest addNewLecturerRequest){
        LecturerEntity lecturer =new LecturerEntity();
        lecturer.setId(addNewLecturerRequest.getId());
        lecturer.setQualification(addNewLecturerRequest.getQualification());
        lecturer.setPosition(addNewLecturerRequest.getPosition());
        return lecturer;
    }

}
