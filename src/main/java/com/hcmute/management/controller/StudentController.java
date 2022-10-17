package com.hcmute.management.controller;


import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.SuccessResponse;
import com.hcmute.management.model.payload.request.Student.AddNewStudentRequest;
import com.hcmute.management.model.payload.request.Student.ChangeInfoStudentRequest;
import com.hcmute.management.model.payload.request.Student.DeleteStudentRequest;
import com.hcmute.management.repository.StudentRepository;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.StudentService;
import com.hcmute.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    private final UserService userService;
    private final StudentService studentService;
    final StudentRepository studentRepository;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<SuccessResponse> getAllStudent() {
        List<StudentEntity> listStudent = studentService.findAllStudent();
        if (listStudent.size() == 0) {
            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.FOUND.value());
            response.setMessage("List student is Empty");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        SuccessResponse response = new SuccessResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Query Successfully");
        response.setSuccess(true);
        response.getData().put("list Student", listStudent);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    private ResponseEntity<SuccessResponse> getStudentById(@PathVariable String id) {
        StudentEntity student = studentService.findById(id);
        if (student == null) {
            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.FOUND.value());
            response.setMessage("Student is Not Found");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        SuccessResponse response = new SuccessResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Query Successfully");
        response.setSuccess(true);
        response.getData().put("Student: ", student);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/insert")
    @ResponseBody
    private ResponseEntity<SuccessResponse> insertStudent(HttpServletRequest req, @RequestBody AddNewStudentRequest addNewStudentRequest) {
        String authorizationHeader = req.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("access token is expired");
            }
            UserEntity user = userService.findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(accessToken)).toString());
            if (user == null) {
                throw new BadCredentialsException("User not found");
            } else {
                if (studentService.findByUserId(user) == null) {
                    StudentEntity newStudent = studentService.saveStudent(addNewStudentRequest, user);
                    SuccessResponse response = new SuccessResponse();
                    response.setMessage("Add student successfully");
                    response.setSuccess(true);
                    response.setStatus(HttpStatus.OK.value());
                    response.getData().put("Student", newStudent);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    SuccessResponse response = new SuccessResponse();
                    response.setStatus(HttpStatus.FOUND.value());
                    response.setMessage("Student is existed");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                }
            }
        }
        throw new BadCredentialsException("access token is missing");
    }

    @PutMapping("/changeinf")
    @ResponseBody
    public ResponseEntity<SuccessResponse> changeInfo(HttpServletRequest req, @RequestBody @Valid ChangeInfoStudentRequest changeInfoStudentRequest) {
        String authorizationHeader = req.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("access token is expired");
            }
            UserEntity user = userService.findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(accessToken)).toString());
            if (user == null) {
                throw new BadCredentialsException("User not found");
            } else {
                StudentEntity newStudent = new StudentEntity();
                if (studentService.findByUserId(user) != null) {
                    newStudent = studentService.changeInf(changeInfoStudentRequest, user);
                    SuccessResponse response = new SuccessResponse();
                    response.setMessage("Change student successfully");
                    response.setSuccess(true);
                    response.setStatus(HttpStatus.OK.value());
                    response.getData().put("Student", newStudent);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    SuccessResponse response = new SuccessResponse();
                    response.setStatus(HttpStatus.FOUND.value());
                    response.setMessage("Student isn't existed");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                }
            }
        }
        throw new BadCredentialsException("access token is missing");
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SuccessResponse> deleteStudent(HttpServletRequest req, @PathVariable("id") String id) {
        String authorizationHeader = req.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("access token is expired");
            }
            UserEntity user = userService.findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(accessToken)).toString());
            if (user == null) {
                throw new BadCredentialsException("User not found");
            } else {
                StudentEntity student = studentService.findById(id);
                if (student == null) {

                    SuccessResponse response = new SuccessResponse();
                    response.setStatus(HttpStatus.FOUND.value());
                    response.setMessage("Student isn't existed");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                } else {
                    studentService.deleteStudent(student.getId());
                    SuccessResponse response = new SuccessResponse();
                    response.setMessage("Delete student successfully");
                    response.setSuccess(true);
                    response.getData().put("Student:", student);
                    response.setStatus(HttpStatus.OK.value());
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        }
        throw new BadCredentialsException("access token is missing");
    }
}
