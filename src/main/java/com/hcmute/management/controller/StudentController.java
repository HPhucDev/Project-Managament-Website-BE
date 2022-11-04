package com.hcmute.management.controller;


import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.SuccessResponse;
import com.hcmute.management.model.payload.request.Student.AddNewStudentRequest;
import com.hcmute.management.model.payload.request.Student.ChangeInfoStudentRequest;
import com.hcmute.management.model.payload.request.Student.DeleteStudentRequest;
import com.hcmute.management.repository.StudentRepository;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.StudentService;
import com.hcmute.management.service.SubjectService;
import com.hcmute.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.parameters.P;
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
    private final SubjectService subjectService;

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
        response.getData().put("listStudent", listStudent);
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

    @PatchMapping("/updateinfo")
    @ResponseBody
    public ResponseEntity<SuccessResponse> updateInfo(HttpServletRequest req, @RequestBody @Valid ChangeInfoStudentRequest changeInfoStudentRequest) {
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
    @PostMapping("/addGroupLeader/{id}")
    public ResponseEntity<SuccessResponse> addGroupLeader(@PathVariable("id") String id,HttpServletRequest req)
    {
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
            }
            else {
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
            }throw new BadCredentialsException("access token is missing");
    }
    @PostMapping("/addGroupMember/{id}")
    public ResponseEntity<SuccessResponse> addGroupMember(@RequestParam(value = "listMember") List<String> listMember,@PathVariable("id") String id,HttpServletRequest req)
    {
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
            }
            else {
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
                    }
                    else
                    {
                    if (listMember.size()+subject.getGroupMember().size()>subject.getGroupCap()-1){
                        response.setStatus(HttpStatus.FOUND.value());
                        response.setMessage("Invalid number of group member");
                        response.setSuccess(false);
                        return new ResponseEntity<>(response, HttpStatus.FOUND);
                    }
                    else
                    {
                            UserEntity tempUser = new UserEntity();
                            for (String i:listMember)
                            {
                                tempUser=userService.findById(i);
                                if (tempUser==null || tempUser.getSubject()!=null || tempUser.getSubjectLeader()!=null)
                                {
                                    response.setStatus(HttpStatus.FOUND.value());
                                    response.setMessage("Member with id " + i +" is not valid");
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
            }throw new BadCredentialsException("access token is missing");
        }

    @DeleteMapping("/deleteGroupMember/{id}")
    public ResponseEntity<SuccessResponse> deleteGroupMember(@RequestParam(value = "listMember") List<String> listMember,@PathVariable("id") String id,HttpServletRequest req)
    {
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
            }
            else {
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
                    }
                    else {
                        UserEntity tempUser = new UserEntity();
                        for (String i:listMember)
                        {
                            tempUser=userService.findById(i);
                            if (tempUser==null || tempUser.getSubject()!=subject)
                            {
                                response.setStatus(HttpStatus.FOUND.value());
                                response.setMessage("Member with id " + i +" is not valid");
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
        }throw new BadCredentialsException("access token is missing");
    }
    @GetMapping("/getStudentInfo")
    public ResponseEntity<SuccessResponse> getStudentInfo(HttpServletRequest req)
    {
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
            } else
            {
                StudentEntity student = studentService.findByUserId(user);
                if (student==null)
                {
                    response.setStatus(HttpStatus.FOUND.value());
                    response.setMessage("You don't have student info");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                }
                response.setMessage("Get Student info success");
                response.setSuccess(true);
                response.getData().put("student",student);
                response.setStatus(HttpStatus.OK.value());
                return new ResponseEntity<>(response,HttpStatus.FOUND);
            }
        }throw new BadCredentialsException("access token is missing");
    }
    @DeleteMapping("/DeleteGroupLeader/{id}")
    public ResponseEntity<SuccessResponse> deleteGroupLeader(@PathVariable("id") String id,HttpServletRequest req)
    {
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
            } else
            {
                SubjectEntity subject = subjectService.getSubjectById(id);
                if (subject == null || subject.getGroupLeader() != user) {
                    response.setStatus(HttpStatus.FOUND.value());
                    response.setMessage("Subject doesn't exists or User is not leader");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                }
                else
                {
                    for (UserEntity tempUser : subject.getGroupMember())
                    {
                        tempUser.setSubject(null);
                        subject.getGroupMember().remove(user);
                    }
                    subject.setGroupLeader(null);
                    user.setSubjectLeader(null);
                }
                subject=subjectService.saveSubject(subject);
                response.setMessage("Delete subject leader and group member success");
                response.setSuccess(true);
                response.setStatus(HttpStatus.OK.value());
                response.getData().put("Leader",subject.getGroupLeader());
                response.getData().put("Member",subject.getGroupMember());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }throw new BadCredentialsException("access token is missing");
    }
    @GetMapping("/studentSubject")
    public ResponseEntity<SuccessResponse> getStudentSubject(HttpServletRequest req)
    {
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
                if (user.getSubjectLeader()==null && user.getSubject()==null)
                {
                    response.setStatus(HttpStatus.FOUND.value());
                    response.setMessage("User has not been assigned to any project");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                }
                else
                {
                    response.setStatus(HttpStatus.OK.value());
                    response.setMessage("Get student Subject info success");
                    response.setSuccess(true);
                    response.getData().put("info",user.getSubject()==null ? user.getSubjectLeader() : user.getSubject());
                    response.getData().put("teamRole",user.getSubject()==null ? "Leader" : "Teammate");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        }throw new BadCredentialsException("access token is missing");
    }
}
