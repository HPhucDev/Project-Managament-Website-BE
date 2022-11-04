package com.hcmute.management.controller;


import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.SuccessResponse;
import com.hcmute.management.model.payload.request.Student.AddNewStudentRequest;
import com.hcmute.management.model.payload.request.Student.ChangeInfoStudentRequest;
import com.hcmute.management.model.payload.request.Student.DeleteStudentRequest;
import com.hcmute.management.model.payload.response.MessageResponse;
import com.hcmute.management.repository.StudentRepository;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.StudentService;
import com.hcmute.management.service.SubjectService;
import com.hcmute.management.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final SubjectService subjectService;

    @GetMapping("")
    @ResponseBody
    @ApiOperation("Find all")
    public ResponseEntity<Object> getAllStudent() {
        List<StudentEntity> listStudent = studentService.findAllStudent();

        Map<String, Object> map = new HashMap<>();
        map.put("content", listStudent);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Find by id")
    @ResponseBody
    private ResponseEntity<Object> getStudentById(@PathVariable String id) {
        StudentEntity student = studentService.findById(id);
        if (student == null) {
            MessageResponse messageResponse = new MessageResponse("Bad Request", "STUDENT_ID_NOT_FOUND", "Student id not found");
            return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("content", student);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("")
    @ApiOperation("Create")
    @ResponseBody
    private ResponseEntity<Object> insertStudent(HttpServletRequest req, @RequestBody AddNewStudentRequest addNewStudentRequest, BindingResult result, HttpServletRequest httpServletRequest) throws Exception {
        String authorizationHeader = req.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("access token is expired");
            }
            if (studentService.findById(addNewStudentRequest.getMssv()) == null) {
                StudentEntity newStudent = studentService.saveStudent(addNewStudentRequest);
                Map<String, Object> map = new HashMap<>();
                map.put("content", newStudent);
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                MessageResponse messageResponse = new MessageResponse("Bad Request", "STUDENT_ID_EXISTED", "Student id has been used");
                return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
            }
        }
        throw new BadCredentialsException("access token is missing");
    }

    @PutMapping("")
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
    @ApiOperation("Delte")
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

    @PostMapping("/addGroupLeader/{id}")
    public ResponseEntity<SuccessResponse> addGroupLeader(@PathVariable("id") String id, HttpServletRequest req) {
        String authorizationHeader = req.getHeader(AUTHORIZATION);
        SuccessResponse response = new SuccessResponse();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("access token is expired");
            }
            UserEntity user = userService.findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(accessToken)).toString());
            if (user == null) {
                throw new BadCredentialsException("User not found");
            } else {
                StudentEntity student = studentService.findByUserId(user);
                if (student == null) {
                    response.setStatus(HttpStatus.FOUND.value());
                    response.setMessage("Account have no student info");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                } else {
                    SubjectEntity subject = subjectService.getSubjectById(id);
                    if (subject == null) {
                        response.setStatus(HttpStatus.FOUND.value());
                        response.setMessage("Subject doesn't exists");
                        response.setSuccess(false);
                        return new ResponseEntity<>(response, HttpStatus.FOUND);
                    } else if (subject.getGroupLeader() != null) {
                        response.setStatus(HttpStatus.FOUND.value());
                        response.setMessage("Subject has been assigned ");
                        response.setSuccess(false);
                        return new ResponseEntity<>(response, HttpStatus.FOUND);
                    } else if (user.getSubjectLeader() != null) {
                        response.setStatus(HttpStatus.FOUND.value());
                        response.setMessage("User has been assigned to another project");
                        response.setSuccess(false);
                        return new ResponseEntity<>(response, HttpStatus.FOUND);
                    }
                    subject.setGroupLeader(user);
                    user.setSubjectLeader(subject);
                    subject = subjectService.saveSubject(subject);
                    response.setMessage("Assign subject leader success");
                    response.setSuccess(true);
                    response.getData().put("subjectName", subject.getName());
                    response.getData().put("user", user);
                    response.setStatus(HttpStatus.OK.value());
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        }
        throw new BadCredentialsException("access token is missing");
    }

    @PostMapping("/addGroupMember/{id}")
    public ResponseEntity<SuccessResponse> addGroupMember(@RequestParam(value = "listMember") List<String> listMember, @PathVariable("id") String id, HttpServletRequest req) {
        String authorizationHeader = req.getHeader(AUTHORIZATION);
        SuccessResponse response = new SuccessResponse();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("access token is expired");
            }
            UserEntity user = userService.findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(accessToken)).toString());
            if (user == null) {
                throw new BadCredentialsException("User not found");
            } else {
                StudentEntity student = studentService.findByUserId(user);
                if (student == null) {
                    response.setStatus(HttpStatus.FOUND.value());
                    response.setMessage("Account have no student info");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                } else {
                    SubjectEntity subject = subjectService.getSubjectById(id);
                    if (subject == null || subject.getGroupLeader() != user) {
                        response.setStatus(HttpStatus.FOUND.value());
                        response.setMessage("Subject doesn't exists or User is not leader");
                        response.setSuccess(false);
                        return new ResponseEntity<>(response, HttpStatus.FOUND);
                    } else {
                        if (listMember.size() + subject.getGroupMember().size() > subject.getGroupCap() - 1) {
                            response.setStatus(HttpStatus.FOUND.value());
                            response.setMessage("Invalid number of group member");
                            response.setSuccess(false);
                            return new ResponseEntity<>(response, HttpStatus.FOUND);
                        } else {
                            UserEntity tempUser = new UserEntity();
                            for (String i : listMember) {
                                tempUser = userService.findById(i);
                                if (tempUser == null || tempUser.getSubject() != null || tempUser.getSubjectLeader() != null) {
                                    response.setStatus(HttpStatus.FOUND.value());
                                    response.setMessage("Member with id " + i + " is not valid");
                                    response.setSuccess(false);
                                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                                }
                                subject.getGroupMember().add(tempUser);
                                tempUser.setSubject(subject);
                            }
                        }
                    }
                    subject = subjectService.saveSubject(subject);
                    response.setMessage("Assign subject group member success");
                    response.setSuccess(true);
                    response.getData().put("subjectName", subject.getName());
                    response.getData().put("listUser", subject.getGroupMember());
                    response.setStatus(HttpStatus.OK.value());
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        }
        throw new BadCredentialsException("access token is missing");
    }

    @DeleteMapping("/deleteGroupMember/{id}")
    public ResponseEntity<SuccessResponse> deleteGroupMember(@RequestParam(value = "listMember") List<String> listMember, @PathVariable("id") String id, HttpServletRequest req) {
        String authorizationHeader = req.getHeader(AUTHORIZATION);
        SuccessResponse response = new SuccessResponse();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("access token is expired");
            }
            UserEntity user = userService.findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(accessToken)).toString());
            if (user == null) {
                throw new BadCredentialsException("User not found");
            } else {
                StudentEntity student = studentService.findByUserId(user);
                if (student == null) {
                    response.setStatus(HttpStatus.FOUND.value());
                    response.setMessage("Account have no student info");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                } else {
                    SubjectEntity subject = subjectService.getSubjectById(id);
                    if (subject == null || subject.getGroupLeader() != user) {
                        response.setStatus(HttpStatus.FOUND.value());
                        response.setMessage("Subject doesn't exists or User is not leader");
                        response.setSuccess(false);
                        return new ResponseEntity<>(response, HttpStatus.FOUND);
                    } else {
                        UserEntity tempUser = new UserEntity();
                        for (String i : listMember) {
                            tempUser = userService.findById(i);
                            if (tempUser == null || tempUser.getSubject() != subject) {
                                response.setStatus(HttpStatus.FOUND.value());
                                response.setMessage("Member with id " + i + " is not valid");
                                response.setSuccess(false);
                                return new ResponseEntity<>(response, HttpStatus.FOUND);
                            }
                            subject.getGroupMember().remove(tempUser);
                            tempUser.setSubject(null);
                        }
                    }
                    subject = subjectService.saveSubject(subject);
                    response.setMessage("Delete subject group member success");
                    response.setSuccess(true);
                    response.getData().put("subjectName", subject.getName());
                    response.getData().put("listUser", subject.getGroupMember());
                    response.setStatus(HttpStatus.OK.value());
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        }
        throw new BadCredentialsException("access token is missing");
    }
}
