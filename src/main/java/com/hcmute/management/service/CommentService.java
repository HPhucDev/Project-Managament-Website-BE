package com.hcmute.management.service;

import com.hcmute.management.model.entity.CommentEntity;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.request.Comment.AddNewCommentRequest;
import com.hcmute.management.model.payload.request.Comment.UpdateCommentRequest;
import com.hcmute.management.model.payload.request.Progress.AddNewProgressRequest;
import com.hcmute.management.model.payload.request.Progress.UpdateProgressRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface CommentService {
    CommentEntity saveComment(AddNewCommentRequest addNewCommentRequest, UserEntity user);
    List<CommentEntity> findAllComment();
    List<CommentEntity> getCommentByProgressId(String id);
    CommentEntity findById(String id);
    CommentEntity updateComment(String id,UpdateCommentRequest updateCommentRequest,UserEntity user);
    Page<CommentEntity> findAllCommentPaging(int pageNo, int pagSize);
    void deleteById(String id);
}
