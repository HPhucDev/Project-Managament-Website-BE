package com.hcmute.management.service.impl;

import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.request.Lecturer.AddNewLecturerRequest;
import com.hcmute.management.model.payload.request.Lecturer.UpdateLecturerRequest;
import com.hcmute.management.repository.LecturerRepository;
import com.hcmute.management.repository.UserRepository;
import com.hcmute.management.service.LecturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class LecturerServiceImpl implements LecturerService {
    private final LecturerRepository lecturerRepository;
    private final UserRepository userRepository;
    @Override
    public LecturerEntity saveLecturer(AddNewLecturerRequest addNewLecturerRequest, UserEntity user) {
        LecturerEntity lecturer= new LecturerEntity();
        user.setFullName(addNewLecturerRequest.getFullName());
        user.setGender(addNewLecturerRequest.getGender());
        user.setEmail(addNewLecturerRequest.getEmail());
        lecturer.setId(addNewLecturerRequest.getId());
        lecturer.setUser(userRepository.save(user));
        lecturer.setQualification(addNewLecturerRequest.getQualification());
        lecturer.setPosition(addNewLecturerRequest.getPosition());
        return lecturerRepository.save(lecturer);
    }

    @Override
    public LecturerEntity updateLecturer(UpdateLecturerRequest updateLecturerRequest, UserEntity user) {
        LecturerEntity lecturer= findByUser(user);
        user.setFullName(updateLecturerRequest.getFullName());
        user.setGender(updateLecturerRequest.getGender());
        user.setEmail(updateLecturerRequest.getEmail());
        lecturer.setUser(userRepository.save(user));
        lecturer.setQualification(updateLecturerRequest.getQualification());
        lecturer.setPosition(updateLecturerRequest.getPosition());
        return lecturerRepository.save(lecturer);
    }
    @Override
    public LecturerEntity findByUser(UserEntity user) {
        LecturerEntity lecturer = lecturerRepository.findByUser(user);
        if(lecturer != null)
            return lecturer;
        else return null;
    }
    @Override
    public List<LecturerEntity> getAllLecturer() {
        List<LecturerEntity>listLecturer =lecturerRepository.findAll();
        return listLecturer;
    }

    @Override
    public LecturerEntity getLecturerById(String id) {
        Optional<LecturerEntity> lecturer =lecturerRepository.findById(id);
        if(lecturer.isEmpty())
            return null;
        return  lecturer.get();
    }

    @Override
    public void deleteByListId(List<String> ListId) {
        for(String id:ListId){
            lecturerRepository.deleteById(id);
        }
    }

    @Override
    public void deleteById(String id) {
        lecturerRepository.deleteById(id);
    }

    @Override
    public Page<LecturerEntity> findAllLecturerPaging(int pageNo, int pagSize) {
        Pageable paging =null;
        paging= PageRequest.of(pageNo,pagSize);
        Page<LecturerEntity> pageResult =lecturerRepository.findAllLecturer(paging);
        return pageResult;
    }
    @Override
    public Page<Object> searchByCriteria(String keyWord, int pageNo, int pageSize, String order) {
        Pageable paging = PageRequest.of(pageNo,pageSize);
        Page<Object> pageResult = lecturerRepository.searchByCriteria(keyWord,paging);
        return pageResult;
    }
}

