package com.hcmute.management.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Component
@Service
public interface ImageStorageService {
String saveAvatarImage(MultipartFile file,String name);
String uploadFile(MultipartFile file,String name);
}
