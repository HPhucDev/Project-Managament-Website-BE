package com.hcmute.management.controller;

import com.hcmute.management.handler.AuthenticateHandler;
import com.hcmute.management.model.entity.ClassEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.response.ErrorResponse;
import com.hcmute.management.service.ClassService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.hcmute.management.controller.StudyController.E401;
import static com.hcmute.management.controller.StudyController.E404;

@ComponentScan
@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassController {
   private final ClassService classService;
   private final AuthenticateHandler authenticateHandler;

   @PostMapping("")
   @ApiOperation("Create")
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public ResponseEntity<Object> addClasses(HttpServletRequest req, @RequestParam String className)
   {
       UserEntity user;
       try
       {
           user=authenticateHandler.authenticateUser(req);
           ClassEntity classEntity = new ClassEntity();
           String id = String.valueOf(UUID.randomUUID());
           classEntity.setId(id);
           classEntity.setClassname(className);
           classService.saveClass(classEntity);
           return new ResponseEntity<>(classEntity,HttpStatus.OK);
       }catch (BadCredentialsException e) {
           return new ResponseEntity<>(new ErrorResponse(E401, "UNAUTHORIZED", "Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
       }
   }
    @PatchMapping("/{classId}")
    @ApiOperation("Update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> updateClasses(HttpServletRequest req,@RequestParam String className,@PathVariable("classId") String id)
    {
        UserEntity user;
        try
        {
            user=authenticateHandler.authenticateUser(req);
            ClassEntity classEntity = classService.findById(id);
            if (classEntity==null)
            {
                return new ResponseEntity<>(new ErrorResponse(E404,"CLASS_NOT_FOUND","Class not found with id provided"),HttpStatus.OK);
            }
            classEntity.setClassname(className);
            classService.saveClass(classEntity);
            return new ResponseEntity<>(classEntity,HttpStatus.OK);
        }
        catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401, "UNAUTHORIZED", "Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }
    @DeleteMapping("/{classId}")
    @ApiOperation("Delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteClasses(HttpServletRequest req,@RequestParam("classId") String id)
    {
        UserEntity user;
        try
        {
            user=authenticateHandler.authenticateUser(req);
            ClassEntity classEntity = classService.findById(id);
            if (classEntity==null)
            {
                return new ResponseEntity<>(new ErrorResponse(E404,"CLASS_NOT_FOUND","Class not found with id provided"),HttpStatus.OK);
            };
            classService.deleteClass(classEntity);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse(E401, "UNAUTHORIZED", "Unauthorized, please login again"), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("")
    @ApiOperation("Get all class")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllClasses()
    {
        List<ClassEntity> classEntities = classService.getAllClass();
        Map<String,Object> map = new HashMap<>();
        map.put("content",classEntities);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }


}
