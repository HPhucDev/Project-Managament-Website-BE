package com.hcmute.management.service;

import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
@Component
public interface EmailService {
    void sendSimpleMessage(String to,String subject,String text);
    void sendSubjectConfirmEmail(SubjectEntity subject)throws MessagingException, UnsupportedEncodingException;

    void sendSubjectCheckedEmail(SubjectEntity subject,String description)throws MessagingException,UnsupportedEncodingException;
}
