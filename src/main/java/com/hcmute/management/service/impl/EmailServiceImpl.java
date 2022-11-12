package com.hcmute.management.service.impl;


import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Override
    public void sendSimpleMessage(String to, String subject, String text)  {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@travel.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendSubjectConfirmEmail(SubjectEntity subjects) throws MessagingException, UnsupportedEncodingException {
        String toAddress ="fithcmute@gmail.com";
        String senderName="UTEMangement";
        String subject = "Please confirm Subject add request";
        String content = "New subject request by [[name]]([[email]])<br>"
                + "Subject Name:[[subjectName]]<br>"
                + "Thank you,<br>"
                + "UTEManagement.";
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true, CharEncoding.UTF_8);
        helper.setFrom("dilawabms900@gmail.com",senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        String subjectName=subjects.getName();
        String email=subjects.getLecturer().getUser().getEmail();
        String name = subjects.getLecturer().getUser().getFullName();
        content = content.replace("[[name]]", name);
        content = content.replace("[[email]]", email);
        content = content.replace("[[subjectName]]", subjectName);
        helper.setText(content,true);
        emailSender.send(message);
    }

}
