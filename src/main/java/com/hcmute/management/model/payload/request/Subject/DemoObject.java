package com.hcmute.management.model.payload.request.Subject;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Data
@NoArgsConstructor
public class DemoObject {
    @ApiParam(name = "Test",value = "Some Test here",example = " This is a test")
    private String test;
    @ApiParam(name = "Test1",value = "Some Test here",example = " This is a test")
    private String test1;

}
