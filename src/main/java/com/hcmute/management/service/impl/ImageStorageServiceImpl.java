package com.hcmute.management.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hcmute.management.service.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageStorageServiceImpl implements ImageStorageService {
    Map r;
    public Cloudinary cloudinary(){
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dzibsuyyj",
                "api_key", "348327194698571",
                "api_secret", "t0ofQuizaxeWSYTuIildkod7Ruc"));
        return cloudinary;
    }

    @Override
    public String saveAvatarImage(MultipartFile file, String name) {
        try {
            r = this.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type","auto","upload_preset","bang","public_id","avatarImage/"+name));
        } catch (IOException e) {
            throw new RuntimeException("Upload fail");
        }
        return (String) r.get("secure_url");
    }
    @Override
    public String uploadFile(MultipartFile file, String name) {
        try {
            switch(file.getContentType()) {
                case "application/pdf":r = this.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type","auto","upload_preset","bang","public_id","file/"+name)); break;
                case "application/x-zip-compressed":r = this.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type","auto","upload_preset","bang","public_id","file/"+name,"format","zip"));break;
                default:r = this.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type","auto","upload_preset","bang","public_id","file/"+name,"format","rar"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Upload fail");
        }
        return (String) r.get("secure_url");
    }
}
