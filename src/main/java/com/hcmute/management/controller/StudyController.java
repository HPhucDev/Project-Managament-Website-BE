package com.hcmute.management.controller;

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

    @GetMapping("/{id}")
    @ApiOperation("Get by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getStudentById(@PathVariable String id) {
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
    private ResponseEntity<Object> createStudent(HttpServletRequest httpServletRequest, @RequestBody AddNewStudentRequest addNewStudentRequest, BindingResult bindingResult) throws Exception {
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
                return new ResponseEntity<>(new ErrorResponse(E400, "USER_ID_NOT_FOUND", "User id existed"), HttpStatus.NOT_FOUND);
        }
        throw new BadCredentialsException("Access token is missing");
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation("Delete")
    public ResponseEntity<Object> deleteStudentById(HttpServletRequest httpServletRequest, @PathVariable("id") String id) {
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("Access token is expired");
            }
            StudentEntity student = studentService.findById(id);
            if (student == null) {
                return new ResponseEntity<>(new ErrorResponse(E404, "STUDENT_ID_NOT_FOUND", "Student id not found"), HttpStatus.NOT_FOUND);
            } else {

                studentService.deleteStudent(id);
                UserEntity user = userService.findById(student.getUser().getId());
                for (RoleEntity role : user.getRoles()) {
                    role.setUsers(null);
                }
                user.getRoles().clear();
                userService.delete(user);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new BadCredentialsException("Access token is missing");
    }

    @PostMapping("/addGroupLeader/{id}")
    @ApiOperation("Add Group Leader")
    public ResponseEntity<Object> addGroupLeader(@PathVariable("id") String id, HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("Access token is expired");
            }
            UserEntity user = userService.findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(accessToken)).toString());
            if (user == null) {
                throw new BadCredentialsException("User not found");
            } else {
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
        }
        throw new BadCredentialsException("Access token is missing");
    }

    @PostMapping("/addGroupMember/{id}")
    @ApiOperation("Add Group Member")
    public ResponseEntity<Object> addGroupMember(@RequestParam(value = "listMember") List<String> listMember, @PathVariable("id") String id, HttpServletRequest req) {
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
                                    response.setStatus(HttpStatus.FOUND.value());
                                    response.setMessage("Member with id " + i + " is not valid");
                                    response.setSuccess(false);
                                    return new ResponseEntity<>(new ErrorResponse(E404, "ID_NOT_VALID", "Member with id " + i + " is not valid"), HttpStatus.NOT_FOUND);
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
    @ApiOperation("Delete Group Member")
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

    @DeleteMapping("/deleteGroupLeader/{id}")
    @ApiOperation("Delete Group Leader")
    public ResponseEntity<SuccessResponse> deleteGroupLeader(@PathVariable("id") String id, HttpServletRequest req) {
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
                SubjectEntity subject = subjectService.getSubjectById(id);
                if (subject == null || subject.getGroupLeader() != user) {
                    response.setStatus(HttpStatus.FOUND.value());
                    response.setMessage("Subject doesn't exists or User is not leader");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.FOUND);
                } else {
                    for (UserEntity tempUser : subject.getGroupMember()) {
                        tempUser.setSubject(null);
                        subject.getGroupMember().remove(user);
                    }
                    subject.setGroupLeader(null);
                    user.setSubjectLeader(null);
                }
                subject = subjectService.saveSubject(subject);
                response.setMessage("Delete subject leader and group member success");
                response.setSuccess(true);
                response.setStatus(HttpStatus.OK.value());
                response.getData().put("Leader", subject.getGroupLeader());
                response.getData().put("Member", subject.getGroupMember());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        throw new BadCredentialsException("access token is missing");
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

