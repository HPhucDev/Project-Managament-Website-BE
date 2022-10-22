package com.hcmute.management.controller;

import com.hcmute.management.handler.MethodArgumentNotValidException;
import com.hcmute.management.mapping.LecturerMapping;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.SuccessResponse;
import com.hcmute.management.model.payload.request.Lecturer.AddNewLecturerRequest;
import com.hcmute.management.model.payload.request.Lecturer.UpdateLecturerRequest;
import com.hcmute.management.repository.LecturerRepository;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.LecturerService;
import com.hcmute.management.service.UserService;
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
import java.util.UUID;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@ComponentScan
@RestController
@RequestMapping("/api/lecture")
@RequiredArgsConstructor
public class LecturerController {
    private final LecturerService lecturerService;
    private final UserService userService;
    final LecturerRepository lecturerRepository;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/add")
    public ResponseEntity<SuccessResponse> addLecturer(@RequestBody @Valid AddNewLecturerRequest addNewLecturerRequest, BindingResult errors, HttpServletRequest httpServletRequest) throws Exception {
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
            UserEntity user = userService.findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(accessToken)).toString());
            if (user == null) {
                throw new BadCredentialsException("User not found");
            } else {
                LecturerEntity findLecturer = lecturerService.getLecturerById(addNewLecturerRequest.getId());
                //id have been existed
                if (findLecturer != null) {
                    response.setStatus((HttpStatus.BAD_REQUEST.value()));
                    response.setMessage("Id have been existed");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                //LecturerEntity lecturer = LecturerMapping.addLecturerToEntity((addNewLecturerRequest));
                LecturerEntity lecturer = lecturerService.saveLecturer(addNewLecturerRequest, user);
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Add Lecturer successfully");
                response.setSuccess(true);
                response.getData().put("LecturerInfo", lecturer);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } else throw new BadCredentialsException("access token is missing");
    }

    @GetMapping("/getall")
    public ResponseEntity<SuccessResponse> getAllLecturer(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        List<LecturerEntity> listLecturer = lecturerService.findAllSubjectPaging(page, size);
        SuccessResponse response = new SuccessResponse();
        if (listLecturer.size() == 0) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("List Lecturer is empty");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get all Lecturer successfully");
        response.setSuccess(true);
        response.getData().put("ListLecturerInfo", listLecturer);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SuccessResponse> updateLecturer(@Valid @RequestBody UpdateLecturerRequest updateLecturerRequest, BindingResult errors, HttpServletRequest httpServletRequest, @PathVariable("id") String id) throws Exception {
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
            UserEntity user = userService.findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(accessToken)).toString());
            if (user == null) {
                throw new BadCredentialsException("User not found");
            } else {
                LecturerEntity lecturer = lecturerService.getLecturerById(id);
                if (lecturer == null) {
                    response.setStatus((HttpStatus.BAD_REQUEST.value()));
                    response.setMessage("Can't find lecturer with id = " + id);
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                lecturerService.updateLecturer(updateLecturerRequest, user, id);
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Update Lecturer successfully");
                response.setSuccess(true);
                response.getData().put("LecturerInfo", lecturer);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } else throw new BadCredentialsException("access token is missing");
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getLecturerById(@PathVariable("id") String userId) {
        LecturerEntity lecturer = lecturerService.getLecturerById(userId);
        SuccessResponse response = new SuccessResponse();
        if (lecturer == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Can't find Lecturer with id " + userId);
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get Lecturer successfully");
        response.setSuccess(true);
        response.getData().put("LecturerInfo", lecturer);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponse> deleteLecturer(@RequestBody List<String> listLecturerId, HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        SuccessResponse response = new SuccessResponse();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token is  expired");
            }
            lecturerService.deleteById(listLecturerId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Delete Lecturer successfully");
            response.setSuccess(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else throw new BadCredentialsException("access token is missing");
    }
}
