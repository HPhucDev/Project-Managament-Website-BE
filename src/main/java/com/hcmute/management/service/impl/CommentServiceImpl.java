package com.hcmute.management.service.impl;

import com.hcmute.management.model.entity.CommentEntity;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.request.Comment.AddNewCommentRequest;
import com.hcmute.management.model.payload.request.Comment.UpdateCommentRequest;
import com.hcmute.management.repository.CommentRepository;
import com.hcmute.management.service.CommentService;
import com.hcmute.management.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ProgressService progressService;

    @Override
    public CommentEntity saveComment(AddNewCommentRequest addNewCommentRequest, UserEntity user) {
        CommentEntity comment =new CommentEntity();
        comment.setMessage(addNewCommentRequest.getMessage());
        comment.setProgressComment(progressService.findById(addNewCommentRequest.getProgressid()));
        comment.setTime(LocalDateTime.now());
        comment.setUserComment(user);
        return commentRepository.save(comment);
    }

    @Override
    public List<CommentEntity> findAllComment() {
        List<CommentEntity> listComment =commentRepository.findAll();
        return  listComment;
    }

    @Override
    public CommentEntity findById(String id) {
        Optional<CommentEntity> comment = commentRepository.findById(id);
        if(comment.isEmpty())
            return null;
        return comment.get();

    }

    @Override
    public CommentEntity updateComment(UpdateCommentRequest updateCommentRequest,UserEntity user) {
        CommentEntity comment =findById(updateCommentRequest.getId());
        comment.setMessage(updateCommentRequest.getMessage());
        comment.setTime(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentEntity> getCommentByProgressId(int id) {
        ProgressEntity progress=progressService.findById(id);
        List<CommentEntity> listComment =commentRepository.findByProgressComment(progress);
        return listComment;
    }
    @Override
    public Page<CommentEntity> findAllCommentPaging(int pageNo, int pagSize) {
        Pageable paging =null;
        paging= PageRequest.of(pageNo,pagSize);
        Page<CommentEntity> pageResult =commentRepository.findAllComment(paging);
        return pageResult;
    }
}
