package com.hcmute.management.mapping;

import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.request.Authenticate.AddNewUserRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapping {
    public static UserEntity registerToEntity(AddNewUserRequest registerRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return new UserEntity( registerRequest.getPassword(),registerRequest.getPhone());
    }
}
