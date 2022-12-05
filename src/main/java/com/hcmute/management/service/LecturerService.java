package com.hcmute.management.service;

import com.hcmute.management.common.LecturerSort;
import com.hcmute.management.common.OrderByEnum;
import com.hcmute.management.common.StudentSort;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.request.Lecturer.AddNewLecturerRequest;
import com.hcmute.management.model.payload.request.Lecturer.UpdateLecturerRequest;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;

@Component
@Service
public interface LecturerService {
    LecturerEntity saveLecturer(AddNewLecturerRequest addNewLecturerRequest, UserEntity user);
    LecturerEntity updateLecturer(UpdateLecturerRequest updateLecturerRequest,UserEntity user);
    List<LecturerEntity> getAllLecturer();
    LecturerEntity getLecturerById(String id);
    void deleteByListId(List<String> ListId);
    void deleteById(String id);
    Page<LecturerEntity> findAllLecturerPaging(int pageNo, int pagSize);
    public LecturerEntity findByUser(UserEntity user);
    Page<Object> searchByCriteria(String keyWord, int pageNo, int pageSize,String sort, String order);
    List<LecturerEntity> search(String keyword, OrderByEnum orderBy, LecturerSort order, int pageindex, int pagesize);
}
