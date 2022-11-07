package com.hcmute.management.controller;

import com.hcmute.management.common.AppUserRole;
import com.hcmute.management.handler.AuthenticateHandler;
import com.hcmute.management.handler.MethodArgumentNotValidException;
import com.hcmute.management.model.entity.CommentEntity;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.request.Comment.AddNewCommentRequest;
import com.hcmute.management.model.payload.request.Comment.UpdateCommentRequest;
import com.hcmute.management.model.payload.request.Lecturer.AddNewLecturerRequest;
import com.hcmute.management.model.payload.request.Lecturer.UpdateLecturerRequest;
import com.hcmute.management.model.payload.response.ErrorResponse;
import com.hcmute.management.model.payload.response.PagingResponse;
import com.hcmute.management.repository.CommentRepository;
import com.hcmute.management.repository.LecturerRepository;
import com.hcmute.management.service.CommentService;
import com.hcmute.management.service.LecturerService;
import com.hcmute.management.service.ProgressService;
import com.hcmute.management.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ComponentScan
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final ProgressService progressService;
    final CommentRepository commentRepository;
    static String E401="Unauthorized";
    static String E404="Not Found";
    static String E400="Bad Request";
    @Autowired
    AuthenticateHandler authenticateHandler;
    @PostMapping("")
    @ApiOperation("Add")
    public ResponseEntity<Object> addComment(@RequestBody @Valid AddNewCommentRequest addNewCommentRequest, BindingResult errors, HttpServletRequest req) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(req);


            ProgressEntity findProgress =progressService.findById(addNewCommentRequest.getProgressid());
            if(findProgress!=null){
                return new ResponseEntity<>(new ErrorResponse(E400,"PROGRESS_NOT_FOUND","Can't find Progress with id provided"),HttpStatus.BAD_REQUEST);
            }
            CommentEntity comment =commentService.saveComment(addNewCommentRequest,user);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }  catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }
    @PatchMapping("")
    @ApiOperation("Update")
    public ResponseEntity<Object> updateComment(@Valid @RequestBody UpdateCommentRequest updateCommentRequest, BindingResult errors, HttpServletRequest req) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(req);
            CommentEntity findComment = commentService.findById(updateCommentRequest.getId());
            if (findComment == null) {
                return new ResponseEntity<>(new ErrorResponse(E404, "COMMENT_NOT_FOUND", "Can't find Comment with id provided"), HttpStatus.NOT_FOUND);

            }
            if (findComment.getUserComment() != user) {
                return new ResponseEntity<>(new ErrorResponse(E400, "INVALID_COMMENT_OWNER", "This is not your Comment"), HttpStatus.BAD_REQUEST);
            }
            CommentEntity updateComment = commentService.updateComment(updateCommentRequest, user);
            return new ResponseEntity<>(updateComment, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401, "UNAUTHORIZED", "Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }
    @DeleteMapping("/{id}")
    @ApiOperation("Delete by id")
    public ResponseEntity<Object>deleteComment(@PathVariable("id") String id,HttpServletRequest req){
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(req);
            CommentEntity findComment =commentService.findById(id);
            if(findComment==null)
            {
                return new ResponseEntity<>(new ErrorResponse(E404, "COMMENT_NOT_FOUND", "Can't find Comment with id provided"), HttpStatus.NOT_FOUND);
            }
            if (findComment.getUserComment() != user) {
                return new ResponseEntity<>(new ErrorResponse(E400, "INVALID_COMMENT_OWNER", "This is not your Comment"), HttpStatus.BAD_REQUEST);
            }
            commentService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/{id}")
    @ApiOperation("Get by Id")
    public ResponseEntity<Object>getCommentById(@PathVariable("id")String id){
        CommentEntity findComment =commentService.findById(id);
        if(findComment==null)
        {
            return new ResponseEntity<>(new ErrorResponse(E404, "COMMENT_NOT_FOUND", "Can't find Comment with id provided"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(findComment,HttpStatus.OK);
    }
    @GetMapping("")
    @ApiOperation("Get all")
    public ResponseEntity<Object> getALLComment() {
        List<CommentEntity> listComment = commentService.findAllComment();
        Map<String,Object> map = new HashMap<>();
        map.put("content",listComment);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
    @GetMapping("/getByProgress/{stayId}")
    @ApiOperation("Get by Progress Id")
    public ResponseEntity<Object> getCommentByProgressId(@PathVariable("ProgressId")int id) {
        ProgressEntity foundProgress=progressService.findById(id);
        if(foundProgress==null){
            return new ResponseEntity<>(new ErrorResponse(E404,"PROGRESS_NOT_FOUND","Can't find Progress with id provided"),HttpStatus.NOT_FOUND);
        }
        List<CommentEntity> listComment = commentService.getCommentByProgressId(id);
        Map<String,Object> map = new HashMap<>();
        map.put("content",listComment);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
    @GetMapping("/paging")
    @ApiOperation("Get All")
    public ResponseEntity<PagingResponse> getAllCommentPaging(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "5") int size)
    {
        Page<CommentEntity> pageComment = commentService.findAllCommentPaging(page,size);
        List<CommentEntity> listComment = pageComment.toList();
        int totalElements = commentService.findAllComment().size();
        PagingResponse pagingResponse = new PagingResponse();
        Map<String,Object> map = new HashMap<>();
        List<Object> Result = Arrays.asList(listComment.toArray());
        pagingResponse.setTotalPages(pageComment.getTotalPages());
        pagingResponse.setEmpty(listComment.size()==0);
        pagingResponse.setFirst(page==0);
        pagingResponse.setLast(page == pageComment.getTotalPages()-1);
        pagingResponse.getPageable().put("pageNumber",page);
        pagingResponse.getPageable().put("pageSize",size);
        pagingResponse.setSize(size);
        pagingResponse.setNumberOfElements(listComment.size());
        pagingResponse.setTotalElements((int) pageComment.getTotalElements());
        pagingResponse.setContent(Result);
        return new ResponseEntity<>(pagingResponse ,HttpStatus.OK);
    }

}
