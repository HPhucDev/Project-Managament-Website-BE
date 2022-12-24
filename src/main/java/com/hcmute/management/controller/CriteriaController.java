package com.hcmute.management.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

//@ComponentScan
//@RestController
//@RestResource(exported = false)
//@RequestMapping("/api/criteria")
//@RequiredArgsConstructor
//public class CriteriaController {
//    @PostMapping("")
//    public ResponseEntity<Object> addNewCriteria(@RequestParam String name,@RequestParam int maxPoint)
//    {
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//}
