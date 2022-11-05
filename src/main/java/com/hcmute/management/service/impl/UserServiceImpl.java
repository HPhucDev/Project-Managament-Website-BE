package com.hcmute.management.service.impl;

import com.hcmute.management.common.AppUserRole;
import com.hcmute.management.model.entity.RoleEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.repository.RoleRepository;
import com.hcmute.management.repository.UserRepository;
import com.hcmute.management.service.UserService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.description.NamedElement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserEntity register(UserEntity user, AppUserRole role) {
        RoleEntity roleEntity = roleRepository.findByName(role);
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleEntity);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public UserEntity findByPhone(String phone) {
        Optional<UserEntity> user = userRepository.findByPhone(phone);
        if (user.isEmpty())
            return null;
        return user.get();
    }

    @Override
    public UserEntity findById(String uuid) {
        Optional<UserEntity> user = userRepository.findById(uuid);
        if(user.isEmpty())
            return null;
        return user.get();
    }
}
