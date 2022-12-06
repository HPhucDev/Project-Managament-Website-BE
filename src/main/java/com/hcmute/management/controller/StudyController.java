package com.hcmute.management.controller;

import com.hcmute.management.common.AppUserRole;
import com.hcmute.management.common.OrderByEnum;
import com.hcmute.management.common.StudentSort;
import com.hcmute.management.handler.AuthenticateHandler;
import com.hcmute.management.handler.MethodArgumentNotValidException;
import com.hcmute.management.handler.ValueDuplicateException;
import com.hcmute.management.model.entity.*;
import com.hcmute.management.model.payload.SuccessResponse;
import com.hcmute.management.model.payload.request.Student.AddNewStudentRequest;
import com.hcmute.management.model.payload.request.Student.ChangeInfoStudentRequest;
import com.hcmute.management.model.payload.response.ErrorResponse;
import com.hcmute.management.model.payload.response.PagingResponse;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.StudentService;
import com.hcmute.management.service.SubjectService;
import com.hcmute.management.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudyController {
    @Autowired
    JwtUtils jwtUtils;
    public static String E400 = "Bad request";
    public static String E404 = "Not found";
    public static String E401 = "Unauthorize";
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final UserService userService;
    @Autowired
    AuthenticateHandler authenticateHandler;

    @GetMapping("/{studentId}")
    @ApiOperation("Get by student id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getStudentById(@PathVariable String studentId) {
        StudentEntity student = studentService.findById(studentId);
        if (student == null) {
            return new ResponseEntity<>(new ErrorResponse(E404, "STUDENT_ID_NOT_FOUND", "Student id not found"), HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("")
    @ApiOperation("Get all")
    public ResponseEntity<Object> getAllStudent() {
        List<StudentEntity> list = studentService.findAllStudent();
        Map<String, Object> map = new HashMap<>();
        map.put("Content", list);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping(value = "", consumes = {"multipart/form-data"})
    @ApiOperation("Create")
    public ResponseEntity<Object> createStudent(@Valid AddNewStudentRequest addNewStudentRequest, @RequestPart MultipartFile file,HttpServletRequest httpServletRequest, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(bindingResult);
        }
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(httpServletRequest);
            String id = addNewStudentRequest.getStudentId();
            StudentEntity findStudent = studentService.findById(addNewStudentRequest.getStudentId());
            if (findStudent != null) {
                return new ResponseEntity<>(new ErrorResponse(E400, "ID_EXISTS", "Id has been used"), HttpStatus.BAD_REQUEST);
            }
            UserEntity foundUser = userService.findByUserName(id);
            if (foundUser != null) {
                return new ResponseEntity<>(new ErrorResponse(E400, "USERNAME_EXISTED", "Username has been used by another student"), HttpStatus.BAD_REQUEST);
            }
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder() ;
            UserEntity addNewUser = new UserEntity(passwordEncoder.encode(id), id);
            addNewUser = userService.register(addNewUser, AppUserRole.ROLE_STUDENT);
            StudentEntity student = studentService.saveStudent(addNewStudentRequest, addNewUser);
            userService.addUserImage(file, addNewUser);
            return new ResponseEntity<>(student, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401, "UNAUTHORIZED", "Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        } catch (ValueDuplicateException e) {
            return new ResponseEntity<>(new ErrorResponse(E400, "EMAIL_ALREADY_EXISTS", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(value = "/{userId}", consumes = {"multipart/form-data"})
    @ApiOperation("Update")
    public ResponseEntity<Object> updateStudentById(HttpServletRequest httpServletRequest, @Valid ChangeInfoStudentRequest changeInfoStudentRequest, @RequestPart MultipartFile file, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(bindingResult);
        }
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(httpServletRequest);
            StudentEntity student = studentService.findByUserId(user);
            if (student == null) {
                return new ResponseEntity<>(new ErrorResponse(E400, "YOU_ARE_NOT_A_STUDENT", "You aren't a Student"), HttpStatus.BAD_REQUEST);

            }
            StudentEntity updateStudent = studentService.updateStudent(changeInfoStudentRequest, user);
            if (!file.isEmpty()) {
                userService.addUserImage(file, user);
            }
            return new ResponseEntity<>(updateStudent, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401, "UNAUTHORIZED", "Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("")
    @ApiOperation("Delete")
    public ResponseEntity<Object> deleteStudentById(HttpServletRequest httpServletRequest, @RequestParam(value = "listStudentId") List<String> listStudentId) {
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(httpServletRequest);
            for (String id : listStudentId) {
                StudentEntity student = studentService.findById(id);
                studentService.deleteStudent(id);
                UserEntity userofStudent = userService.findById(student.getUser().getId());
                for (RoleEntity role : userofStudent.getRoles()) {
                    role.setUsers(null);
                }
                userofStudent.getRoles().clear();
                userService.delete(userofStudent);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401, "UNAUTHORIZED", "Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/addGroupLeader/{subjectId}")
    @ApiOperation("Add Group Leader")
    public ResponseEntity<Object> addGroupLeader(@PathVariable("subjectId") String id, HttpServletRequest request) {
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(request);
            StudentEntity student = studentService.findByUserId(user);
            if (student == null) {
                return new ResponseEntity<>(new ErrorResponse(E404, "STUDENT_NOT_FOUND", "Student not found"), HttpStatus.NOT_FOUND);
            } else {
                SubjectEntity subject = subjectService.getSubjectById(id);
                if (subject == null) {
                    return new ResponseEntity<>(new ErrorResponse(E404, "SUBJECT_DO_NOT_EXISTS", "Subject doesn't exists"), HttpStatus.NOT_FOUND);
                } else if (subject.getGroupLeader() != null) {
                    return new ResponseEntity<>(new ErrorResponse(E400, "SUBJECT_HAS_BEEN_ASSIGNED", "Subject has been assigned"), HttpStatus.BAD_REQUEST);
                } else if (user.getSubjectLeader() != null) {
                    return new ResponseEntity<>(new ErrorResponse(E400, "USER_HAS_BEEN_ASSIGNED_TO_ANOTHER_PROJECT", "User has been assigned to another project"), HttpStatus.BAD_REQUEST);
                }
                subject.setGroupLeader(user);
                user.setSubjectLeader(subject);
                subject = subjectService.saveSubject(subject);
                return new ResponseEntity<>(subject, HttpStatus.OK);
            }
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401, "UNAUTHORIZED", "Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/addGroupMember/{subjectId}")
    @ApiOperation("Add Group Member")
    public ResponseEntity<Object> addGroupMember(@RequestParam(value = "listMember") List<String> listMember, @PathVariable("subjectId") String id, HttpServletRequest req) {
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(req);
            StudentEntity student = studentService.findByUserId(user);
            if (student == null) {
                return new ResponseEntity<>(new ErrorResponse(E404, "ACCOUNT_HAS_NO_INFORMATION", "account has no information"), HttpStatus.NOT_FOUND);
            } else {
                SubjectEntity subject = subjectService.getSubjectById(id);
                if (subject == null || subject.getGroupLeader() != user) {
                    return new ResponseEntity<>(new ErrorResponse(E404, "SUBJECT_DO_NOT_EXIST_OR_USER_IS_NOT_LEADER", "Subject doesn't exists or User is not leader"), HttpStatus.NOT_FOUND);

                } else {
                    if (listMember.size() + subject.getGroupMember().size() > subject.getGroupCap() - 1) {
                        return new ResponseEntity<>(new ErrorResponse(E400, "INVALID_NUMBER_OF_GROUP_MEMBER", "Invalid number of group member"), HttpStatus.BAD_REQUEST);
                    } else {
                        UserEntity tempUser = new UserEntity();
                        for (String i : listMember) {
                            tempUser = userService.findById(i);
                            if (tempUser == null || tempUser.getSubject() != null || tempUser.getSubjectLeader() != null) {
                                return new ResponseEntity<>(new ErrorResponse(E404, "ID_NOT_VALID", "Member with id " + i + " is not valid"), HttpStatus.NOT_FOUND);
                            }
                            subject.getGroupMember().add(tempUser);
                            tempUser.setSubject(subject);
                        }
                    }
                }
                subject = subjectService.saveSubject(subject);
                return new ResponseEntity<>(subject, HttpStatus.OK);
            }
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401, "UNAUTHORIZED", "Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }


    @DeleteMapping("/deleteGroupMember/{subjectId}")
    @ApiOperation("Delete Group Member")
    public ResponseEntity<Object> deleteGroupMember(@RequestParam(value = "listMember") List<String> listMember, @PathVariable("subjectId") String id, HttpServletRequest req) {
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(req);
            StudentEntity student = studentService.findByUserId(user);
            if (student == null) {
                return new ResponseEntity<>(new ErrorResponse(E404, "ACCOUNT_HAVE_NÓ_STUDENT_INFOMATION", "Account have no student information"), HttpStatus.NOT_FOUND);
            } else {
                SubjectEntity subject = subjectService.getSubjectById(id);
                if (subject == null || subject.getGroupLeader() != user) {
                    return new ResponseEntity<>(new ErrorResponse(E404, "SUBJECT_DOES_NOT_EXIST_OR_USER_IS_NOT_LEADER", "Subject does not exist or uer is not leader"), HttpStatus.NOT_FOUND);
                } else {
                    UserEntity tempUser = new UserEntity();
                    for (String i : listMember) {
                        tempUser = userService.findById(i);
                        if (tempUser == null || tempUser.getSubject() != subject) {
                            return new ResponseEntity<>(new ErrorResponse(E400, "MEMBER_ID_IS_NOT_VALID", "Member with id" + i + " is not valid"), HttpStatus.NOT_FOUND);
                        }
                        subject.getGroupMember().remove(tempUser);
                        tempUser.setSubject(null);
                    }
                }
                subject = subjectService.saveSubject(subject);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401, "UNAUTHORIZED", "Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/deleteGroupLeader/{subjectId}")
    @ApiOperation("Delete Group Leader")
    public ResponseEntity<Object> deleteGroupLeader(@PathVariable("subjectId") String id, HttpServletRequest req) {
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(req);
            SubjectEntity subject = subjectService.getSubjectById(id);
            if (subject == null || subject.getGroupLeader() != user) {
                return new ResponseEntity<>(new ErrorResponse(E404, "SUBJECT_DOES_NOT_EXIST_OR_USER_ID_IS_NOT_LEADER", "Subject does not exist or user is not leader"), HttpStatus.NOT_FOUND);
            } else {
                for (UserEntity tempUser : subject.getGroupMember()) {
                    tempUser.setSubject(null);
                    subject.getGroupMember().remove(user);
                }
                subject.setGroupLeader(null);
                user.setSubjectLeader(null);
            }
            subject = subjectService.saveSubject(subject);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401, "UNAUTHORIZED", "Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getSubjectByStudent")
    @ApiOperation("Get Subject by Student")
    public ResponseEntity<Object> getStudentSubject(HttpServletRequest req) {
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(req);
            if (user.getSubjectLeader() == null && user.getSubject() == null) {
                return new ResponseEntity<>(new ErrorResponse(E404, "USER_HAS_NOT_BEEN_ASSIGNED_TO_ANY_PROJECT", "User has not been assigned to any project"), HttpStatus.FOUND);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("info", user.getSubject() == null ? user.getSubjectLeader() : user.getSubject());
                map.put("teamRole", user.getSubject() == null ? "Leader" : "Teammate");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401, "UNAUTHORIZED", "Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/search")
    @ApiOperation("Search by Criteria")
    public ResponseEntity<Object> search(@RequestParam(defaultValue = "0", name = "pageIndex") int pageIndex,
                                         @RequestParam(defaultValue = "5", name = "pageSize") int pageSize, @RequestParam(defaultValue = "DESCENDING") OrderByEnum order, @RequestParam(defaultValue = "MAJOR") StudentSort studentSort, @RequestParam(defaultValue = "", name = "searchText") String searchText) {
        PagingResponse pagingResponse = studentService.search(searchText, order, studentSort, pageIndex, pageSize);
        return new ResponseEntity<>(pagingResponse, HttpStatus.OK);
    }
}

