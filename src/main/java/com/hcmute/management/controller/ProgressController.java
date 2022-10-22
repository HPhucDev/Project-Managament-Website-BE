package com.hcmute.management.controller;

import com.hcmute.management.handler.MethodArgumentNotValidException;
import com.hcmute.management.model.entity.ProgressEntity;
import com.hcmute.management.model.entity.StudentEntity;
import com.hcmute.management.model.entity.UserEntity;
import com.hcmute.management.model.payload.SuccessResponse;
import com.hcmute.management.model.payload.request.Progress.AddNewProgressRequest;
import com.hcmute.management.model.payload.request.Progress.UpdateProgressRequest;
import com.hcmute.management.model.payload.request.Student.UpdateStudentRequest;
import com.hcmute.management.security.JWT.JwtUtils;
import com.hcmute.management.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;
import java.util.UUID;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@ComponentScan
@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/add")
    public ResponseEntity<SuccessResponse> addProgress(@RequestBody @Valid AddNewProgressRequest addNewProgressRequest, BindingResult errors, HttpServletRequest httpServletRequest) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        SuccessResponse response = new SuccessResponse();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token is  expired");
            }
            if (addNewProgressRequest.getModiferdate().compareTo(addNewProgressRequest.getCreatedate()) <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Modify Date or StartDate not valid");
                response.setSuccess(false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else {
                ProgressEntity progress = progressService.saveProgress(addNewProgressRequest);
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Save Subject successfully");
                response.setSuccess(true);
                response.getData().put("Progress Information: ", progress);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } else throw new BadCredentialsException("access token is missing");
    }

    @GetMapping("/showall")
    public ResponseEntity<SuccessResponse> getAllProgress() {
        List<ProgressEntity> listProgress = progressService.findAllProgress();
        if (listProgress.size() == 0) {
            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.FOUND.value());
            response.setMessage("List progress is Empty");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        SuccessResponse response = new SuccessResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Query Successfully");
        response.setSuccess(true);
        response.getData().put("list Student", listProgress);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getProgressById(@PathVariable int id) {
        ProgressEntity progress = progressService.findById(id);
        if (progress == null) {
            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.FOUND.value());
            response.setMessage("Progress is Not Found");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        SuccessResponse response = new SuccessResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Query Successfully");
        response.setSuccess(true);
        response.getData().put("Student: ", progress);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<SuccessResponse> updateProgress(HttpServletRequest req, @RequestBody @Valid UpdateProgressRequest updateProgressRequest, @PathVariable("id") int id) {
        String authorizationHeader = req.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken)) {
                throw new BadCredentialsException("access token is expired");
            }
            ProgressEntity progress = new ProgressEntity();
            if (progressService.findById(id) != null) {
                progress = progressService.updateProgress(updateProgressRequest, id);
                SuccessResponse response = new SuccessResponse();
                response.setMessage("Change progress successfully");
                response.setSuccess(true);
                response.setStatus(HttpStatus.OK.value());
                response.getData().put("Student", progress);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                SuccessResponse response = new SuccessResponse();
                response.setStatus(HttpStatus.FOUND.value());
                response.setMessage("Progress isn't existed");
                response.setSuccess(false);
                return new ResponseEntity<>(response, HttpStatus.FOUND);
            }
        }
        throw new BadCredentialsException("access token is missing");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponse> deleteProgress(@RequestBody List<Integer> listProgressId, HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        SuccessResponse response = new SuccessResponse();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token is  expired");
            }
            progressService.deleteById(listProgressId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Delete Subject successfully");
            response.setSuccess(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else throw new BadCredentialsException("access token is missing");
    }

}
