package com.hcmute.management.controller;

import com.hcmute.management.common.MajorEnum;
import com.hcmute.management.common.OrderByEnum;
import com.hcmute.management.common.SubjectSort;
import com.hcmute.management.common.SubjectStatus;
import com.hcmute.management.handler.AuthenticateHandler;
import com.hcmute.management.handler.MethodArgumentNotValidException;
import com.hcmute.management.mapping.SubjectMapping;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.SuccessResponse;
import com.hcmute.management.model.payload.request.Subject.AddNewSubjectRequest;
import com.hcmute.management.model.payload.request.Subject.DemoObject;
import com.hcmute.management.model.payload.request.Subject.UpdateSubjectRequest;
import com.hcmute.management.model.payload.response.ErrorResponse;
import com.hcmute.management.model.payload.response.PagingResponse;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.EmailService;
import com.hcmute.management.service.LecturerService;
import com.hcmute.management.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;


@ComponentScan
@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
public class SubjectController {
    @Autowired
    JwtUtils jwtUtils;
    private final SubjectService subjectService;
    private final AuthenticateHandler authenticateHandler;
    private final EmailService emailService;
    private final LecturerService lecturerService;
    public static String E400="Bad request";
    public static String E404="Not found";
    public static String E401="Unauthorize";
    @PostMapping(value = "",consumes = {"multipart/form-data"})
    @ApiOperation("Create")
    public ResponseEntity<Object> addSubject(@RequestPart MultipartFile file,
                                             @Valid AddNewSubjectRequest addNewSubjectRequest,
                                             BindingResult errors,
                                             HttpServletRequest httpServletRequest,
                                             @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                             @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) throws Exception
    {
        if (errors.hasErrors())
        {
            throw new MethodArgumentNotValidException(errors);
        }
        UserEntity user;
        try
        {
            user = authenticateHandler.authenticateUser(httpServletRequest);
            SubjectEntity subject= SubjectMapping.addSubjectToEntity(addNewSubjectRequest);
            subject.setStartDate(startDate);
            subject.setEndDate(endDate);
            if (subject.getEndDate().compareTo(subject.getStartDate())<=0)
            {
                return new ResponseEntity<>(new ErrorResponse(E400,"INVALID_START_END_DATE","Invalid start or end date"), HttpStatus.BAD_REQUEST);
            }
            LecturerEntity lecturer =lecturerService.getLecturerById(addNewSubjectRequest.getLecturerId());
            if (lecturer==null)
            {
                return new ResponseEntity<>(new ErrorResponse(E404,"LECTURE_NOT_FOUND","Can't find lecture with id provided"), HttpStatus.BAD_REQUEST);

            }
            else {
                subject.setLecturer(user.getLecturer());
                subject.setStatus(Integer.valueOf(SubjectStatus.PENDING.getStatus()));
                String uuid = String.valueOf(UUID.randomUUID());
                subject.setId(uuid);

                String url = subjectService.uploadSubjectFile(file,subject);
                subject.setAttachmentLink(url);
                subject=subjectService.saveSubject(subject);
                emailService.sendSubjectConfirmEmail(subject);
                return new ResponseEntity<>(subject,HttpStatus.OK);
            }
        }catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);

        }
    }
