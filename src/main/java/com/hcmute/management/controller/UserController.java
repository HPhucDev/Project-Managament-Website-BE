package com.hcmute.management.controller;

import com.hcmute.management.handler.AuthenticateHandler;
import com.hcmute.management.handler.FileNotImageException;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.request.User.AddUserInfoRequest;
import com.hcmute.management.model.payload.response.ErrorResponse;
import com.hcmute.management.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static com.hcmute.management.controller.SubjectController.E400;
import static com.hcmute.management.controller.SubjectController.E401;

@ComponentScan
@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticateHandler authenticateHandler;
    @PostMapping(value = "/userInfo",consumes = {"multipart/form-data"})
    @ApiOperation("Create User Info")
    public ResponseEntity<Object> addUserInfo(AddUserInfoRequest addUserInfoRequest, @RequestPart(required = false,name = "imageFile") MultipartFile file, HttpServletRequest req)
    {
        UserEntity user;
        try
        {
            user = authenticateHandler.authenticateUser(req);
            try
            {
                user.setFullName(addUserInfoRequest.getFullName());
                user.setGender(addUserInfoRequest.getGender());
                user.setEmail(addUserInfoRequest.getEmail());
                if (file!=null) {
                    user = userService.addUserImage(file, user);
                }
                else
                {
                    user = userService.saveUser(user);
                }
                return new ResponseEntity<>(user,HttpStatus.OK);
            }catch (FileNotImageException fileNotImageException)
            {
                return new ResponseEntity<>(new ErrorResponse("Unsupported Media Type","FILE_NOT_IMAGE",fileNotImageException.getMessage()),HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }
            catch (RuntimeException runtimeException)
            {
                return new ResponseEntity<>(new ErrorResponse("Bad request","FAIL_TO_UPLOAD",runtimeException.getMessage()),HttpStatus.BAD_REQUEST);
            }
        } catch (BadCredentialsException e)
        {
            return new ResponseEntity<>(new ErrorResponse(E401,"UNAUTHORIZED","Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping(value = "/file",consumes = {"multipart/form-data"})
    @ApiOperation("Create")
    public ResponseEntity<Object> addFile(@RequestPart MultipartFile file)
    {
        System.out.println(file.getContentType());
        String url = userService.uploadFile(file,"demo");
        return new ResponseEntity<>(url,HttpStatus.OK);
    }
}
