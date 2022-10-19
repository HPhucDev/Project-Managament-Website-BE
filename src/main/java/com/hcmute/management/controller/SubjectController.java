package com.hcmute.management.controller;

import com.hcmute.management.handler.MethodArgumentNotValidException;
import com.hcmute.management.mapping.SubjectMapping;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.payload.SuccessResponse;
import com.hcmute.management.model.payload.request.Subject.AddNewSubjectRequest;
import com.hcmute.management.model.payload.request.Subject.UpdateSubjectRequest;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@ComponentScan
@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/add")
    public ResponseEntity<SuccessResponse> addSubject(@RequestBody @Valid AddNewSubjectRequest addNewSubjectRequest, BindingResult errors, HttpServletRequest httpServletRequest) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        SuccessResponse response = new SuccessResponse();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token is  expired");
            }
            SubjectEntity subject = SubjectMapping.addSubjectToEntity(addNewSubjectRequest);
            if (subject.getEndDate().compareTo(subject.getStartDate()) <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("EndDate or StartDate not valid");
                response.setSuccess(false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else {
                subjectService.saveSubject(subject);
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Save Subject successfully");
                response.setSuccess(true);
                response.getData().put("SubjectInfo", subject);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } else throw new BadCredentialsException("access token is missing");
    }

    @GetMapping("/showall")
    public ResponseEntity<SuccessResponse> getAllSubject(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size) {
        List<SubjectEntity> listSubject = subjectService.findAllSubjectPaging(page, size);
        SuccessResponse response = new SuccessResponse();
        if (listSubject.size() == 0) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("List Subject is empty");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Save Subject successfully");
        response.setSuccess(true);
        response.getData().put("ListSubjectInfo", listSubject);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SuccessResponse> updateSubject(@Valid @RequestBody UpdateSubjectRequest updateSubjectRequest, BindingResult errors, HttpServletRequest httpServletRequest, @PathVariable("id") String id) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        SuccessResponse response = new SuccessResponse();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token is  expired");
            }
            SubjectEntity subject = subjectService.getSubjectById(id);
            if (subject == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Can't find subject with id " + id);
                response.setSuccess(false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            subject = SubjectMapping.updateRequestToEntity(subject, updateSubjectRequest);
            if (subject.getEndDate().compareTo(subject.getStartDate()) <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("EndDate or StartDate not valid");
                response.setSuccess(false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            subjectService.saveSubject(subject);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Save Subject successfully");
            response.setSuccess(true);
            response.getData().put("SubjectInfo", subject);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else throw new BadCredentialsException("access token is missing");
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getSubjectById(@PathVariable("id") String id) {
        SubjectEntity subject = subjectService.getSubjectById(id);
        SuccessResponse response = new SuccessResponse();
        if (subject == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Can't find subject with id " + id);
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Save Subject successfully");
        response.setSuccess(true);
        response.getData().put("SubjectInfo", subject);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponse> deleteSubject(@RequestBody List<String> listSubjectId, HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        SuccessResponse response = new SuccessResponse();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token is  expired");
            }
            subjectService.deleteById(listSubjectId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Delete Subject successfully");
            response.setSuccess(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else throw new BadCredentialsException("access token is missing");
    }

}