//    @GetMapping("/paging")
//    @ApiOperation("Get All")
//    public ResponseEntity<PagingResponse> getAllSubjectPaging(@RequestParam(defaultValue = "0") int page,
//                                                              @RequestParam(defaultValue = "5") int size)
//    {
//        Page<SubjectEntity> pageSubject = subjectService.findAllSubjectPaging(page,size);
//        List<SubjectEntity> listSubject = pageSubject.toList();
//        int totalElements = subjectService.getAllSubject().size();
//        PagingResponse pagingResponse = new PagingResponse();
//        Map<String,Object> map = new HashMap<>();
//        List<Object> Result = Arrays.asList(listSubject.toArray());
//        pagingResponse.setTotalPages(pageSubject.getTotalPages());
//        pagingResponse.setEmpty(listSubject.size()==0);
//        pagingResponse.setFirst(page==0);
//        pagingResponse.setLast(page == pageSubject.getTotalPages()-1);
//        pagingResponse.getPageable().put("pageNumber",page);
//        pagingResponse.getPageable().put("pageSize",size);
//        pagingResponse.setSize(size);
//        pagingResponse.setNumberOfElements(listSubject.size());
//        pagingResponse.setTotalElements((int) pageSubject.getTotalElements());
//        pagingResponse.setContent(Result);
//        return new ResponseEntity<>(pagingResponse ,HttpStatus.OK);
//    }
    @GetMapping("")
    @ApiOperation("Get All")
    public ResponseEntity<Object> getAll()
    {
        SuccessResponse response = new SuccessResponse();
        List<SubjectEntity> listSubject = subjectService.getAllSubject();
        Map<String,Object> map = new HashMap<>();
        map.put("content",listSubject);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
    @PatchMapping(value = "/{id}",consumes = {"multipart/form-data"})
    @ApiOperation("Update")
    public ResponseEntity<Object> updateSubject(@Valid UpdateSubjectRequest updateSubjectRequest,
                                                @RequestPart MultipartFile file,
                                                BindingResult errors,
                                                HttpServletRequest httpServletRequest,
                                                @RequestParam MajorEnum majorEnum,
                                                @PathVariable("id") String id,
                                                @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        UserEntity user;
        try
        {
            user=authenticateHandler.authenticateUser(httpServletRequest);
            SubjectEntity subject = subjectService.getSubjectById(id);
            if (subject==null)
            {
                return new ResponseEntity<>(new ErrorResponse(E404,"SUBJECT_NOT_FOUND","Can't find subject with id provided"), HttpStatus.NOT_FOUND);

            }
            subject=SubjectMapping.updateRequestToEntity(subject,updateSubjectRequest);
            subject.setStartDate(startDate);
            subject.setEndDate(endDate);
            if (subject.getEndDate().compareTo(subject.getStartDate())<=0)
            {
                return new ResponseEntity<>(new ErrorResponse(E400,"INVALID_START_END_DATE","Invalid start or end date"), HttpStatus.BAD_REQUEST);
            }
            String url = subjectService.uploadSubjectFile(file,subject);
            subject.setAttachmentLink(url);
            subject=subjectService.saveSubject(subject);
            return new ResponseEntity<>(subject,HttpStatus.OK);
        } catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/{id}")
    @ApiOperation("Get by id")
    public ResponseEntity<Object> getSubjectById(@PathVariable("id") String id)
    {
        SubjectEntity subject = subjectService.getSubjectById(id);
        SuccessResponse response = new SuccessResponse();
        if(subject==null)
        {
            return new ResponseEntity<>(new ErrorResponse(E404,"SUBJECT_NOT_FOUND","Can't find subject with id provided"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(subject,HttpStatus.OK);
    }
    @DeleteMapping("")
    @ApiOperation("Delete")
    public ResponseEntity<Object> deleteSubject(@RequestParam List<String> listSubjectId,HttpServletRequest httpServletRequest)
    {
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(httpServletRequest);
            subjectService.deleteById(listSubjectId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);

        }
    }
    @GetMapping("/search")
    @ApiOperation("Search by Criteria")
    public ResponseEntity<Object> searchByCriteria(
            @ApiParam(required = false,value = "Nhập thông tin bạn muốn tìm kiếm vào đây") String searchKey,
            @RequestParam(defaultValue = "0",name = "pageIndex") int page,
            @RequestParam(defaultValue = "5",name = "pageSize") int size,
            @ApiParam(defaultValue = "NULL",name = "subjectStatus",value = "Trạng thái của đề tài") SubjectStatus subjectStatus,
            @ApiParam(required = false,value = "Mã số của giảng viên") String lectureId,
            @ApiParam(defaultValue = "NULL",value = "Chuyên ngành") MajorEnum major,
            @ApiParam(defaultValue = "START_DATE",value = "Lọc theo cái gì nè?")SubjectSort sort,
            @ApiParam(defaultValue = "DESCENDING",value = "Lọc theo thứ tự gì nè?")OrderByEnum order
            )
    {
        System.out.println(subjectStatus);
        Page<SubjectEntity> pageSubject = subjectService.searchByCriteria(searchKey,subjectStatus.getStatus(),page,size,lectureId,major.getName(), sort.getName(), order.getName());
        List<SubjectEntity> listSubject = pageSubject.toList();
        PagingResponse pagingResponse = new PagingResponse();
        Map<String,Object> map = new HashMap<>();
        List<Object> Result = Arrays.asList(listSubject.toArray());
        pagingResponse.setTotalPages(pageSubject.getTotalPages());
        pagingResponse.setEmpty(listSubject.size()==0);
        pagingResponse.setFirst(page==0);
        pagingResponse.setLast(page == pageSubject.getTotalPages()-1);
        pagingResponse.getPageable().put("pageNumber",page);
        pagingResponse.getPageable().put("pageSize",size);
        pagingResponse.setSize(size);
        pagingResponse.setNumberOfElements(listSubject.size());
        pagingResponse.setTotalElements((int) pageSubject.getTotalElements());
        pagingResponse.setContent(Result);
        return new ResponseEntity<>(pagingResponse ,HttpStatus.OK);
    }
    @PostMapping("/approve/{subjectId}")
    @ApiOperation("Approve Subject")
    public ResponseEntity<Object> approveSubject(@PathVariable("subjectId") String id,HttpServletRequest req)
    {
        UserEntity user;
        try
        {
            user = authenticateHandler.authenticateUser(req);
            SubjectEntity subject = subjectService.getSubjectById(id);
            if (subject == null)
            {
                return new ResponseEntity<>(new ErrorResponse(E404,"SUBJECT_NOT_FOUND","Can't find subject with id provided"), HttpStatus.NOT_FOUND);
            }
            if (subject.getStatus()!=Integer.valueOf(SubjectStatus.PENDING.getStatus()))
            {
                return new ResponseEntity<>(new ErrorResponse(E400,"INVALID_SUBJECT_STATUS","Subject status is invalid"), HttpStatus.BAD_REQUEST);

            }
                subject.setStatus(Integer.valueOf(SubjectStatus.UNASSIGNED.getStatus()));
                subject = subjectService.saveSubject(subject);
                return new ResponseEntity<>(subject,HttpStatus.OK);
        } catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/deny/{subjectId}")
    @ApiOperation("Deny Subject")
    public ResponseEntity<Object> denySubject(@PathVariable("subjectId") String id,@RequestParam String description, HttpServletRequest req) throws Exception
    {
        UserEntity user;
        try
        {
            user = authenticateHandler.authenticateUser(req);
            SubjectEntity subject = subjectService.getSubjectById(id);
            if (subject == null)
            {
                return new ResponseEntity<>(new ErrorResponse(E404,"SUBJECT_NOT_FOUND","Can't find subject with id provided"), HttpStatus.NOT_FOUND);
            }
            if (subject.getStatus()!=Integer.valueOf(SubjectStatus.PENDING.getStatus()))
            {
                return new ResponseEntity<>(new ErrorResponse(E400,"INVALID_SUBJECT_STATUS","Subject status is invalid"), HttpStatus.BAD_REQUEST);

            }
            subject.setStatus(Integer.valueOf(SubjectStatus.NEED_CHECKED.getStatus()));
            subject = subjectService.saveSubject(subject);
            emailService.sendSubjectCheckedEmail(subject,description);
            return new ResponseEntity<>(subject,HttpStatus.OK);
        } catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }

//    @PostMapping(value = "/demo",consumes = {"multipart/form-data"})
//    public ResponseEntity<Object> test (@ModelAttribute("demo") DemoObject demoObject)
//    {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
