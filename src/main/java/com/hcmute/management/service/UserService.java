package com.hcmute.management.service;

import com.hcmute.management.handler.FileNotImageException;
import com.hcmute.management.model.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Component
@Service
public interface UserService {
UserEntity register(UserEntity user,String role);

UserEntity findByPhone(String phone);

    UserEntity findById(String uuid);
UserEntity addUserImage(MultipartFile file,UserEntity user) throws FileNotImageException;
String uploadFile(MultipartFile file,String name);
}
