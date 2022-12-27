package com.hcmute.management.controller;

import com.hcmute.management.handler.AuthenticateHandler;
import com.hcmute.management.handler.FileNotImageException;
import com.hcmute.management.handler.MethodArgumentNotValidException;
import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.SuccessResponse;
import com.hcmute.management.model.payload.request.Progress.AddNewProgressRequest;
import com.hcmute.management.model.payload.request.Progress.UpdateProgressRequest;
import com.hcmute.management.model.payload.response.ErrorResponse;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.AttachmentService;
import com.hcmute.management.service.ProgressService;
import com.hcmute.management.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@ComponentScan
@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;
    private final AttachmentService attachmentService;
    private final AuthenticateHandler authenticateHandler;
    private final SubjectService subjectService;
    @Autowired
    JwtUtils jwtUtils;

    public static String E400 = "Bad request";
    public static String E404 = "Not found";
    public static String E401 = "Unauthorize";

    @PostMapping(value = "",consumes = {"multipart/form-data"})
    @ApiOperation("Create")
    public ResponseEntity<Object> addProgress(@Valid AddNewProgressRequest addNewProgressRequest, @RequestPart MultipartFile[] files, BindingResult errors, HttpServletRequest httpServletRequest) throws Exception {
       UserEntity user;
       try {
           user = authenticateHandler.authenticateUser(httpServletRequest);
           SubjectEntity subject = subjectService.getSubjectById(addNewProgressRequest.getSubjectId());
           if (user==null || subject==null) {
               return new ResponseEntity<>(new ErrorResponse(E400,"INVALID_REQUEST","Your request is invalid, please check and try again"), HttpStatus.BAD_REQUEST);
           }
           if (user.getSubjectLeader()!=subject && user.getSubject()!=subject)
           {
               return new ResponseEntity<>(new ErrorResponse(E400,"INVALID_USER","You are not assign to this subject"), HttpStatus.BAD_REQUEST);
           }
           if (progressService.findBySubjectAndWeek(subject,addNewProgressRequest.getWeek())!=null)
           {
               return new ResponseEntity<>(new ErrorResponse(E400,"INVALID_WEEK","This week have progress"),HttpStatus.BAD_REQUEST);
           }
           try {
                     ProgressEntity progress = progressService.saveProgress(user,addNewProgressRequest);
                     attachmentService.uploadFile(files, progress);
                     return new ResponseEntity<>(progress, HttpStatus.OK);
                 }catch (FileNotImageException fileNotImageException)
                 {
                     return new ResponseEntity<>(new ErrorResponse("Unsupported Media Type","FILE_TYPE_NOT_VALID",fileNotImageException.getMessage()),HttpStatus.UNSUPPORTED_MEDIA_TYPE);
                 }
            } catch (BadCredentialsException e)
       {
           return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
       }
    }

    @GetMapping("")
    @ApiOperation("Get all")
    public ResponseEntity<Object> getAllProgress() {
        List<ProgressEntity> listProgress = progressService.findAllProgress();

            Map<String, Object> map = new HashMap<>();
            map.put("Content", listProgress);
            return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get by id")
    public ResponseEntity<Object> getProgressById(@PathVariable("id") String id) {
        ProgressEntity progress = progressService.findById(id);
        if (progress == null) {
            return new ResponseEntity<>(new ErrorResponse(E404,"PROGRESS_NOT_FOUND", "Progress not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(progress, HttpStatus.OK);
    }

    @PatchMapping("/{progressId}")
    @ApiOperation("Update")
    @ResponseBody
    public ResponseEntity<Object> updateProgress(HttpServletRequest req, @RequestBody @Valid UpdateProgressRequest updateProgressRequest, @PathVariable("progressId") String id) {
        UserEntity user;
        try
        {
            user=authenticateHandler.authenticateUser(req);
            ProgressEntity progress = new ProgressEntity();
            if (progressService.findById(id) != null) {
                progress = progressService.updateProgress(updateProgressRequest, id);
                return new ResponseEntity<>(progress, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorResponse(E404, "PROGRESS_NOT_FOUND","Progress not found"), HttpStatus.NOT_FOUND);
            }
        }
        catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }

    }

    @DeleteMapping("")
    @ApiOperation("Delete")
    public ResponseEntity<Object> deleteProgress(@RequestParam List<String> listProgressId, HttpServletRequest httpServletRequest) {
        UserEntity user;
        try
        {
            user=authenticateHandler.authenticateUser(httpServletRequest);
            progressService.deleteById(listProgressId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/getPercent/{subjectId}")
    @ApiOperation("Get percent progress by subject id")
    public ResponseEntity<Object> getPercentbySubjectId(@PathVariable("subjectId") String subjectId) {
        if(subjectService.getSubjectById(subjectId)== null) {
            return new ResponseEntity<>(new ErrorResponse(E404, "SUBJECT_NOT_FOUND","Subject not found"),HttpStatus.NOT_FOUND);
        }
        else {
            int count = progressService.getPercent(subjectId);
            float percent = (float) count / 15 * 100;
            Map<String, Object> map = new HashMap();
            map.put("Percent", percent);
            map.put("Total",count + "/" + "15");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

}
