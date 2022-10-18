package com.hcmute.management.service;

import com.hcmute.management.model.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Component
@Service
public interface UserService {
UserEntity register(UserEntity user,String role);

UserEntity findByPhone(String phone);

    UserEntity findById(String uuid);
}
