package com.hcmute.management.service.impl;

import com.hcmute.management.handler.FileNotImageException;
import com.hcmute.management.model.entity.RoleEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.repository.RoleRepository;
import com.hcmute.management.repository.UserRepository;
import com.hcmute.management.service.ImageStorageService;
import com.hcmute.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ImageStorageService imageStorageService;

    @Override
    public UserEntity register(UserEntity user, String role) {
        Optional<RoleEntity> roleEntity=roleRepository.findByName(role);
        if (roleEntity.isEmpty())
            return null;
        else {
            if(user.getRoles()==null){
            Set<RoleEntity> RoleSet=new HashSet<>();
            RoleSet.add(roleEntity.get());
            user.setRoles(RoleSet);
        }
            else
                user.getRoles().add(roleEntity.get());

        }
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

    @Override
    public UserEntity addUserImage(MultipartFile file, UserEntity user) throws FileNotImageException {
        if (file.isEmpty())
        {
            return  null;
        }
        if (!isImageFile(file))
        {
            throw  new FileNotImageException("This file is not Image type");
        }
        else
        {
            String uuid = String.valueOf(UUID.randomUUID());
            String url = imageStorageService.saveAvatarImage(file, user.getId()+ "/img" + uuid);
            user.setImgLink(url);
            user =userRepository.save(user);
            return user;
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String name) {
        String uuid = String.valueOf(UUID.randomUUID());
        String url = imageStorageService.uploadFile(file,"demo" + "/img" + name+ uuid);
        return url;
    }

    public boolean isImageFile(MultipartFile file) {
        return Arrays.asList(new String[] {"image/png","image/jpg","image/jpeg", "image/bmp"})
                .contains(file.getContentType().trim().toLowerCase());
    }
    public boolean isUploadFile(MultipartFile file) {
        return Arrays.asList(new String[] {"application/pdf","application/x-zip-compressed","application/octet-stream"})
                .contains(file.getContentType().trim().toLowerCase());
    }
}
