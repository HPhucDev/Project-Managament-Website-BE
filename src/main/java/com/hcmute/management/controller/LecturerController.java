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
                //throw new BadCredentialsException("access token is  expired");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setMessage("access token is expired");
                response.setSuccess(false);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            //UserEntity user = userService.findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(accessToken)).toString());
            UserEntity user =userService.findById(addNewLecturerRequest.getUserid());
            if (user == null) {
                //throw new BadCredentialsException("User not found");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("User not found");
                response.setSuccess(false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            else {
                LecturerEntity findLecturer =lecturerService.getLecturerById(addNewLecturerRequest.getId());
                //id have been existed
                if (findLecturer!=null){
                    response.setStatus((HttpStatus.FOUND.value()));
                    response.setMessage("Id is existed");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                }
                //LecturerEntity lecturer = LecturerMapping.addLecturerToEntity((addNewLecturerRequest));
                LecturerEntity lecturer=lecturerService.saveLecturer(addNewLecturerRequest,user);
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Add Lecturer successfully");
                response.setSuccess(true);
                response.getData().put("Lecturer", lecturer);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } else {
            //throw new BadCredentialsException("access token is missing");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("access token is missing");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<SuccessResponse> getAllLecturer() {
        List<LecturerEntity> listLecturer = lecturerService.getAllLecturer();
        SuccessResponse response = new SuccessResponse();
        if (listLecturer.size() == 0) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("List Lecturer is empty");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get all Lecturer successfully");
        response.setSuccess(true);
        response.getData().put("ListLecturerInfo", listLecturer);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<SuccessResponse> updateLecturer(@Valid @RequestBody UpdateLecturerRequest updateLecturerRequest, BindingResult errors, HttpServletRequest httpServletRequest, @PathVariable("id") String id) throws Exception {
        SuccessResponse response = new SuccessResponse();
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                //throw new BadCredentialsException("access token is  expired");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setMessage("access token is expired");
                response.setSuccess(false);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            //UserEntity user = userService.findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(accessToken)).toString());
            LecturerEntity lecturer = lecturerService.getLecturerById(id);

            if (lecturer == null) {
                response.setStatus((HttpStatus.NOT_FOUND.value()));
                response.setMessage("Can't find lecturer with id:" + id);
                response.setSuccess(false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                UserEntity user =lecturer.getUser();
                if (user == null) {
                    // throw new BadCredentialsException("User not found");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("User not found");
                response.setSuccess(false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                lecturerService.updateLecturer(updateLecturerRequest,user,id);
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Update Lecturer successfully");
                response.setSuccess(true);
                response.getData().put("LecturerInfo", lecturer);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } else {
            //throw new BadCredentialsException("access token is missing");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("access token is missing");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse>getLecturerById(@PathVariable("id")String id){
        LecturerEntity lecturer = lecturerService.getLecturerById(id);
        SuccessResponse response = new SuccessResponse();
        if(lecturer==null)
        {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("Can't find Lecturer with id:"+id);
            response.setSuccess(false);
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get Lecturer successfully");
        response.setSuccess(true);
        response.getData().put("LecturerInfo",lecturer);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponse>deleteLecturer(@RequestBody List<String> listLecturerId,HttpServletRequest httpServletRequest){
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        SuccessResponse response = new SuccessResponse();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                //throw new BadCredentialsException("access token is  expired");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setMessage("access token is expired");
                response.setSuccess(false);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            lecturerService.deleteById(listLecturerId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Delete Lecturer successfully");
            response.setSuccess(true);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else {
            //throw new BadCredentialsException("access token is missing");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("access token is missing");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

}
