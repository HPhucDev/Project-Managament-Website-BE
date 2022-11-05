package com.hcmute.management.controller;

import com.hcmute.management.handler.AuthenticateHandler;
import com.hcmute.management.handler.MethodArgumentNotValidException;
import com.hcmute.management.mapping.SubjectMapping;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.SuccessResponse;
import com.hcmute.management.model.payload.request.Subject.AddNewSubjectRequest;
import com.hcmute.management.model.payload.request.Subject.UpdateSubjectRequest;
import com.hcmute.management.model.payload.response.ErrorResponse;
import com.hcmute.management.model.payload.response.PagingResponse;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.SubjectService;
import io.swagger.annotations.Api;
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

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@ComponentScan
@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;
    private final AuthenticateHandler authenticateHandler;
    public static String E400="Bad request";
    public static String E404="Not found";
    public static String E401="Unauthorize";
    @Autowired
    JwtUtils jwtUtils;
    @PostMapping("")
    @ApiOperation("Create")
    public ResponseEntity<Object> addSubject(@RequestBody @Valid AddNewSubjectRequest addNewSubjectRequest, BindingResult errors, HttpServletRequest httpServletRequest) throws Exception
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
            if (subject.getEndDate().compareTo(subject.getStartDate())<=0)
            {
                return new ResponseEntity<>(new ErrorResponse(E400,"INVALID_START_END_DATE","Invalid start or end date"), HttpStatus.BAD_REQUEST);
            }
            else {
                subject = subjectService.saveSubject(subject);
                return new ResponseEntity<>(subject,HttpStatus.OK);
            }

        }catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);

        }
    }
    @GetMapping("/paging")
    @ApiOperation("Get All")
    public ResponseEntity<PagingResponse> getAllSubjectPaging(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size)
    {
        Page<SubjectEntity> pageSubject = subjectService.findAllSubjectPaging(page,size);
        List<SubjectEntity> listSubject = pageSubject.toList();
        int totalElements = subjectService.getAllSubject().size();
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
    @PatchMapping("/{id}")
    @ApiOperation("Update")
    public ResponseEntity<Object> updateSubject(@Valid @RequestBody UpdateSubjectRequest updateSubjectRequest,BindingResult errors,HttpServletRequest httpServletRequest,@PathVariable("id") String id) throws Exception {
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
            if (subject.getEndDate().compareTo(subject.getStartDate())<=0)
            {
                return new ResponseEntity<>(new ErrorResponse(E400,"INVALID_START_END_DATE","Invalid start or end date"), HttpStatus.BAD_REQUEST);
            }
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
    public ResponseEntity<Object> deleteSubject(@RequestBody List<String> listSubjectId,HttpServletRequest httpServletRequest)
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

}
