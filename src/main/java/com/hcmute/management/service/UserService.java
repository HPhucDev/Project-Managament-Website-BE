package com.hcmute.management.service;

import com.hcmute.management.common.AppUserRole;
import com.hcmute.management.handler.FileNotImageException;
import com.hcmute.management.model.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Component
@Service
public interface UserService {
UserEntity register(UserEntity user, AppUserRole role);

UserEntity findByUserName(String userName);

    UserEntity findById(String uuid);
    void delete(UserEntity user);
UserEntity addUserImage(MultipartFile file,UserEntity user) throws FileNotImageException;
String uploadFile(MultipartFile file,String name);
}
