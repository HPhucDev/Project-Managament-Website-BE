package com.hcmute.management.service;

import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.request.Lecturer.AddNewLecturerRequest;
import com.hcmute.management.model.payload.request.Lecturer.UpdateLecturerRequest;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;

@Component
@Service
public interface LecturerService {
    LecturerEntity saveLecturer(AddNewLecturerRequest addNewLecturerRequest, UserEntity user);
    LecturerEntity updateLecturer(UpdateLecturerRequest updateLecturerRequest,UserEntity user,String id);
    List<LecturerEntity> getAllLecturer();
    LecturerEntity getLecturerById(String id);
    void deleteById(List<String> ListId);
    List <LecturerEntity>findAllLecturerPaging(int pageNo,int pagSize);
    public LecturerEntity findByUserId(UserEntity user);
}
