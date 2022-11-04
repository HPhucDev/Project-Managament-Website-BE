package com.hcmute.management.service;

import com.hcmute.management.common.AppUserRole;
import com.hcmute.management.model.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Component
@Service
public interface UserService {
UserEntity register(UserEntity user, AppUserRole role);

UserEntity findByPhone(String phone);

    UserEntity findById(String uuid);
}
