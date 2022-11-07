package com.hcmute.management.controller;


import com.hcmute.management.handler.MethodArgumentNotValidException;
import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.SuccessResponse;
import com.hcmute.management.model.payload.request.Student.AddNewStudentRequest;
import com.hcmute.management.model.payload.request.Student.ChangeInfoStudentRequest;
import com.hcmute.management.model.payload.request.Student.DeleteStudentRequest;
import com.hcmute.management.model.payload.response.ErrorResponse;

import com.hcmute.management.repository.StudentRepository;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.StudentService;
import com.hcmute.management.service.SubjectService;
import com.hcmute.management.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public static String E400="Bad request";
    public static String E404="Not found";

    @GetMapping("")
    @ResponseBody
    @ApiOperation("Get All")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllStudent() {
        List<StudentEntity> listStudent = studentService.findAllStudent();
        Map<String,Object> map = new HashMap();
        map.put("Content", listStudent);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation("Get by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PostMapping("")
    @ResponseBody
    @ApiOperation("Create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    private ResponseEntity<Object> createStudent(HttpServletRequest req, @RequestBody AddNewStudentRequest addNewStudentRequest, BindingResult errors) throws Exception{
        if (errors.hasErrors())
        {
            throw new MethodArgumentNotValidException(errors);
        }
        String authorizationHeader = req.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("access token is expired");
            }
            if (studentService.findById(addNewStudentRequest.getMssv()) == null) {
                StudentEntity newStudent = studentService.saveStudent(addNewStudentRequest);
                return new ResponseEntity<>(newStudent, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorResponse(E400,"STUDENT_EXISTED","Student is existed"), HttpStatus.BAD_REQUEST);
            }

        }
        throw new BadCredentialsException("access token is missing");
    }

    @PatchMapping("/{id}")
    @ResponseBody
    @ApiOperation("Update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> updateInfo(HttpServletRequest req, @RequestBody @Valid ChangeInfoStudentRequest changeInfoStudentRequest, @PathVariable("id") String userid) {
        String authorizationHeader = req.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("access token is expired");
            }
                StudentEntity newStudent = new StudentEntity();
                if (studentService.findStudentbyUserId(userid) != null) {
                    newStudent = studentService.changeInf(changeInfoStudentRequest, userid);
                    return new ResponseEntity<>(newStudent, HttpStatus.OK);
                } else {

                    return new ResponseEntity<>(new ErrorResponse(E400,"STUDENT_NOT_FOUND","Student not found"), HttpStatus.NOT_FOUND);
                }

        }
        throw new BadCredentialsException("access token is missing");
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation("Delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteStudent(HttpServletRequest req, @PathVariable("id") String id) {
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

                    return new ResponseEntity<>(new ErrorResponse(E404,"STUDENT_NOT_FOUND","student not found"), HttpStatus.NOT_FOUND);
                } else {
                    studentService.deleteStudent(student.getId());
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        throw new BadCredentialsException("access token is missing");
    }
    @PostMapping("/addGroupLeader/{id}")
    @ApiOperation("Add Group Leader")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> addGroupLeader(@PathVariable("id") String id,HttpServletRequest req)
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
                        return new ResponseEntity<>(new ErrorResponse(E404,"SUBJECT_DO_NOT_EXISTS","Subject doesn't exists"), HttpStatus.NOT_FOUND);
                    } else if (subject.getGroupLeader() != null) {
                        return new ResponseEntity<>(new ErrorResponse(E400,"SUBJECT_HAS_BEEN_ASSIGNED","Subject has been assigned"), HttpStatus.BAD_REQUEST);
                    } else if (user.getSubjectLeader() != null) {
                        return new ResponseEntity<>(new ErrorResponse(E400,"USER_HAS_BEEN_ASSIGNED_TO_ANOTHER_PROJECT","User has been assigned to another project"), HttpStatus.BAD_REQUEST);
                    }
                    subject.setGroupLeader(user);
                    user.setSubjectLeader(subject);
                    subject = subjectService.saveSubject(subject);
                    Map<String, Object> map = new HashMap<>();
                    map.put("Content", subject);
                    map.put("",user);
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }
            }throw new BadCredentialsException("access token is missing");
    }
    @PostMapping("/addGroupMember/{id}")
    @ApiOperation("Add Group Member")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> addGroupMember(@RequestParam(value = "listMember") List<String> listMember,@PathVariable("id") String id,HttpServletRequest req)
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

                    return new ResponseEntity<>(new ErrorResponse(E404,"ACCOUNT_HAS_NO_INFORMATION","account has no information"), HttpStatus.NOT_FOUND);

                } else {
                    SubjectEntity subject = subjectService.getSubjectById(id);
                    if (subject == null || subject.getGroupLeader() != user) {
                        return new ResponseEntity<>(new ErrorResponse(E404,"SUBJECT_DO_NOT_EXIST_OR_USER_IS_NOT_LEADER","Subject doesn't exists or User is not leader"), HttpStatus.NOT_FOUND);

                    }
                    else
                    {
                    if (listMember.size()+subject.getGroupMember().size()>subject.getGroupCap()-1){
                        return new ResponseEntity<>(new ErrorResponse(E400,"INVALID_NUMBER_OF_GROUP_MEMBER","Invalid number of group member"), HttpStatus.BAD_REQUEST);

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
                                    return new ResponseEntity<>(new ErrorResponse(E404,"ID_NOT_VALID","Member with id " + i +" is not valid"), HttpStatus.NOT_FOUND);
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
    @ApiOperation("Delete Group Member")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
//    @GetMapping("/Getinf/{id}")
//    @ApiOperation("Get student information")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<SuccessResponse> getStudentInfo(HttpServletRequest req)
//    {
//        String authorizationHeader = req.getHeader(AUTHORIZATION);
//        SuccessResponse response = new SuccessResponse();
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String accessToken = authorizationHeader.substring("Bearer ".length());
//            if (jwtUtils.validateExpiredToken(accessToken)) {
//                throw new BadCredentialsException("access token is expired");
//            }
//            UserEntity user = userService.findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(accessToken)).toString());
//            if (user == null) {
//                throw new BadCredentialsException("User not found");
//            } else
//            {
//                StudentEntity student = studentService.findByUserId(user);
//                if (student==null)
//                {
//                    response.setStatus(HttpStatus.FOUND.value());
//                    response.setMessage("You don't have student info");
//                    response.setSuccess(false);
//                    return new ResponseEntity<>(response, HttpStatus.FOUND);
//                }
//                response.setMessage("Get Student info success");
//                response.setSuccess(true);
//                response.getData().put("student",student);
//                response.setStatus(HttpStatus.OK.value());
//                return new ResponseEntity<>(response,HttpStatus.FOUND);
//            }
//        }throw new BadCredentialsException("access token is missing");
//    }
    @DeleteMapping("/deleteGroupLeader/{id}")
    @ApiOperation("Delete Group Leader")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @GetMapping("/getAllSubject")
    @ApiOperation("Get All Subject")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
