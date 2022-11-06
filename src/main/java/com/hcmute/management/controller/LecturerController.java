package com.hcmute.management.controller;

import com.hcmute.management.common.AppUserRole;
import com.hcmute.management.handler.AuthenticateHandler;
import com.hcmute.management.handler.MethodArgumentNotValidException;
import com.hcmute.management.mapping.LecturerMapping;
import com.hcmute.management.model.entity.LecturerEntity;
import com.hcmute.management.model.entity.SubjectEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.SuccessResponse;
import com.hcmute.management.model.payload.request.Lecturer.AddNewLecturerRequest;
import com.hcmute.management.model.payload.request.Lecturer.UpdateLecturerRequest;
import com.hcmute.management.model.payload.response.ErrorResponse;
import com.hcmute.management.model.payload.response.PagingResponse;
import com.hcmute.management.repository.LecturerRepository;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.LecturerService;
import com.hcmute.management.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    static String E401="Unauthorized";
    static String E404="Not Found";
    static String E400="Bad Request";
    @Autowired
    AuthenticateHandler authenticateHandler;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("")
    @ApiOperation("Add")
    public ResponseEntity<Object> addLecturer(@RequestBody @Valid AddNewLecturerRequest addNewLecturerRequest, BindingResult errors, HttpServletRequest req) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        UserEntity user;
        try {
        user = authenticateHandler.authenticateUser(req);
        String id =addNewLecturerRequest.getId();
        LecturerEntity findLecturerById =lecturerService.getLecturerById(id);
        if(findLecturerById!=null){
            return new ResponseEntity<>(new ErrorResponse(E400,"ID_EXISTS","Id has been used"),HttpStatus.BAD_REQUEST);
        }
        UserEntity foundUser =userService.findByPhone(id);
        if(foundUser!=null){
            return new ResponseEntity<>(new ErrorResponse(E400,"PHONE_EXISTS","Phone has been used by another Lecturer"),HttpStatus.BAD_REQUEST);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserEntity addNewUser =new UserEntity(passwordEncoder.encode(id),id);
        addNewUser=userService.register(addNewUser, AppUserRole.ROLE_LECTURER);
        LecturerEntity lecturer=lecturerService.saveLecturer(addNewLecturerRequest,addNewUser);
        return new ResponseEntity<>(lecturer, HttpStatus.OK);
            }  catch (BadCredentialsException e) {
                return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);

        }
    }
    @GetMapping("")
    @ApiOperation("Get all")
    public ResponseEntity<Object> getAllLecturer() {
        List<LecturerEntity> listLecturer = lecturerService.getAllLecturer();
        Map<String,Object> map = new HashMap<>();
        map.put("content",listLecturer);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
//    @GetMapping("/getallpaging")
//    public ResponseEntity<Object>getAllLecturerPaging(@RequestParam(defaultValue = "0") int page,
//                                                              @RequestParam(defaultValue = "5") int size){
//        List<LecturerEntity> listLecturer = lecturerService.findAllLecturerPaging(page,size);
//        int totalElements = lecturerService.getAllLecturer().size();
//        PagingResponse response = new PagingResponse();
//        if (listLecturer.size()==0)
//        {
//            response.setStatus(HttpStatus.NOT_FOUND.value());
//            response.setMessage("List Lecturer is empty");
//            response.setSuccess(false);
//            response.setEmpty(true);
//            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
//        }
//
//        response.setStatus(HttpStatus.OK.value());
//        response.setMessage("Get all Lecturer successfully");
//        response.setSuccess(true);
//        response.getData().put("listLecturerInfo",listLecturer);
//        response.setEmpty(false);
//        response.setFirst(page==0);
//        response.setSize(size);
//        response.setTotalPages(totalElements%size==0 ? totalElements/size : totalElements/size+1);
//        response.setLast( page == response.getTotalPages()-1);
//        response.setTotalElements(totalElements);
//        return new ResponseEntity<>(response,HttpStatus.OK);
//    }
    @PatchMapping("")
    @ApiOperation("Update")
    public ResponseEntity<Object> updateLecturer(@Valid @RequestBody UpdateLecturerRequest updateLecturerRequest, BindingResult errors, HttpServletRequest req) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(req);
            LecturerEntity lecturer = lecturerService.findByUser(user);
            if (lecturer == null) {
                return new ResponseEntity<>(new ErrorResponse(E400,"YOU_ARE_NOT_A_LECTURER","You aren't a Lecturer"),HttpStatus.BAD_REQUEST);

            }
            LecturerEntity updateLecturer=lecturerService.updateLecturer(updateLecturerRequest,user);
            return new ResponseEntity<>(updateLecturer, HttpStatus.OK);
        } catch (BadCredentialsException e) {
        return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
    }
    }
    @GetMapping("/{id}")
    @ApiOperation("Get by Id")
    public ResponseEntity<Object>getLecturerById(@PathVariable("id")String id){
        LecturerEntity lecturer = lecturerService.getLecturerById(id);
        if(lecturer==null)
        {
            return new ResponseEntity<>(new ErrorResponse(E404,"LECTURER_NOT_FOUND","Can't find Lecturer with id provided"+id),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lecturer,HttpStatus.OK);
    }
    @DeleteMapping("")
    @ApiOperation("Delete")
    public ResponseEntity<Object>deleteLecturer(@RequestBody List<String> listLecturerId,HttpServletRequest req){
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(req);
            lecturerService.deleteByListId(listLecturerId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }
    @DeleteMapping("/{id}")
    @ApiOperation("Delete by id")
    public ResponseEntity<Object>deleteLecturerById(@PathVariable("id") String id,HttpServletRequest req){
        UserEntity user;
        try {
            user = authenticateHandler.authenticateUser(req);
            LecturerEntity lecturer =lecturerService.getLecturerById(id);
            if(lecturer==null)
            {
                return new ResponseEntity<>(new ErrorResponse(E404,"LECTURER_NOT_FOUND","Can't find Lecturer with id provided+"+id),HttpStatus.NOT_FOUND);
            }
            if(user.equals(lecturer.getUser())){
                return new ResponseEntity<>(new ErrorResponse(E400,"YOU_CAN_NOT_DELETE_YOUR_ACCOUNT","You can't delete your account"),HttpStatus.BAD_REQUEST);
            }
            lecturerService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }

}
