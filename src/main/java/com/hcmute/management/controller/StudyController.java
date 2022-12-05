package com.hcmute.management.controller;

import com.hcmute.management.handler.AuthenticateHandler;
import com.hcmute.management.handler.MethodArgumentNotValidException;
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
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


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

    private final AuthenticateHandler authenticateHandler;

    @GetMapping("/{id}")
    @ApiOperation("Get by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getStudentById(@PathVariable() String id) {
        StudentEntity student = studentService.findById(id);
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

    @PostMapping("")
    @ResponseBody
    @ApiOperation("Create")
    public ResponseEntity<Object> createStudent(HttpServletRequest httpServletRequest, @RequestBody AddNewStudentRequest addNewStudentRequest, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(bindingResult);
        }
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("Access token is expired");
            }
            if (studentService.findById(addNewStudentRequest.getMssv()) == null) {
                StudentEntity student = studentService.saveStudent(addNewStudentRequest);
                return new ResponseEntity<>(student, HttpStatus.OK);
            } else
                return new ResponseEntity<>(new ErrorResponse(E400, "STUDENT_ID_EXISTED", "Student id existed"), HttpStatus.BAD_REQUEST);
        }
        throw new BadCredentialsException("Access token is missing");
    }

    @PatchMapping("/{id}")
    @ResponseBody
    @ApiOperation("Update")
    public ResponseEntity<Object> updateStudentById(HttpServletRequest httpServletRequest, @RequestBody @Valid ChangeInfoStudentRequest changeInfoStudentRequest, @PathVariable("id") String userid, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(bindingResult);
        }
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("Access token is expired");
            }
            if (studentService.findStudentbyUserId(userid) != null) {
                StudentEntity student = studentService.changeInf(changeInfoStudentRequest, userid);
                return new ResponseEntity<>(student, HttpStatus.OK);
            } else
                return new ResponseEntity<>(new ErrorResponse(E400, "STUDENT_ID_EXISTED", "Student id existed"), HttpStatus.BAD_REQUEST);
        }
        throw new BadCredentialsException("Access token is missing");
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation("Delete")
    public ResponseEntity<Object> deleteStudentById(HttpServletRequest httpServletRequest, @PathVariable("id") String id) {
       UserEntity user;
        try {
            user=authenticateHandler.authenticateUser(httpServletRequest);
            StudentEntity student = studentService.findById(id);
            if (student == null) {
                return new ResponseEntity<>(new ErrorResponse(E404, "STUDENT_NOT_FOUND", "Student not found"), HttpStatus.NOT_FOUND);
            } else {

                studentService.deleteStudent(id);
                for (RoleEntity role : user.getRoles()) {
                    role.setUsers(null);
                }
                user.getRoles().clear();
                userService.delete(user);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
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
                    Map<String, Object> map = new HashMap<>();
                    map.put("Content", subject);
                    map.put("", user);
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }
        catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/addGroupMember/{subjectId}")
    @ApiOperation("Add Group Member")
    public ResponseEntity<Object> addGroupMember(@RequestParam(value = "listMember") List<String> listMember, @PathVariable("subjectId") String id, HttpServletRequest req) {
        UserEntity user;
        try
        {
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
                                    return new ResponseEntity<>(new ErrorResponse(E400,"STUDENT_NOT_VALID","Member with id  "+ i +"  is not valid"), HttpStatus.BAD_REQUEST);
                                }
                                subject.getGroupMember().add(tempUser);
                                tempUser.setSubject(subject);
                            }
                        }
                    }
                    subject = subjectService.saveSubject(subject);
                    return new ResponseEntity<>(subject, HttpStatus.OK);
                }
            }
        catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/deleteGroupMember/{subjectId}")
    @ApiOperation("Delete Group Member")
    public ResponseEntity<Object> deleteGroupMember(@RequestParam(value = "listStudentId") List<String> listMember, @PathVariable(name = "subjectId") String id, HttpServletRequest req) {
        UserEntity user;
        try
        {
            user = authenticateHandler.authenticateUser(req);
                StudentEntity student = studentService.findByUserId(user);
                if (student == null) {
                    return new ResponseEntity<>(new ErrorResponse(E404,"INFO_NOT_FOUND","Account has no student info"), HttpStatus.NOT_FOUND);
                } else {
                    SubjectEntity subject = subjectService.getSubjectById(id);
                    if (subject == null || subject.getGroupLeader() != user) {
                        return new ResponseEntity<>(new ErrorResponse(E404,"SUBJECT_NOT_FOUND_OR_NOT_LEADER","Subject doesn't exists or User is not leader"), HttpStatus.NOT_FOUND);
                    } else {
                        UserEntity tempUser = new UserEntity();
                        for (String i : listMember) {
                            tempUser = userService.findById(i);
                            if (tempUser == null || tempUser.getSubject() != subject) {
                                return new ResponseEntity<>(new ErrorResponse(E400,"STUDENT_NOT_VALID","Member with id  "+ i +"  is not valid"), HttpStatus.BAD_REQUEST);
                            }
                            subject.getGroupMember().remove(tempUser);
                            tempUser.setSubject(null);
                        }
                    }
                    subject=subjectService.saveSubject(subject);
                    return new ResponseEntity<>(subject,HttpStatus.OK);
                }
            }
        catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/deleteGroupLeader/{id}")
    @ApiOperation("Delete Group Leader")
    public ResponseEntity<Object> deleteGroupLeader(@PathVariable("id") String id, HttpServletRequest req) {
        UserEntity user;
        try {
            user= authenticateHandler.authenticateUser(req);
            SubjectEntity subject = subjectService.getSubjectById(id);
            if (subject == null || subject.getGroupLeader() != user) {
                return new ResponseEntity<>(new ErrorResponse(E404,"SUBJECT_NOT_FOUND_OR_NOT_LEADER","Subject doesn't exists or User is not leader"), HttpStatus.NOT_FOUND);
            } else {
                for (UserEntity tempUser : subject.getGroupMember()) {
                    tempUser.setSubject(null);
                    subject.getGroupMember().remove(user);
                }
                subject.setGroupLeader(null);
                user.setSubjectLeader(null);
            }
            subject=subjectService.saveSubject(subject);
            return new ResponseEntity<>(subject,HttpStatus.OK);
        }
        catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/getAllSubject")
    @ApiOperation("Get All Subject")
    public ResponseEntity<SuccessResponse> getStudentSubject(HttpServletRequest req) {
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
                if (user.getSubjectLeader() == null && user.getSubject() == null) {
                    response.setStatus(HttpStatus.FOUND.value());
                    response.setMessage("User has not been assigned to any project");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                } else {
                    response.setStatus(HttpStatus.OK.value());
                    response.setMessage("Get student Subject info success");
                    response.setSuccess(true);
                    response.getData().put("info", user.getSubject() == null ? user.getSubjectLeader() : user.getSubject());
                    response.getData().put("teamRole", user.getSubject() == null ? "Leader" : "Teammate");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
        }
        throw new BadCredentialsException("access token is missing");
    }

    @GetMapping("/search")
    @ApiOperation("Search by Criteria")
    public ResponseEntity<Object> search(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "5") int size) {
        Page<StudentEntity> studentEntityPage = studentService.search(page, size);
        List<StudentEntity> listStudent = studentEntityPage.toList();
        int totalElements = studentService.findAllStudent().size();
        int totalPage = totalElements % size == 0 ? totalElements / size : totalElements / size + 1;
        PagingResponse pagingResponse = new PagingResponse();
        Map<String, Object> map = new HashMap<>();
        List<Object> Result = Arrays.asList(listStudent.toArray());
        pagingResponse.setTotalPages(totalPage);
        pagingResponse.setEmpty(listStudent.size() == 0);
        pagingResponse.setFirst(page == 0);
        pagingResponse.setLast(page == totalPage - 1);
        pagingResponse.getPageable().put("pageNumber", page);
        pagingResponse.getPageable().put("pageSize", size);
        pagingResponse.setSize(size);
        pagingResponse.setNumberOfElements(listStudent.size());
        pagingResponse.setTotalElements(totalElements);
        pagingResponse.setContent(Result);
        return new ResponseEntity<>(pagingResponse, HttpStatus.OK);
    }
}

